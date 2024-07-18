package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.mo.Account;
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
        var bill=new ServiceBill(billCode, service, null, bankCoordinates, Float.parseFloat(amount), false);
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

    public static void getBills(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("aziendatrasportidb");
        var newDao=Controller.getMySqlDAO("azienda_trasporti");
        var cookieDao=Controller.getCookieDAO(request, response);
        var accountDAO=cookieDao.getAccountDAO();
        var account=accountDAO.findLoggedAccount();

        var billDAO=dao.getBillDAO();
        var billList=billDAO.findAll();
        for (var bill: billList) {

            var serviceDAO=newDao.getServiceDAO();
            var service=serviceDAO.findByCode(bill.getService().getCode());

            var clientDAO=newDao.getClientDAO();
            var client=clientDAO.findBySocialReason(service.getClientCompany().getSocialReason());
            service.setClientCompany(client);
            bill.setService(service);
        }

        dao.confirm();
        newDao.confirm();
        attributes.add(new Object[]{"billList", billList});
        attributes.add(new Object[]{"selectedTab", "bills"});
        attributes.add(new Object[]{"viewUrl", (account.getLevel()==Account.MANAGER_LEVEL ? "/clientManager/" :
            (account.getLevel()==Account.ADMIN_LEVEL ? "/admin/" :
            (account.getLevel()==Account.WORKER_LEVEL ? "/worker/" : "")))+"bills/bills"});
    }
}
