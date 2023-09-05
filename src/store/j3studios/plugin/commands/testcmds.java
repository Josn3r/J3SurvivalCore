package store.j3studios.plugin.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import store.j3studios.plugin.menu.protections.ProtectionShop;
import store.j3studios.plugin.menu.protections.ProtectionShopTest;
import store.j3studios.plugin.utils.ItemBuilder;
import store.j3studios.plugin.utils.Tools;

public class testcmds implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player p = (Player)sender;
        
        if (cmd.getName().equalsIgnoreCase("testprote")) {
            //new ProtectionShop(p).open(p);
            new ProtectionShopTest(p).open(p);
            Tools.get().playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);                    
            return true;
        }        
        return true;
    }
    
}
