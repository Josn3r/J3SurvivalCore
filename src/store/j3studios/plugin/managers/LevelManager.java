package store.j3studios.plugin.managers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;
import store.j3studios.plugin.utils.Tools;

public class LevelManager {
    private SCore core;
    
    public LevelManager (SCore core) {
        this.core = core;
    }
	
    public String Text (String textToTranslate) {
        return Tools.get().Text(textToTranslate);
    }
        
    public void giveExp (Player p, Integer exp) {
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
		
        Integer playerLevel = sp.getLevel();
        Integer playerExp = sp.getExp();
        
        Integer maxExp = playerLevel * 532;
        
        if ((playerExp+exp) < maxExp) {
            sp.setExp(sp.getExp() + exp);
            sp.setTotalExp(sp.getTotalExp() + exp);
        } else {  
            Integer sobrante = (playerExp+exp)-maxExp;
            Integer giving = exp-sobrante;
            
            sp.setExp(sp.getExp() + giving);
            sp.setTotalExp(sp.getTotalExp() + giving);
            
            levelUP(p);
            giveExp(p, sobrante);
        }
    }
	    
    public Integer getNeededExp (Player p) {        
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
        Integer playerLevel = sp.getLevel();            
        return playerLevel*532;
    }
    
    public void levelUP (Player p) {
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());        
        Integer playerLevel = sp.getLevel();						
        // lastRank
        if (playerLevel == 1000) {
            return;
        }        
        sp.setLevel(sp.getLevel() + 1);
        sp.setExp(0);
        Tools.get().playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 10.0f, 1.0f);		
    }
    
    public SCore getCore() {
        return core;
    }
	
}
