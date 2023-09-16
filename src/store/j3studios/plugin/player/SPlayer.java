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
	
    // ECONOMY
    private Integer silvers = 0;
    private Integer golds = 0;
    
    // MANA
    private Double mana = 20.0;
    
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

    /*
    
    
    */

    public Double getMana() {
        return mana;
    }

    public void setMana(Double mana) {
        this.mana = mana;
    }
    
    
    
    /*
    
    
    */
    
    
    public String getJob1() {
        return job1;
    }

    public void setJob1(String job1) {
        this.job1 = job1;
    }

    public String getMarryUUID() {
        return marryUUID;
    }

    public void setMarryUUID(String marryUUID) {
        this.marryUUID = marryUUID;
    }

    public String getMarryName() {
        return marryName;
    }

    public void setMarryName(String marryName) {
        this.marryName = marryName;
    }

    public String getMarryDate() {
        return marryDate;
    }

    public void setMarryDate(String marryDate) {
        this.marryDate = marryDate;
    }

    public Integer getSilvers() {
        return silvers;
    }

    public void setSilvers(Integer silvers) {
        this.silvers = silvers;
    }

    public Integer getGolds() {
        return golds;
    }

    public void setGolds(Integer golds) {
        this.golds = golds;
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
