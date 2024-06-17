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
        request.setAttribute("loggedAccount", DispatchCollector.getAccount(request, response));
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void getTrucks(HttpServletRequest request, HttpServletResponse response) {

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var truckList=truckDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("truckList", truckList);
        request.setAttribute("licenseList", licenseList);
        commonState(request, response);
    }

    public static void newTruck(HttpServletRequest request, HttpServletResponse response) {

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/trucks/newTruck");
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void addTruck(HttpServletRequest request, HttpServletResponse response) {

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
        commonState(request, response);
    }

    public static void removeTruck(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var code=request.getParameter("code");

        truckDAO.removeTruck(truckDAO.findByCode(Integer.parseInt(code)));
        var truckList=truckDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("truckList", truckList);
        request.setAttribute("licenseList", licenseList);
        commonState(request, response);
    }

    public static void updateTruck(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();

        var code=request.getParameter("code");
        var numberPlate=request.getParameter("numberPlate");
        var brand=request.getParameter("brand");
        var model=request.getParameter("model");
        var available=request.getParameter("available");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        if (!code.isEmpty() && !numberPlate.isEmpty() && !brand.isEmpty() && !model.isEmpty()) {

            var truck=new Truck(numberPlate, brand, model, available!=null, false);
            truck.setCode(Integer.parseInt(code));
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
        commonState(request, response);
    }

    public static void editTruck(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var code=request.getParameter("code");
        System.out.println(code);
        var truck=truckDAO.findByCode(Integer.parseInt(code));

        dao.commit();
        dao.close();
        request.setAttribute("truck", truck); //set list as new session attribute
        request.setAttribute("licenseList", licenseList);
        request.setAttribute("viewUrl", "/admin/trucks/newTruck"); //set URL for forward view dispatch
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }
}
