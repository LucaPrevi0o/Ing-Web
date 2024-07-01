package ingweb.main.aziendatrasporti.dao.mysql;

import ingweb.main.aziendatrasporti.dao.*;
import ingweb.main.aziendatrasporti.dao.mysql.dao.*;
import java.sql.*;

//DAOFactory MySQL-specific implementation
public class MySqlDAOFactory extends DAOFactory {

    private final Connection connection; //static reference to a valid SQL database connection

    //set up the MySQL database connection via username/password specifying the database name
    public MySqlDAOFactory(String username, String password, String dbName) {

        var url=MySqlQueryManager.getURL(username, password, dbName); //set database URL location to access its data
        this.connection=MySqlQueryManager.getConnection(url); //establish the connection with the database via URL
    }

    //database access requires transaction management in order to have a functional multi-user experience that
    //preserves data validation and concurrent access to the Web Server without interfering
    public void confirm() {

        try { //commit and close the executed transaction after it's done

            connection.commit();
            connection.close();
        } catch (SQLException e) { //execute rollback if a problem occurs and the execution has to be aborted

            e.printStackTrace(); //print out an error message (possible error page redirection?)
            try { connection.rollback(); } //execute rollback if connection is still available
            catch (Exception x) { x.printStackTrace(); } //every other exception is handled here
        }
    }

    //list of DAO elements implemented for the MySQL database; each DAO has a reference to the database connection
    //that needs to be established every time the Web Server starts a new transaction for the request processing
    public AccountDAO getAccountDAO() { return new MySqlAccountDAO(connection); }
    public WorkerDAO getWorkerDAO() { return new MySqlWorkerDAO(connection); }
    public ServiceDAO getServiceDAO() { return new MySqlServiceDAO(connection, "servizio"); }
    public ClientDAO getClientDAO() { return new MySqlClientDAO(connection); }
    public TruckDAO getTruckDAO() { return new MySqlTruckDAO(connection); }
    public LicenseDAO getLicenseDAO() { return new MySqlLicenseDAO(connection); }
    public AssignmentDAO getAssignmentDAO() { return new MySqlAssignmentDAO(connection); }
}
