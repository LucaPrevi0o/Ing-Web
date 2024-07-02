package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.WorkerDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.Worker;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySqlWorkerDAO extends MySqlDAO<Worker> implements WorkerDAO {

    public MySqlWorkerDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Worker get(String[] item) { return new Worker(Integer.parseInt(item[0]), item[1], item[2], item[3], Date.valueOf(item[4]), item[5], item[6].equals("1")); }

    public ArrayList<Worker> findAll() { return select(); }
    public Worker findByCode(int code) { return select(0, code); }
    public void addWorker(Worker worker) { insert(worker.asList()); }
    public int findLastCode() { return lastCode(); }
    public void removeWorker(Worker worker) { remove(worker.getCode()); }
    public void updateWorker(Worker worker) { update(worker.asList()); }
}
