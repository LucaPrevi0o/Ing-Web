package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class ServiceController implements Controller {

    public static void getServiceList(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();
        //var serviceList=serviceDAO.findAllNotAssigned();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        //attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceList"});
    }

    public static void getServices(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();
        //var serviceList=serviceDAO.findAllAssigned();

        dao.confirm();
        //attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"viewUrl", "/admin/services/assignedServices"});
        attributes.add(new Object[]{"selectedTab", "services"});
        Controller.commonState(request, response);
    }

    public static void newService(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var clientDAO=dao.getClientDAO();
        var clientList=clientDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"clientList", clientList});
        attributes.add(new Object[]{"viewUrl", "/admin/services/newService"});
        Controller.commonState(request, response);
    }

    public static void addService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get worker DAO implementation for the selected database
        var clientDAO=dao.getClientDAO();
        var licenseDAO=dao.getLicenseDAO();

        var name=request.getParameter("name");
        var clientCompany=request.getParameter("clientCompany");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime")+":00";
        var duration=request.getParameter("duration")+":00";
        var licenses=request.getParameterValues("license");

        var client=clientDAO.findByCode(Integer.parseInt(clientCompany));
        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !clientCompany.isEmpty() && !date.isEmpty() && !startTime.isEmpty() && !duration.isEmpty()) {

            //var service=new Service(name, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);
            //service.setClientCompany(client);
            //serviceDAO.addService(service);
            //service=serviceDAO.findByDateStartTimeAndDuration(service.getDate(), service.getStartTime(), service.getDuration());
            //licenseDAO.addLicensesByService(service, licenseList);
        }

        //var serviceList=serviceDAO.findAllNotAssigned(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        //attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceList"});
        Controller.commonState(request, response);
    }

    public static void removeService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var code=request.getParameter("code");
        //serviceDAO.removeService(serviceDAO.findByCode(Integer.parseInt(code)));

        //var serviceList=serviceDAO.findAllNotAssigned(); //return account list filtered by admin level
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        //attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceList"});
        Controller.commonState(request, response);
    }

    public static void updateService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get worker DAO implementation for the selected database
        var clientDAO=dao.getClientDAO();
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var clientCompany=request.getParameter("clientCompany");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime");
        var duration=request.getParameter("duration");
        var licenses=request.getParameterValues("license");

        var client=clientDAO.findByCode(Integer.parseInt(clientCompany));
        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !date.isEmpty() && !startTime.isEmpty() && !duration.isEmpty()) {

            var service=new Service(Integer.parseInt(code), name, client, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);
            service.setValidLicenses(licenseList);
            //serviceDAO.updateService(service);
            licenseDAO.updateLicensesByService(service, licenseList);
        }

        //var serviceList=serviceDAO.findAllNotAssigned(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        //attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceList"});
        Controller.commonState(request, response);
    }

    public static void editService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get service DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var clientDAO=dao.getClientDAO();
        var licenseList=licenseDAO.findAll();
        var clientList=clientDAO.findAll();

        var code=request.getParameter("code");
        //var service=serviceDAO.findByCode(Integer.parseInt(code));

        dao.confirm();
        //attributes.add(new Object[]{"service", service});
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"clientList", clientList});
        attributes.add(new Object[]{"viewUrl", "/admin/services/newService"});
        Controller.commonState(request, response);
    }

    public static void deleteAssignment(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get service DAO implementation for the selected database
        var code=request.getParameter("code");
        //var service=serviceDAO.findByCode(Integer.parseInt(code));

        //service.setTruck(null);
        //service.setFirstDriver(null);
        //service.setSecondDriver(null);
        //serviceDAO.updateAssignment(service);
        //var serviceList=serviceDAO.findAllAssigned();

        dao.confirm();
        //attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/assignedServices"});
        Controller.commonState(request, response);
    }

    public static void assignService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get service DAO implementation for the selected database
        var workerDAO=dao.getWorkerDAO();
        var truckDAO=dao.getTruckDAO();
        var code=request.getParameter("code");
        //var service=serviceDAO.findByCode(Integer.parseInt(code));
        var workerList=workerDAO.findAll();
        //var truckList=truckDAO.findAllAvailableByService(service);

        dao.confirm();
        //attributes.add(new Object[]{"service", service});
        attributes.add(new Object[]{"workerList", workerList});
        //attributes.add(new Object[]{"truckList", truckList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceAssignment"});
        Controller.commonState(request, response);
    }

    public static void confirmService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get service DAO implementation for the selected database
        var workerDAO=dao.getWorkerDAO();
        var truckDAO=dao.getTruckDAO();

        var code=request.getParameter("code");
        var selectedTruck=request.getParameter("selectedTruck");
        var selectedWorker=request.getParameter("selectedWorker");
        var secondWorker=request.getParameter("secondWorker");

        //var service=serviceDAO.findByCode(Integer.parseInt(code));
        var truck=truckDAO.findByCode(Integer.parseInt(selectedTruck));
        var firstDriver=workerDAO.findByCode(Integer.parseInt(selectedWorker));
        var secondDriver=(secondWorker.equals("none") ? null : workerDAO.findByCode(Integer.parseInt(secondWorker)));

        //service.setFirstDriver(firstDriver);
        //service.setTruck(truck);
        truck.setAvailable(false);
        truckDAO.updateTruck(truck);
        //if (secondDriver!=null && !firstDriver.equals(secondDriver)) service.setSecondDriver(secondDriver);
        //serviceDAO.assignService(service);
        //var serviceList=serviceDAO.findAllAssigned();

        dao.confirm();
        //attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/assignedServices"});
        Controller.commonState(request, response);
    }
}