package ingweb.main.aziendatrasporti;

import java.io.*;
import java.rmi.ServerException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

//servlet front dispatcher: used as a logical handler for different user interactions, allows
@WebServlet(name="Servizi", urlPatterns={"/Servizi"})
public class Servizi extends HttpServlet {

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

            var viewUrl=(String)request.getAttribute("viewUrl"); //get parameter for view to forward
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
