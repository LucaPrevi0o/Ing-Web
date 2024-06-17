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
        var serviceList=serviceDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("serviceList", serviceList);
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
            System.out.println(service);
            service.setValidLicenses(licenseList);
            serviceDAO.addService(service);
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
        var code=request.getParameter("name").substring(1);
        serviceDAO.removeService(serviceDAO.findByCode(Integer.parseInt(code)));

        var serviceList=serviceDAO.findAll(); //return account list filtered by admin level
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("serviceList", serviceList);
        commonState(request, response);
    }
}