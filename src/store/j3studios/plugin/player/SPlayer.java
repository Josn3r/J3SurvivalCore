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
    
    // LEVEL && EXP
    private Integer level = 1;
    private Integer exp = 0;
    private Integer totalExp = 0;
        	
    // ECONOMY
    private Integer silvers = 0;
    private Integer golds = 0;
    
    // HEALTH && MANA
    private Double health = 120.0;
    private Double mana = 100.0;
    
    // OPTIONS - SETTINGS
    private boolean enable_msg = false;
    
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(Integer totalExp) {
        this.totalExp = totalExp;
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
    
    /*
    
    ALL SETTINGS SECTION
    
    */

    public boolean isEnableMSG() {
        return enable_msg;
    }

    public void setEnableMSG(boolean value) {
        this.enable_msg = value;
    }  
    
}
