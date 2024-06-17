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
import java.util.Arrays;

public class MySqlTruckDAO implements TruckDAO {

    private final Connection connection;
    private final String[] allColumns={"codice", "targa", "marca", "modello", "disponibile", "deleted", "patenti"};

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

    private Truck getTruck(String[] item) {

        var truck=new Truck(Integer.parseInt(item[0]), item[1], item[2], item[3], item[4].equals("1"), item[5].equals("1"));
        var licenses=new ArrayList<License>();
        for (var license: item[6].split(",")) licenses.add(new License(license));
        truck.setNeededLicenses(licenses);
        return truck;
    }

    public MySqlTruckDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Truck> findAll() {

        var result=new ArrayList<Truck>(); //empty list
        var query="SELECT mezzo.*, GROUP_CONCAT(patenti_mezzo.patente) AS '"+allColumns[allColumns.length-1]+"' FROM mezzo, patenti_mezzo WHERE mezzo.targa=patenti_mezzo.targa GROUP BY mezzo.targa";
        var trucks=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var trucksList=MySqlQueryManager.asList(trucks, allColumns); //parse results
        for (var item: trucksList) { //add every element of the result set as new worker

            //parse obtained result as correct data type
            var truck=getTruck(item);
            if (!truck.isDeleted()) result.add(truck); //add worker to the result list if not set as deleted
        }

        return result; //return list of valid trucks
    }

    public Truck findByCode(int code) {

        var query="SELECT mezzo.*, GROUP_CONCAT(patenti_mezzo.patente) AS '"+allColumns[allColumns.length-1]+"' FROM mezzo, patenti_mezzo WHERE mezzo.targa=patenti_mezzo.targa AND mezzo.codice='"+code+"' GROUP BY mezzo.targa";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var truck=getTruck(item);
        return (truck.isDeleted() ? null : truck);
    }

    public Truck findByNumberPlate(String numberPlate) {

        var query="SELECT mezzo.*, GROUP_CONCAT(patenti_mezzo.patente) AS '"+allColumns[allColumns.length-1]+"' FROM mezzo, patenti_mezzo WHERE mezzo.targa=patenti_mezzo.targa AND mezzo.targa='"+numberPlate+"' GROUP BY mezzo.targa";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var truck=getTruck(item);
        return (truck.isDeleted() ? null : truck);
    }

    //insert a new account in the database passing login data
    public void addTruck(Truck truck) {

        var query="insert into mezzo ("+parseParams()+") values ("+addParams()+")"; //empty query
        MySqlQueryManager.execute(connection, query, truck.asList()); //execute insertion with parameters
    }

    //remove account from database (setting the logic deletion true)
    public void removeTruck(Truck truck) {

        var query="update mezzo set deleted=1 where (codice = ?)"; //empty query
        MySqlQueryManager.execute(connection, query, new Object[]{truck.getCode()}); //execute update with parameters
    }

    public void updateTruck(Truck truck) {

        var query="update mezzo set ";
        for (var i=1; i<allColumns.length-2; i++) query+=(allColumns[i]+"=?, ");
        query+=(allColumns[allColumns.length-2]+"=? where codice = '"+truck.getCode()+"'");
        MySqlQueryManager.execute(connection, query, truck.asList());
    }
}
