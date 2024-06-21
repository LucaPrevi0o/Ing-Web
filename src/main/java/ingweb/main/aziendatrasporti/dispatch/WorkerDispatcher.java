package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.mo.Account;
import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Worker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;

public class WorkerDispatcher implements DispatchCollector {

    private static void commonState(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewUrl", "/admin/workers/workers");
        request.setAttribute("selectedTab", "workers");
        request.setAttribute("loggedAccount", DispatchCollector.getAccount(request, response));
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void getWorkers(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var workerList=workerDAO.findAll(); //return account list filtered by admin level
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("workerList", workerList);
        commonState(request, response);
    }

    public static void addWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var newDao=DispatchCollector.getMySqlDAO("aziendatrasportidb");
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
            var account=new Account(fiscalCode, name.toLowerCase()+surname.toLowerCase(), name+" "+surname, false, false);
            accountDAO.addAccount(account);
            worker.setLicenses(licenseList);
            workerDAO.addWorker(worker);
            licenseDAO.addLicensesByWorker(worker, licenseList);
        }

        var workerList=workerDAO.findAll(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        newDao.commit();
        newDao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("workerList", workerList);
        commonState(request, response);
    }

    public static void newWorker(HttpServletRequest request, HttpServletResponse response) {

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/workers/newWorker");
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void removeWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var newDao=DispatchCollector.getMySqlDAO("aziendatrasportidb");
        var accountDAO=newDao.getAccountDAO();
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var worker=workerDAO.findByCode(Integer.parseInt(code));
        workerDAO.removeWorker(worker);
        accountDAO.removeAccount(new Account(worker.getFiscalCode(), null, null, false, false));
        var workerList=workerDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        newDao.commit();
        newDao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("workerList", workerList);
        commonState(request, response);
    }

    public static void updateWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
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

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("workerList", workerList);
        commonState(request, response);
    }

    public static void editWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var newDao=DispatchCollector.getMySqlDAO("aziendatrasportidb");
        var accountDAO=newDao.getAccountDAO();
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var name=request.getParameter("code");
        var worker=workerDAO.findByCode(Integer.parseInt(name));
        accountDAO.removeAccount(new Account(worker.getFiscalCode(), null, null, false, false));

        dao.commit();
        dao.close();
        newDao.commit();
        newDao.close();
        request.setAttribute("worker", worker); //set list as new session attribute
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/workers/newWorker"); //set URL for forward view dispatch
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }
}
