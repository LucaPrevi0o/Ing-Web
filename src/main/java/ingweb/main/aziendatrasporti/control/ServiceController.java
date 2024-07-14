package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.License;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class ServiceController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao, boolean requested) {

        var serviceDAO=dao.getServiceDAO();
        var serviceList=(requested ? serviceDAO.findAllRequested() : serviceDAO.findAllNotAssigned());

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
        attributes.add(new Object[]{"viewUrl", (requested ? "/admin/services/requests/requests" : "/admin/services/serviceList")});
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
        listView(request, response, dao, false);
    }

    public static void getRequests(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao, true);
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
        var startTime=request.getParameter("startTime");
        startTime+=(startTime.matches("[0-9]{2}:[0-9]{2}") ? ":00" : "");
        var duration=request.getParameter("duration");
        duration+=(duration.matches("[0-9]{2}:[0-9]{2}") ? ":00" : "");
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
        listView(request, response, dao, false);
    }

    public static void removeService(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();

        var code=request.getParameter("code");
        serviceDAO.removeService(serviceDAO.findByCode(Integer.parseInt(code)));
        listView(request, response, dao, false);
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
        listView(request, response, dao, false);
    }
}