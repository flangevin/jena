/*
 * (c) Copyright 2006, 2007, 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sdb.sql;
/* H2 contribution from Martin HEIN (m#)/March 2008 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.sdb.SDBException;
import com.hp.hpl.jena.sdb.shared.Access;
import com.hp.hpl.jena.sdb.shared.SDBNotFoundException;
import com.hp.hpl.jena.sdb.store.DatabaseType;

public class JDBC
{
    private static Log log = LogFactory.getLog(JDBC.class) ; 
    // The "well known" not a JDBC connection really scheme
    public static final String jdbcNone = "jdbc:none" ;

    private static Map<DatabaseType, String> driver = new HashMap<DatabaseType, String>() ;
    static {
        driver.put(DatabaseType.MySQL,      "com.mysql.jdbc.Driver") ;
        driver.put(DatabaseType.PostgreSQL, "org.postgresql.Driver") ;
        driver.put(DatabaseType.H2,         "org.h2.Driver") ;
        driver.put(DatabaseType.HSQLDB,     "org.hsqldb.jdbcDriver") ;
        driver.put(DatabaseType.Derby,      "org.apache.derby.jdbc.EmbeddedDriver") ;
        //driver.put(DatabaseType.Derby,       "org.apache.derby.jdbc.ClientDriver") ;
        driver.put(DatabaseType.SQLServer,  "com.microsoft.sqlserver.jdbc.SQLServerDriver") ;
        driver.put(DatabaseType.Oracle,     "oracle.jdbc.driver.OracleDriver") ;
        driver.put(DatabaseType.DB2,        "com.ibm.db2.jcc.DB2Driver") ;
    }
    
    static public String getDriver(DatabaseType dbType) { return driver.get(dbType) ; }
    
    /** Explicitly load the HSQLDB driver */ 
    static public void loadDriverHSQL()  { loadDriver(driver.get(DatabaseType.HSQLDB)) ; }
    /** Explicitly load the H2 driver */ 
    static public void loadDriverH2()  { loadDriver(driver.get(DatabaseType.H2)) ; }
    /** Explicitly load the MySQL driver */ 
    static public void loadDriverMySQL() { loadDriver(driver.get(DatabaseType.MySQL)) ; }
    /** Explicitly load the PostgreSQL driver */ 
    static public void loadDriverPGSQL() { loadDriver(driver.get(DatabaseType.PostgreSQL)); }
    /** Explicitly load the Derby driver */ 
    static public void loadDriverDerby() { loadDriver(driver.get(DatabaseType.Derby)); }
    /** Explicitly load the SQLServer driver */ 
    static public void loadDriverSQLServer() { loadDriver(driver.get(DatabaseType.SQLServer)); }
    /** Explicitly load the Oracle driver */ 
    static public void loadDriverOracle() { loadDriver(driver.get(DatabaseType.Oracle)); }
    /** Explicitly load the DB2 driver */ 
    static public void loadDriverDB2() { loadDriver(driver.get(DatabaseType.DB2)); }
    
    static public void loadDriver(String className) { loadClass(className) ; }
    
    static public String guessDriver(String type)
    { 
        return getDriver(DatabaseType.fetch(type)) ;
    }
    
    // This is the only place a driver is created.
    public static Connection createConnection(String url, String user, String password) throws SQLException
    {
        if ( log.isDebugEnabled() )
            log.debug("Create JDBC connection: "+url);
        
        if ( url.equals(jdbcNone) )
            return null ;
        
        if ( user == null )
            user = Access.getUser() ;
        if ( password == null )
            password = Access.getPassword() ;

        return DriverManager.getConnection(url, user, password) ;
    }
    
//    static public void loadClass(String className)
//    { Loader.loadClass(className) ; }

    static private void loadClass(String className)
    { 
        try { Class.forName(className); }
        catch (ClassNotFoundException ex)
        { throw new SDBNotFoundException("Class.forName("+className+")", ex) ; } 
    }

    public static String makeURL(String type, String host, String dbName)
    { return makeURL(type, host, dbName, null, null) ; }

    // How to make URLs.
    public static String makeURL(String type, String host, String dbName, String user, String password)
    {
        if ( log.isDebugEnabled() )
            log.debug(String.format("Create JDBC URL: (type=%s, host=%s, dbName=%s)", type, host, dbName)) ;
        
        type = type.toLowerCase() ;
        
        if ( user == null )
            user = Access.getUser() ;
        if ( password == null )
            password = Access.getPassword() ;
        
        if ( type.equals("mysql") || type.equals("postgresql") || type.equals("pgsql") )
        {
            String s = String.format("jdbc:%s://%s/%s", type, host, dbName) ;
            return s ;
        }
        
        if ( type.startsWith("hsql"))
        {
            String s = String.format("jdbc:%s:%s:%s", type, host, dbName) ;
            return s ;
        }
        
        if ( type.startsWith("h2"))
        {
            if ( type.startsWith("h2:tcp") || type.startsWith("h2:ssl"))
            {
                String s = String.format("jdbc:%s://%s/%s", type, host, dbName) ;
                return s ;
            }

            // The rest including -- h2:file, h2:mem or h2
            // Ignores host
            String s = String.format("jdbc:%s:%s", type, dbName) ;
            return s ;
        }

        if ( type.startsWith("pgsql"))
        {
        	String s = String.format("jdbc:%s://%s/%s", type, host, dbName) ;
        	return s ;
        }
        
        if ( type.equals("derby") )
        {
            //jdbc:derby:sdb2;create=true
            String s = String.format("jdbc:%s:%s;create=true", type, dbName) ;
            return s ;
        }
        
        if ( type.equals("mssqlserver") || type.equals("sqlserver") )
        {
            //jdbc:sqlserver://localhost;databaseName=sdb_layout1
            String s = String.format("jdbc:%s://%s;databaseName=%s", "sqlserver", host, dbName) ;
            return s ;
        }
        
        if ( type.equals("mssqlserverexpress") || type.equals("sqlserverexpress"))
        {
            //jdbc:sqlserver://${TESTHOST}\\SQLEXPRESS;databaseName=jenatest"
            String s = String.format("jdbc:%s://%s\\SQLEXPRESS;databaseName=%s","sqlserver",host, dbName) ;
            return s ;
        }
        
        if ( type.startsWith("oracle:") )
        {
        	String s = String.format("jdbc:%s:@%s:%s", type, host, dbName) ;
        	return s;
        }
        
        if ( type.equals("oracle") )
        {
            // If not specified, use the thin driver.
            String s = String.format("jdbc:%s:thin:@%s:%s", type, host, dbName) ;
            return s;
        }
        
        if ( type.equals("db2") )
        {
            String s = String.format("jdbc:%s://%s/%s", type, host, dbName) ;
            return s;
        }

        if ( type.equals("none") )
            return jdbcNone ;
        
        throw new SDBException("Don't know how to construct a JDBC URL for "+type) ;
    }
}

/*
 * (c) Copyright 2006, 2007, 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
