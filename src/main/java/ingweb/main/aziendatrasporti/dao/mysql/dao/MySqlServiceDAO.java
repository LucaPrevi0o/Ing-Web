package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ServiceDAO;

import java.sql.Connection;

public class MySqlServiceDAO implements ServiceDAO {

    private Connection connection;

    public MySqlServiceDAO(Connection connection) { this.connection=connection; }
}
