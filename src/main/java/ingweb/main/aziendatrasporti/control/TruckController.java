package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.License;
import ingweb.main.aziendatrasporti.mo.mo.Truck;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class TruckController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var truckDAO=dao.getTruckDAO();
        var truckList=truckDAO.findAll();

        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();
        for (var truck: truckList) truck.setNeededLicenses(licenseDAO.findAllByTruck(truck));

        dao.confirm();
        attributes.add(new Object[]{"truckList", truckList});
        attributes.add(new Object[]{"selectedTab", "trucks"});
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"viewUrl", "/admin/trucks/trucks"});
    }

    private static void formView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"selectedTab", "trucks"});
        attributes.add(new Object[]{"viewUrl", "/admin/trucks/newTruck"});
    }

    public static void getTrucks(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao);
    }

    public static void newTruck(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        formView(request, response, dao);
    }

    public static void addTruck(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");

        var numberPlate=request.getParameter("numberPlate");
        var brand=request.getParameter("brand");
        var model=request.getParameter("model");
        var available=request.getParameter("available");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        var truckDAO=dao.getTruckDAO();
        var code=truckDAO.findLastCode()+1;

        var truck=new Truck(code, numberPlate, brand, model, available!=null, false);
        truck.setNeededLicenses(licenseList);
        truckDAO.addTruck(truck);

        var licenseDAO=dao.getLicenseDAO();
        licenseDAO.addLicensesByTruck(truck, licenseList);
        listView(request, response, dao);
    }

    public static void removeTruck(HttpServletRequest request, HttpServletResponse response) {

        var dao= Controller.getMySqlDAO("azienda_trasporti");
        var code=request.getParameter("code");

        var truckDAO=dao.getTruckDAO();
        truckDAO.removeTruck(truckDAO.findByCode(Integer.parseInt(code)));
        listView(request, response, dao);
    }

    public static void updateTruck(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");

        var code=request.getParameter("code");
        var numberPlate=request.getParameter("numberPlate");
        var brand=request.getParameter("brand");
        var model=request.getParameter("model");
        var available=request.getParameter("available");
        var licenses=request.getParameterValues("license");

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));

        var truck=new Truck(Integer.parseInt(code), numberPlate, brand, model, available!=null, false);
        truck.setNeededLicenses(licenseList);

        var truckDAO=dao.getTruckDAO();
        truckDAO.updateTruck(truck);

        var licenseDAO=dao.getLicenseDAO();
        licenseDAO.updateLicensesByTruck(truck, licenseList);
        listView(request, response, dao);
    }

    public static void editTruck(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");

        var code=request.getParameter("code");
        var truckDAO=dao.getTruckDAO();
        var truck=truckDAO.findByCode(Integer.parseInt(code));

        var licenseDAO=dao.getLicenseDAO();
        truck.setNeededLicenses(licenseDAO.findAllByTruck(truck));

        attributes.add(new Object[]{"truck", truck});
        formView(request, response, dao);
    }
}
