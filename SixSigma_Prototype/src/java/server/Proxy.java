/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
 
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author Nikita
 */
public class Proxy {
    
    private static void doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException
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
    public static void main(String[] args)
    {
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
      s.executeQuery("select  TRANSACTION_DATE, ENTRY_AMOUNT from capstone.data2014 " +
        "where COMPANY_NUMBER='9' and ENTRY_AMOUNT_SIGN = 'p' " +
        "group by TRANSACTION_DATE");
        ResultSet rs = s.getResultSet();
            int i = 0;
            while (rs.next()) {
                
                String dt = rs.getString("TRANSACTION_DATE");
                String em = rs.getString("ENTRY_AMOUNT");
                i++;
                System.out.println("Transaction Date: " + dt + " Entry Amount: " + em);
            }
            rs.close();
            s.close();
            System.out.println("Total rows: " + i);
      
      con.close();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    finally
    {
      System.exit(0);
    }
  }
//    public static void main(String... args)
//    {
//        String host = "keybank.heinz.cmu.edu";
//        String user="jason";
//        String password="jason";
//
//        final String dbuserName = "root";
//        final String dbpassword = "root";
//        final String dburl = "jdbc:mysql://localhost:3306/";
//        final String db_name = "capstone";
//
//        Connection db = null;
//        Session session= null;
//
//        System.out.println("Initializing Mysql...");
//        try {
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            JSch jsch = new JSch();
//            session=jsch.getSession(user, host, 22);
//            session.setPassword(password);
//            session.setConfig(config);
//            session.connect();
//            System.out.println("SSH Connected");
//
//            String url = dburl+ db_name+"?useSSL=false";
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            db = DriverManager.getConnection(url, dbuserName, dbpassword);
//            System.out.println("DB connection successful for database \"" + db_name + "\"");
//        } catch (Exception e) {
//            System.err.println("DB connection failed: " + e.getMessage());
//            System.exit(1);
//        } finally {
//            if (db != null) {
//                try {
//                    db.close();
//                    System.out.println("DB connection closed.");
//                } catch (Exception e) {
//                    System.err.println("DB connection close error: " + e.getMessage());
//                }
//            }
//        }
//    }
}
