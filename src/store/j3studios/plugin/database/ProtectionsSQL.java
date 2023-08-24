package store.j3studios.plugin.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;

public class ProtectionsSQL {
    
    private static ProtectionsSQL ins;
    
    public static ProtectionsSQL get() {
        if (ins == null)
            ins = new ProtectionsSQL();
        return ins;
    }
    
    //
    
    public ArrayList<Integer> loadProtectionData() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `id` FROM `protections_data`;");
            st.executeQuery();
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
        return list;
    }
    
    public Integer getProteID(String proteUUID) {
        Integer id = 0;
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `id` FROM `protections_data` WHERE `proteID` = '" + proteUUID + "';");
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
    
    public Integer getOwnerID(String ownerUUID) {
        Integer id = 0;
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `id` FROM `player_data` WHERE `uuid` = '" + ownerUUID + "';");
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
    
    public String getOwnerUUID (Integer userID) {
        String id = "null";
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `uuid` FROM `player_data` WHERE `id` = '" + userID + "';");
            st.executeQuery();
            ResultSet rs = st.getResultSet();
            id = rs.next()? rs.getString("uuid") : "null";
            rs.close();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
        return id;
    }
    
    public void saveProtection(String proteUUID, Integer userID, String members, String centerPoint, Integer size) {
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("INSERT INTO `protections_data` VALUES (?, ?, ?, ?, ?, ?)");
            st.setInt(1, 0);
            st.setString(2, proteUUID);
            st.setInt(3, userID);
            st.setString(4, members);
            st.setString(5, centerPoint);
            st.setInt(6, size);            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
        //this.saveProtectionUserID(this.getProteID(proteUUID), userID);
    }
    
    public void saveProtectionOwnerUserID(Integer proteID, Integer userID) {
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("INSERT INTO `protections_ownerUser_data` VALUES (?, ?, ?)");
            st.setInt(1, 0);
            st.setInt(2, userID);
            st.setInt(3, proteID);    
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
    }
    
    public void saveProtectionMemberUserID(Integer proteID, Integer userID) {
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("INSERT INTO `protections_memberUser_data` VALUES (?, ?, ?)");
            st.setInt(1, 0);
            st.setInt(2, userID);
            st.setInt(3, proteID);    
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
    }
    
    public void deleteProtection (String proteUUID) {
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `uuid` FROM `player_data` WHERE `proteID` = '" + proteUUID + "';");
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
    }
    
    public Object loadData(Integer id, String data) {
        Object o = null;
        try {
            PreparedStatement st = SQL.get().getConnection().prepareStatement("SELECT `" + data + "` FROM `protections_data` WHERE `id` = '" + id + "';");
            st.executeQuery();
            ResultSet rs = st.getResultSet();
            o = rs.next()? rs.getObject(data) : null;
            rs.close();
            st.close();
        } catch (SQLException e) {
            SCore.debug(e.getMessage());
        }
        return o;
    }
    
}
