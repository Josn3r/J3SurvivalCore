package store.j3studios.plugin.commands.essentials;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.utils.Tools;

public class OnlineCMD implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
        if (command.getName().equalsIgnoreCase("list")) {
            String onlinePlayers = "";
            for (Player players : Bukkit.getOnlinePlayers()) {
                onlinePlayers = new Tools().Text(onlinePlayers + "&7" + players.getName() + "&f, ");
            }
            onlinePlayers = onlinePlayers.substring(0, onlinePlayers.length() - 2);
            Integer totalPlayers = Bukkit.getOnlinePlayers().size();
            
            if (!(sender instanceof Player)) {            
                SCore.debug("&fHay &e" + totalPlayers + " &fjugadores conectados.");
                SCore.debug("&eJugadores: " + onlinePlayers + "&f.");
                return true;
            } else {
                Player player = (Player)sender;
                
                if (!player.hasPermission("j3survivalcore.command.list")) {
                    player.sendMessage(new Tools().Text("&cNo tienes permiso para usar ese comando."));
                    return true;
                }
                
                player.sendMessage(new Tools().Text("&fHay &e" + totalPlayers + " &fjugadores conectados."));
                player.sendMessage(new Tools().Text("&eJugadores: " + onlinePlayers + "&f."));
            }
        }       
        
        return true;
    }
    
}
