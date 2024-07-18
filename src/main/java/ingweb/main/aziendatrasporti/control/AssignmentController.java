package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Time;
import java.util.ArrayList;

public class AssignmentController implements Controller {

    private static ArrayList<Assignment> fillAssignments(DAOFactory dao, ArrayList<Assignment> assignmentList) {

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

        return assignmentList;
    }

    private static ArrayList<Assignment> getAssignmentList(DAOFactory dao, Account loggedAccount, boolean completed) {

        var assignmentList=getAssignmentListByAccount(dao, loggedAccount, completed);
        return fillAssignments(dao, assignmentList);
    }

    private static ArrayList<Assignment> getAssignmentListByAccount(DAOFactory dao, Account loggedAccount, boolean completed) {

        var worker=new Worker(null, null, loggedAccount.getUsername(), null, null);
        var client=new ClientCompany(null, loggedAccount.getPassword(), null, null, null, null, null);

        var assignmentDAO=dao.getAssignmentDAO();
        return (loggedAccount.getLevel()==Account.ADMIN_LEVEL ? (completed ? assignmentDAO.findAllCompleted() : assignmentDAO.findAll()) :
            (loggedAccount.getLevel()==Account.MANAGER_LEVEL ? assignmentDAO.findAllByClientCompany(client) :
            (loggedAccount.getLevel()==Account.WORKER_LEVEL ? assignmentDAO.findAllByWorker(worker) : null)));
    }

    private static String getViewURL(Account loggedAccount, String urlName) {

        var url="/";
        url+=(loggedAccount.getLevel()==Account.ADMIN_LEVEL ? "admin/" :
            (loggedAccount.getLevel()==Account.MANAGER_LEVEL ? "clientManager/" :
            (loggedAccount.getLevel()==Account.WORKER_LEVEL ? "worker/" : "")));
        return url+"assignments/"+urlName;
    }

    private static void commonAttributes(HttpServletRequest request, HttpServletResponse response, String viewUrl) {

        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", getViewURL(loggedAccount, viewUrl)});
    }

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao, boolean completed) {

        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();

        var assignmentList=getAssignmentList(dao, loggedAccount, completed);
        dao.confirm();
        attributes.add(new Object[]{"assignmentList", assignmentList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", getViewURL(loggedAccount, (completed ? "completed" : "assignments"))});
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
        commonAttributes(request, response, "newAssignment");
    }

    private static void problemView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var code=request.getParameter("code");
        var assignmentDAO=dao.getAssignmentDAO();
        var assignment=assignmentDAO.findByCode(Integer.parseInt(code));
        dao.confirm();

        attributes.add(new Object[]{"assignment", assignment});
        commonAttributes(request, response, "serviceProblem");
    }

    private static void detailsView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var code=request.getParameter("code");
        var assignmentDAO=dao.getAssignmentDAO();
        var assignment=assignmentDAO.findByCode(Integer.parseInt(code));
        var serviceDAO=dao.getServiceDAO();
        var service=serviceDAO.findByCode(assignment.getService().getCode());

        var clientDAO=dao.getClientDAO();
        service.setClientCompany(clientDAO.findBySocialReason(service.getClientCompany().getSocialReason()));
        assignment.setService(service);

        var truckDAO=dao.getTruckDAO();
        assignment.setTruck(truckDAO.findByNumberPlate(assignment.getTruck().getNumberPlate()));

        dao.confirm();
        attributes.add(new Object[]{"assignment", assignment});
        commonAttributes(request, response, "details");
    }

    public static void getAssignments(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao, false);
    }

    public static void getCompleted(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao, true);
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
        var assignment=new Assignment(code, service, firstDriver, secondDriver, truck, null, false, false);
        assignmentDAO.addAssignment(assignment);
        listView(request, response, dao, false);
    }

    public static void removeAssignment(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var code=request.getParameter("code");

        var assignmentDAO=dao.getAssignmentDAO();
        var assignment=assignmentDAO.findByCode(Integer.parseInt(code));
        assignmentDAO.deleteAssignment(assignment);
        listView(request, response, dao, false);
    }

    public static void completeAssignment(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var code=request.getParameter("code");

        var assignmentDAO=dao.getAssignmentDAO();
        var assignment=assignmentDAO.findByCode(Integer.parseInt(code));
        assignment.setCompleted(true);
        assignmentDAO.updateAssignment(assignment);
        listView(request, response, dao, false);
    }

    public static void newProblem(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        problemView(request, response, dao);
    }

    public static void signalAssignment(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var code=request.getParameter("code");
        var comment=request.getParameter("comment");

        var assignmentDAO=dao.getAssignmentDAO();
        var assignment=assignmentDAO.findByCode(Integer.parseInt(code));
        assignment.setComment(comment);
        assignmentDAO.updateAssignment(assignment);
        listView(request, response, dao, false);
    }

    public static void viewDetails(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        detailsView(request, response, dao);
    }

    public static void openRequest(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        dao.confirm();

        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"selectedTab", "request"});
        attributes.add(new Object[]{"viewUrl", getViewURL(loggedAccount, "requestService")});
    }

    public static void requestService(HttpServletRequest request, HttpServletResponse response) {

        var mySqlDAO=Controller.getMySqlDAO("azienda_trasporti");
        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();

        var clientDAO=mySqlDAO.getClientDAO();
        var client=clientDAO.findBySocialReason(loggedAccount.getPassword());

        var serviceDAO=mySqlDAO.getServiceDAO();
        var name=request.getParameter("name");
        var duration=request.getParameter("duration");
        duration+=(duration.matches("[0-9]{2}:[0-9]{2}") ? ":00" : "");
        var licenses=request.getParameterValues("license");

        var code=serviceDAO.findLastCode()+1;
        var service=new Service(code, name, client, null, null, Time.valueOf(duration), false);

        var licenseList=new ArrayList<License>();
        for (var license: licenses) licenseList.add(new License(license));
        service.setValidLicenses(licenseList);
        serviceDAO.addService(service);

        var licenseDAO=mySqlDAO.getLicenseDAO();
        licenseDAO.addLicensesByService(service);

        var assignmentList=getAssignmentList(mySqlDAO, loggedAccount, false);
        mySqlDAO.confirm();
        attributes.add(new Object[]{"assignmentList", assignmentList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", getViewURL(loggedAccount, "assignments")});
    }
}