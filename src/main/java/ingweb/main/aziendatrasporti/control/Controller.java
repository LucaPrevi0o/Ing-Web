package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public interface Controller {

    ArrayList<Object[]> attributes=new ArrayList<>();

    static void commonState(HttpServletRequest request, HttpServletResponse response, String selectedTab) {

        attributes.add(new Object[]{"selectedTab", selectedTab});
        attributes.add(new Object[]{"loggedAccount", getLoggedAccount(request, response)});
        Controller.setAllAttributes(request);
    }

    static void setAllAttributes(HttpServletRequest request) {

        System.out.println("\nAttribute setting for request - Attributes: "+attributes.size());
        for (var attribute : attributes) {

            System.out.println("New attribute [Name: \""+attribute[0]+"\" - Value: "+attribute[1]+"]");
            request.setAttribute((String)attribute[0], attribute[1]);
        }

        System.out.println("Attributes set for request");
        attributes.clear();
    }

    static Account getLoggedAccount(HttpServletRequest request, HttpServletResponse response) {

        return getCookieDAO(request, response).getAccountDAO().findLoggedAccount();
    }

    static DAOFactory getMySqlDAO(String name) { return DAOFactory.getByName("mysql", "LoginManager", "L4mm5hkX", name); }
    static DAOFactory getCookieDAO(HttpServletRequest request, HttpServletResponse response) { return DAOFactory.getByName("cookie", request, response); }
}
