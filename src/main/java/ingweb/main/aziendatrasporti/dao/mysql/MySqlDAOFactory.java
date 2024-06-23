package ingweb.main.aziendatrasporti.dao.mysql;

import ingweb.main.aziendatrasporti.dao.*;
import ingweb.main.aziendatrasporti.dao.mysql.dao.*;
import java.sql.*;

public class MySqlDAOFactory extends DAOFactory {

    private final Connection connection;

    //set up the MySQL database connection via username/password specifying the database name
    public MySqlDAOFactory(String username, String password, String dbName) {

        var url=MySqlQueryManager.getURL(username, password, dbName); //set database URL location to access its data
        this.connection=MySqlQueryManager.getConnection(url);
    }

    public void confirm() {

        try {

            connection.commit();
            connection.close();
        } catch (SQLException e) {

            e.printStackTrace();
            try { connection.rollback(); }
            catch (Exception x) { x.printStackTrace(); }
        }
    }

    //list of DAO elements implemented by MySQL database
    public AccountDAO getAccountDAO() { return new MySqlAccountDAO(connection); }
    public WorkerDAO getWorkerDAO() { return new MySqlWorkerDAO(connection); }
    public ServiceDAO getServiceDAO() { return new MySqlServiceDAO(connection); }
    public ClientDAO getClientDAO() { return new MySqlClientDAO(connection); }
    public TruckDAO getTruckDAO() { return new MySqlTruckDAO(connection); }
    public LicenseDAO getLicenseDAO() { return new MySqlLicenseDAO(connection); }
}
