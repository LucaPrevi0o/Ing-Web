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

    public MySqlWorkerDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Worker> findAll() {

        var workers=new ArrayList<Worker>();
        var newS="SELECT dipendente.*, GROUP_CONCAT(patenti_autista.patente) AS 'patenti' FROM dipendente, patenti_autista WHERE dipendente.codice_fiscale=patenti_autista.dipendente GROUP BY dipendente.codice_fiscale";
        var res=MySqlQueryManager.getResult(connection, newS); //execute query on the database
        var newArr=Arrays.copyOf(allColumns, allColumns.length+1);
        newArr[newArr.length-1]="patenti";
        var resList=MySqlQueryManager.asList(res, newArr); //parse results
        for (var item: resList) {

            var worker=new Worker(item[0], item[1], item[2], Date.valueOf(item[3]), item[4], item[5].equals("1"));
            var licenses=new ArrayList<License>();
            for (var license: item[6].split(",")) licenses.add(new License(license));
            worker.setLicenses(licenses);
            System.out.println(worker);
            workers.add(worker);
        }
        return workers; //return null if none is found
    }

    //return, if exists, the worker having a specified fiscal code
    public Worker findByFiscalCode(String fiscalCode) {

        var newS="SELECT dipendente.*, GROUP_CONCAT(patenti_autista.patente) AS 'patenti' FROM dipendente, patenti_autista WHERE dipendente.codice_fiscale=patenti_autista.dipendente AND dipendente.codice_fiscale='"+fiscalCode+"' GROUP BY dipendente.codice_fiscale";
        var res=MySqlQueryManager.getResult(connection, newS); //execute query on the database
        var newArr=Arrays.copyOf(allColumns, allColumns.length+1);
        newArr[newArr.length-1]="patenti";
        var resList=MySqlQueryManager.asList(res, newArr); //parse results
        for (var item: resList) {

            var worker=new Worker(item[0], item[1], item[2], Date.valueOf(item[3]), item[4], item[5].equals("1"));
            var licenses=new ArrayList<License>();
            for (var license: item[6].split(",")) licenses.add(new License(license));
            worker.setLicenses(licenses);
            System.out.println(worker);
            return worker;
        }
        return null;
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
