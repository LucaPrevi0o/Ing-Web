package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.TruckDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.License;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import ingweb.main.aziendatrasporti.mo.mo.Truck;
import java.sql.Connection;
import java.util.ArrayList;

public class MySqlTruckDAO extends MySqlDAO<Truck> implements TruckDAO {

    public MySqlTruckDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Truck get(String[] item) { return new Truck(Integer.parseInt(item[0]), item[1], item[2], item[3], item[4].equals("1"), item[5].equals("1")); }

    public ArrayList<Truck> findAll() { return select(); }
    public Truck findByCode(int code) { return select(0, code); }
    public void addTruck(Truck truck) { insert(truck.asList()); }
    public int findLastCode() { return lastCode(); }
    public void removeTruck(Truck truck) { remove(truck.getCode()); }
    public void updateTruck(Truck truck) { update(truck.asList()); }

    public ArrayList<Truck> findAvailableByService(Service service) {

        var result=new ArrayList<Truck>();
        var query=
            "SELECT m.* " +
            "FROM mezzo m " +
            "JOIN patenti_mezzo pm ON m.targa = pm.targa " +
            "WHERE m.disponibile = 1 " +
            "AND NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM assegnamento a " +
            "    JOIN servizio s ON s.codice = a.servizio" +
            "    WHERE s.data = (" +
            "       select s2.data from servizio s2" +
            "       where s2.codice = '"+service.getCode()+"'" +
            "    )" +
            "    AND s.ora_inizio = (" +
            "       select s2.ora_inizio from servizio s2" +
            "       where s2.codice = '"+service.getCode()+"'" +
            "    )" +
            "    AND s.durata = (" +
            "       select s2.durata from servizio s2" +
            "       where s2.codice = '"+service.getCode()+"'" +
            "    )" +
            "    AND a.mezzo = m.targa" +
            ")" +
            "GROUP BY m.codice " +
            "HAVING NOT EXISTS ( " +
            "    SELECT ps.patente  " +
            "    FROM patenti_servizio ps  " +
            "    WHERE ps.servizio = '"+service.getCode()+"'" +
            "    AND ps.patente NOT IN ( " +
            "        SELECT pm2.patente  " +
            "        FROM patenti_mezzo pm2  " +
            "        WHERE pm2.targa = m.targa " +
            "    ) " +
            ")";

        var res=MySqlQueryManager.getResult(getConnection(), query);
        var resList=MySqlQueryManager.asList(res, getColumns());
        for (var item: resList) {

            var truck=get(item);
            if (!truck.isDeleted()) result.add(truck);
        }

        return result;
    }
}