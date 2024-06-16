package ingweb.main.aziendatrasporti.dispatch;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;

public interface DispatchCollector {

    static ArrayList<Object[]> getAllAttributes(HttpServletRequest session) {

        var attributes=new ArrayList<Object[]>();
        if (session.getSession(false)!=null) {

            var attributeNames=session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {

                var next=attributeNames.nextElement();
                attributes.add(new Object[]{next, session.getAttribute(next)});
            }
        }

        System.out.println("All attributes found:");
        for (var attribute: attributes) System.out.println(attribute[0]+" - "+attribute[1]);
        System.out.println();
        return attributes;
    }

    static void setAllAttributes(HttpServletRequest session, ArrayList<Object[]> attributes) {

        System.out.println("starting attribute updating");
        if (session.getSession(false)!=null && !attributes.isEmpty()) for (var attribute: attributes) {

            System.out.println("Adding attribute "+attribute[0]+" of value "+attribute[1]);
            session.setAttribute((String)attribute[0], attribute[1]);
        }
        System.out.println();
    }

    static void removeAllAttributes(HttpServletRequest session) {

        if (session.getSession(false)!=null) {

            var attributeNames=session.getAttributeNames();
            while (attributeNames.hasMoreElements()) session.removeAttribute(attributeNames.nextElement());
        }
    }

    static Object findByName(String name, ArrayList<Object[]> attributes) {

        for (var attribute: attributes) if (attribute[0].equals(name)) return attribute[1];
        return null;
    }

    static Account getAccount(HttpServletRequest request, HttpServletResponse response) {

        return getCookieDAO(request, response).getAccountDAO().findLoggedAccount();
    }

    static DAOFactory getMySqlDAO(String name) { return DAOFactory.getByName("mysql", "LoginManager", "L4mm5hkX", name); }
    static DAOFactory getCookieDAO(HttpServletRequest request, HttpServletResponse response) { return DAOFactory.getByName("cookie", request, response); }
}
