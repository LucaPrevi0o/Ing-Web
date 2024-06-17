package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ServiceDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Worker;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class MySqlServiceDAO implements ServiceDAO {

    private final Connection connection;
    private final String[] allColumns={"codice", "nome", "cliente", "data", "ora_inizio", "durata", "primo_autista", "secondo_autista", "targa_mezzo", "deleted"};

    private String parseParams() {

        var s="";
        for (var i=1; i<allColumns.length-1; i++) s+=allColumns[i]+", ";
        return s+allColumns[allColumns.length-1];
    }

    private String addParams() {

        var s="";
        for (var i=1; i<allColumns.length-1; i++) s+="?, ";
        return s+"?";
    }

    private Service getService(String[] item) {

        return new Service(Integer.parseInt(item[0]), item[1], Date.valueOf(item[2]), Time.valueOf(item[3]), Time.valueOf(item[4]), item[5].equals("1"));
    }

    public MySqlServiceDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Service> findAll() {

        var services=new ArrayList<Service>();
        var query="select codice, nome, data, ora_inizio, durata, deleted from servizio";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, new String[]{"codice", "nome", "data", "ora_inizio", "durata", "deleted"}); //parse results
        for (var item: resList) { //add every element of the result set as new service

            System.out.println(Arrays.toString(item));
            var service=new Service(Integer.parseInt(item[0]), item[1], Date.valueOf(item[2]), Time.valueOf(item[3]), Time.valueOf(item[4]), item[5].equals("1"));
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }
        return services; //return list of valid services
    }

    public Service findByCode(int code) {

        var query="select codice, nome, data, ora_inizio, durata, deleted from servizio";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, new String[]{"codice", "nome", "data", "ora_inizio", "durata", "deleted"}); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var service=getService(item);
        return (service.isDeleted() ? null : service);
    }

    public ArrayList<Service> findByClientCompany(ClientCompany clientCompany) {

        var services=new ArrayList<Service>();
        var query="select * from servizio where cliente="+clientCompany;
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new service

            //parse obtained result as correct data type
            var service=new Service(Integer.parseInt(item[0]), item[1], Date.valueOf(item[2]), Time.valueOf(item[3]), Time.valueOf(item[4]), item[5].equals("1"));
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }

        return services; //return list of valid services
    }

    public ArrayList<Service> findByDate(Date date) {

        var services=new ArrayList<Service>();
        var query="select * from servizio where data="+date;
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new service

            //parse obtained result as correct data type
            var service=new Service(Integer.parseInt(item[0]), item[1], Date.valueOf(item[2]), Time.valueOf(item[3]), Time.valueOf(item[4]), item[5].equals("1"));
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }

        return services; //return list of valid services
    }

    public void addService(Service service) {

        var query="insert into servizio (nome, data, ora_inizio, durata, deleted) values (?, ?, ?, ?, ?)";
        MySqlQueryManager.execute(connection, query, service.data());
    }

    public void removeService(Service service) {

        var query="update servizio set deleted=1 where (codice = ?)";
        MySqlQueryManager.execute(connection, query, new Object[]{service.getCode()});
    }
}
