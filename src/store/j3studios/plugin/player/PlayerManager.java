package store.j3studios.plugin.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerManager {
    
    private static PlayerManager pm;
    private Map<UUID, SPlayer> players = new HashMap<>();
    
    public static PlayerManager get() {
        if (pm == null) {
            pm = new PlayerManager();
        }
        return pm;
    }
    
    public Map<UUID, SPlayer> getPlayers() {
        return this.players;
    }
    
    public SPlayer getPlayer(UUID uID) {
        return this.players.get(uID);
    }
    
    public boolean doesPlayerExists (UUID uID) {
        return this.players.containsKey(uID);
    }
    
    public void createPlayer (Player p) {
        this.players.put(p.getUniqueId(), new SPlayer(p.getUniqueId(), p));
    }
    
    public void removePlayer(Player p) {
        this.players.remove(p.getUniqueId());
    }
        
    public Set<SPlayer> PlayersSet() {
        HashSet<SPlayer> hashSet = new HashSet<SPlayer>();
        for (UUID uID : getPlayers().keySet()) {
            hashSet.add(this.getPlayer(uID));
        }
        return hashSet;
    }
}
