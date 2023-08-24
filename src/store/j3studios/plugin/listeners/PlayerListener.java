package store.j3studios.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import store.j3studios.plugin.database.PlayerSQL;
import store.j3studios.plugin.database.ProtectionsSQL;
import store.j3studios.plugin.database.SQL;
import store.j3studios.plugin.managers.ScoreboardManager;
import store.j3studios.plugin.player.PlayerManager;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void onPlayerJoinEvent (PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        
        if (!PlayerManager.get().doesPlayerExists(p.getUniqueId())) {
            PlayerManager.get().createPlayer(p);
        }
        PlayerSQL.get().createPlayer(p);   
        PlayerSQL.get().loadProtectionOwnerUser(p.getUniqueId().toString(), PlayerSQL.get().getUserID(p.getUniqueId().toString()));
        PlayerSQL.get().loadProtectionMemberUser(p.getUniqueId().toString(), PlayerSQL.get().getUserID(p.getUniqueId().toString()));
        ScoreboardManager.get().createScoreboard(p);
    }
    
    public void onPlayerMoveEvent (PlayerMoveEvent e) {
        Player p = e.getPlayer();        
    }
    
}
