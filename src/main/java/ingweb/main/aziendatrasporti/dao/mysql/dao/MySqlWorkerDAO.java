package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.WorkerDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Truck;
import ingweb.main.aziendatrasporti.mo.Worker;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySqlWorkerDAO implements WorkerDAO {

    private final Connection connection;
    private final String[] allColumns={"codice", "nome", "cognome", "codice_fiscale", "data_nascita", "numero_telefono", "deleted", "patenti"};

    private String parseParams() {

        var s="";
        for (var i=1; i<allColumns.length-2; i++) s+=(allColumns[i]+", ");
        return s+allColumns[allColumns.length-2];
    }

    private String addParams() {

        var s="";
        for (var i=1; i<allColumns.length-2; i++) s+="?, ";
        return s+"?";
    }

    private Worker getWorker(String[] item) {

        var worker=new Worker(Integer.parseInt(item[0]), item[1], item[2], item[3], Date.valueOf(item[4]), item[5], item[6].equals("1"));
        var licenses=new ArrayList<License>();
        for (var license: item[7].split(",")) licenses.add(new License(license));
        worker.setLicenses(licenses);
        return worker;
    }

    public MySqlWorkerDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Worker> findAll() {

        var workers=new ArrayList<Worker>();
        var query="SELECT dipendente.*, GROUP_CONCAT(patenti_autista.patente) AS '"+allColumns[allColumns.length-1]+"' FROM dipendente, patenti_autista WHERE dipendente.codice_fiscale=patenti_autista.dipendente GROUP BY dipendente.codice_fiscale ORDER BY dipendente.cognome, dipendente.nome";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) {

            var worker=getWorker(item);
            if (!worker.isDeleted()) workers.add(worker);
        }
        return workers; //return null if none is found
    }

    public ArrayList<Worker> findAllByLicenses(ArrayList<License> licenses) {

        var result=new ArrayList<Worker>(); //empty list
        for (var license: licenses) {

            var query="select dipendente.*, group_concat(patenti_autista.patente) as '"+allColumns[allColumns.length-1]+"' from dipendente join patenti_autista on dipendente.codice_fiscale=patenti_autista.dipendente where patenti_autista.patente='"+license.getCategory()+"' group by dipendente.codice_fiscale ORDER BY dipendente.cognome, dipendente.nome";
            var workers=MySqlQueryManager.getResult(connection, query); //execute query on the database
            var workerList=MySqlQueryManager.asList(workers, allColumns); //parse results
            for (var item: workerList) { //add every element of the result set as new worker

                //parse obtained result as correct data type
                var worker=getWorker(item);
                if (!worker.isDeleted()) result.add(worker); //add worker to the result list if not set as deleted
            }
        }

        return result; //return list of valid trucks
    }

    //return, if exists, the worker having a specified fiscal code
    public Worker findByCode(int code) {

        var query="SELECT dipendente.*, GROUP_CONCAT(patenti_autista.patente) AS 'patenti' FROM dipendente, patenti_autista WHERE dipendente.codice_fiscale=patenti_autista.dipendente AND dipendente.codice='"+code+"' GROUP BY dipendente.codice_fiscale ORDER BY dipendente.cognome, dipendente.nome";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var worker=getWorker(item);
        return (worker.isDeleted() ? null : worker);
    }

    //return, if exists, the worker having a specified fiscal code
    public Worker findByFiscalCode(String fiscalCode) {

        var query="SELECT dipendente.*, GROUP_CONCAT(patenti_autista.patente) AS 'patenti' FROM dipendente, patenti_autista WHERE dipendente.codice_fiscale=patenti_autista.dipendente AND dipendente.codice_fiscale='"+fiscalCode+"' GROUP BY dipendente.codice_fiscale ORDER BY dipendente.cognome, dipendente.nome";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var worker=getWorker(item);
        return (worker.isDeleted() ? null : worker);
    }

    //insert a new account in the database passing login data
    public void addWorker(Worker worker) {

        var query="insert into dipendente ("+parseParams()+") values ("+addParams()+")"; //empty query
        MySqlQueryManager.execute(connection, query, worker.asList()); //execute insertion with parameters
    }

    //remove account from database (setting the logic deletion true)
    public void removeWorker(Worker worker) {

        var query="update dipendente set deleted=1 where (codice = ?)"; //empty query
        MySqlQueryManager.execute(connection, query, new Object[]{worker.getCode()}); //execute update with parameters
    }

    public void updateWorker(Worker worker) {

        var query="update dipendente set ";
        for (var i=1; i<allColumns.length-2; i++) query+=(allColumns[i]+"=?, ");
        query+=(allColumns[allColumns.length-2]+"=? where codice = '"+worker.getCode()+"'");
        MySqlQueryManager.execute(connection, query, worker.asList());
    }
}
