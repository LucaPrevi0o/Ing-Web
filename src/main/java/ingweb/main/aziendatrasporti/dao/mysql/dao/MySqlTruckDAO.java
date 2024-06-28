package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.TruckDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Truck;
import java.sql.Connection;
import java.util.ArrayList;

public class MySqlTruckDAO implements TruckDAO {

    private final Connection connection;
    private String[] data={"codice", "targa", "marca", "modello", "disponibile", "deleted"};
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
        if (item.length>=6) {

            var licenses=new ArrayList<License>();
            for (var license: item[6].split(",")) licenses.add(new License(license));
            truck.setNeededLicenses(licenses);}
        return truck;
    }

    private Truck get(String[] item) {

        return new Truck(Integer.parseInt(item[0]), item[1], item[2], item[3], item[4].equals("1"), item[5].equals("1"));
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

    public ArrayList<Truck> findAllByLicenses(ArrayList<License> licenses) {

        var result=new ArrayList<Truck>(); //empty list
        for (var license: licenses) {

            var query="select mezzo.*, group_concat(patenti_mezzo.patente) as '"+allColumns[allColumns.length-1]+"' from mezzo join patenti_mezzo on mezzo.targa=patenti_mezzo.targa where mezzo.disponibile=1 and patenti_mezzo.patente='"+license.getCategory()+"' group by mezzo.targa";
            var trucks=MySqlQueryManager.getResult(connection, query); //execute query on the database
            var trucksList=MySqlQueryManager.asList(trucks, allColumns); //parse results
            for (var item: trucksList) { //add every element of the result set as new worker

                //parse obtained result as correct data type
                var truck=getTruck(item);
                if (!truck.isDeleted()) result.add(truck); //add worker to the result list if not set as deleted
            }
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

    public ArrayList<Truck> findAllAvailableByService(Service service) {

        var result=new ArrayList<Truck>(); //empty list
        var query="SELECT DISTINCT m.*, GROUP_CONCAT(pm.patente) AS patenti \n" +
                "FROM mezzo m \n" +
                "JOIN patenti_mezzo pm ON m.targa = pm.targa \n" +
                "WHERE m.disponibile = 1 \n" +
                "GROUP BY m.codice \n" +
                "HAVING NOT EXISTS (\n" +
                "    SELECT ps.patente \n" +
                "    FROM patenti_servizio ps \n" +
                "    WHERE ps.servizio = (\n" +
                "        SELECT s.codice \n" +
                "        FROM servizio s \n" +
                "        WHERE s.data = "+service.getDate()+"\n" +
                "        AND (\n" +
                "            (s.ora_inizio < "+service.getStartTime()+" AND ADDTIME(s.ora_inizio, s.durata) > "+service.getStartTime()+") OR \n" +
                "            (s.ora_inizio >= @startTime AND s.ora_inizio <= ADDTIME("+service.getStartTime()+", "+service.getDuration()+"))\n" +
                "        )\n" +
                "    )\n" +
                "    AND ps.patente NOT IN (\n" +
                "        SELECT pm2.patente \n" +
                "        FROM patenti_mezzo pm2 \n" +
                "        WHERE pm2.targa = m.targa\n" +
                "    )\n" +
                ");";

        System.out.println(query);
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, data);
        for (var item: resList) { //add every element of the result set as new worker

            //parse obtained result as correct data type
            var truck=get(item);
            if (!truck.isDeleted()) result.add(truck); //add worker to the result list if not set as deleted
        }

        return result; //return list of valid trucks
    }

    public ArrayList<Truck> findAllAssigned() {

        var result=new ArrayList<Truck>();
        var query="SELECT m.*, group_concat(pm.patente) as patenti FROM mezzo m JOIN patenti_mezzo pm ON m.targa = pm.targa join servizio s on s.targa_mezzo =m.targa GROUP BY m.codice";
        var trucks=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var trucksList=MySqlQueryManager.asList(trucks, allColumns); //parse results
        for (var item: trucksList) { //add every element of the result set as new worker

            //parse obtained result as correct data type
            var truck=getTruck(item);
            if (!truck.isDeleted()) result.add(truck); //add worker to the result list if not set as deleted
        }

        return result; //return list of valid trucks
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
