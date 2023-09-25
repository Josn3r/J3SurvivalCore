package store.j3studios.plugin.commands.essentials.economy;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.managers.EconomyManager;
import store.j3studios.plugin.utils.Tools;

public class BalanceCMD implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (cmd.getName().equalsIgnoreCase("balance")) {            
            if (!(sender instanceof Player)) {
                if (args.length == 0) {
                    SCore.debug("&7Correct Usage: &e/balance [player]");
                    return true;
                }
                
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    SCore.debug("&cPlayer &e" + playerName + " &cnot is online!");
                    return true;
                }
                         
                SCore.debug("&7Silver's Balance de &f" + playerName + "&7: &7" + Tools.get().formatMoney(SCore.get().getEM().getBalance(target, EconomyManager.WALLET_TYPE.SILVERS)));
                SCore.debug("&6Gold's Balance de &f" + playerName + "&6: &7" + Tools.get().formatMoney(SCore.get().getEM().getBalance(target, EconomyManager.WALLET_TYPE.GOLDS)));
                return true;
            } else {
                Player player = (Player)sender;
                if (args.length == 0) {
                    player.sendMessage(Tools.get().Text("&7Silver's Balance: &f" + Tools.get().formatMoney(SCore.get().getEM().getBalance(player, EconomyManager.WALLET_TYPE.SILVERS))));
                    player.sendMessage(Tools.get().Text("&6Gold's Balance: &e" + Tools.get().formatMoney(SCore.get().getEM().getBalance(player, EconomyManager.WALLET_TYPE.GOLDS))));
                    return true;
                }
                
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    player.sendMessage(new Tools().Text("&cPlayer &e" + playerName + " &cnot is online!"));
                    return true;
                }

                player.sendMessage(Tools.get().Text("&7Silver's Balance de &f" + playerName + "&7: &f" + Tools.get().formatMoney(SCore.get().getEM().getBalance(player, EconomyManager.WALLET_TYPE.SILVERS))));
                player.sendMessage(Tools.get().Text("&6Gold's Balance de &f" + playerName + "&6: &e" + Tools.get().formatMoney(SCore.get().getEM().getBalance(player, EconomyManager.WALLET_TYPE.GOLDS))));
                return true;
            }
        }
        
        return true;
    }
    
}
