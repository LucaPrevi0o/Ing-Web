package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.Assignment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AssignmentController implements Controller {

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

        var code=request.getParameter("code");
        var serviceDAO=dao.getServiceDAO();
        var service=serviceDAO.findByCode(Integer.parseInt(code));

        var clientDAO=dao.getClientDAO();
        service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));

        var workerDAO=dao.getWorkerDAO();
        var workerList=workerDAO.findAvailableByService(service);

        var truckDAO=dao.getTruckDAO();
        var truckList=truckDAO.findAvailableByService(service);

        dao.confirm();
        attributes.add(new Object[]{"service", service});
        attributes.add(new Object[]{"workerList", workerList});
        attributes.add(new Object[]{"truckList", truckList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/serviceAssignment"});
    }

    public static void newAssignment(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        formView(request, response, dao);
    }

    public static void addAssignment(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var serviceCode=request.getParameter("code");
        var selectedTruck=request.getParameter("selectedTruck");
        var firstWorker=request.getParameter("firstWorker");
        var secondWorker=request.getParameter("secondWorker");

        var serviceDAO=dao.getServiceDAO();
        var service=serviceDAO.findByCode(Integer.parseInt(serviceCode));

        var truckDAO=dao.getTruckDAO();
        var truck=truckDAO.findByCode(Integer.parseInt(selectedTruck));

        var workerDAO=dao.getWorkerDAO();
        var firstDriver=workerDAO.findByCode(Integer.parseInt(firstWorker));
        var secondDriver=(secondWorker.equals("none") ? null : workerDAO.findByCode(Integer.parseInt(secondWorker)));

        var assignmentDAO=dao.getAssignmentDAO();
        var code=assignmentDAO.findLastCode()+1;
        var assignment=new Assignment(code, service, firstDriver, secondDriver, truck, false);
        assignmentDAO.addAssignment(assignment);
        listView(request, response, dao);
    }
}