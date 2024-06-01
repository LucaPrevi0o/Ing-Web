package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Truck;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;

public class TruckDispatcher implements DispatchCollector {

    private static void commonState(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewUrl", "/admin/trucks/trucks");
        request.setAttribute("selectedTab", "trucks");
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void getTrucks(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        System.out.println("truck dispatcher searching for attribute");
        var attribute=DispatchCollector.findByName("loggedAccount", DispatchCollector.getAllAttributes(request));
        System.out.println("truck dispatcher sees logged account as "+attribute);
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var truckList=truckDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("truckList", truckList);
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("loggedAccount", loggedAccount);
        commonState(request, response);
    }

    public static void newTruck(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        //DispatchCollector.commonView(request);
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/trucks/newTruck");
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void addTruck(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();

        var numberPlate=request.getParameter("numberPlate");
        var brand=request.getParameter("brand");
        var model=request.getParameter("model");
        var available=request.getParameter("available");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        if (!numberPlate.isEmpty() && !brand.isEmpty() && !model.isEmpty()) {

            var truck=new Truck(numberPlate, brand, model, available!=null, false);
            truck.setNeededLicenses(licenseList);
            truckDAO.addTruck(truck);
            licenseDAO.addLicensesByTruck(truck, licenseList);
        }

        var truckList=truckDAO.findAll();
        licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("truckList", truckList);
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("loggedAccount", loggedAccount);
        commonState(request, response);
    }

    public static void removeTruck(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var numberPlate=request.getParameter("name").substring(1);

        truckDAO.removeTruck(truckDAO.findByNumberPlate(numberPlate));
        var truckList=truckDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("truckList", truckList);
        request.setAttribute("licenseList", licenseList);
        commonState(request, response);
    }

    public static void updateTruck(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();

        var numberPlate=request.getParameter("numberPlate");
        var brand=request.getParameter("brand");
        var model=request.getParameter("model");
        var available=request.getParameter("available");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        if (!numberPlate.isEmpty() && !brand.isEmpty() && !model.isEmpty()) {

            var truck=new Truck(numberPlate, brand, model, available!=null, false);
            truck.setNeededLicenses(licenseList);
            truckDAO.updateTruck(truck);
            licenseDAO.updateLicensesByTruck(truck, licenseList);
        }

        var truckList=truckDAO.findAll();
        licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("truckList", truckList);
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("loggedAccount", loggedAccount);
        commonState(request, response);
    }

    public static void editTruck(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var name=request.getParameter("name");
        var data=name.split("\\.");
        var truck=truckDAO.findByNumberPlate(data[0]);

        dao.commit();
        dao.close();
        request.setAttribute("truck", truck); //set list as new session attribute
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/trucks/newTruck"); //set URL for forward view dispatch
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }
}
