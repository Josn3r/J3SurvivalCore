package store.j3studios.plugin.player;

import java.util.List;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import me.josn3r.plugin.utils.ScoreboardUtil;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.managers.ScoreboardManager;
import store.j3studios.plugin.utils.Tools;

public class SPlayer {
    
    private final UUID uuid;
    private final Player player;
        	
    // ECONOMY
    private Integer silvers = 0;
    private Integer golds = 0;
    
    // HEALTH && MANA
    private Double health = 120.0;
    private Double mana = 100.0;
    
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

    public Double getHealth() {
        return health;
    }

    public void setHealth(Double health) {
        this.health = health;
    }

    public Double getMana() {
        return mana;
    }

    public void setMana(Double mana) {
        this.mana = mana;
    }
        
    /*
        
    */

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
    
    //
    
    public void startHealing() {
        Tools.get().playParticle(Particle.VILLAGER_HAPPY, getPlayer(), 1, 1.0, 2.5, 1.0);
    }
    
}
