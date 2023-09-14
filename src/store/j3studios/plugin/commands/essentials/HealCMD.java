package store.j3studios.plugin.commands.essentials;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.utils.Tools;

public class HealCMD implements TabExecutor {

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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (command.getName().equalsIgnoreCase("heal")) {
            if (!(sender instanceof Player)) {
                if (args.length == 0) {
                    SCore.debug("&7Correct Usage: &e/heal [player]");
                    return true;
                }
                
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    SCore.debug("&cPlayer &e" + playerName + " &cnot is online!");
                    return true;
                }
                
                SCore.debug("&fHa regenerado la vida del jugador &e" + playerName + "&f.");
                target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                target.sendMessage(new Tools().Text("&fTu vida ha sido regenerada por el servidor."));
                return true;
            } else {
                Player player = (Player)sender;

                if (args.length == 0) {
                    if (!player.hasPermission("j3survivalcore.command.heal")) {
                        player.sendMessage(new Tools().Text("&cNo tienes permiso para usar ese comando."));
                        return true;
                    }
                    
                    player.sendMessage(new Tools().Text("&eTu vida ha sido regenerada!"));
                    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                    return true;
                } else {
                    if (!player.hasPermission("j3survivalcore.command.heal.others")) {
                        player.sendMessage(new Tools().Text("&cNo tienes permiso para usar ese comando."));
                        return true;
                    }
                    
                    String playerName = args[0];
                    Player target = Bukkit.getPlayer(playerName);
                    if (target == null) {
                        player.sendMessage(new Tools().Text("&cPlayer &e" + playerName + " &cnot is online!"));
                        return true;
                    }
                    
                    player.sendMessage(new Tools().Text("&fHa regenerado la vida del jugador &e" + playerName + "&f."));
                    target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                    target.sendMessage(new Tools().Text("&fTu vida ha sido regenerada por &e" + player.getName() + "&f."));
                    return true;
                }                
            }
        }
        
        return true;
    }
    
}
