package store.j3studios.plugin.commands.essentials;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.utils.Tools;

public class FlyCMD implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        /*
        Like usage:
        - /gamemode 0-3
        - /gamemode 0-3 [player]
        */
        if (command.getName().equalsIgnoreCase("gamemode")) {
            
            if (!(sender instanceof Player)) {
                if (args.length == 0) {
                    SCore.debug("&cYou can't set gamemode to console!");
                    SCore.debug("&cUsage: &f/fly [player]");
                    return true;
                }
                                
                String playerName = args[0];                
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    SCore.debug("&cERROR: &fPlayer &e" + playerName + " &fis offline!");
                    return true;
                }
                
                if (!target.getAllowFlight()) {
                    target.setAllowFlight(true);
                    target.setFlying(true);                
                    target.sendMessage(new Tools().Text("&6Your fly status has been update to &atrue&6."));
                    SCore.debug(new Tools().Text("&e" + playerName + " &ffly status has been update to &atrue&f."));
                } else {
                    target.setAllowFlight(false);
                    target.setFlying(false);                
                    target.sendMessage(new Tools().Text("&6Your fly status has been update to &cfalse&6."));
                    SCore.debug(new Tools().Text("&e" + playerName + " &ffly status has been update to &cfalse&f."));
                }
                return true;
            }
        
            Player player = (Player)sender;
            
            if (args.length == 0) {
                if (!hasPerm(player)) {
                    return true;
                }
                
                if (!player.getAllowFlight()) {
                    player.setAllowFlight(true);
                    player.setFlying(true);                
                    player.sendMessage(new Tools().Text("&6Your fly status has been update to &atrue&6."));
                } else {
                    player.setAllowFlight(false);
                    player.setFlying(false);                
                    player.sendMessage(new Tools().Text("&6Your fly status has been update to &cfalse&6."));
                }
            } else {
                if (!hasPermOthers(player)) {
                    return true;
                }

                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    player.sendMessage(new Tools().Text("&cERROR: &fPlayer &e" + playerName + " &fis offline!"));
                    return true;
                }

                if (!target.getAllowFlight()) {
                    target.setAllowFlight(true);
                    target.setFlying(true);                
                    target.sendMessage(new Tools().Text("&6Your fly status has been update to &atrue&6."));
                    player.sendMessage(new Tools().Text("&e" + playerName + " &ffly status has been update to &atrue&f."));
                } else {
                    target.setAllowFlight(false);
                    target.setFlying(false);                
                    target.sendMessage(new Tools().Text("&6Your fly status has been update to &cfalse&6."));
                    player.sendMessage(new Tools().Text("&e" + playerName + " &ffly status has been update to &cfalse&f."));
                } 
            }
            
            return true;
        }
         
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i = 0; i < players.length; i++) {
                playerNames.add(players[i].getName());
            }
            return playerNames;
        }
        return null;
    }
    
    private Boolean hasPerm (Player player) {
        if (player.hasPermission("j3survivalcore.fly") || player.hasPermission("*") || player.hasPermission("j3survivalcore.fly.*") || player.isOp())  {
            return true;
        }
        player.sendMessage(new Tools().Text("&cYou don't have permission for this command."));
        return false;
    }
    
    private Boolean hasPermOthers (Player player) {
        if (player.hasPermission("j3survivalcore.fly.others") || player.hasPermission("*") || player.hasPermission("j3survivalcore.fly.*") || player.isOp())  {
            return true;
        }
        player.sendMessage(new Tools().Text("&cYou don't have permission for this command."));
        return false;
    }
    
}
