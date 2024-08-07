package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.TruckDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
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

    public ArrayList<Truck> findAll() { return selectAll(); }
    public ArrayList<Truck> findAllAvailable() { return selectAll(new int[]{4}, new Object[]{"1"}); }
    public Truck findByCode(int code) { return select(new int[]{0}, new Object[]{code}); }
    public Truck findByNumberPlate(String numberPlate) { return select(new int[]{1}, new Object[]{numberPlate}); }
    public void addTruck(Truck truck) { insert(truck.asList()); }
    public int findLastCode() { return lastCode(); }
    public void removeTruck(Truck truck) { remove(truck.getCode()); }
    public void updateTruck(Truck truck) { update(truck.asList()); }

    public ArrayList<Truck> findAvailableByService(Service service) {

        var query="SELECT m.* " +
            "FROM mezzo m " +
            "JOIN patenti_mezzo pm ON m.targa = pm.mezzo " +
            "WHERE m.disponibile = '1' AND "+
            "NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM assegnamento a " +
            "    JOIN servizio s ON s.codice = a.servizio" +
            "    WHERE s.data = '"+service.getDate()+"' AND (("+
            "       s.ora_inizio > '"+service.getStartTime()+"' AND s.ora_inizio < ADDTIME('"+service.getStartTime()+"', '"+service.getDuration()+"')"+
            "    ) OR ("+
            "       s.ora_inizio < '" +service.getStartTime()+"' AND '"+service.getStartTime()+"' < ADDTIME(s.ora_inizio, s.durata)"+
            "    )) AND a.mezzo = m.targa" +
            ")" +
            "GROUP BY m.codice " +
            "HAVING NOT EXISTS ( " +
            "    SELECT ps.patente  " +
            "    FROM patenti_servizio ps  " +
            "    WHERE ps.servizio = '"+service.getCode()+"'" +
            "    AND ps.patente NOT IN ( " +
            "        SELECT pm2.patente  " +
            "        FROM patenti_mezzo pm2  " +
            "        WHERE pm2.mezzo = m.targa " +
            "    ) " +
            ")";
        return findList(query);
    }
}