package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ServiceDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.Service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class MySqlServiceDAO implements ServiceDAO {

    private final Connection connection;
    private final String[] allColumns={"codice", "nome", "cliente", "data", "ora_inizio", "durata", "primo_autista", "secondo_autista", "targa_mezzo", "deleted"};

    private String parseParams() {

        var s="";
        for (var i=0; i<allColumns.length-1; i++) s+=allColumns[i]+", ";
        return s+allColumns[allColumns.length-1];
    }

    private String addParams() {

        var s="";
        for (var i=0; i<allColumns.length-1; i++) s+="?, ";
        return s+"?";
    }

    public MySqlServiceDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Service> findAll() {

        var services=new ArrayList<Service>();
        var query="select * from servizio";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new service

            //parse obtained result as correct data type
            var service=new Service(Integer.parseInt(item[0]), item[1], null, Date.valueOf(item[3]), Time.valueOf(item[4]), Time.valueOf(item[5]), null, null, null, item[9].equals("1"));
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }

        return services; //return list of valid services
    }

    public Service findByCode(int code) {

        var query="select * from servizio where codice="+code;
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (!resList.isEmpty()) {

            if (resList.size()>1) return null; //return null error value if list has more than 1 element
            var item=resList.get(0); //get first (and only) instance of the list and return its value
            return new Service(Integer.parseInt(item[0]), item[1], null, Date.valueOf(item[3]), Time.valueOf(item[4]), Time.valueOf(item[5]), null, null, null, item[9].equals("1"));
        }
        return null; //return null if none is found
    }

    public ArrayList<Service> findByClientCompany(ClientCompany clientCompany) {

        var services=new ArrayList<Service>();
        var query="select * from servizio where cliente="+clientCompany;
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new service

            //parse obtained result as correct data type
            var service=new Service(Integer.parseInt(item[0]), item[1], null, Date.valueOf(item[3]), Time.valueOf(item[4]), Time.valueOf(item[5]), null, null, null, item[9].equals("1"));
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
            var service=new Service(Integer.parseInt(item[0]), item[1], null, Date.valueOf(item[3]), Time.valueOf(item[4]), Time.valueOf(item[5]), null, null, null, item[9].equals("1"));
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }

        return services; //return list of valid services
    }

    public void addService(Service service) {

        var query="insert into servizio ("+parseParams()+") values ("+addParams()+")";
        MySqlQueryManager.execute(connection, query, service.asList());
    }
}
