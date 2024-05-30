package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.WorkerDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Worker;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySqlWorkerDAO implements WorkerDAO {

    private final Connection connection;
    private final String[] allColumns={"nome", "cognome", "codice_fiscale", "data_nascita", "numero_telefono", "deleted"};

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

    private Worker itemToObject(String[] item) {

        var worker=new Worker(item[0], item[1], item[2], Date.valueOf(item[3]), item[4], item[5].equals("1"));
        var query="select * from patenti_autista where dipendente='"+worker.getFiscalCode()+"'";
        var licenses=MySqlQueryManager.getResult(connection, query);
        var licensesList=MySqlQueryManager.asList(licenses, new String[]{"dipendente", "patente"});
        var foundLicenses=new ArrayList<License>();
        for (var license: licensesList) foundLicenses.add(new License(license[1]));
        worker.setLicenses(foundLicenses);
        return worker;
    }

    public MySqlWorkerDAO(Connection connection) { this.connection=connection; }

    //return a list of every worker found on the database
    public ArrayList<Worker> findAll() {

        var result=new ArrayList<Worker>(); //empty list
        var query="select * from dipendente"; //SQL query to extract data
        var workers=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var workersList=MySqlQueryManager.asList(workers, allColumns); //parse results
        for (var item: workersList) { //add every element of the result set as new worker

            //parse obtained result as correct data type
            var worker=itemToObject(item);
            if (!worker.isDeleted()) result.add(worker); //add worker to the result list if not set as deleted
        }

        return result; //return list of valid workers
    }

    //return, if exists, the worker having a specified fiscal code
    public Worker findByFiscalCode(String fiscalCode) {

        var query="select * from dipendente where codice_fiscale='"+fiscalCode+"'"; //SQL query to extract data
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
    public void addWorker(Worker worker) {

        var query="insert into dipendente ("+parseParams()+") values ("+addParams()+")"; //empty query
        MySqlQueryManager.execute(connection, query, worker.asList()); //execute insertion with parameters
    }

    //remove account from database (setting the logic deletion true)
    public void removeWorker(Worker worker) {

        var query="update dipendente set deleted=1 where (codice_fiscale = ?)"; //empty query
        MySqlQueryManager.execute(connection, query, new Object[]{worker.getFiscalCode()}); //execute update with parameters
    }

    public void updateWorker(Worker worker) {

        var params=worker.asList();
        var query="update dipendente set ";
        for (var i=0; i<params.length-1; i++) query+=(allColumns[i]+"=?, ");
        query+=(allColumns[allColumns.length-1]+"=? where (codice_fiscale = '"+worker.getFiscalCode()+"')");
        MySqlQueryManager.execute(connection, query, params);
    }
}
