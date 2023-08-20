package store.j3studios.plugin.player;

import java.util.UUID;
import org.bukkit.entity.Player;

public class SPlayer {
    
    private UUID uuid;
    private Player player;
     
    private String job1 = null;
    
    public SPlayer (UUID uuid, Player player) {
        this.uuid = uuid;
        this.player = player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public String getJob1() {
        return job1;
    }
        
}
