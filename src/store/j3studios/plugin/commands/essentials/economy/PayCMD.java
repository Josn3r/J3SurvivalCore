package store.j3studios.plugin.commands.essentials.economy;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.managers.EconomyManager;
import store.j3studios.plugin.utils.Tools;

public class PayCMD implements TabExecutor {

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

    // COMMAND: /pay [player] [silvers/golds] [amount]
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (cmd.getName().equalsIgnoreCase("pay")) {
            if (!(sender instanceof Player)) {
                SCore.debug("&cEste comando solo puede ser usado por jugadores.");
                return true;
            }
        
            Player p = (Player)sender;
            
            if (args.length <= 1) {
                p.sendMessage(Tools.get().Text("&7Correct Usage: &e/pay [player] [amount]"));
                return true;
            }
            
            String playerName = args[0];
            Player target = Bukkit.getPlayer(playerName);
            if (target == null) {
                p.sendMessage(new Tools().Text("&cPlayer &e" + playerName + " &cnot is online!"));
                return true;
            }
                        
            if (!Tools.get().isInt(args[1])) {
                p.sendMessage(Tools.get().Text("&7Correct Usage: &e/pay [player] &c[amount]"));
                return true;
            }
            Integer amount = Integer.valueOf(args[2]);
            
            Integer playerBalance = SCore.get().getEM().getBalance(p, EconomyManager.WALLET_TYPE.SILVERS);
            
            if (playerBalance >= amount) {
                SCore.get().getEM().addBalance(target, EconomyManager.WALLET_TYPE.SILVERS, amount);
                SCore.get().getEM().takeBalance(p, EconomyManager.WALLET_TYPE.SILVERS, amount);
                
                p.sendMessage(Tools.get().Text("&fHas enviado &e" + Tools.get().formatMoney(amount) + " &fSilver's al jugador &e" + playerName + "&f."));
                Tools.get().playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                target.sendMessage(Tools.get().Text("&fHas recibido &e" + Tools.get().formatMoney(amount) + " &fSilver's de parte del jugador &e" + p.getName() + "&f."));
                Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            } else {
                p.sendMessage(Tools.get().Text("&cNo tienes &fSilver's &csuficientes para esa transacción."));
                return true;
            }
            
            
            return true;
        }
        
        return true;
    }
    
}
