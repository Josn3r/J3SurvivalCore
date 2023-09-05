package store.j3studios.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import store.j3studios.plugin.database.PlayerSQL;
import store.j3studios.plugin.database.ProtectionsSQL;
import store.j3studios.plugin.database.SQL;
import store.j3studios.plugin.managers.MagicManager;
import store.j3studios.plugin.managers.ScoreboardManager;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.utils.Tools;

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
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
            
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
            
        if (item.getType() == Material.DIAMOND_SWORD) {
            event.setCancelled(true);
            MagicManager.get().slash(player);
        }        
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }

            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.STICK) {
                event.setCancelled(true);
                MagicManager.get().healingEffect(player, 10.0);
                return;
            }
            
            if (item.getType() == Material.IRON_SWORD) {
                event.setCancelled(true);
                MagicManager.get().orbitalStrike(player, player.getLocation());
            }
            
            if (item.getType() == Material.DIAMOND_SWORD) {
                event.setCancelled(true);
                //MagicManager.get().ceguera(player);
                MagicManager.get().slash(player);
            }

            /*if (!item.getItemMeta().getLore().contains("healing")) {
                return;
            }*/
            
        }
    }
    
}
