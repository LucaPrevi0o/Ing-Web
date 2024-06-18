package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.mo.ClientCompany;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Date;

public class ClientDispatcher implements DispatchCollector {

    private static void commonState(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewUrl", "/admin/clients/clientCompanies"); //set URL for forward view dispatch
        request.setAttribute("loggedAccount", DispatchCollector.getAccount(request, response));
        request.setAttribute("selectedTab", "clients");
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void getClients(HttpServletRequest request, HttpServletResponse response) {

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO();
        var clientList=clientDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("clientList", clientList);
        commonState(request, response);
    }

    public static void newClient(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewUrl", "/admin/clients/newClientCompany");
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void addClient(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO(); //get worker DAO implementation for the selected database

        var name=request.getParameter("name");
        var socialReason=request.getParameter("socialReason");
        var location=request.getParameter("location");
        var managerName=request.getParameter("managerName");
        var managerFiscalCode=request.getParameter("managerFiscalCode");
        var managerBirthDate=request.getParameter("managerBirthDate");
        var managerTelNumber=request.getParameter("managerTelNumber");

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !socialReason.isEmpty() && !location.isEmpty() && !managerName.isEmpty()) {

            var clientCompany=new ClientCompany(name, socialReason, location, managerName, managerFiscalCode, Date.valueOf(managerBirthDate), managerTelNumber, false);
            clientDAO.addClient(clientCompany);
        }

        var clientList=clientDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("clientList", clientList);
        commonState(request, response);
    }

    public static void removeClient(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO(); //get worker DAO implementation for the selected database
        var code=request.getParameter("code");

        clientDAO.removeClient(clientDAO.findByCode(Integer.parseInt(code)));
        var clientList=clientDAO.findAll();

        dao.commit();
        dao.close();
        request.setAttribute("clientList", clientList);
        commonState(request, response);
    }

    public static void updateClient(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO(); //get worker DAO implementation for the selected database

        var code=request.getParameter("code");
        var name=request.getParameter("name");
        var socialReason=request.getParameter("socialReason");
        var location=request.getParameter("location");
        var managerName=request.getParameter("managerName");
        var managerFiscalCode=request.getParameter("managerFiscalCode");
        var managerBirthDate=request.getParameter("managerBirthDate");
        var managerTelNumber=request.getParameter("managerTelNumber");

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !socialReason.isEmpty() && !location.isEmpty() && !managerName.isEmpty() && !managerFiscalCode.isEmpty() && !managerBirthDate.isEmpty() && !managerTelNumber.isEmpty()) {

            var clientCompany=new ClientCompany(name, socialReason, location, managerName, managerFiscalCode, Date.valueOf(managerBirthDate), managerTelNumber, false);
            clientCompany.setCode(Integer.parseInt(code));
            clientDAO.updateClient(clientCompany);
        }

        var clientList=clientDAO.findAll(); //return account list filtered by admin level

        dao.commit();
        dao.close();
        request.setAttribute("clientList", clientList);
        commonState(request, response);
    }

    public static void editClient(HttpServletRequest request, HttpServletResponse response) {

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO();

        var name=request.getParameter("code");
        var clientCompany=clientDAO.findByCode(Integer.parseInt(name));

        dao.commit();
        dao.close();
        request.setAttribute("clientCompany", clientCompany); //set list as new session attribute
        request.setAttribute("viewUrl", "/admin/clients/newClientCompany"); //set URL for forward view dispatch
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }
}
