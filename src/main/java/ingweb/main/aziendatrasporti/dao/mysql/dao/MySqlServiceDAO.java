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
    private final String[] insertion={"nome", "cliente", "data", "ora_inizio", "durata", "deleted"};
    private final String[] data={"codice", "nome", "cliente", "data", "ora_inizio", "durata", "deleted", "patenti", "nome_cliente"};
    private final String[] shortData={"codice", "nome", "data", "ora_inizio", "durata", "deleted"};

    private String parseInsertion() {

        var s="";
        for (var i=0; i<insertion.length-1; i++) s+=insertion[i]+", ";
        return s+insertion[insertion.length-1];
    }

    private String addInsertion() { //parameter list is not the same as the

        var s="";
        for (var i=0; i<insertion.length-1; i++) s+="?, ";
        return s+"?";
    }

    private String parseShortData() { //return all values

        var s="";
        for (var i=0; i<shortData.length-1; i++) s+=shortData[i]+", ";
        return s+shortData[shortData.length-1];
    }

    private String parseData() {

        var s="";
        for (var i=0; i<data.length-3; i++) s+="servizio."+data[i]+", ";
        return s+"servizio."+data[data.length-3];
    }

    private Service getService(String[] item) {

        var client=new ClientCompany(item[8], item[2], null, null, null, null, null, false);
        var service=new Service(Integer.parseInt(item[0]), item[1], client, Date.valueOf(item[3]), Time.valueOf(item[4]), Time.valueOf(item[5]), item[6].equals("1"));
        var licenses=new ArrayList<License>();
        for (var license: item[7].split(",")) licenses.add(new License(license));
        service.setValidLicenses(licenses);
        return service;
    }

    private Service getShortService(String[] item) {

        return new Service(Integer.parseInt(item[0]), item[1], Date.valueOf(item[2]), Time.valueOf(item[3]), Time.valueOf(item[4]), item[5].equals("1"));
    }

    public MySqlServiceDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Service> findAllData() {

        //does need client company and license list (to show in service list)
        var services=new ArrayList<Service>();
        var query="select "+parseData()+", group_concat(patenti_servizio.patente) as "+data[data.length-2]+",azienda_cliente.nome as "+data[data.length-1]+" from servizio, patenti_servizio, azienda_cliente where servizio.codice=patenti_servizio.servizio and azienda_cliente.ragione_sociale=servizio.cliente group by servizio.codice";
        System.out.println(query);
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, data); //parse results
        for (var item: resList) { //add every element of the result set as new service

            var service=getService(item);
            if (!service.isDeleted()) services.add(service); //add service to the result list if not set as deleted
        }
        return services; //return list of valid services
    }

    public Service findDataByCode(int code) {

        //does need client company and license list (to show in service list)
        var query="select "+parseData()+", group_concat(patenti_servizio.patente) as "+data[data.length-2]+", azienda_cliente.nome as "+data[data.length-1]+" from servizio, patenti_servizio, azienda_cliente where ragione_sociale=cliente and servizio.codice=servizio and servizio.codice='"+code+"' group by servizio.codice";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, data); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var service=getService(item);
        return (service.isDeleted() ? null : service);
    }

    public Service findDataByDateStartTimeAndDuration(Date date, Time startTime, Time duration) {

        //does not need client company
        var query="select "+parseShortData()+" from servizio where data='"+date+"' and ora_inizio='"+startTime+"' and durata='"+duration+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, shortData); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var service=getShortService(item);
        return (service.isDeleted() ? null : service);
    }

    public void addService(Service service) {

        //does need client company (even if actual value is set in dispatcher)
        var query="insert into servizio ("+parseInsertion()+") values ("+addInsertion()+")";
        MySqlQueryManager.execute(connection, query, service.data());
    }

    public void removeService(Service service) {

        //does not need client company
        var query="update servizio set deleted=1 where (codice = ?)";
        MySqlQueryManager.execute(connection, query, new Object[]{service.getCode()});
    }

    public void updateService(Service service) {

        //does need client company (to update data in db)
        var query="update servizio set nome=?, cliente=?, data=?, ora_inizio=?, durata=?, deleted=? where codice = '"+service.getCode()+"'";
        MySqlQueryManager.execute(connection, query, service.data());
    }
}
