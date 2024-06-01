package ingweb.main.aziendatrasporti.dispatch;

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
            worker.setLicenses(licenseList);
            workerDAO.addWorker(worker);
            licenseDAO.addLicensesByWorker(worker, licenseList);
        }

        var workerList=workerDAO.findAll(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("workerList", workerList);
        commonState(request, response);
    }

    public static void newWorker(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/workers/newWorker");
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void removeWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();

        var fiscalCode=request.getParameter("name").substring(1);
        workerDAO.removeWorker(workerDAO.findByFiscalCode(fiscalCode));
        var workerList=workerDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("workerList", workerList);
        commonState(request, response);
    }

    public static void updateWorker(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
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
            worker.setLicenses(licenseList);
            workerDAO.updateWorker(worker);
            licenseDAO.updateLicensesByWorker(worker, licenseList);
        }

        var workerList=workerDAO.findAll(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("workerList", workerList);
        commonState(request, response);
    }

    public static void editWorker(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var workerDAO=dao.getWorkerDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var name=request.getParameter("name");
        var data=name.split("\\.");
        var worker=workerDAO.findByFiscalCode(data[2]);

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("worker", worker); //set list as new session attribute
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/workers/newWorker"); //set URL for forward view dispatch
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }
}
