package store.j3studios.plugin.protections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
    
    public void registerProtection (Player p, Selection selection) {
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
        
        String proteUUID = Tools.get().getAlphaNumericString(16);
        ArrayList<String> members = new ArrayList<String>();
        
        Protections prote = new Protections(proteUUID, p, members, selection);
        protections.put(proteUUID, prote);
        
        sp.getProtectionOwner().add(prote.getUuid());
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
