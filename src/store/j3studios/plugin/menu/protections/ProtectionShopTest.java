package store.j3studios.plugin.menu.protections;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import store.j3studios.plugin.menu.Menu;
import store.j3studios.plugin.menu.MenuIA;
import store.j3studios.plugin.utils.ItemBuilder;
import store.j3studios.plugin.utils.Tools;

public class ProtectionShopTest extends MenuIA {

    Integer size = 10;
    Boolean goldType = false;
    Double costPerSize = 2350.00;
    
    public ProtectionShopTest (Player p) {
        super("&3&lProtections", 6, "survivalrp:blank_menu");
        
        CustomStack sumar1 = CustomStack.getInstance("survivalrp:icon_mas_uno");
        CustomStack sumar10 = CustomStack.getInstance("survivalrp:icon_mas_diez");
        CustomStack restar1 = CustomStack.getInstance("survivalrp:icon_menos_uno");
        CustomStack restar10 = CustomStack.getInstance("survivalrp:icon_menos_diez");
        
        ItemStack itemSumar1 = sumar1.getItemStack();
        ItemStack itemSumar10 = sumar10.getItemStack();
        ItemStack itemRestar1 = restar1.getItemStack();
        ItemStack itemRestar10 = restar10.getItemStack();
        
        Double cost = (costPerSize * size);
        Double tax = (cost/10);
            
        this.set(11, ItemBuilder.crearItem(itemRestar1, 1, "&c-10", ""));
        this.set(12, ItemBuilder.crearItem(itemRestar10, 1, "&c-1", ""));
        this.set(13, ItemBuilder.crearItem(Material.IRON_ORE, 1, "&6Proteccion &fx" + size, 
                            "&7Bloque de protección.",
                            " ",
                            "&fTamaño de Protección: &e" + size + "x" + size,
                            "&fPrecio: &6&l$&e" + Tools.get().formatMoney(cost),
                            "&fCosto de Impuestos: &6&l$&e" + Tools.get().formatMoney(tax),
                            " "));
        this.set(14, ItemBuilder.crearItem(itemSumar1, 1, "&a+1", ""));
        this.set(15, ItemBuilder.crearItem(itemSumar10, 1, "&a+10", ""));
    }
    
    @Override
    public void onClose(Player p) {
    
    }

    @Override
    public void onClick(Player p, InventoryClickEvent e) {
        String name = Tools.get().Text(e.getCurrentItem().getItemMeta().getDisplayName());
        
        if (name.equalsIgnoreCase(Tools.get().Text("&c-1"))) {
            if (size == 128){
                size = 64;
            } else {
                size-=1;
            }
            if (size < 10)
                size = 10;
            if (size <= 64)
                goldType = false;
        }
        if (name.equalsIgnoreCase(Tools.get().Text("&c-10"))) {
            if (size == 128){
                size = 64;
            } else {
                size-=10;
            }
            if (size < 10)
                size = 10;
            if (size <= 64)
                goldType = false;
        }
        if (name.equalsIgnoreCase(Tools.get().Text("&a+1"))) {
            ++size;
            if (size > 64)
                size = 128;
            if (size > 64)
                goldType = true;
        }
        if (name.equalsIgnoreCase(Tools.get().Text("&a+10"))) {
            size+=10;
            if (size > 64)
                size = 128;
            if (size > 64)
                goldType = true;
        }
        
        Double cost = (costPerSize * size);
        Double tax = (cost/10);
        if (goldType) {
            this.set(13, ItemBuilder.crearItem(Material.GOLD_ORE, 1, "&6Proteccion &fx" + size, 
                            "&7Bloque de protección.",
                            " ",
                            "&fTamaño de Protección: &e" + size + "x" + size,
                            "&fPrecio: &6&l10g",
                            "&fCosto de Impuestos: &6&l$&eExento",
                            " "));
        } else {
            this.set(13, ItemBuilder.crearItem(Material.IRON_ORE, 1, "&6Proteccion &fx" + size, 
                            "&7Bloque de protección.",
                            " ",
                            "&fTamaño de Protección: &e" + size + "x" + size,
                            "&fPrecio: &6&l$&e" + Tools.get().formatMoney(cost),
                            "&fCosto de Impuestos: &6&l$&e" + Tools.get().formatMoney(tax),
                            " "));
        }
        
    }
    
}
