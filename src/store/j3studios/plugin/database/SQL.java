package store.j3studios.plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;
import store.j3studios.plugin.SCore;

public class SQL {
    
    private static SQL ins;
    
    public static SQL get() {
        if (ins == null)
            ins = new SQL();
        return ins;
    }
    
    private Connection con;
    
    private String hostname = "";
    private Integer port = 3307;
    private String username = "";
    private String password = "";
    private String database = "";
    
    public Connection getConnection() {
        return con;
    }
    
    public void openConnection() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true&useUnicode=yes", username, password);
            SCore.debug("&aMySQL connect success!");
            
            // CREATE TABLE
        } catch (SQLException e) {
            SCore.debug("&cDatabase MySQL Failed during connecting! Please check your config...");
        }
    }
    
    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
        }
    }
    
    public void updateConnection() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(SCore.get(), () -> {
            Connection connect = SQL.get().getConnection();
            try {
                PreparedStatement keepAlive = connect.prepareStatement("SELECT 1 FROM `player_data`");
                keepAlive.executeQuery();
            } catch (SQLException e) {
                
            }
        }, 0, 100L);
    }
    
    // CREATE TABLE PLAYER_DATA
    public void createTable_PlayerData() {
        Connection connect = SQL.get().getConnection();
        try {
            Statement state = connect.createStatement();
            state.executeUpdate("CREATE TABLE IF NOT EXISTS `player_data` ("
                    + "`id` INT NOT NULL AUTO_INCREMENT,"
                    + "`uuid` VARCHAR(255),"
                    + "`nickname` VARCHAR(255),"
                    + "PRIMARY KEY (id))");
            state.close();
        } catch (SQLException e) {
            
        }
    }
    
    
    
}
