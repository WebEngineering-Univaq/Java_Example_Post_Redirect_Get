package it.univaq.f4i.iw.examples;

import it.univaq.f4i.iw.framework.result.HTMLResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import it.univaq.f4i.iw.framework.utils.ServletHelpers;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe Della Penna
 *
 *
 */
public class FormHandlerPRG extends HttpServlet {

    private void action_render(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Wire transfer");
        result.appendToBody("<h1>Wire transfer</h1>");
        result.appendToBody("<form method=\"post\" action=\"transferprg\">");
        result.appendToBody("<p>Receiver identity:");
        result.appendToBody("<input type=\"text\" name=\"r\"/></p>");
        result.appendToBody("<p>Amount to transfer: ");
        result.appendToBody("<input type=\"number\" name=\"a\"/></p>");
        result.appendToBody("</p><input type=\"submit\" name=\"s\" value=\"Execute\"/></p>");
        result.appendToBody("</form>");
        result.appendToBody("<p><a href=\"transfer\">Switch to the dangerous form without post-redirect-get</a></p>");
        result.activate(request, response);
    }

    private void action_execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8"); //safer, declares the same encoding as the one actually used in the origin form
        String receiver = request.getParameter("r");
        int amount = SecurityHelpers.checkNumeric(request.getParameter("a"));

        if (amount > 0 && receiver != null && !receiver.isBlank()) {
            String transaction = UUID.randomUUID().toString();
            response.sendRedirect("result?a=" + URLEncoder.encode(String.valueOf(amount), "utf-8") + "&r=" + URLEncoder.encode(receiver, "utf-8") + "&t=" + URLEncoder.encode(transaction, "utf-8"));
        } else {
            throw new Exception("Insufficient or malformed parameters");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            if (request.getMethod().equals("POST")) {
                action_execute(request, response);
            } else {
                action_render(request, response);
            }
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
