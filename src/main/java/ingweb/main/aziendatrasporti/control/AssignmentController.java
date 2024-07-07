package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.Account;
import ingweb.main.aziendatrasporti.mo.mo.Assignment;
import ingweb.main.aziendatrasporti.mo.mo.Worker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AssignmentController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        var worker=new Worker(null, null, loggedAccount.getUsername(), null, null);
        cookieDAO.confirm();

        var assignmentDAO=dao.getAssignmentDAO();
        var assignmentList=(loggedAccount.getLevel()==Account.ADMIN_LEVEL ? assignmentDAO.findAll() : assignmentDAO.findAllByWorker(worker));
        for (var assignment: assignmentList) {

            var workerDAO=dao.getWorkerDAO();
            assignment.setFirstDriver(workerDAO.findByFiscalCode(assignment.getFirstDriver().getFiscalCode()));
            assignment.setSecondDriver(workerDAO.findByFiscalCode(assignment.getSecondDriver().getFiscalCode()));

            var truckDAO=dao.getTruckDAO();
            assignment.setTruck(truckDAO.findByNumberPlate(assignment.getTruck().getNumberPlate()));

            var serviceDAO=dao.getServiceDAO();
            var service=serviceDAO.findByCode(assignment.getService().getCode());

            var clientDAO=dao.getClientDAO();
            service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));
            assignment.setService(service);
        }

        dao.confirm();
        attributes.add(new Object[]{"assignmentList", assignmentList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", (loggedAccount.getLevel()==Account.ADMIN_LEVEL ? "/admin/assignments/assignments" : "/worker/services/services")});
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
        attributes.add(new Object[]{"viewUrl", "/admin/assignments/newAssignment"});
    }

    public static void getAssignments(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao);
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

    public static void removeAssignment(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var code=request.getParameter("code");

        var assignmentDAO=dao.getAssignmentDAO();
        var assignment=assignmentDAO.findByCode(Integer.parseInt(code));
        assignmentDAO.removeAssignment(assignment);
        listView(request, response, dao);
    }

    public static void completeAssignment(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var code=request.getParameter("code");

        var assignmentDAO=dao.getAssignmentDAO();
        var assignment=assignmentDAO.findByCode(Integer.parseInt(code));
        assignmentDAO.completeAssignment(assignment);
        listView(request, response, dao);
    }
}