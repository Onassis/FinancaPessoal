package br.com.fenix.util;

import java.sql.SQLException;

public class DbUtil {
	  public static void handleSQLException(SQLException e) { 
	        // handle the SQL exception 
	        int errorCode = e.getErrorCode(); 
	        String sqlState = e.getSQLState(); 
	        String errorMessage = e.getMessage(); 
	  
	        System.out.println("SQL Error Code: " + errorCode); 
	        System.out.println("SQL State: " + sqlState); 
	        System.out.println("Error Message: " + errorMessage); 
	  
	        e.printStackTrace(); 
	    } 
}
