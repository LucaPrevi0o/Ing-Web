package ingweb.main.aziendatrasporti;

import java.io.*;
import java.rmi.ServerException;
import ingweb.main.aziendatrasporti.control.Controller;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

//Dispatcher: Java Servlet class that handles every request passed to it via URL;
//the URL has to be in the form "http://<localhost>:<servlet-port>/<artifact-url>/<url-pattern>
//to be a valid HTTP request handled by this Servlet
@WebServlet(name="Servizi", urlPatterns={"/Servizi"})
public class Dispatcher extends HttpServlet {

    //main request processing: as the URL redirects to this specific page, the body is expected to contain an
    //"action" attribute in the form of "<controller>.<action>"; the Servlet then proceeds to call the specified
    //action as a Java function in the specified controller (defined in the Web Server file system as a Java
    //class containing the function) and forwards the HTML view to the selected JSP file via the attribute "viewUrl"
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8"); //set response content header
        PrintWriter out=response.getWriter(); //reference Servlet writer
        try {

            var param=request.getParameter("action"); //get parameter for action to control
            if (param==null) param="LoginController.view"; //set default parameter for initial call

            var action=param.split("\\."); //split action in tokens
            var dispClass=Class.forName("ingweb.main.aziendatrasporti.control."+action[0]); //call class by action
            var dispMethod=dispClass.getMethod(action[1], HttpServletRequest.class, HttpServletResponse.class); //get method from called class
            dispMethod.invoke(null, request, response); //call class static method by action

            //apply every attribute to the request and dispatch the view
            var viewUrl=Controller.getViewURL(); //get HTML view page to forward
            if (!param.equals("LoginController.logout") && !param.equals("LoginController.doLogin")) Controller.commonState(request, response); //set all attributes in forwarded request
            else Controller.setAllAttributes(request); //this handles logout action without overwriting the "loggedAccount" attribute
            var view=request.getRequestDispatcher("jsp/"+viewUrl+".jsp"); //set view control from parameter
            view.forward(request, response); //execute forward control
        } catch (Exception e) {

            e.printStackTrace(out);
            throw new ServerException("Dispacther Servlet Error", e);
        } finally { out.close(); }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
