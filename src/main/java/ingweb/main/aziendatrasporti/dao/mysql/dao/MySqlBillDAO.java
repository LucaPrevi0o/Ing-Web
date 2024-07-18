package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.BillDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import ingweb.main.aziendatrasporti.mo.mo.ServiceBill;
import java.sql.Connection;
import java.util.ArrayList;

public class MySqlBillDAO extends MySqlDAO<ServiceBill> implements BillDAO {

    public MySqlBillDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public ServiceBill get(String[] item) {

        return new ServiceBill(Integer.parseInt(item[0]),
            new Service(Integer.parseInt(item[1]), null, null, null, null, null, false),
            item[2], item[3], Float.parseFloat(item[4]), item[5].equals("1"));
    }

    public int findLastCode() { return lastCode(); }
    public ArrayList<ServiceBill> findAll() { return selectAll(); }
    public void addBill(ServiceBill serviceBill) { insert(serviceBill.asList()); }
}
