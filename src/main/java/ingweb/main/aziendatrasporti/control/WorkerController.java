package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.mo.Account;
import ingweb.main.aziendatrasporti.mo.mo.License;
import ingweb.main.aziendatrasporti.mo.mo.Worker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;

public class WorkerController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, ArrayList<Worker> workerList, ArrayList<License> licenseList) {

        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"workerList", workerList});
        attributes.add(new Object[]{"selectedTab", "workers"});
        attributes.add(new Object[]{"viewUrl", "/admin/workers/workers"});
    }

    private static void formView(HttpServletRequest request, HttpServletResponse response, ArrayList<License> licenseList) {

        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"selectedTab", "workers"});
        attributes.add(new Object[]{"viewUrl", "/admin/workers/newWorker"});
    }

    public static void getWorkers(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var workerList=workerDAO.findAll(); //return account list filtered by admin level
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        listView(request, response, workerList, licenseList);
    }

    public static void addWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var newDao= Controller.getMySqlDAO("aziendatrasportidb");
        var accountDAO=newDao.getAccountDAO();
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();

        var name=request.getParameter("name");
        var surname=request.getParameter("surname");
        var fiscalCode=request.getParameter("fiscalCode");
        var birthDate=request.getParameter("birthDate");
        var telNumber=request.getParameter("telNumber");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !surname.isEmpty() && !fiscalCode.isEmpty() && !birthDate.isEmpty() && !telNumber.isEmpty()) {

            var worker=new Worker(name, surname, fiscalCode, Date.valueOf(birthDate), telNumber, false);
            var account=new Account(fiscalCode, name.toLowerCase(), name+" "+surname, false, false);
            accountDAO.addAccount(account);
            worker.setLicenses(licenseList);
            workerDAO.addWorker(worker);
            licenseDAO.addLicensesByWorker(worker, licenseList);
        }

        var workerList=workerDAO.findAll(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.confirm();
        newDao.confirm();
        listView(request, response, workerList, licenseList);
    }

    public static void newWorker(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        formView(request, response, licenseList);
    }

    public static void removeWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var newDao= Controller.getMySqlDAO("aziendatrasportidb");
        var accountDAO=newDao.getAccountDAO();
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var worker=workerDAO.findByCode(Integer.parseInt(code));
        workerDAO.removeWorker(worker);
        accountDAO.removeAccount(new Account(worker.getFiscalCode(), null, null, false, false));
        var workerList=workerDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        newDao.confirm();
        listView(request, response, workerList, licenseList);
    }

    public static void updateWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var surname=request.getParameter("surname");
        var fiscalCode=request.getParameter("fiscalCode");
        var birthDate=request.getParameter("birthDate");
        var telNumber=request.getParameter("telNumber");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !surname.isEmpty() && !fiscalCode.isEmpty() && !birthDate.isEmpty() && !telNumber.isEmpty()) {

            var worker=new Worker(name, surname, fiscalCode, Date.valueOf(birthDate), telNumber, false);
            worker.setCode(Integer.parseInt(code));
            worker.setLicenses(licenseList);
            workerDAO.updateWorker(worker);
            licenseDAO.updateLicensesByWorker(worker, licenseList);
        }

        var workerList=workerDAO.findAll(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.confirm();
        listView(request, response, workerList, licenseList);
    }

    public static void editWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var newDao=Controller.getMySqlDAO("aziendatrasportidb");
        var accountDAO=newDao.getAccountDAO();
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var name=request.getParameter("code");
        var worker=workerDAO.findByCode(Integer.parseInt(name));
        accountDAO.removeAccount(new Account(worker.getFiscalCode(), null, null, false, false));

        dao.confirm();
        newDao.confirm();
        attributes.add(new Object[]{"worker", worker});
        formView(request, response, licenseList);
    }
}
