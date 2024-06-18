package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ServiceDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class MySqlServiceDAO implements ServiceDAO {

    private final Connection connection;
    private final String[] data={"codice", "nome", "cliente", "data", "ora_inizio", "durata", "deleted", "patenti"};
    private final String[] shortData={"codice", "nome", "data", "ora_inizio", "durata", "deleted"};
    private final String[] allColumns={"codice", "nome", "cliente", "data", "ora_inizio", "durata", "primo_autista", "secondo_autista", "targa_mezzo", "deleted"};

    private String parseShortData() {

        var s="";
        for (var i=0; i<shortData.length-1; i++) s+=shortData[i]+", ";
        return s+shortData[shortData.length-1];
    }

    private String addShortData() {

        var s="";
        for (var i=0; i<shortData.length-1; i++) s+="?, ";
        return s+"?";
    }

    private String parseData() {

        var s="";
        for (var i=1; i<data.length-2; i++) s+=data[i]+", ";
        return s+data[data.length-2];
    }

    private String addData() {

        var s="";
        for (var i=1; i<data.length-2; i++) s+="?, ";
        return s+"?";
    }

    private Service getService(String[] item) {

        var service=new Service(Integer.parseInt(item[0]), item[1], Date.valueOf(item[2]), Time.valueOf(item[3]), Time.valueOf(item[4]), item[5].equals("1"));
        var licenses=new ArrayList<License>();
        for (var license: item[6].split(",")) licenses.add(new License(license));
        service.setValidLicenses(licenses);
        return service;
    }

    public MySqlServiceDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Service> findAllData() {

        var services=new ArrayList<Service>();
        var query="select "+parseShortData()+", group_concat(patenti_servizio.patente) as "+data[data.length-1]+" from servizio, patenti_servizio where codice=servizio group by servizio.codice";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, shortData); //parse results
        for (var item: resList) { //add every element of the result set as new service

            var service=getService(item);
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }
        return services; //return list of valid services
    }

    public ArrayList<Service> findAllNotAssigned() {

        var services=new ArrayList<Service>();
        var query="select * from servizio where cliente='' or primo_autista='' or targa_mezzo=''";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new service

            var service=getService(item);
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }
        return services; //return list of valid services
    }

    public Service findDataByCode(int code) {

        var query="select "+parseShortData()+", group_concat(patenti_servizio.patente) as "+data[data.length-1]+" from servizio, patenti_servizio where codice=servizio and servizio.codice='"+code+"' group by servizio.codice";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, shortData); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var service=getService(item);
        return (service.isDeleted() ? null : service);
    }

    public Service findDataByDateStartTimeAndDuration(Date date, Time startTime, Time duration) {

        var query="select "+parseShortData()+" from servizio where data='"+date+"' and ora_inizio='"+startTime+"' and durata='"+duration+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, shortData); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var service=getService(item);
        return (service.isDeleted() ? null : service);
    }

    public ArrayList<Service> findByClientCompany(ClientCompany clientCompany) {

        var services=new ArrayList<Service>();
        var query="select "+parseShortData()+", group_concat(patenti_servizio.patente) as patenti from servizio, patenti_servizio where codice=servizio and servizio.cliente='"+clientCompany+"' group by servizio.codice";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, shortData); //parse results
        for (var item: resList) { //add every element of the result set as new service

            var service=getService(item);
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }
        return services; //return list of valid services
    }

    public ArrayList<Service> findByDate(Date date) {

        var services=new ArrayList<Service>();
        var query="select "+parseData()+", group_concat(patenti_servizio.patente) as "+data[data.length-1]+" from servizio, patenti_servizio where codice=servizio and servizio.data='"+date+"' group by servizio.codice";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, data); //parse results
        for (var item: resList) { //add every element of the result set as new service

            var service=getService(item);
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }
        return services; //return list of valid services
    }

    public void addService(Service service) {

        var query="insert into servizio ("+parseData()+") values ("+addData()+")";
        System.out.println(query);
        MySqlQueryManager.execute(connection, query, service.data());
    }

    public void removeService(Service service) {

        var query="update servizio set deleted=1 where (codice = ?)";
        MySqlQueryManager.execute(connection, query, new Object[]{service.getCode()});
    }

    public void updateService(Service service) {

        var query="update servizio set nome=?, data=?, ora_inizio=?, durata=?, deleted=? where codice = '"+service.getCode()+"'";
        MySqlQueryManager.execute(connection, query, service.data());
    }
}
