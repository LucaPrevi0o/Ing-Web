package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.mo.mo.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginController implements Controller {

    private static void rejectLogin(HttpServletRequest request, HttpServletResponse response) {

        attributes.add(new Object[]{"viewUrl", "/login"});
        attributes.add(new Object[]{"access", "not-registered"});
    }

    private static void accept(HttpServletRequest request, HttpServletResponse response, Account loggedAccount) {

        var viewUrl="/";
        viewUrl+=(loggedAccount.getLevel()==Account.WORKER_LEVEL ? "worker" :
            (loggedAccount.getLevel()==Account.MANAGER_LEVEL ? "clientManager" :
            (loggedAccount.getLevel()==Account.ADMIN_LEVEL ? "admin" : "")));
        viewUrl+="/welcome";
        attributes.add(new Object[]{"viewUrl", viewUrl});
    }

    public static void doLogin(HttpServletRequest request, HttpServletResponse response) {

        var cookieDAO=Controller.getCookieDAO(request, response);
        var mySqlDAO=Controller.getMySqlDAO("aziendatrasportidb");
        var mySqlAccountDAO=mySqlDAO.getAccountDAO();
        var cookieAccountDAO=cookieDAO.getAccountDAO();
        var loggedAccount=cookieAccountDAO.findLoggedAccount(); //check for already logged account (not null cookie value)

        if (loggedAccount!=null) { //if account cookie is already set, login directly with same validation

            mySqlDAO.confirm();
            accept(request, response, loggedAccount);
        } else { //proceed to validate credential if account has not logged already

            //get account credentials from login form
            var username=request.getParameter("username");
            var password=request.getParameter("password");

            loggedAccount=mySqlAccountDAO.findByUsernameAndPassword(username, password); //find account instance from database by username
            if (loggedAccount==null) rejectLogin(request, response);
            else {

                cookieAccountDAO.createAccount(loggedAccount); //if the account is valid, set its data as a new cookie
                accept(request, response, loggedAccount); //proceed to account validation by level
            }
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
        cookieAccountDAO.deleteAccount(loggedAccount); //delete cookie for current account

        var mySqlDAO=Controller.getMySqlDAO("aziendatrasportidb");
        var mySqlAccountDAO=mySqlDAO.getAccountDAO();

        var username=request.getParameter("username");
        var password=request.getParameter("password");
        var name=request.getParameter("name");
        var bankCoordinates=request.getParameter("bankCoordinates");
        var account=new Account(loggedAccount.getCode(), username, password, name, loggedAccount.getProfile(), bankCoordinates, loggedAccount.getLevel(), false);
        if (!username.isEmpty() && !password.isEmpty() && !name.isEmpty()){

            mySqlAccountDAO.updateAccount(account); //update account in database
            cookieAccountDAO.createAccount(account); //set new cookie from updated data
        }
        mySqlDAO.confirm();

        accept(request, response, account); //redo login with new account
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) { attributes.add(new Object[]{"viewUrl", "/login"}); }
}
