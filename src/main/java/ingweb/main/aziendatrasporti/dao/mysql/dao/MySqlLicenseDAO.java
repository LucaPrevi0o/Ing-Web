package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.LicenseDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.License;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import ingweb.main.aziendatrasporti.mo.mo.Truck;
import ingweb.main.aziendatrasporti.mo.mo.Worker;
import java.sql.Connection;
import java.util.ArrayList;

public class MySqlLicenseDAO implements LicenseDAO {

    private final Connection connection;
    private final String[] allColumns={"categoria"};

    public MySqlLicenseDAO(Connection connection) { this.connection=connection; }

    public ArrayList<License> findAll() {

        ArrayList<License> licenses=new ArrayList<>();
        var query="select * from patente";
        var res= MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) licenses.add(new License(item[0])); //add license to list
        return licenses; //return list of valid services
    }

    public ArrayList<License> findAllByService(Service service) {

        ArrayList<License> licenses=new ArrayList<>();
        var query="select * from patenti_servizio where servizio='"+service.getCode()+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, new String[]{"servizio", "patente"}); //parse results
        for (var item: resList) licenses.add(new License(item[1])); //add license to list
        return licenses; //return list of valid services
    }

    public ArrayList<License> findAllByTruck(Truck truck) {

        ArrayList<License> licenses=new ArrayList<>();
        var query="select * from patenti_mezzo where mezzo='"+truck.getNumberPlate()+"'";
        var res= MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, new String[]{"mezzo", "patente"}); //parse results
        for (var item: resList) licenses.add(new License(item[1])); //add license to list
        return licenses; //return list of valid services
    }

    public ArrayList<License> findAllByWorker(Worker worker) {

        ArrayList<License> licenses=new ArrayList<>();
        var query="select * from patenti_autista where dipendente='"+worker.getFiscalCode()+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, new String[]{"dipendente", "patente"}); //parse results
        for (var item: resList) licenses.add(new License(item[1])); //add license to list
        return licenses; //return list of valid services
    }

    public void addLicensesByWorker(Worker worker, ArrayList<License> licenses) {

        for (var license: licenses) {

            var query="insert into patenti_autista (dipendente, patente) values (?, ?)";
            var params=new Object[]{worker.getFiscalCode(), license.getCategory()};
            MySqlQueryManager.execute(connection, query, params);
        }
    }

    public void updateLicensesByWorker(Worker worker, ArrayList<License> licenses) {

        var query="delete from patenti_autista where dipendente=?";
        MySqlQueryManager.execute(connection, query, new Object[]{worker.getFiscalCode()});
        addLicensesByWorker(worker, licenses);
    }

    public void addLicensesByTruck(Truck truck, ArrayList<License> licenses) {

        for (var license: licenses) {

            var query="insert into patenti_mezzo (mezzo, patente) values (?, ?)";
            var params=new Object[]{truck.getNumberPlate(), license.getCategory()};
            MySqlQueryManager.execute(connection, query, params);
        }
    }

    public void updateLicensesByTruck(Truck truck, ArrayList<License> licenses) {

        var query="delete from patenti_mezzo where mezzo=?";
        MySqlQueryManager.execute(connection, query, new Object[]{truck.getNumberPlate()});
        addLicensesByTruck(truck, licenses);
    }

    public void addLicensesByService(Service service) {

        for (var license: service.getValidLicenses()) {

            var query="insert into patenti_servizio (servizio, patente) values (?, ?)";
            var params=new Object[]{service.getCode(), license.getCategory()};
            MySqlQueryManager.execute(connection, query, params);
        }
    }

    public void updateLicensesByService(Service service, ArrayList<License> licenses) {

        var query="delete from patenti_servizio where servizio=?";
        MySqlQueryManager.execute(connection, query, new Object[]{service.getCode()});
        addLicensesByService(service);
    }
}
