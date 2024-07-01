package ingweb.main.aziendatrasporti.dao.cookie;

import ingweb.main.aziendatrasporti.dao.*;
import ingweb.main.aziendatrasporti.dao.cookie.dao.CookieAccountDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//DAOFactory for cookie handling, only used for login management on different accounts
public class CookieDAOFactory extends DAOFactory {

    private final HttpServletRequest request; //HTTP request
    private final HttpServletResponse response; //HTTP response

    public CookieDAOFactory(HttpServletRequest request, HttpServletResponse response) {

        this.request=request;
        this.response=response;
    }

    //each DAO implementation, apart from the AccountDAO, is not present, because cookie handling is not a
    //valid database implementation (and thus is not supposed to return a valid instance for the DAO); only
    //account management is present in both database and cookie implementations, as the cookies enable a fixed
    //set of data from which restore the virtual session without actually implementing a server-side stateful connection
    public void confirm() {}
    public AccountDAO getAccountDAO() { return new CookieAccountDAO(request, response); }
    public WorkerDAO getWorkerDAO() { return null; }
    public ServiceDAO getServiceDAO() { return null; }
    public TruckDAO getTruckDAO() { return null; }
    public ClientDAO getClientDAO() { return null; }
    public LicenseDAO getLicenseDAO() { return null; }
    public AssignmentDAO getAssignmentDAO() { return null; }
}
