/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AccCategory;
import model.AccDaily;
import model.AccMonthly;

/**
 * This class is the servlet for handling UI requests from homepage
 * @author Nikita
 */
@WebServlet(name = "detail", urlPatterns = {"/detail"})
public class detail extends HttpServlet {

    public static List<AccDaily> accDailyData;
    public static List<AccMonthly> accMonthlyData;
    public static List<AccCategory> accCategoryData;

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        accDailyData = new ArrayList<AccDaily>();
        accMonthlyData = new ArrayList<AccMonthly>();
        accCategoryData = new ArrayList<AccCategory>();
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
            
        Connection con = dbConnect();
        Gson gson = new Gson();
        
        accDailyData = getAccDailyData(con);   
	String jsonDailyString = gson.toJson(accDailyData);
	response.setContentType("application/json");
	response.getWriter().write(jsonDailyString);
   
        accMonthlyData = getAccMonthlyData(con);   
	String jsonMonthlyString = gson.toJson(accMonthlyData);
	response.setContentType("application/json");
	response.getWriter().write(jsonMonthlyString);
        
        accCategoryData = getAccCategoryData(con);   
	String jsonCategoryString = gson.toJson(accCategoryData);
	response.setContentType("application/json");
	response.getWriter().write(jsonCategoryString);
        
        dbClose(con);
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

    private List<AccDaily> getAccDailyData(Connection con) {
        List<AccDaily> tempData = new ArrayList<AccDaily>();
	
        try
        {
          Statement s = con.createStatement();
          s.executeQuery("select TRANSACTION_DATE, ENTRY_AMOUNT from capstone.data2014 " +
            "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' " +
            "order by TRANSACTION_DATE");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TRANSACTION_DATE");
                String em = rs.getString("ENTRY_AMOUNT");
                   
                AccDaily d1 = new AccDaily(dt,Long.parseLong(em)); 
                tempData.add(d1);
            }
            
            rs.close();
            s.close();
            
        }
        catch(Exception e )
        {
          e.printStackTrace();
        }
                
        return tempData;

    }
    
    private List<AccMonthly> getAccMonthlyData(Connection con) {
        List<AccMonthly> tempData = new ArrayList<AccMonthly>();
	
        try
        {
          Statement s = con.createStatement();
          s.executeQuery("select TRANSACTION_DATE, ENTRY_AMOUNT from capstone.data2014 " +
            "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' " +
            "order by TRANSACTION_DATE");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TRANSACTION_DATE");
                String em = rs.getString("ENTRY_AMOUNT");
                   
                AccMonthly d1 = new AccMonthly(dt,Long.parseLong(em)); 
                tempData.add(d1);
            }
            
            rs.close();
            s.close();
            
        }
        catch(Exception e )
        {
          e.printStackTrace();
        }
                
        return tempData;

    }
    
    private List<AccCategory> getAccCategoryData(Connection con) {
        List<AccCategory> tempData = new ArrayList<AccCategory>();
	
        try
        {
          Statement s = con.createStatement();
          s.executeQuery("select TRANSACTION_DATE, ENTRY_AMOUNT from capstone.data2014 " +
            "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' " +
            "order by TRANSACTION_DATE");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TRANSACTION_DATE");
                String em = rs.getString("ENTRY_AMOUNT");
                   
                AccCategory d1 = new AccCategory(dt,Long.parseLong(em)); 
                tempData.add(d1);
            }
            
            rs.close();
            s.close();
            
        }
        catch(Exception e )
        {
          e.printStackTrace();
        }
                
        return tempData;

    }
    
    public static Connection dbConnect() {
        Connection con = null;
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
          con = DriverManager.getConnection("jdbc:mysql://localhost:"+nLocalPort, strDbUser, strDbPassword);
          
        }
        catch(Exception e )
        {
          e.printStackTrace();
        }
        return con;
      
          
    }
    
    public static void dbClose(Connection con) {
        
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(summary.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
}
