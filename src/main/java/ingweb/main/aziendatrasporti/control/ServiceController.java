package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.Account;
import ingweb.main.aziendatrasporti.mo.mo.License;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class ServiceController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var serviceDAO=dao.getServiceDAO();
        var serviceList=serviceDAO.findAllNotAssigned();

        for (var service: serviceList) {

            var clientDAO=dao.getClientDAO();
            service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));

            var licenseDAO=dao.getLicenseDAO();
            service.setValidLicenses(licenseDAO.findAllByService(service));
        }

        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceList"});
    }

    private static void formView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var clientDAO=dao.getClientDAO();
        var clientList=clientDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"clientList", clientList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/newService"});
    }

    public static void getServices(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao);
    }

    public static void newService(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        formView(request, response, dao);
    }

    public static void addService(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var name=request.getParameter("name");
        var client=request.getParameter("clientCompany");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime")+":00";
        var duration=request.getParameter("duration")+":00";
        var licenses=request.getParameterValues("license");

        var clientDAO=dao.getClientDAO();
        var clientCompany=clientDAO.findByCode(Integer.parseInt(client));

        var serviceDAO=dao.getServiceDAO();
        var code=serviceDAO.findLastCode()+1;
        var service=new Service(code, name, clientCompany, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        service.setValidLicenses(licenseList);
        serviceDAO.addService(service);

        var licenseDAO=dao.getLicenseDAO();
        licenseDAO.addLicensesByService(service);
        listView(request, response, dao);
    }

    public static void removeService(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();

        var code=request.getParameter("code");
        serviceDAO.removeService(serviceDAO.findByCode(Integer.parseInt(code)));
        listView(request, response, dao);
    }

    public static void editService(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();

        var code=request.getParameter("code");
        var service=serviceDAO.findByCode(Integer.parseInt(code));

        var clientDAO=dao.getClientDAO();
        service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));

        var licenseDAO=dao.getLicenseDAO();
        service.setValidLicenses(licenseDAO.findAllByService(service));

        attributes.add(new Object[]{"service", service});
        formView(request, response, dao);
    }

    public static void updateService(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var client=request.getParameter("clientCompany");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime");
        startTime+=(startTime.matches("[0-9]{2}:[0-9]{2}") ? ":00" : "");
        var duration=request.getParameter("duration");
        duration+=(duration.matches("[0-9]{2}:[0-9]{2}") ? ":00" : "");
        var licenses=request.getParameterValues("license");

        var clientDAO=dao.getClientDAO();
        var clientCompany=clientDAO.findByCode(Integer.parseInt(client));
        var service=new Service(Integer.parseInt(code), name, clientCompany, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        service.setValidLicenses(licenseList);

        var serviceDAO=dao.getServiceDAO();
        serviceDAO.updateService(service);

        var licenseDAO=dao.getLicenseDAO();
        licenseDAO.updateLicensesByService(service, licenseList);
        listView(request, response, dao);
    }

    public static void requestService(HttpServletRequest request, HttpServletResponse response) {

        var mySqlDAO=Controller.getMySqlDAO("azienda_trasporti");
        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();

        var clientDAO=mySqlDAO.getClientDAO();
        var client=clientDAO.findBySocialReason(loggedAccount.getPassword());

        var serviceDAO=mySqlDAO.getServiceDAO();
        var name=request.getParameter("name");
        var date=request.getParameter("date");
        var duration=request.getParameter("duration");
        var licenses=request.getParameterValues("license");

        var code=serviceDAO.findLastCode()+1;
        var service=new Service(code, name, client, Date.valueOf(date), null, Time.valueOf(duration), false);

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        service.setValidLicenses(licenseList);
        serviceDAO.addService(service);

        var assignmentDAO=mySqlDAO.getAssignmentDAO();
        var assignmentList=assignmentDAO.findAllByClientCompany(client);
        for (var assignment: assignmentList) {

            var workerDAO=mySqlDAO.getWorkerDAO();
            assignment.setFirstDriver(workerDAO.findByFiscalCode(assignment.getFirstDriver().getFiscalCode()));
            assignment.setSecondDriver(workerDAO.findByFiscalCode(assignment.getSecondDriver().getFiscalCode()));

            var truckDAO=mySqlDAO.getTruckDAO();
            assignment.setTruck(truckDAO.findByNumberPlate(assignment.getTruck().getNumberPlate()));

            serviceDAO=mySqlDAO.getServiceDAO();
            service=serviceDAO.findByCode(assignment.getService().getCode());

            clientDAO=mySqlDAO.getClientDAO();
            service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));
            assignment.setService(service);
        }

        mySqlDAO.confirm();
        attributes.add(new Object[]{"assignmentList", assignmentList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/assignments/assignments"});
    }
}