package ingweb.main.aziendatrasporti.dao.mysql.dao;
import ingweb.main.aziendatrasporti.dao.AssignmentDAO;

import java.sql.Connection;

public class MySqlAssignmentDAO implements AssignmentDAO {

    private Connection connection;

    public MySqlAssignmentDAO(Connection connection) { this.connection = connection; }
}
