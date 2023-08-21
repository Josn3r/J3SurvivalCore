package store.j3studios.plugin.protections;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
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
                    p.sendMessage(Tools.get().Text("&c&lHEY! &7No puedes romper en esta zona protegida por &e" + protes.getOwner().getName()));
                    return;
                }
                
                if (protes.getSelection().getCenterPoint().equals(e.getBlock())) {
                    if (!sp.getProtectionOwner().contains(protes.getUuid())) {
                        e.setCancelled(true);
                        p.sendMessage(Tools.get().Text("&c&lHEY! &7No puedes romper la protección si no eres el lider &e" + protes.getOwner().getName() + "&7. (ID: " + protes.getUuid() + ")"));
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
    
    @EventHandler
    public void onBlockPlaceEvent (BlockPlaceEvent e) {
        Player p = e.getPlayer();
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
        
        for (Protections protes : ProtectionManager.get().ProtectionsSet()) {
            if (protes.containsBlock(e.getBlock())) {
                if (!sp.getProtectionMember().contains(protes.getUuid()) && !sp.getProtectionOwner().contains(protes.getUuid())) {
                    e.setCancelled(true);
                    p.sendMessage(Tools.get().Text("&c&lHEY! &7No puedes colocar bloques en esta zona protegida por &e" + protes.getOwner().getName()));
                    return;
                }
            }
        }
        
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(Tools.get().Text("&6Proteccion: &fx"))) {
            if (sp.getProtectionOwner().size() >= 3) {
                e.setCancelled(true);
                p.sendMessage(Tools.get().Text("&cEstás al limite de protecciones! No puedes tener más de 3!"));
                Tools.get().playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return;
            }
            
            String displayName = item.getItemMeta().getDisplayName();
            Integer size = Integer.valueOf(displayName.replace(Tools.get().Text("&6Proteccion: &fx"), ""));
            Double cost = (1450.00 * size);
            Double tax = (cost/10);
            
            if (ProtectionManager.get().checkRadiusProtection(e.getBlockPlaced().getLocation(), size)) {
                e.setCancelled(true);
                Selection selection = new Selection(e.getBlockPlaced(), size);
                Protections prote = ProtectionManager.get().getOverlaps(selection);
                p.sendMessage(Tools.get().Text("&cNo puedes colocar tu protección ahí, pues los limites se unen con la protección de &e" + prote.getOwner().getName()));
                Tools.get().playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return;
            }
            
            Tools.get().playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            ProtectionManager.get().registerProtection(p, new Selection(e.getBlockPlaced(), size));
            p.sendMessage(Tools.get().Text("&a&lFELICIDADES! &fHas protegido una nueva zona!"));
            p.sendMessage(Tools.get().Text("&cRecuerda que por tu protección deberás pagar &6&l$&e" + Tools.get().formatMoney(tax) + " &cde impuestos cada 24 horas!"));  
        }
    }
    
    @EventHandler
    public void onPlayerMoveEvent (PlayerMoveEvent e) {
        Player p = e.getPlayer();
        SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
        boolean inProte = false;
        
        for (Protections protes : ProtectionManager.get().ProtectionsSet()) {
            if (protes.containsPlayer(p)) {
                if (!playerRegionMap.containsKey(p)) {
                    String owner = protes.getOwner().getName();
                    Tools.get().sendActionBar(p, "&fEstás en la región de &e" + owner);
                    Tools.get().playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    playerRegionMap.put(p, protes);
                    inProte = true;
                    break;
                }
                
                if (playerRegionMap.containsKey(p) && !protes.equals(playerRegionMap.get(p))) {
                    String owner = protes.getOwner().getName();
                    Tools.get().sendActionBar(p, "&fEstás en la región de &e" + owner);
                    Tools.get().playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    playerRegionMap.replace(p, protes);                    
                }
                inProte = true;
                break;
            }
        }
        
        if (!inProte && playerRegionMap.containsKey(p)) {
            String owner = playerRegionMap.get(p).getOwner().getName();
            Tools.get().sendActionBar(p, "&cAbandonando la región de &e" + owner);
            Tools.get().playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            playerRegionMap.remove(p);
        }
    }
    
}
