package com.kaear.common;

import com.kaear.cli.*;
import com.kaear.gui.*;


// Database SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

// The rest...
import java.util.Properties;
import java.io.File;
import java.lang.Process;

public class dbase
{
	private String framework = "embedded";
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String protocol = "jdbc:derby:";
	private Statement s;
	private Statement screate;
	private Connection conn;
	private int verbosityLevel = 0;

	public dbase()
	{
	
		verbosityLevel = kerpowObjectManager.verbosityLevel;
    	System.out.println("Initialising the database ...");

		try
		{
			Class.forName(driver).newInstance();

        	Properties props = new Properties();
        	props.put("user", "user1");
        	props.put("password", "user1");

			conn = DriverManager.getConnection(protocol + "kerpowDB;create=true");

        	System.out.println("Connected to database kerpowDB\n");

        	conn.setAutoCommit(false);
			s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			screate = conn.createStatement();
			
        	}
		catch (Throwable e)
        	{
        	    new exhandle("dbase init failed:", e, verbosityLevel);
		}

	}
	
	public DatabaseMetaData getMetaData()
	{
		try { return conn.getMetaData(); } 
		catch (Throwable e) { 
		System.out.println("getMetaData failed: " + e); 
		return null; }
	}
	
	public void deInit()
	{
		dbCommit();
		try
		{
		// Commit and release transaction/connection objects
		s.close();
		screate.close();
		conn.close(); 
		}
		catch (Throwable e)
        	{
        	    new exhandle("deInit failed:", e, verbosityLevel);
		}

	}
	
	public void dbCommit()
	{
		try
		{
			conn.commit();
		} catch (Throwable e)
        	{
        	    new exhandle("Commit failed:", e, verbosityLevel);
		}
	}

	/**
	 *   Allows the creation of tables
	 */
	public boolean sqlMake(String sqlstmt, String error)
	{
		try {
			screate.execute(sqlstmt);
			dbCommit();
			return true;
		}
		catch (Throwable e) { 
			new exhandle(error, e, verbosityLevel);
			return false;
		}
	}

	/**
	 *   Executes an SQL statment that does not return a ResultSet
	 */
	public boolean sqlRun(String sqlstmt, String error)
	{
		try {
			s.execute(sqlstmt);
			dbCommit();
			return true;
		}
		catch (Throwable e) { 
			new exhandle(error, e, verbosityLevel);
			return false;
		}
	}

	/**
	 *   Executes an SQL statment that returns a ResultSet
	 */
	public ResultSet sqlExe(String sqlstmt, String error)
	{
		try {
			return s.executeQuery(sqlstmt);
		}
		catch (Throwable e) { 
			new exhandle(error, e, verbosityLevel);
			return null;
		}
	}

	public void setVerbosity(int level)
	{
		verbosityLevel = level;
	}
	
}
