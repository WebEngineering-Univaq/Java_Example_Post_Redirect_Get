package it.univaq.f4i.iw.examples;

import it.univaq.f4i.iw.framework.result.HTMLResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import it.univaq.f4i.iw.framework.utils.ServletHelpers;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe Della Penna
 *
 */
public class FormResponseHandler extends HttpServlet {

    private void action_render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8"); //safer, declares the same encoding as the one actually used in the origin form
        String receiver = request.getParameter("r");
        int amount = SecurityHelpers.checkNumeric(request.getParameter("a"));
        String transaction = request.getParameter("t");

        if (amount > 0 && receiver != null && !receiver.isBlank() && transaction != null && !transaction.isBlank()) {
            HTMLResult result = new HTMLResult(getServletContext());
            result.setTitle("Transfer successful!");
            result.appendToBody("<p>You have just sent <strong>" + amount + "€</strong> to <strong>" + HTMLResult.sanitizeHTMLOutput(receiver) + "</strong>. Transaction ID: " + transaction + "</p>");
            result.appendToBody("<p><em>Refreshing this page shouldn't be harmful...</em></p>");
            result.activate(request, response);
        } else {
            throw new Exception("Insufficient or malformed parameters");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_render(request, response);

        } catch (Exception ex) {
            request.setAttribute("exception", ex);
            ServletHelpers.handleError(request, response, getServletContext());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
        return "A kind servlet";

    }// </editor-fold>
}
