/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import app.StockAnalysis;
import bean.Stock;
import bean.StockPrice;
import bean.TransferObj;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.ApplicationConstants;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "ApplicationServlet", urlPatterns = {"/ApplicationServlet"})
public class ApplicationServlet extends HttpServlet {

    String action = "";
    String responsePage = "";

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String responseString="";
        try {
            // TODO output your page here
        /*    out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ApplicationServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApplicationServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */

            action = request.getParameter(ApplicationConstants.UIACTION_NAME);
            if (action==null || action.equals("")) {
                action = (String) request.getAttribute(ApplicationConstants.UIACTION_NAME);
            }
            if (action!=null && !action.equals("")) {

                if (action.equals(ApplicationConstants.UIACTION_DISPLAY_STOCKS)) {
                    StockAnalysis sa = new StockAnalysis();
                    TransferObj tfo = sa.displayAllStockList();
                    request.setAttribute(ApplicationConstants.TRANSFER_OBJ, tfo);
                    if (tfo != null) {
                        responsePage = "/jsp/home.jsp";
                    }
                } else if(action.equals("ajax")){
                    JSONArray dataArr=new JSONArray();
                    Stock st=new StockAnalysis().getDatafromFile("SBIN");
                    for(Object o:st.getStockList()){
                        StockPrice sprice=(StockPrice) o;
                        JSONObject ob=new JSONObject();
                        ob.put("Date",sprice.getDate()+"");
                        ob.put("Open",sprice.getOpen()+"");
                        ob.put("High",sprice.getHigh()+"");
                        ob.put("Low",sprice.getLow()+"");
                        ob.put("Close",sprice.getClose()+"");
                        ob.put("Volume",sprice.getVolume()+"");
                        dataArr.add(ob);
                    }
                    responseString=dataArr.toString();
                }
                
            } else {
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet ApplicationServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Nothing is present</h1>");
                out.println("</body>");
                out.println("</html>");
            }
            if(action.equals("ajax")){
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
             response.addHeader("Access-Control-Allow-Origin", "*");
             response.getWriter().write(responseString);
            }else{
            RequestDispatcher rd = getServletContext().getRequestDispatcher(responsePage);
            rd.forward(request, response);
            }

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
