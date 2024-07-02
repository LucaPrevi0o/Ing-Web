package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.TruckDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
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
    public Truck findByCode(int code) { return select(code); }
    public void addTruck(Truck truck) { insert(truck.asList()); }
    public int findLastCode() { return lastCode(); }
    public void removeTruck(Truck truck) { remove(truck.getCode()); }
    public void updateTruck(Truck truck) { update(truck.asList()); }
}