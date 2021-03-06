/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
import model.DBProperties;
import model.OrgCategory;
import model.OrgMonthly;
import model.OrgSummary;


/**
 * This class is the servlet for handling UI requests from homepage
 * @author Nikita
 */
@WebServlet(name = "summary", urlPatterns = {"/summary"})
public class summary extends HttpServlet {

    public static Connection con = null;
    public static Session sess = null;
    public static List<OrgDaily> orgDailyData;
    public static List<OrgMonthly> orgMonthlyData;
    public static List<OrgCategory> orgCategoryData;
    public static OrgSummary orgSummaryData;

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        orgSummaryData = null;
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
            
        con = dbConnect();
        Gson gson = new Gson();
        
        
        orgDailyData = getOrgDailyData(con);   
        orgMonthlyData = getOrgMonthlyData(con);           
        orgCategoryData = getOrgCategoryData(con);   
        
        dbClose(con);
        
        orgSummaryData = new OrgSummary(orgDailyData,orgMonthlyData,orgCategoryData);
        String jsonSummaryString = orgSummaryData.toJSON();
	response.setContentType("application/json");
	response.getWriter().write(jsonSummaryString);
        
        
        
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
          s.executeQuery("select PROC_DATE AS TDATE, ROUND(SUM(ENTRY_AMOUNT),2) AS AMT from capstone.data2014    "
                  + "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p'"
                  + "GROUP BY PROC_DATE ORDER BY PROC_DATE desc LIMIT 7");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TDATE");
                String em = rs.getString("AMT");
                   
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
          s.executeQuery("select MONTH(STR_TO_DATE(PROC_DATE,'%m/%d/%y')) AS TDATE, SUM(ENTRY_AMOUNT) AS AMT from capstone.data2014   "
                  + "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') >=  '20140101' "
                  + "AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') <=  '20141231' "
                  + "Group by MONTH(STR_TO_DATE(PROC_DATE,'%m/%d/%y'))");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TDATE");
                String em = rs.getString("AMT");
                   
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
          s.executeQuery("SELECT b.mcc_category AS CATG, SUM(a.Total_Spending) AS AMT "
                  + "FROM (SELECT MERCHANT_CATEGORY as mcc, SUM(ENTRY_AMOUNT) as 'Total_Spending' "
                  + "FROM capstone.data2014 WHERE COMPANY_NUMBER = '9' AND ENTRY_AMOUNT_SIGN = 'p' "
                  + "AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') >=  '20140101' AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') <=  '20141231' "
                  + "GROUP BY MERCHANT_CATEGORY) a join capstone.mcc_code b on (a.mcc = b.mcc) "
                  + "GROUP BY b.mcc_category");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("CATG");
                String em = rs.getString("AMT");
                   
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
        
        try
        {
          // database login password
          sess = detail.doSshTunnel(DBProperties.getStrSshUser(), DBProperties.getStrSshPassword(), DBProperties.getStrSshHost(), 
                  DBProperties.getnSshPort(), DBProperties.getStrRemoteHost(), DBProperties.getnLocalPort(),
                  DBProperties.getnRemotePort());

          Class.forName("com.mysql.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:"+DBProperties.getnLocalPort(), 
                  DBProperties.getStrDbUser(), DBProperties.getStrDbPassword());
          
        }
        catch(Exception e )
        {
          e.printStackTrace();
        }
        return con;
      
          
    }
    
    public static void dbClose(Connection con) {
        
        try {
            sess.disconnect();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(summary.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    static Session doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException
    {
        final JSch jsch = new JSch();
        Session session = jsch.getSession( strSshUser, strSshHost, 22 );
        session.setPassword( strSshPassword );

        final Properties config = new Properties();
        config.put( "StrictHostKeyChecking", "no" );
        session.setConfig( config );

        session.connect();
        session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
        
        return session;
    }
    

}
