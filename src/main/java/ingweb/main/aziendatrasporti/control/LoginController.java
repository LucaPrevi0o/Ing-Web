package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.mo.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;

public class LoginController implements Controller {

    private static void adminLogin(HttpServletRequest request, HttpServletResponse response, Account account) {

        attributes.add(new Object[]{"loggedAccount", account});
        attributes.add(new Object[]{"viewUrl", "/admin/welcome"});
    }

    private static void workerLogin(HttpServletRequest request, HttpServletResponse response, Account account) {

        attributes.add(new Object[]{"loggedAccount", account});
        attributes.add(new Object[]{"viewUrl", "/worker/welcome"});
    }

    private static void managerLogin(HttpServletRequest request, HttpServletResponse response, Account account) {

        attributes.add(new Object[]{"loggedAccount", account});
        attributes.add(new Object[]{"viewUrl", "/clientManager/welcome"});
    }

    private static void rejectLogin(HttpServletRequest request, HttpServletResponse response, String rejectReason) {

        attributes.add(new Object[]{"viewUrl", "/login"});
        attributes.add(new Object[]{"access", rejectReason});
    }

    private static void accept(HttpServletRequest request, HttpServletResponse response, Account loggedAccount, ArrayList<Account> adminList, ArrayList<Account> managerList) {

        if (adminList.contains(loggedAccount)) adminLogin(request, response, loggedAccount);
        else if (managerList.contains(loggedAccount)) managerLogin(request, response, loggedAccount);
        else workerLogin(request, response, loggedAccount);
    }

    public static void doLogin(HttpServletRequest request, HttpServletResponse response) {

        var cookieDAO=Controller.getCookieDAO(request, response);
        var mySqlDAO=Controller.getMySqlDAO("aziendatrasportidb");
        var mySqlAccountDAO=mySqlDAO.getAccountDAO();
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var adminList=mySqlAccountDAO.findAllByLevel(Account.ADMIN_LEVEL); //get list of admin accounts
        var managerList=mySqlAccountDAO.findAllByLevel(Account.MANAGER_LEVEL); //get list of client company manager accounts
        var loggedAccount=cookieAccountDAO.findLoggedAccount(); //check for already logged account (not null cookie value)

        if (loggedAccount!=null) { //if account cookie is already set, login directly with same validation

            mySqlDAO.confirm();
            accept(request, response, loggedAccount, adminList, managerList);
        } else { //proceed to validate credential if account has not logged already

            //get account credentials from login form
            var username=request.getParameter("username");
            var password=request.getParameter("password");

            loggedAccount=mySqlAccountDAO.findByUsername(username); //find account instance from database by username
            if (loggedAccount!=null) cookieAccountDAO.createAccount(loggedAccount); //if the account is valid, set its data as a new cookie
            var accountList=mySqlAccountDAO.findAll(); //get list of every account in db
            mySqlDAO.confirm();

            //check for credential validation and manage login
            if (!accountList.contains(loggedAccount)) rejectLogin(request, response, "not-registered");
            else if (!loggedAccount.getPassword().equals(password)) rejectLogin(request, response, "denied");
            else accept(request, response, loggedAccount, adminList, managerList);
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

    public static void manageProfile(HttpServletRequest request, HttpServletResponse response) {

        attributes.add(new Object[]{"viewUrl", "/account"});
    }

    public static void editAccount(HttpServletRequest request, HttpServletResponse response) {

        var cookieDAO=Controller.getCookieDAO(request, response);
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount();
        cookieAccountDAO.deleteAccount(loggedAccount);

        var mySqlDAO=Controller.getMySqlDAO("aziendatrasportidb");
        var mySqlAccountDAO=mySqlDAO.getAccountDAO();

        var username=request.getParameter("username");
        var password=request.getParameter("password");
        var name=request.getParameter("name");
        var account=new Account(loggedAccount.getCode(), username, password, name, loggedAccount.getLevel(), false);
        mySqlAccountDAO.updateAccount(account);
        cookieAccountDAO.createAccount(account);
        mySqlDAO.confirm();

        attributes.add(new Object[]{"loggedAccount", account});
        if (loggedAccount.getLevel()==Account.ADMIN_LEVEL) attributes.add(new Object[]{"viewUrl", "/admin/welcome"});
        else if (loggedAccount.getLevel()==Account.MANAGER_LEVEL) attributes.add(new Object[]{"viewUrl", "/clientManager/welcome"});
        else attributes.add(new Object[]{"viewUrl", "/worker/welcome"});
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) { attributes.add(new Object[]{"viewUrl", "/login"}); }
}
