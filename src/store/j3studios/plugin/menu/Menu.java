package store.j3studios.plugin.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.utils.Tools;

public abstract class Menu implements Listener {
    
    Inventory _inv;
    
    public Menu (String title, Integer rows) {
        _inv = Bukkit.createInventory(null, (9*rows), Tools.get().Text(title));
        SCore.get().getServer().getPluginManager().registerEvents(this, SCore.get());
    }
    
    public void add (ItemStack item) {
        this._inv.addItem(new ItemStack[]{item});
    }
    
    public void set (Integer slot, ItemStack item) {
        this._inv.setItem(slot, item);
    }
    
    public void clear() {
        this._inv.clear();
    }
    
    public void open (Player p) {
        p.openInventory(_inv);
    }
    
    @EventHandler
    public void onInventoryClick (InventoryClickEvent e) {
        if (e.getInventory().equals(_inv) && e.getCurrentItem() != null && _inv.contains(e.getCurrentItem()) && e.getWhoClicked() instanceof Player) {
            this.onClick((Player)e.getWhoClicked(), e);
            e.setCancelled(true);
            Player p = (Player)e.getWhoClicked();
            p.setItemOnCursor(null);
        }
    }
    
    @EventHandler
    public void onInventoryClose (InventoryCloseEvent e) {
        if (e.getInventory().equals(_inv) && e.getPlayer() instanceof Player) {
            this.onClose((Player)e.getPlayer());
        }
    }
    
    public abstract void onClose (Player p);
    
    public abstract void onClick (Player p, InventoryClickEvent e);
    
}
