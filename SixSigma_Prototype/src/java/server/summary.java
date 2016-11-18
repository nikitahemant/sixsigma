/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import model.OrgDaily;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is the servlet for handling UI requests from homepage
 * @author Nikita
 */
@WebServlet(name = "summary", urlPatterns = {"/summary"})
public class summary extends HttpServlet {

    public static List<OrgDaily> orgDailyData;

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        orgDailyData = new ArrayList<OrgDaily>();
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
            
        orgDailyData = getOrgDailyData();
                
        Gson gson = new Gson();

	String jsonString = gson.toJson(orgDailyData);

	response.setContentType("application/json");

	response.getWriter().write(jsonString);
   
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

    private List<OrgDaily> getOrgDailyData() {
        List<OrgDaily> tempData = new ArrayList<OrgDaily>();
	
        OrgDaily d1 = new OrgDaily("Oct-15",  33474.04); 
	tempData.add(d1);
        
        OrgDaily d2 = new OrgDaily("Nov-15",  22580.02);
        tempData.add(d2);
                
        return tempData;

    }

}
