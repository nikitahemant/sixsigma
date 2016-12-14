/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Nikita
 */
public class DBProperties {
          private static final String strSshUser = "jason";                  // SSH loging username
          private static final String strSshPassword = "jason";                   // SSH login password
          private static final String strSshHost = "128.2.144.201";          // hostname or ip or SSH server
          private static final int nSshPort = 22;                                    // remote SSH host port number
          private static final String strRemoteHost = "localhost";  // hostname or ip of your database server
          private static final int nLocalPort = 3366;                                // local port number use to bind SSH tunnel
          private static final int nRemotePort = 3306;                               // remote port number of your database 
          private static final String strDbUser = "root";                    // database loging username
          private static final String strDbPassword = "root"; 

    public static String getStrSshUser() {
        return strSshUser;
    }

    public static String getStrSshPassword() {
        return strSshPassword;
    }

    public static String getStrSshHost() {
        return strSshHost;
    }

    public static int getnSshPort() {
        return nSshPort;
    }

    public static String getStrRemoteHost() {
        return strRemoteHost;
    }

    public static int getnLocalPort() {
        return nLocalPort;
    }

    public static int getnRemotePort() {
        return nRemotePort;
    }

    public static String getStrDbUser() {
        return strDbUser;
    }

    public static String getStrDbPassword() {
        return strDbPassword;
    }
          
          
}
