package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Truck;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class TruckController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, ArrayList<Truck> truckList, ArrayList<License> licenseList) {

        attributes.add(new Object[]{"truckList", truckList});
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"viewUrl", "/admin/trucks/trucks"});
        Controller.commonState(request, response, "trucks");
    }

    private static void formView(HttpServletRequest request, HttpServletResponse response, ArrayList<License> licenseList) {

        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"viewUrl", "/admin/trucks/newTruck"});
        Controller.commonState(request, response, null);
    }

    public static void getTrucks(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var truckList=truckDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        listView(request, response, truckList, licenseList);
    }

    public static void newTruck(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        formView(request, response, licenseList);
    }

    public static void addTruck(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
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
        listView(request, response, truckList, licenseList);
    }

    public static void removeTruck(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var code=request.getParameter("code");

        truckDAO.removeTruck(truckDAO.findByCode(Integer.parseInt(code)));
        var truckList=truckDAO.findAll();
        var licenseList=licenseDAO.findAll();

        dao.commit();
        dao.close();
        listView(request, response, truckList, licenseList);
    }

    public static void updateTruck(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
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
        listView(request, response, truckList, licenseList);
    }

    public static void editTruck(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var truckDAO=dao.getTruckDAO();
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var code=request.getParameter("code");
        var truck=truckDAO.findByCode(Integer.parseInt(code));

        dao.commit();
        dao.close();
        attributes.add(new Object[]{"truck", truck});
        formView(request, response, licenseList);
    }
}
