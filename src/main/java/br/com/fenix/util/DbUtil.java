package br.com.fenix.util;

import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

public class DbUtil {
//	  private static final Logger LOGGER = LoggerFactory.getLogger(SQLExceptionHandlingBestPractices.class);
	  
	public static String handleSQLException(Exception ex) {
			if (ex instanceof SQLException) {
			ex.printStackTrace(System.err);
			System.err.println("SQLState: " + ((SQLException) ex).getSQLState());
			System.err.println("Error Code: " + ((SQLException) ex).getErrorCode());
			System.err.println("Message: " + ex.getMessage());
			Throwable t = ex.getCause();
			while (t != null) {
				System.out.println("Cause: " + t);
				t = t.getCause();
			}		        
		}
		if (ex instanceof PSQLException) {
			ex.printStackTrace(System.err);
			System.err.println("SQLState: " + ((PSQLException) ex).getSQLState());
			System.err.println("Error Code: " + ((PSQLException) ex).getErrorCode());
			System.err.println("Message: " + ex.getMessage());
			Throwable t = ex.getCause();
			while (t != null) {
				System.out.println("Cause: " + t);
				t = t.getCause();
			}		        
		}
		if (ex instanceof DataAccessException) {
			DataAccessException DAexception = (DataAccessException) ex; 
			return handleSQLException ( DAexception); 
		}
		return ex.getLocalizedMessage();
	}

	  public static String  handleSQLException(DataAccessException ex) {
		  String errorMensagemString  = ""; 

		  if ( ex instanceof 	OptimisticLockingFailureException) { 
			  System.out.println("OptimisticLockingFailureException"); 
		  }
		  if ( ex instanceof 	DataIntegrityViolationException) { 
			  System.out.println("DataIntegrityViolationException"); 
		  }

		  ex.printStackTrace(System.err);
		  System.err.println(ex.getLocalizedMessage()); 
		  Throwable t = ex.getCause();
		  while (t != null) {
			  System.out.println("Cause: " + t);
			  t = t.getCause();
		  }

		  return ex.getLocalizedMessage();
	  }
//	  public static void handleSQLException(SQLException e) { 
//	        // handle the SQL exception 
//	        int errorCode = e.getErrorCode(); 
//	        String sqlState = e.getSQLState(); 
//	        String errorMessage = e.getMessage(); 
//	  
//	        System.out.println("SQL Error Code: " + errorCode); 
//	        System.out.println("SQL State: " + sqlState); 
//	        System.out.println("Error Message: " + errorMessage); 
//	  
//	        e.printStackTrace(); 
//	    } 
//	  

//	  public static void handleSQLException(SQLException ex) {
//	        for (Throwable e : ex) {
//	            if (e instanceof SQLException) {
//	                LOGGER.error("SQLState: {}", ((SQLException) e).getSQLState());
//	                LOGGER.error("Error Code: {}", ((SQLException) e).getErrorCode());
//	                LOGGER.error("Message: {}", e.getMessage());
//	                Throwable t = ex.getCause();
//	                while (t != null) {
//	                    LOGGER.error("Cause: {}", t);
//	                    t = t.getCause();
//	                }
//	            }
//	        }
}
