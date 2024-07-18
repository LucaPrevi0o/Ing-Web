package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.mo.ServiceBill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BillController implements Controller {

    public static void newBill(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();

        var licenseDAO=dao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();

        var code=request.getParameter("code");
        var service=serviceDAO.findByCode(Integer.parseInt(code));

        var clientDAO=dao.getClientDAO();
        var client=clientDAO.findBySocialReason(service.getClientCompany().getSocialReason());

        service.setClientCompany(client);
        dao.confirm();

        attributes.add(new Object[]{"service", service});
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/assignments/serviceBill"});
    }

    public static void addBill(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("aziendatrasportidb");
        var newDao=Controller.getMySqlDAO("azienda_trasporti");

        var licenseDAO=newDao.getLicenseDAO();
        var licenseList=licenseDAO.findAll();
        var serviceDAO=newDao.getServiceDAO();

        var serviceCode=request.getParameter("code");
        var service=serviceDAO.findByCode(Integer.parseInt(serviceCode));

        var clientDAO=newDao.getClientDAO();
        var client=clientDAO.findBySocialReason(service.getClientCompany().getSocialReason());
        service.setClientCompany(client);

        var billDAO=dao.getBillDAO();
        var billCode=billDAO.findLastCode();
        var amount=request.getParameter("amount");
        var bankCoordinates=request.getParameter("bankCoordinates");
        var bill=new ServiceBill(billCode, service, null, null, bankCoordinates, Float.parseFloat(amount), false);
        billDAO.addBill(bill);

        var assignmentDAO=newDao.getAssignmentDAO();
        var assignment=assignmentDAO.findByService(service);
        assignmentDAO.removeAssignment(assignment);
        dao.confirm();
        newDao.confirm();
        attributes.add(new Object[]{"service", service});
        attributes.add(new Object[]{"licenseList", licenseList});
        attributes.add(new Object[]{"selectedTab", "services"});
        attributes.add(new Object[]{"viewUrl", "/admin/services/services"});
    }
}
