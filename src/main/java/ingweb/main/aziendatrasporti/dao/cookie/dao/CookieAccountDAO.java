package ingweb.main.aziendatrasporti.dao.cookie.dao;

import ingweb.main.aziendatrasporti.dao.AccountDAO;
import ingweb.main.aziendatrasporti.control.Controller;
import ingweb.main.aziendatrasporti.mo.Account;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class CookieAccountDAO implements AccountDAO {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public CookieAccountDAO(HttpServletRequest request, HttpServletResponse response) {

        this.request=request;
        this.response=response;
    }

    public void createAccount(Account account) {

        var cookie=new Cookie("loggedAccount", account.getUsername());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void deleteAccount(Account account) {

        var cookie=new Cookie("loggedAccount", account.getUsername());
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void addAccount(Account account) {}
    public void removeAccount(Account account) {}
    public void updateAccount(Account account) {}
    public ArrayList<Account> findAll(boolean admin) { return null; }
    public Account findByUsernameAndPassword(String username, String password) { return null; }
    public Account findByUsername(String username) { return null; }

    public Account findLoggedAccount() {

        var cookies=request.getCookies();
        if (cookies!=null) for (var cookie: cookies)
            if (cookie.getName().equals("loggedAccount")) {

                var dao=Controller.getMySqlDAO("aziendatrasportidb");
                var accountDAO=dao.getAccountDAO();
                return accountDAO.findByUsername(cookie.getValue());
            }
        return null;
    }
}
