/*
 * Created by:  Pieter Sartain
 *
 * Licensed under the GPL:
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 *
 */

package com.kaear.common;

import com.kaear.gui.*;
import com.kaear.interfaces.*;


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

import java.net.InetAddress;
import org.apache.derby.drda.*;

public class database implements dbase
{
	private String driver = "org.apache.derby.jdbc.ClientDriver";
	private String protocol = "jdbc:derby:";
	private Statement s;
	private Statement screate;
	private Connection conn;

	public database()
	{

    	new exhandle("Initialising ...", null);

		try
		{
		
			System.setProperty("derby.drda.portNumber","2323");
			System.setProperty("derby.drda.host","192.168.1.12");
			System.setProperty("derby.drda.startNetworkServer", "true");
				
			//NetworkServerControl server = new NetworkServerControl(InetAddress.getLocalHost(), 2323);
			//NetworkServerControl server = new NetworkServerControl(InetAddress.getByName("localhost"), 2323);
			
			//NetworkServerControl server = new NetworkServerControl();
			//server.start (null);
			
	    	//new exhandle("Database initialised on: " + server.getCurrentProperties(), null);
			//new exhandle("Database initialised on " + InetAddress.getByName("localhost"), null);
			//new exhandle("Database initialised on " + InetAddress.getLocalHost(), null);
			
			new exhandle("Database initialised", null);
			
		}
		catch (Throwable e)
		{
			new exhandle("dbase initialisation failed:\n", e);
		}

    	new exhandle("Aquiring host ...", null);
		String host = "";
		
/* 		try
		{
			host = "//" + updateAddy().getHostName() + ":2323/";
	    	new exhandle("Aquired host: " + host, null);
		}
		catch (Throwable e)
		{
       	    new exhandle("dbase host aquisition failed: ", e);
		}
 */	
    	new exhandle("Connecting ...", null);
		
//		host = "//localhost:2323/";

		try
		{
			//Class.forName(driver).newInstance();
			//Class.forName(driver);
			Class.forName("org.apache.derby.jdbc.ClientDriver");

			Properties props = new Properties();
        	props.put("user", "user1");
        	props.put("password", "user1");
			props.put("create", "true");


//		System.setProperty("user","user1");
//		System.setProperty("password","user1");
		

			//conn = DriverManager.getConnection(protocol + host + "kerpowDB;create=true");
			conn = DriverManager.getConnection("jdbc:derby://192.168.1.12:2323/kerpowDB",props);

			new exhandle("Connected to database " + host + "kerpowDB\n", null);

        	//conn.setAutoCommit(false);
//			s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	//		screate = conn.createStatement();
			
        }
		catch (Throwable e)
        {
        	    new exhandle("dbase connect failed, host was: " + host + "\n", e);
				e.printStackTrace();
		}

	}
	
 	private InetAddress updateAddy() throws Exception
	{
/* 		try
		{
			String hostAddress = InetAddress.getHostAddress();
			byte[] hostIP = InetAddress.getAddress();
			myaddy = InetAddress.getByAddress(hostAddress, hostIP);
		}
		catch (Throwable e)
		{
	    	new exhandle("Failed to get an address for this host.", null);
		}
 */	
		InetAddress local = InetAddress.getLocalHost();
    	return local;
		
	} 

	public DatabaseMetaData getMetaData()
	{
		try { return conn.getMetaData(); } 
		catch (Throwable e) { 
		new exhandle("getMetaData failed: ", e);
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
        	    new exhandle("deInit failed:", e);
		}

	}
	
	public void dbCommit()
	{
		try
		{
			conn.commit();
		} catch (Throwable e)
        	{
        	    new exhandle("Commit failed:", e);
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
			new exhandle(error, e);
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
			new exhandle(error, e);
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
			new exhandle(error, e);
			return null;
		}
	}

}
