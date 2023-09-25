package store.j3studios.plugin.managers;

import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;

public class EconomyManager {
    
    private SCore core;
    
    public EconomyManager (SCore core) {
        this.core = core;
    }
    
    public Integer getBalance (Player player, WALLET_TYPE type) {
        SPlayer sp = PlayerManager.get().getPlayer(player.getUniqueId());        
        if (type == WALLET_TYPE.GOLDS) {
            return sp.getSilvers();
        }
        return sp.getGolds();
    }
    
    public void setBalance (Player player, WALLET_TYPE type, Integer value) {
        SPlayer sp = PlayerManager.get().getPlayer(player.getUniqueId());        
        if (type == WALLET_TYPE.GOLDS) {
            sp.setGolds(value);
        } else {
            sp.setSilvers(value);
        }
    }
    
    public void addBalance (Player player, WALLET_TYPE type, Integer value) {
        SPlayer sp = PlayerManager.get().getPlayer(player.getUniqueId()); 
        Integer newValue = this.getBalance(player, type) + value;
        this.setBalance(player, type, newValue);
    }
    
    public void takeBalance (Player player, WALLET_TYPE type, Integer value) {
        SPlayer sp = PlayerManager.get().getPlayer(player.getUniqueId());        
        Integer newValue = this.getBalance(player, type) - value;
        this.setBalance(player, type, newValue);
    }
    
    public enum WALLET_TYPE {
        SILVERS,
        GOLDS;
    }
    
}
