package store.j3studios.plugin.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import me.josn3r.plugin.utils.ScoreboardUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.managers.ScoreboardManager;
import store.j3studios.plugin.utils.Tools;

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

    /*
    
    */
    
    public void createScoreboard(final Player p) {
        final ScoreboardUtil s = new ScoreboardUtil("&6&lSURVIVAL RP", "normal");
       
    	new BukkitRunnable() {
            @Override
            public void run() {  
                if (!getPlayer().isOnline()) {
                    cancel();
                    return;
                }            	
            	Integer i = 0;  
                
            	s.setName(Tools.get().Text(PlaceholderAPI.setPlaceholders(p, ScoreboardManager.get().getName(p))));      
            	            	
            	List<String> lineas = ScoreboardManager.get().getLines(p);
                int line = lineas.size()-1;
                while (line >= 0) {
                    s.lines(i, Tools.get().Text(ScoreboardManager.get().vars(p, lineas.get(line))));
                    ++i;
                    --line;
                }            	
                while (i < 15) {
                    s.removeLines(i);
                    ++i;
                }
            }	           
        }.runTaskTimerAsynchronously(SCore.get(), 0L, 20L);
        s.build(p);
    }
    
}
