/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server.Proxy;
import com.google.gson.Gson;
import model.OrgDaily;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
	
        try
        {
          String strSshUser = "jason";                  // SSH loging username
          String strSshPassword = "jason";                   // SSH login password
          String strSshHost = "128.2.144.201";          // hostname or ip or SSH server
          int nSshPort = 22;                                    // remote SSH host port number
          String strRemoteHost = "localhost";  // hostname or ip of your database server
          int nLocalPort = 3366;                                // local port number use to bind SSH tunnel
          int nRemotePort = 3306;                               // remote port number of your database 
          String strDbUser = "root";                    // database loging username
          String strDbPassword = "root";                    // database login password

          Proxy.doSshTunnel(strSshUser, strSshPassword, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);

          Class.forName("com.mysql.jdbc.Driver");
          Connection con = DriverManager.getConnection("jdbc:mysql://localhost:"+nLocalPort, strDbUser, strDbPassword);

          Statement s = con.createStatement();
          s.executeQuery("select TRANSACTION_DATE, ENTRY_AMOUNT from capstone.data2014 " +
            "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' " +
            "order by TRANSACTION_DATE");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TRANSACTION_DATE");
                String em = rs.getString("ENTRY_AMOUNT");
                   
                OrgDaily d1 = new OrgDaily(dt,Long.parseLong(em)); 
                tempData.add(d1);
            }
            
            rs.close();
            s.close();
            
            con.close();
        }
        catch(Exception e )
        {
          e.printStackTrace();
        }
                
        return tempData;

    }

}
