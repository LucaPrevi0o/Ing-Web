package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.Account;
import ingweb.main.aziendatrasporti.mo.mo.License;
import ingweb.main.aziendatrasporti.mo.mo.Worker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;

public class WorkerController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var workerDAO=dao.getWorkerDAO();
        var workerList=workerDAO.findAll();

        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();
        for (var worker: workerList) worker.setLicenses(licenseDAO.findAllByWorker(worker));

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"workerList", workerList});
        attributes.add(new Object[]{"selectedTab", "workers"});
        attributes.add(new Object[]{"viewUrl", "/admin/workers/workers"});
    }

    private static void formView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"selectedTab", "workers"});
        attributes.add(new Object[]{"viewUrl", "/admin/workers/newWorker"});
    }

    public static void getWorkers(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao);
    }

    public static void addWorker(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var newDao=Controller.getMySqlDAO("aziendatrasportidb");

        var name=request.getParameter("name");
        var surname=request.getParameter("surname");
        var fiscalCode=request.getParameter("fiscalCode");
        var birthDate=request.getParameter("birthDate");
        var telNumber=request.getParameter("telNumber");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        var workerDAO=dao.getWorkerDAO();
        var code=workerDAO.findLastCode()+1;

        var worker=new Worker(code, name, surname, fiscalCode, Date.valueOf(birthDate), telNumber, false);
        worker.setLicenses(licenseList);

        var account=new Account(fiscalCode, name.toLowerCase(), name+" "+surname, 1);
        workerDAO.addWorker(worker);
        var accountDAO=newDao.getAccountDAO();
        accountDAO.addAccount(account);
        newDao.confirm();

        var licenseDAO=dao.getLicenseDAO();
        licenseDAO.addLicensesByWorker(worker, licenseList);
        listView(request, response, dao);
    }

    public static void newWorker(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        formView(request, response, dao);
    }

    public static void removeWorker(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var newDao=Controller.getMySqlDAO("aziendatrasportidb");

        var code=request.getParameter("code");
        var workerDAO=dao.getWorkerDAO();
        var worker=workerDAO.findByCode(Integer.parseInt(code));
        workerDAO.removeWorker(worker);

        var accountDAO=newDao.getAccountDAO();
        accountDAO.removeAccount(new Account(worker.getFiscalCode(), null, null, 0));

        newDao.confirm();
        listView(request, response, dao);
    }

    public static void updateWorker(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var surname=request.getParameter("surname");
        var fiscalCode=request.getParameter("fiscalCode");
        var birthDate=request.getParameter("birthDate");
        var telNumber=request.getParameter("telNumber");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        var worker=new Worker(Integer.parseInt(code), name, surname, fiscalCode, Date.valueOf(birthDate), telNumber, false);
        worker.setLicenses(licenseList);

        var workerDAO=dao.getWorkerDAO();
        workerDAO.updateWorker(worker);

        var licenseDAO=dao.getLicenseDAO();
        licenseDAO.updateLicensesByWorker(worker, licenseList);

        listView(request, response, dao);
    }

    public static void editWorker(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var name=request.getParameter("code");

        var workerDAO=dao.getWorkerDAO();
        var worker=workerDAO.findByCode(Integer.parseInt(name));

        var licenseDAO=dao.getLicenseDAO();
        worker.setLicenses(licenseDAO.findAllByWorker(worker));

        attributes.add(new Object[]{"worker", worker});
        formView(request, response, dao);
    }
}
