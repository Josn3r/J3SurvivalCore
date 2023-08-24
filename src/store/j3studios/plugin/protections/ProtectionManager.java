package store.j3studios.plugin.protections;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.database.PlayerSQL;
import store.j3studios.plugin.database.ProtectionsSQL;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;
import store.j3studios.plugin.utils.Tools;

public class ProtectionManager {
    
    private static ProtectionManager pm;
    private Map<String, Protections> protections = new HashMap<String, Protections>();
    
    public static ProtectionManager get() {
        if (pm == null) {
            pm = new ProtectionManager();
        }
        return pm;
    }
    
    public void loadProtections() {
        for (Integer id : ProtectionsSQL.get().loadProtectionData()) {
            String proteUUID = String.valueOf(ProtectionsSQL.get().loadData(id, "proteID"));
            Integer userID = Integer.valueOf(ProtectionsSQL.get().loadData(id, "userID").toString());
            String members = String.valueOf(ProtectionsSQL.get().loadData(id, "members"));
            Location centerPoint = Tools.get().setStringToLoc(String.valueOf(ProtectionsSQL.get().loadData(id, "centerPoint")), true);
            Integer size = Integer.valueOf(ProtectionsSQL.get().loadData(id, "radius").toString());
            
            String ownerUUID = ProtectionsSQL.get().getOwnerUUID(userID);
            
            byte[] members64 = Base64.getDecoder().decode(members);
            String membersToArray = new String(members64);
            ArrayList<String> membersList = new ArrayList<>();
            if (membersToArray.contains(" /// ")) {
                for (String str : membersToArray.split(" /// ")) {
                    membersList.add(str);
                }
            }
            
            Protections prote = new Protections(proteUUID, ownerUUID, membersList, new Selection(centerPoint.getBlock(), size));
            protections.put(proteUUID, prote);
            
            //SCore.debug("&7- Loaded - " + proteUUID + ", Owner UUID = " + prote.getOwnerUUID().toString() + ", Owner Nick = " + prote.getOwner().getName());
        }
        SCore.debug("&eSe han cargado &f" + protections.size() + " &eprotecciones.");
    }
    
    public void registerProtection (Player p, Selection selection) {
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
        
        String proteUUID = Tools.get().getAlphaNumericString(16);
        ArrayList<String> members = new ArrayList<String>();
        
        Protections prote = new Protections(proteUUID, p, members, selection);
        protections.put(proteUUID, prote);
        sp.getProtectionOwner().add(prote.getUuid());        
        
        this.saveProtection(proteUUID);
    }
    
    public void saveProtection (String proteUUID) {
        Protections prote = this.getProtection(proteUUID);
        
        String protectionMember64 = "";
        String proteMembers = "";
        if (!prote.getMembers().isEmpty()) {
            for (String str : prote.getMembers()) {
                proteMembers = proteMembers + str + " /// ";
            }
            proteMembers = proteMembers.substring(0, proteMembers.length()-5);
        }
        protectionMember64 = Base64.getEncoder().encodeToString(proteMembers.toString().getBytes());
        
        Integer userID = ProtectionsSQL.get().getOwnerID(prote.getOwnerUUID().toString());        
        ProtectionsSQL.get().saveProtection(prote.getUuid(), userID, proteMembers, Tools.get().setLocToString(prote.getLoc()), prote.getSize());
        ProtectionsSQL.get().saveProtectionOwnerUserID(ProtectionsSQL.get().getProteID(prote.getUuid()), userID);
    }
    
    public void removeProtection(String proteUUID) {
        this.protections.remove(proteUUID);
    }
    
    //

    public Map<String, Protections> getProtections() {
        return protections;
    }
    
    public Protections getProtection (String proteUID) {
        return this.protections.get(proteUID);
    }
    
    public Set<Protections> ProtectionsSet() {
        HashSet<Protections> hashSet = new HashSet<Protections>();
        for (String puid : this.getProtections().keySet()) {
            hashSet.add(this.getProtection(puid));
        }
        return hashSet;
    }
    
    public Boolean checkRadiusProtection(Location loc, Integer radius) {
        Selection selection = new Selection(loc.getBlock(), radius);
        return this.regionOverlaps(selection);
    }
    
    public Boolean regionOverlaps (Selection selection) {
        for (Protections region : this.ProtectionsSet()) {
            if (region.overlaps(selection))
                return true;
        }
        return false;
    }
    
    public Protections getOverlaps (Selection selection) {
        for (Protections region : this.ProtectionsSet()) {
            if (region.overlaps(selection))
                return region;
        }
        return null;
    }
    
}
