package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.mo.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginController implements Controller {

    private static void adminLogin(HttpServletRequest request, HttpServletResponse response, Account account) {

        attributes.add(new Object[]{"loggedAccount", account});
        attributes.add(new Object[]{"viewUrl", "/admin/welcome"});
    }

    private static void workerLogin(HttpServletRequest request, HttpServletResponse response, Account account) {

        attributes.add(new Object[]{"loggedAccount", account});
        attributes.add(new Object[]{"viewUrl", "/worker/welcome"});
    }

    private static void rejectLogin(HttpServletRequest request, HttpServletResponse response, String rejectReason) {

        attributes.add(new Object[]{"viewUrl", "/login"});
        attributes.add(new Object[]{"access", rejectReason});
    }

    public static void doLogin(HttpServletRequest request, HttpServletResponse response) {

        var username=request.getParameter("username");
        var password=request.getParameter("password");

        var mySqlDAO=Controller.getMySqlDAO("aziendatrasportidb");
        var cookieDAO=Controller.getCookieDAO(request, response);
        var mySqlAccountDAO=mySqlDAO.getAccountDAO();
        var cookieAccountDAO=cookieDAO.getAccountDAO();

        var adminList=mySqlAccountDAO.findAllByLevel(Account.ADMIN_LEVEL); //return account list filtered by admin level
        var accountList=mySqlAccountDAO.findAll(); //return account list filtered by admin level
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        if (loggedAccount==null) {

            loggedAccount=mySqlAccountDAO.findByUsername(username);
            if (loggedAccount!=null) cookieAccountDAO.createAccount(loggedAccount);
        }

        mySqlDAO.confirm();
        cookieDAO.confirm();
        if (!accountList.contains(loggedAccount)) rejectLogin(request, response, "not-registered");
        else if (!loggedAccount.getPassword().equals(password)) rejectLogin(request, response, "denied");
        else {

            if (adminList.contains(loggedAccount)) adminLogin(request, response, loggedAccount);
            else workerLogin(request, response, loggedAccount);
        }
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        if (loggedAccount!=null) cookieAccountDAO.deleteAccount(loggedAccount);
        attributes.add(new Object[]{"viewUrl", "/login"});
        cookieDAO.confirm();
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) { attributes.add(new Object[]{"viewUrl", "/login"}); }
}
