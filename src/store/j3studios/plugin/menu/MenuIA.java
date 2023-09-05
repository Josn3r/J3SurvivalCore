package store.j3studios.plugin.menu;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.utils.Tools;

public abstract class MenuIA implements Listener {
    
    TexturedInventoryWrapper _inv;
    
    public MenuIA (String title, Integer rows, String fontImage) {
        //_inv = Bukkit.createInventory(null, (9*rows), Tools.get().Text(title));
        _inv = new TexturedInventoryWrapper(null, (9*rows), Tools.get().Text(title), new FontImageWrapper(fontImage));
        SCore.get().getServer().getPluginManager().registerEvents(this, SCore.get());
    }
    
    public void add (ItemStack item) {
        this._inv.getInternal().addItem(new ItemStack[]{item});
    }
    
    public void set (Integer slot, ItemStack item) {
        this._inv.getInternal().setItem(slot, item);
    }
    
    public void clear() {
        this._inv.getInternal().clear();
    }
    
    public void open (Player p) {
        _inv.showInventory(p);
    }
    
    @EventHandler
    public void onInventoryClick (InventoryClickEvent e) {
        if (e.getInventory().equals(_inv.getInternal()) && e.getCurrentItem() != null && _inv.getInternal().contains(e.getCurrentItem()) && e.getWhoClicked() instanceof Player) {
            this.onClick((Player)e.getWhoClicked(), e);
            e.setCancelled(true);
            Player p = (Player)e.getWhoClicked();
            p.setItemOnCursor(null);
        }
    }
    
    @EventHandler
    public void onInventoryClose (InventoryCloseEvent e) {
        if (e.getInventory().equals(_inv.getInternal()) && e.getPlayer() instanceof Player) {
            this.onClose((Player)e.getPlayer());
        }
    }
    
    public abstract void onClose (Player p);
    
    public abstract void onClick (Player p, InventoryClickEvent e);
    
}
