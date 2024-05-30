package ingweb.main.aziendatrasporti.dao.cookie;

import ingweb.main.aziendatrasporti.dao.*;
import ingweb.main.aziendatrasporti.dao.cookie.dao.CookieAccountDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieDAOFactory extends DAOFactory {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public CookieDAOFactory(HttpServletRequest request, HttpServletResponse response) {

        this.request=request;
        this.response=response;
    }

    public void commit() {

    }

    public void rollback() {

    }

    public void close() {

    }

    public AccountDAO getAccountDAO() { return new CookieAccountDAO(request, response); }
    public WorkerDAO getWorkerDAO() { return null; }
    public ServiceDAO getServiceDAO() { return null; }
    public TruckDAO getTruckDAO() { return null; }
    public ClientDAO getClientDAO() { return null; }
    public LicenseDAO getLicenseDAO() { return null; }
}
