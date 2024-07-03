package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.AssignmentDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.*;
import java.sql.Connection;
import java.util.ArrayList;

public class MySqlAssignmentDAO extends MySqlDAO<Assignment> implements AssignmentDAO {

    public MySqlAssignmentDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Assignment get(String[] item) {

        return new Assignment(Integer.parseInt(item[0]),
            new Service(Integer.parseInt(item[1]), null, null, null, null, null, false),
            new Worker(null, null, item[2], null, null),
            new Worker(null, null, item[3], null, null),
            new Truck(item[4], null, null, false), item[5].equals("1")
        );
    }

    public ArrayList<Assignment> findAll() { return selectAll(); }
    public Assignment findByCode(int code) { return select(0, code); }
    public int findLastCode() { return lastCode(); }
    public void addAssignment(Assignment assignment) { insert(assignment.asList()); }
    public void removeAssignment(Assignment assignment) { remove(assignment.getCode()); }
    public void updateAssignment(Assignment assignment) { update(assignment.asList()); }
}