package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.mo.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginDispatcher implements DispatchCollector {

    private static void commonState(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("viewUrl", "/login"); //set URL for forward view dispatch
        DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
    }

    public static void validate(HttpServletRequest request, HttpServletResponse response) {

        var username=request.getParameter("username");
        var password=request.getParameter("password");

        //get registered account list specifying DAO database implementation
        var mySqlDAO=DispatchCollector.getMySqlDAO("aziendatrasportidb");
        var dao=DispatchCollector.getMySqlDAO("azienda_trasporti");
        var serviceDAO=dao.getServiceDAO();
        var cookieDAO=DispatchCollector.getCookieDAO(request, response);
        var mySqlAccountDAO=mySqlDAO.getAccountDAO(); //get account DAO implementation for the selected database
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var validatedAccount=mySqlAccountDAO.findByUsername(username);
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        var adminList=mySqlAccountDAO.findAll(true); //return account list filtered by admin level
        var accountList=mySqlAccountDAO.findAll(false); //return account list filtered by admin level
        mySqlDAO.commit();
        mySqlDAO.close();

        if (loggedAccount!=null || (!accountList.isEmpty() && !adminList.isEmpty() && accountList.contains(validatedAccount) && validatedAccount.getPassword().equals(password))) {

            if (loggedAccount==null) cookieAccountDAO.createAccount(validatedAccount);
            if (adminList.contains(validatedAccount) || (loggedAccount!=null && adminList.contains(loggedAccount))) request.setAttribute("viewUrl", "/admin/welcome"); //set URL for forward view dispatch
            else {

                request.setAttribute("viewUrl", "/worker/servicePage");
                var pwd=(loggedAccount!=null ? loggedAccount.getUsername() : validatedAccount.getUsername());
                var serviceList=serviceDAO.findAllAssignedByFiscalCode(pwd);
                request.setAttribute("serviceList", serviceList);
            }
            request.setAttribute("loggedAccount", loggedAccount!=null ? loggedAccount : validatedAccount);
            DispatchCollector.setAllAttributes(request, DispatchCollector.getAllAttributes(request));
            return; //break early for permitted login
        }

        //set access as denied (account has no admin permission)
        if (validatedAccount!=null && validatedAccount.getUsername().equals(username) && !validatedAccount.getPassword().equals(password)) request.setAttribute("access", "denied");
        else request.setAttribute("access", "not-registered"); //username/password have no account related
        commonState(request, response);
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        var cookieDAO=DispatchCollector.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        if (loggedAccount!=null) cookieAccountDAO.deleteAccount(loggedAccount);
        commonState(request, response);
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) { commonState(request, response); }
}
