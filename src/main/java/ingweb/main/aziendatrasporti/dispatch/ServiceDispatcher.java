package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Worker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class ServiceDispatcher implements DispatchCollector {

    private static void commonState(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewUrl", "/admin/services/serviceList");
        request.setAttribute("selectedTab", "services");
        request.setAttribute("loggedAccount", DispatchCollector.getAccount(request, response));
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void getServiceList(HttpServletRequest request, HttpServletResponse response) {

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();
        var serviceList=serviceDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("serviceList", serviceList);
        request.setAttribute("licenseList", licenseList);
        commonState(request, response);
    }

    public static void newService(HttpServletRequest request, HttpServletResponse response) {

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/services/newService");
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void addService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();

        var name=request.getParameter("name");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime")+":00";
        var duration=request.getParameter("duration")+":00";
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !date.isEmpty() && !startTime.isEmpty() && !duration.isEmpty()) {

            var service=new Service(name, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);
            serviceDAO.addService(service);
            service=serviceDAO.findByDateStartTimeAndDuration(service.getDate(), service.getStartTime(), service.getDuration());
            licenseDAO.addLicensesByService(service, licenseList);
        }

        var serviceList=serviceDAO.findAll(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("serviceList", serviceList);
        commonState(request, response);
    }

    public static void removeService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var code=request.getParameter("code");
        serviceDAO.removeService(serviceDAO.findByCode(Integer.parseInt(code)));

        var serviceList=serviceDAO.findAll(); //return account list filtered by admin level
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("serviceList", serviceList);
        commonState(request, response);
    }

    public static void updateService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get worker DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var date=request.getParameter("date");
        var startTime=request.getParameter("startTime");
        var duration=request.getParameter("duration");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !date.isEmpty() && !startTime.isEmpty() && !duration.isEmpty()) {

            var service=new Service(name, Date.valueOf(date), Time.valueOf(startTime), Time.valueOf(duration), false);
            service.setCode(Integer.parseInt(code));
            service.setValidLicenses(licenseList);
            serviceDAO.updateService(service);
            licenseDAO.updateLicensesByService(service, licenseList);
        }

        var serviceList=serviceDAO.findAll(); //return account list filtered by admin level
        licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("serviceList", serviceList);
        commonState(request, response);
    }

    public static void editService(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO(); //get service DAO implementation for the selected database
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var code=request.getParameter("code");
        var service=serviceDAO.findByCode(Integer.parseInt(code));

        dao.commit();
        dao.close();
        request.setAttribute("service", service); //set list as new session attribute
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/services/newService"); //set URL for forward view dispatch
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }
}