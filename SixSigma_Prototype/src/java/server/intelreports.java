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
import model.intelRebate;
import model.DBProperties;

/**
 * This class is the servlet for handling UI requests from homepage
 * @author Nikita
 */
@WebServlet(name = "intelreports", urlPatterns = {"/intelreports"})
public class intelreports extends HttpServlet {
    
    public static Connection con = null;
    public static Session sess = null;
    public static List<intelRebate> intelRebateData;
    

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        intelRebateData = new ArrayList<intelRebate>();
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
        
        intelRebateData = getRebateData(); 
        dbClose();
	String jsonRebateString = gson.toJson(intelRebateData);
	response.setContentType("application/json");
	response.getWriter().write(jsonRebateString);
   
        
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

    private List<intelRebate> getRebateData() {
        List<intelRebate> tempData = new ArrayList<intelRebate>();
	
        try
        {
          Statement s = con.createStatement();
          s.executeQuery("SELECT a1.mcc_category AS CATG, a1.industry_average As IAMT, a2.COMPANY9_average AS OAMT " +
"FROM " +
" (SELECT mcc_category,avg(ENTRY_AMOUNT) as 'industry_average'" +
" FROM capstone.data2014 JOIN capstone.mcc_code on MERCHANT_CATEGORY=mcc" +
" WHERE COMPANY_NUMBER in (SELECT COMPANY_NUMBER" +
" FROM capstone.comp_nacis_credit" +
" WHERE nacis_code= (select nacis_code" +
"				from capstone.comp_nacis_credit" +
"				where COMPANY_NUMBER='9') and credit_bucket=(select credit_bucket" +
"				from capstone.comp_nacis_credit" +
"				where COMPANY_NUMBER='9') and COMPANY_NUMBER<>'9')and ENTRY_AMOUNT_SIGN='P'" +
" GROUP BY mcc_category) a1 " +
" LEFT JOIN (select mcc_category,avg(ENTRY_AMOUNT) as 'COMPANY9_average'" +
" FROM capstone.data2014 JOIN capstone.mcc_code on MERCHANT_CATEGORY=mcc" +
" where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN='P'" +
" group by mcc_category" +
") a2 USING (mcc_category)");
            ResultSet rs = s.getResultSet();
            while (rs.next()) {

                String dt = rs.getString("CATG");
                String iamt = rs.getString("IAMT");
                String oamt = rs.getString("OAMT");
                long iamtd = Math.round(Double.parseDouble(iamt));
                long oamtd = Math.round(Double.parseDouble(oamt));
                intelRebate d1 = new intelRebate(dt,iamtd * 0.01,oamtd * 0.01); 
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
    
    public static void dbClose() {
        
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