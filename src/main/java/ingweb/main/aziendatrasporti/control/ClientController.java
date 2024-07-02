package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;

public class ClientController implements Controller {

    private static void listView(HttpServletRequest request, HttpServletResponse response, DAOFactory dao) {

        var clientDAO=dao.getClientDAO();
        var clientList=clientDAO.findAll();

        dao.confirm();
        attributes.add(new Object[]{"clientList", clientList});
        attributes.add(new Object[]{"selectedTab", "clients"});
        attributes.add(new Object[]{"viewUrl", "/admin/clients/clientCompanies"});
    }

    private static void formView(HttpServletRequest request, HttpServletResponse response) {

        attributes.add(new Object[]{"viewUrl", "/admin/clients/newClientCompany"});
    }

    public static void getClients(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        listView(request, response, dao);
    }

    public static void newClient(HttpServletRequest request, HttpServletResponse response) { formView(request, response); }

    public static void addClient(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO(); //get worker DAO implementation for the selected database

        var name=request.getParameter("name");
        var socialReason=request.getParameter("socialReason");
        var location=request.getParameter("location");
        var managerName=request.getParameter("managerName");
        var managerFiscalCode=request.getParameter("managerFiscalCode");
        var managerBirthDate=request.getParameter("managerBirthDate");
        var managerTelNumber=request.getParameter("managerTelNumber");

        var code=clientDAO.findLastCode()+1;
        var clientCompany=new ClientCompany(code, name, socialReason, location, managerName, managerFiscalCode, Date.valueOf(managerBirthDate), managerTelNumber, false);

        clientDAO.addClient(clientCompany);
        listView(request, response, dao);
    }

    public static void removeClient(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO();
        var code=request.getParameter("code");

        clientDAO.removeClient(clientDAO.findByCode(Integer.parseInt(code)));
        listView(request, response, dao);
    }

    public static void updateClient(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO();

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var socialReason=request.getParameter("socialReason");
        var location=request.getParameter("location");
        var managerName=request.getParameter("managerName");
        var managerFiscalCode=request.getParameter("managerFiscalCode");
        var managerBirthDate=request.getParameter("managerBirthDate");
        var managerTelNumber=request.getParameter("managerTelNumber");

        var clientCompany=new ClientCompany(Integer.parseInt(code), name, socialReason, location, managerName, managerFiscalCode, Date.valueOf(managerBirthDate), managerTelNumber, false);

        clientDAO.updateClient(clientCompany);
        listView(request, response, dao);
    }

    public static void editClient(HttpServletRequest request, HttpServletResponse response) {

        var dao=Controller.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO();

        var name=request.getParameter("code");
        var clientCompany=clientDAO.findByCode(Integer.parseInt(name));

        attributes.add(new Object[]{"clientCompany", clientCompany});
        formView(request, response);
    }
}
