package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.mo.ClientCompany;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ClientDispatcher implements DispatchCollector {

    private static void commonState(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewUrl", "/admin/clients/clientCompanies"); //set URL for forward view dispatch
        request.setAttribute("selectedTab", "clients");
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void getClients(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO();
        var clientList=clientDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request);
        request.setAttribute("clientList", clientList);
        request.setAttribute("loggedAccount", loggedAccount);
        commonState(request, response);
    }

    public static void newClient(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);
        request.setAttribute("viewUrl", "/admin/clients/newClientCompany");
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void addClient(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO(); //get worker DAO implementation for the selected database

        var name=request.getParameter("name");
        var socialReason=request.getParameter("socialReason");
        var location=request.getParameter("location");
        var manager=request.getParameter("manager");

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !socialReason.isEmpty() && !location.isEmpty() && !manager.isEmpty()) {

            var clientCompany=new ClientCompany(name, socialReason, location, manager, false);
            clientDAO.addClient(clientCompany);
        }

        var clientList=clientDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("clientList", clientList);
        request.setAttribute("loggedAccount", loggedAccount);
        commonState(request, response);
    }

    public static void removeClient(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO(); //get worker DAO implementation for the selected database
        var socialReason=request.getParameter("name").substring(1);

        clientDAO.removeClient(clientDAO.findBySocialReason(socialReason));
        var clientList=clientDAO.findAll();

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("clientList", clientList);
        request.setAttribute("loggedAccount", loggedAccount);
        commonState(request, response);
    }

    public static void updateClient(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO(); //get worker DAO implementation for the selected database

        var name=request.getParameter("name");
        var socialReason=request.getParameter("socialReason");
        var location=request.getParameter("location");
        var manager=request.getParameter("manager");

        //add new record in database if parameter list is full
        if (!name.isEmpty() && !socialReason.isEmpty() && !location.isEmpty() && !manager.isEmpty()) {

            var clientCompany=new ClientCompany(name, socialReason, location, manager, false);
            clientDAO.updateClient(clientCompany);
        }

        var clientList=clientDAO.findAll(); //return account list filtered by admin level

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("clientList", clientList);
        request.setAttribute("loggedAccount", loggedAccount);
        commonState(request, response);
    }

    public static void editClient(HttpServletRequest request, HttpServletResponse response) {

        //get parameters passed by login page
        var loggedAccount=DispatchCollector.getAccount(request, response);

        //get registered account list specifying DAO database implementation
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var clientDAO=dao.getClientDAO();

        var name=request.getParameter("name");
        var data=name.split("\\.");
        var clientCompany=clientDAO.findBySocialReason(data[1]);
        System.out.println(clientCompany);

        dao.commit();
        dao.close();
        DispatchCollector.commonView(request); //append every attribute in current session
        request.setAttribute("clientCompany", clientCompany); //set list as new session attribute
        request.setAttribute("viewUrl", "/admin/clients/newClientCompany"); //set URL for forward view dispatch
        request.setAttribute("loggedAccount", loggedAccount);
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }
}
