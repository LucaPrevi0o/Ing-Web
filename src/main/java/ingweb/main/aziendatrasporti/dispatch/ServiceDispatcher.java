package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Truck;
import ingweb.main.aziendatrasporti.mo.Worker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class ServiceDispatcher implements DispatchCollector {

    public static void getServices(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();
        var workerDAO=dao.getWorkerDAO();
        var serviceList=serviceDAO.findAll();
        var workerList=workerDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request);
        request.setAttribute("serviceList", serviceList);
        request.setAttribute("workerList", workerList);
        request.setAttribute("viewUrl", "/admin/services/services"); //set URL for forward view dispatch
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void getResources(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var workerDAO=dao.getWorkerDAO();
        var clientDAO=dao.getClientDAO();
        var licenseDAO=dao.getLicenseDAO();
        var truckList=truckDAO.findAll();
        var workerList=workerDAO.findAll();
        var clientList=clientDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request);
        request.setAttribute("truckList", truckList);
        request.setAttribute("workerList", workerList);
        request.setAttribute("clientList", clientList);
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/services/newService");
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void addService(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();
        var workerDAO=dao.getWorkerDAO();
        var clientDAO=dao.getClientDAO();
        var truckDAO=dao.getTruckDAO();

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var client=request.getParameter("client");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime");
        var duration=request.getParameter("duration");
        var truck=request.getParameter("truck");
        var workers=request.getParameterValues("worker");

        var clientCompany=clientDAO.findBySocialReason(client);
        var firstDriver=workerDAO.findByFiscalCode(workers[0]);
        var secondDriver=workerDAO.findByFiscalCode(workers[1]);
        var usedTruck=truckDAO.findByNumberPlate(truck);
        var service=new Service(Integer.parseInt(code), name, clientCompany, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), firstDriver, secondDriver, usedTruck, false);
        serviceDAO.addService(service);

        var serviceList=serviceDAO.findAll();
        dao.commit();
        dao.close();
        DispatchCollector.commonView(request);
        request.setAttribute("serviceList", serviceList);
        request.setAttribute("viewUrl", "/admin/services/services");
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void newService(HttpServletRequest request, HttpServletResponse response) {

        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var workerDAO=dao.getWorkerDAO();
        var truckDAO=dao.getTruckDAO();
        var serviceDAO=dao.getServiceDAO();
        var clientDAO=dao.getClientDAO();
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var client=request.getParameter("client");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime");
        var duration=request.getParameter("duration");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        Service service=null;
        for (var license: licenses) licenseList.add(new License(license));

        var clientCompany=clientDAO.findBySocialReason(client);
        if (!code.isEmpty() && name.isEmpty() && clientCompany!=null && date.isEmpty() && startTime.isEmpty() && duration.isEmpty()) {

            service=new Service(Integer.parseInt(code), name, clientCompany, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);
            service.setValidLicenses(licenseList);
            serviceDAO.addService(service);
            licenseDAO.addLicensesByService(service, licenseList);
        }

        var validWorkers=new ArrayList<Worker>();
        var validTrucks=new ArrayList<Truck>();

        for (var worker: workerDAO.findAll())
            if (worker.getLicenses().equals(service.getValidLicenses())) validWorkers.add(worker);

        for (var truck: truckDAO.findAll())
            if (truck.getNeededLicenses().equals(service.getValidLicenses())) validTrucks.add(truck);
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DispatchCollector.commonView(request);
        request.setAttribute("viewUrl", "/admin/services/services"); //set URL for forward view dispatch
    }
}
