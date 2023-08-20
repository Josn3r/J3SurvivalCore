package store.j3studios.plugin.player;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.entity.Player;

public class SPlayer {
    
    private UUID uuid;
    private Player player;
     
    private String job1 = null;
    
    // MARRY
    private String marryUUID = null;
    private String marryName = null;
    private String marryDate = null;
	
    // PROTECTION
    private ArrayList<String> protectionOwner = new ArrayList<String>();
    private ArrayList<String> protectionMember = new ArrayList<String>();

    // SPAZIOCOINS
    private int spaziocoins = 0;
    
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

    public ArrayList<String> getProtectionOwner() {
        return protectionOwner;
    }

    public ArrayList<String> getProtectionMember() {
        return protectionMember;
    }

    
}
