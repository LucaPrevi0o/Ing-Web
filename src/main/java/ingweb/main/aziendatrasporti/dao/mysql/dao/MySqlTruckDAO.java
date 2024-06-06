package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.TruckDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.dispatch.DispatchCollector;
import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Truck;
import ingweb.main.aziendatrasporti.mo.Worker;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

public class MySqlTruckDAO implements TruckDAO {

    private final Connection connection;
    private final String[] allColumns={"codice", "targa", "marca", "modello", "disponibile", "deleted"};

    public MySqlTruckDAO(Connection connection) { this.connection=connection; }

    private String parseParams() {

        var s="";
        for (var i=0; i<allColumns.length-1; i++) s+=(allColumns[i]+", ");
        return s+allColumns[allColumns.length-1];
    }

    private String addParams() {

        var s="";
        for (var i=0; i<allColumns.length-1; i++) s+="?, ";
        return s+"?";
    }

    private Truck itemToObject(String[] item) {

        var truck=new Truck(item[0], item[1], item[2], item[3].equals("1"), item[4].equals("1"));
        var query="select * from patenti_mezzo where targa='"+truck.getNumberPlate()+"'";
        var licenses=MySqlQueryManager.getResult(connection, query);
        var licensesList=MySqlQueryManager.asList(licenses, new String[]{"targa", "patente"});
        var foundLicenses=new ArrayList<License>();
        for (var license: licensesList) foundLicenses.add(new License(license[1]));
        truck.setNeededLicenses(foundLicenses);
        return truck;
    }

    public ArrayList<Truck> findAll() {

        var result=new ArrayList<Truck>(); //empty list
        var query="select * from mezzo"; //SQL query to extract data
        var workers=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var workersList=MySqlQueryManager.asList(workers, allColumns); //parse results
        for (var item: workersList) { //add every element of the result set as new worker

            //parse obtained result as correct data type
            var worker=itemToObject(item);
            if (!worker.isDeleted()) result.add(worker); //add worker to the result list if not set as deleted
        }

        return result; //return list of valid workers
    }

    public Truck findByNumberPlate(String numberPlate) {

        var query="select * from mezzo where targa='"+numberPlate+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (!resList.isEmpty()) {

            if (resList.size()>1) return null; //return null error value if list has more than 1 element
            var item=resList.get(0); //get first (and only) instance of the list and return its value
            return itemToObject(item);
        }
        return null; //return null if none is found
    }

    //insert a new account in the database passing login data
    public void addTruck(Truck truck) {

        var query="insert into mezzo ("+parseParams()+") values ("+addParams()+")"; //empty query
        MySqlQueryManager.execute(connection, query, truck.asList()); //execute insertion with parameters
    }

    //remove account from database (setting the logic deletion true)
    public void removeTruck(Truck truck) {

        var query="update mezzo set deleted=1 where (targa = ?)"; //empty query
        MySqlQueryManager.execute(connection, query, new Object[]{truck.getNumberPlate()}); //execute update with parameters
    }

    public void updateTruck(Truck truck) {

        var params=truck.asList();
        var query="update mezzo set ";
        for (var i=0; i<params.length-1; i++) query+=(allColumns[i]+"=?, ");
        query+=(allColumns[allColumns.length-1]+"=? where (targa = '"+truck.getNumberPlate()+"')");
        MySqlQueryManager.execute(connection, query, params);
    }
}
