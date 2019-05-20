/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class DBManager {

    public static String translateFromMySQLtoSQLite(String query) {
        return query
                .replaceAll("INT", "INTEGER")
                .replaceAll("AUTO_INCREMENT", "")
                .replaceAll("UNIQUE INDEX ID_UNIQUE", "PRIMARY KEY")
        ;
    }

    private String hostname;
    private String port;
    private String database;
    private String user;
    private String password;
    private String driver;

    private Connection connection;
    
//    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_MYSQL =  "com.mysql.cj.jdbc.Driver";
    public static final String DRIVER_SQLLITE = "org.sqlite.JDBC";

    /**
     * Use this costructor for SQLite database
     * @param database Name of SQLite database
     * @param driver
     */
    public DBManager(String database, String driver) {
        this(null, null, database, null, null, driver);
    }
    
    /**
     * Use this costructor for MySQL database
     * @param hostname
     * @param port
     * @param database
     * @param user
     * @param password
     * @param driver 
     */
    public DBManager(String hostname, String port, String database, String user, String password, String driver) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.driver = driver;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        connection = null;
    }

    public void connect() throws SQLException {
        if(driver.contains("mysql"))
            this.connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useLegacyDatetimeCode=false&serverTimezone=UTC", user, password);
        else if(driver.contains("sqlite"))
        	this.connection = DriverManager.getConnection("jdbc:sqlite:" + database);
//        	this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        	
        else
            throw new SQLException("Cannot parse JDBC Driver Name");
    }

    public void disconnect() throws SQLException {
        this.connection.close();
        this.connection = null;
    }

    public Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed())
            this.connect();
        
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getDriver() {
        return driver;
    }
    
    
}
