package store.j3studios.plugin.protections;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;
import store.j3studios.plugin.utils.ItemBuilder;
import store.j3studios.plugin.utils.Tools;

public class ProtectionListener implements Listener {
    
    private final Map<Player, Protections> playerRegionMap = new HashMap<Player, Protections>();
    
    @EventHandler
    public void onBlockBreakEvent (BlockBreakEvent e) {
        Player p = e.getPlayer();
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
        
        for (Protections protes : ProtectionManager.get().ProtectionsSet()) {
            if (protes.containsBlock(e.getBlock())) {
                if (!sp.getProtectionMember().contains(protes.getUuid()) && !sp.getProtectionOwner().contains(protes.getUuid())) {
                    e.setCancelled(true);
                    p.sendMessage(Tools.get().Text("&c&lHEY! &7No puedes romper en esta zona protegida por &e" + protes.getOwner()));
                    return;
                }
                
                if (protes.getSelection().getCenterPoint().equals(e.getBlock())) {
                    if (!sp.getProtectionOwner().contains(protes.getUuid())) {
                        e.setCancelled(true);
                        p.sendMessage(Tools.get().Text("&c&lHEY! &7No puedes romper la protección si no eres el lider &e" + protes.getOwner() + "&7. (ID: " + protes.getUuid() + ")"));
                        return;
                    }
                    
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    
                    Integer size = protes.getSize();
                    Double cost = (1450.00 * size);
                    Double tax = (cost/10);
                    
                    p.getInventory().addItem(ItemBuilder.crearItem(Material.GOLD_ORE, 1, "&6Proteccion: &fx" + size, 
                            "&7Bloque de protección.",
                            " ",
                            "&fTamaño de Protección: &e" + size + "x" + size,
                            "&fCosto de Impuestos: &6&l$&e" + Tools.get().formatMoney(tax),
                            " "));
                    
                    Tools.get().playSound(p, Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
                    p.sendMessage(Tools.get().Text("&fEliminaste la protección con el ID &e" + protes.getUuid()));
                    sp.getProtectionOwner().remove(protes.getUuid());
                    ProtectionManager.get().removeProtection(protes.getUuid());
                }
            }
        }
        
        
    }
    
    
    
}
