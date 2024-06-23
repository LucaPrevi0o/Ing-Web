package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.dao.cookie.CookieDAOFactory;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAOFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//a DAO factory allows the reference to the database-specific implementation of every DAO by user choice
public abstract class DAOFactory {

    private static final String[] impl={"mysql", "cookie"}; //list of database-specific implementation for every DAO

    //implement different methods for transaction management that follow the same logic;
    //database access can be different between different data sources (files, different DB...) and so is not set as
    //a standard abstract method to implement
    public abstract void confirm();

    //list of methods to return every DAO for different data structure
    public abstract AccountDAO getAccountDAO(); //account (for login management)
    public abstract WorkerDAO getWorkerDAO(); //worker
    public abstract ServiceDAO getServiceDAO();
    public abstract TruckDAO getTruckDAO();
    public abstract ClientDAO getClientDAO();
    public abstract LicenseDAO getLicenseDAO();

    //return a different DAOFactory implementation based on the specified package name and parameters
    public static DAOFactory getByName(String name, Object... args) {

        if (name.equals(impl[0])) { //setup for MySQL database

            if (args.length!=3) return null; //setup for MySQL connection requires access validation to DB schema
            else return new MySqlDAOFactory((String)args[0], (String)args[1], (String)args[2]); //MySQL database implementation
        } else if (name.equals(impl[1])) {

            if (args.length!=2) return null;
            else return new CookieDAOFactory((HttpServletRequest) args[0], (HttpServletResponse) args[1]);
        }

        return null; //null value returned for non-implemented database access packages
    }
}
