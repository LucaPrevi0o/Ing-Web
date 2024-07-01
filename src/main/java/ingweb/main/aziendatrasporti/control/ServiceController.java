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

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var serviceDAO=dao.getServiceDAO();
        var licenseDAO=dao.getLicenseDAO();
        var clientDAO=dao.getClientDAO();

        var licenseList=licenseDAO.findAll();
        var serviceList=serviceDAO.findAll();
        for (var service: serviceList) {

            service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));
            service.setValidLicenses(licenseDAO.findAllByService(service));
        }

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"serviceList", serviceList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceList"});
    }

    private static void formView(HttpServletRequest request, HttpServletResponse response,DAOFactory dao) {

        var licenseDAO=dao.getLicenseDAO();
        var clientDAO=dao.getClientDAO();

        var licenseList=licenseDAO.findAll();
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
        var serviceDAO=dao.getServiceDAO();
        var licenseDAO=dao.getLicenseDAO();
        var clientDAO=dao.getClientDAO();

        var name=request.getParameter("name");
        var client=request.getParameter("clientCompany");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime")+":00";
        var duration=request.getParameter("duration")+":00";
        var licenses=request.getParameterValues("license");

        var clientCompany=clientDAO.findByCode(Integer.parseInt(client));
        var code=serviceDAO.findLastCode()+1;
        var service=new Service(code, name, clientCompany, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        service.setValidLicenses(licenseList);

        serviceDAO.addService(service);
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
        var clientDAO=dao.getClientDAO();
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var service=serviceDAO.findByCode(Integer.parseInt(code));
        service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));
        service.setValidLicenses(licenseDAO.findAllByService(service));

        attributes.add(new Object[]{"service", service});
        formView(request, response, dao);
    }

    public static void updateService(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();
        var clientDAO=dao.getClientDAO();
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var client=request.getParameter("clientCompany");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime");
        startTime+=(startTime.matches("[0-9]{2}:[0-9]{2}") ? ":00" : "");
        var duration=request.getParameter("duration");
        duration+=(duration.matches("[0-9]{2}:[0-9]{2}") ? ":00" : "");
        var licenses=request.getParameterValues("license");

        var clientCompany=clientDAO.findByCode(Integer.parseInt(client));
        var service=new Service(Integer.parseInt(code), name, clientCompany, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        service.setValidLicenses(licenseList);

        serviceDAO.updateService(service);
        licenseDAO.updateLicensesByService(service, licenseList);
        listView(request, response, dao);
    }
}