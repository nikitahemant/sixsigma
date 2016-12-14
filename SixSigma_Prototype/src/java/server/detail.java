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
import model.AccCategory;
import model.AccDaily;
import model.AccMonthly;
import model.AccSummary;
import model.DBProperties;


/**
 * This class is the servlet for handling UI requests from homepage
 * @author Nikita
 */
@WebServlet(name = "detail", urlPatterns = {"/detail"})
public class detail extends HttpServlet {

    public static Connection con = null;
    public static Session sess = null;
    public static AccSummary accSummaryData;
    public static List<AccDaily> accDailyData;
    public static List<AccMonthly> accMonthlyData;
    public static List<AccCategory> accCategoryData;

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        accSummaryData = null;
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
            
        con = dbConnect();
        Gson gson = new Gson();
        
        accDailyData = getAccDailyData(con);   
        accMonthlyData = getAccMonthlyData(con);   
        accCategoryData = getAccCategoryData(con);   

        dbClose(con);
        accSummaryData = new AccSummary(accDailyData,accMonthlyData,accCategoryData);
        
        String jsonSummaryString = accSummaryData.toJSON();
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

    private List<AccDaily> getAccDailyData(Connection con) {
        List<AccDaily> tempData = new ArrayList<AccDaily>();
	
        try
        {
          Statement s = con.createStatement();
          s.executeQuery("select PROC_DATE AS TDATE, ROUND(SUM(ENTRY_AMOUNT),2) AS AMT from capstone.data2014    "
                  + "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' AND ACCOUNT_NBR='976574000000000' "
                  + "GROUP BY PROC_DATE ORDER BY PROC_DATE desc LIMIT 7");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TDATE");
                String em = rs.getString("AMT");
                   
                AccDaily d1 = new AccDaily(dt,Double.parseDouble(em)); 
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
          s.executeQuery("select MONTH(STR_TO_DATE(PROC_DATE,'%m/%d/%y')) AS TDATE, SUM(ENTRY_AMOUNT) AS AMT from capstone.data2014    "
                  + "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') >=  '20140101' "
                  + "AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') <=  '20141231' AND ACCOUNT_NBR = '37787200000000' "
                  + "Group by MONTH(STR_TO_DATE(PROC_DATE,'%m/%d/%y'))");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("TDATE");
                String em = rs.getString("AMT");
                   
                AccMonthly d1 = new AccMonthly(dt,Double.parseDouble(em)); 
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
          s.executeQuery("SELECT b.mcc_category AS CATG, SUM(a.Total_Spending) AS AMT FROM "
                  + "(SELECT MERCHANT_CATEGORY as mcc, SUM(ENTRY_AMOUNT) as 'Total_Spending' "
                  + "FROM capstone.data2014   WHERE COMPANY_NUMBER = '9' "
                  + "AND ENTRY_AMOUNT_SIGN = 'p' AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') >=  '20140101' "
                  + "AND STR_TO_DATE(PROC_DATE,'%m/%d/%y') <=  '20141231' AND ACCOUNT_NBR = '37787200000000' "
                  + "GROUP BY MERCHANT_CATEGORY) a join capstone.mcc_code b on (a.mcc = b.mcc) "
                  + "GROUP BY b.mcc_category");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("CATG");
                String em = rs.getString("AMT");
                   
                AccCategory d1 = new AccCategory(dt,Double.parseDouble(em)); 
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
