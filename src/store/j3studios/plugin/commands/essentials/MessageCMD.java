package store.j3studios.plugin.commands.essentials;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;
import store.j3studios.plugin.utils.Tools;

public class MessageCMD implements TabExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        /*
        
        COMMANDS:
        /msg [player] [message]
        /togglemsg
        
        */
        
        if (command.getName().equalsIgnoreCase("message")) {            
            if (!(sender instanceof Player)) {
                if (args.length == 0) {
                    SCore.debug("&7Correct Usage: &e/message [player] [message]");
                    return true;
                }
                
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    SCore.debug("&cPlayer &e" + playerName + " &cnot is online!");
                    return true;
                }
                
                String message = new Tools().compileWords(args, 1);
                
                
                SCore.debug("&6[MSG] &eYou -> " + playerName + ": &f" + message);
                target.sendMessage(new Tools().Text("&6[MSG] &eServer -> You: &f" + message));
                new Tools().playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                
                return true;
            } else {
                Player player = (Player)sender;
                if (args.length == 0) {
                    player.sendMessage(new Tools().Text("&7Correct Usage: &e/message [player] [message]"));
                    return true;
                }
                
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    player.sendMessage(new Tools().Text("&cPlayer &e" + playerName + " &cnot is online!"));
                    return true;
                }
                SPlayer st = PlayerManager.get().getPlayer(target.getUniqueId());
                
                if (target == player) {
                    player.sendMessage(new Tools().Text("&cYou can't send a message to you!"));
                    return true;
                }
                
                if (!st.isEnableMSG()) {
                    if (!player.hasPermission("j3survivalcore.msg.bypass")) {
                        player.sendMessage(new Tools().Text("&cEl jugador &f" + playerName + " &ctienes bloqueado los mensajes privados."));
                        return true;
                    }
                }
                
                String message = new Tools().compileWords(args, 1);
                
                player.sendMessage(new Tools().Text("&6[MSG] &eYou -> " + playerName + ": &f" + message));
                new Tools().playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                target.sendMessage(new Tools().Text("&6[MSG] &e" + player.getName() + " -> You: &f" + message));
                new Tools().playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                return true;
            }
        }
        
        /*
        COMMAND: /togglemsg
        */
        
        if (command.getName().equalsIgnoreCase("togglemsg")) {
            if (!(sender instanceof Player)) {
                SCore.debug("&cEste comando solo está disponible para jugadores.");
                return true;
            }
            
            Player p = (Player)sender;
            SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
            
            if (!p.hasPermission("j3survivalcore.msg.toggle")) {
                p.sendMessage(Tools.get().Text("&cNo tienes permiso para bloquear los mensajes privados."));
                return true;
            }
            
            if (sp.isEnableMSG()) {
                sp.setEnableMSG(false);
                p.sendMessage(Tools.get().Text("&6[MSG] &eAhora no recibirás mensajes privados."));
            } else {
                sp.setEnableMSG(true);
                p.sendMessage(Tools.get().Text("&6[MSG] &eAhora no recibirás mensajes privados."));
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
    
    @EventHandler
    public void onPlayerCommandPreProcess (PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().contains("/msg") || event.getMessage().toLowerCase().contains("/tell")) {
            Player player = event.getPlayer();
            
            player.sendMessage(new Tools().Text("&c&lINFO: &fEse comando fué reemplazado a: &e/message &fo &e/dm"));
            event.setCancelled(true);
        }
    }
    
}
