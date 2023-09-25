package store.j3studios.plugin.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;

public class PlayerSQL {
    
    private static PlayerSQL ins;
    
    public static PlayerSQL get() {
        if (ins == null)
            ins = new PlayerSQL();
        return ins;
    }
    
    //
        
    public String getNickname(String uuid) {
        String value = null;
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `nickname` FROM `player_data` WHERE `uuid` = '" + uuid + "';");
            st.executeQuery();
            ResultSet rs = st.getResultSet();
            value = rs.next()? rs.getString("nickname") : null;
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
        return value;
    }
        
    public void createPlayer(Player player) {
        try {
            PreparedStatement st1;
            st1 = SQL.get().getConnection().prepareStatement("SELECT * FROM `player_data` WHERE `uuid` = ? LIMIT 1;");
            st1.setString(1, player.getUniqueId().toString());
            
            st1.executeQuery();
            ResultSet rs = st1.getResultSet();
            if (!rs.next()) {
                PreparedStatement st;
                st = SQL.get().getConnection().prepareStatement("INSERT INTO `player_data` VALUES (?, ?, ?, ?, ?, ?)");
                st.setInt(1, 0);
                
                st.setString(2, player.getUniqueId().toString());
                st.setString(3, player.getName());      
                
                st.setInt(4, 1);
                st.setInt(5, 0);
                st.setInt(6, 0);
                
                st.executeUpdate();
                st.close();
            } else {
                PreparedStatement st;
                st = SQL.get().getConnection().prepareStatement("UPDATE `player_data` SET `nickname` = ? WHERE `uuid` = ? LIMIT 1;");
                st.setString(1, player.getName());      
                st.setString(2, player.getUniqueId().toString());
                st.executeUpdate();
                st.close();
            }
            rs.close();
            st1.close();
        } catch (SQLException e) {
            SCore.debug("createPlayer Exception #1:" + e.getMessage());
        }
    }
    
    /**/
    
    public Integer getUserID(String userUUID) {
        Integer id = 0;
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `id` FROM `player_data` WHERE `uuid` = '" + userUUID + "';");
            st.executeQuery();
            ResultSet rs = st.getResultSet();
            id = rs.next()? rs.getInt("id") : 0;
            rs.close();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
        return id;
    }
    
}