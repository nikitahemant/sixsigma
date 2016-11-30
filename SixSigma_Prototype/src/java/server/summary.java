/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server.Proxy;
import com.google.gson.Gson;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import model.OrgDaily;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.OrgCategory;
import model.OrgMonthly;

/**
 * This class is the servlet for handling UI requests from homepage
 * @author Nikita
 */
@WebServlet(name = "summary", urlPatterns = {"/summary"})
public class summary extends HttpServlet {

    public static List<OrgDaily> orgDailyData;
    public static List<OrgMonthly> orgMonthlyData;
    public static List<OrgCategory> orgCategoryData;

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        orgDailyData = new ArrayList<OrgDaily>();
        orgMonthlyData = new ArrayList<OrgMonthly>();
        orgCategoryData = new ArrayList<OrgCategory>();
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
        
        orgDailyData = getOrgDailyData(con);   
	String jsonDailyString = gson.toJson(orgDailyData);
	response.setContentType("application/json");
	response.getWriter().write(jsonDailyString);
   
        orgMonthlyData = getOrgMonthlyData(con);   
	String jsonMonthlyString = gson.toJson(orgMonthlyData);
//	response.setContentType("application/json");
//	response.getWriter().write(jsonMonthlyString);
        
        orgCategoryData = getOrgCategoryData(con);   
	String jsonCategoryString = gson.toJson(orgCategoryData);
//	response.setContentType("application/json");
//	response.getWriter().write(jsonCategoryString);
        
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

    private List<OrgDaily> getOrgDailyData(Connection con) {
        List<OrgDaily> tempData = new ArrayList<OrgDaily>();
	
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
                   
                OrgDaily d1 = new OrgDaily(dt,Double.parseDouble(em)); 
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
    
    private List<OrgMonthly> getOrgMonthlyData(Connection con) {
        List<OrgMonthly> tempData = new ArrayList<OrgMonthly>();
	
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
                   
                OrgMonthly d1 = new OrgMonthly(dt,Double.parseDouble(em)); 
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
    
    private List<OrgCategory> getOrgCategoryData(Connection con) {
        List<OrgCategory> tempData = new ArrayList<OrgCategory>();
	
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
                   
                OrgCategory d1 = new OrgCategory(dt,Double.parseDouble(em)); 
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

          summary.doSshTunnel(strSshUser, strSshPassword, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);

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
    
    static void doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException
    {
        final JSch jsch = new JSch();
        Session session = jsch.getSession( strSshUser, strSshHost, 22 );
        session.setPassword( strSshPassword );

        final Properties config = new Properties();
        config.put( "StrictHostKeyChecking", "no" );
        session.setConfig( config );

        session.connect();
        session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
    }
    

}
