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

public class GamemodeCMD implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        /*
        Like usage:
        - /gamemode 0-3
        - /gamemode 0-3 [player]
        */
        if (command.getName().equalsIgnoreCase("gamemode")) {
            
            if (!(sender instanceof Player)) {
                if (args.length <= 1) {
                    SCore.debug("&cYou can't set gamemode to console!");
                    SCore.debug("&cUsage: &f/gamemode 0-3 [player]");
                    return true;
                }
                
                if (!new Tools().isInt(args[0])) {
                    SCore.debug("&cUsage: &f/gamemode &c0-3 &f[player]");
                    return true;
                }
                
                Integer GMID = Integer.valueOf(args[0]);
                if (GMID > 3) {
                    SCore.debug("&cUsage: &f/gamemode &c0-3 &f[player]");
                    return true;
                }
                
                String playerName = args[1];
                
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    SCore.debug("&cERROR: &fPlayer &e" + playerName + " &fis offline!");
                    return true;
                }
                
                target.setGameMode(this.getGameMode(GMID));
                target.sendMessage(new Tools().Text("&6Your GameMode has been update to &a" + this.getGameMode(GMID).name()));
                SCore.debug(new Tools().Text("&e" + playerName + " &fGameMode has been update to &a" + this.getGameMode(GMID).name()));
                return true;
            }
        
            Player player = (Player)sender;
            
            if (args.length == 0) {
                if (!hasPerm(player)) {
                    return true;
                }
                player.sendMessage(new Tools().Text("&cUsage: &f/gamemode 0-3 &7[player]"));
                return true;
            }
            
            if (args.length > 1) {
                if (!hasPermOthers(player)) {
                    return true;
                }
                
                if (!new Tools().isInt(args[0])) {
                    player.sendMessage(new Tools().Text("&cUsage: &f/gamemode &c0-3 &f[player]"));
                    return true;
                }
                
                Integer GMID = Integer.valueOf(args[0]);
                if (GMID > 3) {
                    player.sendMessage(new Tools().Text("&cUsage: &f/gamemode &c0-3 &f[player]"));
                    return true;
                }

                String playerName = args[1];

                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    player.sendMessage(new Tools().Text("&cERROR: &fPlayer &e" + playerName + " &fis offline!"));
                    return true;
                }

                target.setGameMode(this.getGameMode(GMID));
                target.sendMessage(new Tools().Text("&6Your GameMode has been update to &a" + this.getGameMode(GMID).name()));

                player.sendMessage(new Tools().Text("&e" + playerName + " &fGameMode has been update to &a" + this.getGameMode(GMID).name()));
            
            } else {
                if (!hasPerm(player)) {
                    return true;
                }
                
                if (!new Tools().isInt(args[0])) {
                    player.sendMessage(new Tools().Text("&cUsage: &f/gamemode &c0-3"));
                    return true;
                }

                Integer GMID = Integer.valueOf(args[0]);
                if (GMID > 3) {
                    SCore.debug("&cUsage: &f/gamemode &c0-3");
                    return true;
                }

                player.setGameMode(this.getGameMode(GMID));
                player.sendMessage(new Tools().Text("&6Your GameMode has been update to &a" + this.getGameMode(GMID).name())); 
                
            }
            
            
                
                   
            return true;
        }
        
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("0");
            suggestions.add("1");
            suggestions.add("2");
            suggestions.add("3");
            return suggestions;
        }
        if (args.length == 2) {
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
    
    private GameMode getGameMode(Integer number) {
        switch(number) {
            case 0 -> {
                return GameMode.SURVIVAL;
            }
            case 1 -> {
                return GameMode.CREATIVE;
            }
            case 2 -> {
                return GameMode.ADVENTURE;
            }
            case 3 -> {
                return GameMode.SPECTATOR;
            }
        }
        return null;
    }
    
    private Boolean hasPerm (Player player) {
        if (player.hasPermission("j3survivalcore.gamemode") || player.hasPermission("*") || player.hasPermission("j3survivalcore.gamemode.*") || player.isOp())  {
            return true;
        }
        player.sendMessage(new Tools().Text("&cYou don't have permission for this command."));
        return false;
    }
    
    private Boolean hasPermOthers (Player player) {
        if (player.hasPermission("j3survivalcore.gamemode.others") || player.hasPermission("*") || player.hasPermission("j3survivalcore.gamemode.*") || player.isOp())  {
            return true;
        }
        player.sendMessage(new Tools().Text("&cYou don't have permission for this command."));
        return false;
    }

    
}
