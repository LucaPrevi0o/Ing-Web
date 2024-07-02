package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ServiceDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class MySqlServiceDAO extends MySqlDAO<Service> implements ServiceDAO {

    public MySqlServiceDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Service get(String[] item) {

        return new Service(Integer.parseInt(item[0]), item[1],
            new ClientCompany(null, item[2], null, null, null, null, null),
            Date.valueOf(item[3]), Time.valueOf(item[4]), Time.valueOf(item[5]), item[6].equals("1"));
    }

    public ArrayList<Service> findAll() { return select(); }
    public Service findByCode(int code) { return select(0, code); }
    public int findLastCode() { return lastCode(); }
    public void addService(Service service) { insert(service.asList()); }
    public void removeService(Service service) { remove(service.getCode()); }
    public void updateService(Service service) { update(service.asList()); }
}
