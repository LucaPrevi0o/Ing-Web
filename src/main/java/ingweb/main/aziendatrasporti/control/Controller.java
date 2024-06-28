package ingweb.main.aziendatrasporti.control;

import ingweb.main.aziendatrasporti.dao.DAOFactory;
import ingweb.main.aziendatrasporti.mo.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

//Controller interface: every controller in the Web Server has to inherit from this in order to properly work;
//this interface allows for every attribute that needs to be set for the HTML view to be inserted in a list
//and then appended as a list of attributes for the request at the end of the request processing
public interface Controller {

    ArrayList<Object[]> attributes=new ArrayList<>(); //list of attributes to be set

    //this function handles "loggedAccount" attribute independently, without need to explicitly add it to the list
    //after every request handling for multiple HTML calls
    static void commonState(HttpServletRequest request, HttpServletResponse response) {

        var account=getLoggedAccount(request, response); //get logged account data from cookies
        var attribute=new Object[]{"loggedAccount", account}; //create new attribute to add
        if (account!=null && !attributes.contains(attribute)) attributes.add(attribute); //add the attribute to the list only when not present
        setAllAttributes(request); //proceed to apply every attribute in the list
    }

    //extract every attribute from the list and apply it to the forwarded request
    static void setAllAttributes(HttpServletRequest request) {

        System.out.println("Attribute setting for request - Total attributes: "+attributes.size());
        for (var attribute: attributes) { //loop over every element in the list

            System.out.println("New attribute {Name: \""+attribute[0]+"\" - Value: ["+attribute[1]+"]}");
            request.setAttribute((String)attribute[0], attribute[1]); //apply its value as a new key-value mapped attribute
        }

        System.out.println("Done!\n");
        attributes.clear(); //clear the list at the end to avoid double attribute insertion
    }

    //extract the "viewUrl" attribute from the list to get the selected HTML view page
    static String getViewURL() {

        for (var attribute: attributes) if (attribute[0].equals("viewUrl")) return (String)attribute[1]; //return the file name if the attribute is in the list
        return null; //return null if no valid attribute is set
    }

    //return the account data from the "loggedAccount" cookie value
    static Account getLoggedAccount(HttpServletRequest request, HttpServletResponse response) {

        return getCookieDAO(request, response).getAccountDAO().findLoggedAccount();
    }

    //return different DAOFactory instances based off the parameters received
    static DAOFactory getMySqlDAO(String name) { return DAOFactory.getByName("mysql", "LoginManager", "L4mm5hkX", name); } //get the MySQL DAOFactory implementation
    static DAOFactory getCookieDAO(HttpServletRequest request, HttpServletResponse response) { return DAOFactory.getByName("cookie", request, response); } //get cookie-relative DAOFactory implementation
}
