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

public class EconomyCMD implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        // ARGS: / 0      1      2       3        X
        // CMDS: /eco [option] [type] [player] [value]
        
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("set");
            suggestions.add("add");
            suggestions.add("take");
            suggestions.add("reset");
            return suggestions;
        }
        
        if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("SILVERS");
            suggestions.add("GOLDS");
            return suggestions;
        }
        
        if (args.length == 3) {
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
        
        if (cmd.getName().equalsIgnoreCase("economy")) {
            if (!(sender instanceof Player)) {
                if (args.length == 0) {
                    SCore.debug("&6Economy Correct Usages:");
                    SCore.debug("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]");
                    SCore.debug("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]");
                    SCore.debug("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]");
                    SCore.debug("&fReset Player Balance: &e/eco reset [silvers/golds] [player]");
                    return true;
                }
                
                if (!args[0].equalsIgnoreCase("set") && !args[0].equalsIgnoreCase("add") && args[0].equalsIgnoreCase("take") && args[0].equalsIgnoreCase("reset")) {
                    SCore.debug("&6Economy Correct Usages:");
                    SCore.debug("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]");
                    SCore.debug("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]");
                    SCore.debug("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]");
                    SCore.debug("&fReset Player Balance: &e/eco reset [silvers/golds] [player]");
                    return true;
                }
                
                if (!args[1].equalsIgnoreCase("silvers") && !args[1].equalsIgnoreCase("golds")) {
                    SCore.debug("&6Economy Correct Usages:");
                    SCore.debug("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]");
                    SCore.debug("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]");
                    SCore.debug("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]");
                    SCore.debug("&fReset Player Balance: &e/eco reset [silvers/golds] [player]");
                    return true;
                }
                String walletType = args[1].toUpperCase();                
                String strWallet = (walletType.equalsIgnoreCase("SILVERS") ? "Silver's" : "Gold's");
                
                String playerName = args[2];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    SCore.debug("&cPlayer &e" + playerName + " &cnot is online!");
                    return true;
                }
                
                if (args[0].equalsIgnoreCase("reset")) {
                    SCore.get().getEM().setBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), 0);
                    
                    SCore.debug("&cHas reiniciado el balance &f(" + strWallet + "&f) &cdel jugador &f" + playerName + "&c.");
                    target.sendMessage(Tools.get().Text("&cEl servidor ha reiniciado tu " + strWallet + " balance."));
                    Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);                
                    return true;
                } else {
                    if (args.length <= 3) {
                        SCore.debug("&6Economy Correct Usages:");
                        SCore.debug("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]");
                        SCore.debug("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]");
                        SCore.debug("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]");
                        return true;
                    }
                    
                    if (!Tools.get().isInt(args[3])) {
                        SCore.debug("&6Economy Correct Usages:");
                        SCore.debug("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]");
                        SCore.debug("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]");
                        SCore.debug("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]");
                        return true;
                    }
                    Integer amount = Integer.valueOf(args[3]);
                    if (amount < 0) {
                        amount = 0;
                    }
                    
                    if (args[0].equalsIgnoreCase("set")) {
                        SCore.get().getEM().setBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), amount);
                        
                        SCore.debug("&eHas colocado el balance &f(" + strWallet + "&f) &edel jugador &f" + playerName + " &een &f" + Tools.get().formatMoney(amount) + "&e.");
                        target.sendMessage(Tools.get().Text("&eEl servidor ha modificado tu &f" + strWallet + " balance &ea &f" + Tools.get().formatMoney(amount) + "&e."));
                        Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f); 
                        
                    } else if (args[0].equalsIgnoreCase("add")) {
                        SCore.get().getEM().addBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), amount);
                        Integer tBalance = SCore.get().getEM().getBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType));
                        
                        SCore.debug("&eHas sumado el balance &f(" + strWallet + "&f) &edel jugador &f" + playerName + " &een &f" + Tools.get().formatMoney(amount) + "&e. Total en balance: " + Tools.get().formatMoney(tBalance) + "&e.");
                        target.sendMessage(Tools.get().Text("&eEl servidor a agregado &f" + Tools.get().formatMoney(amount) + " a tu &f" + strWallet + " balance&e. Total en " + strWallet + " balance: &f" + Tools.get().formatMoney(tBalance) + "&e."));
                        Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f); 
                        
                    } else if (args[0].equalsIgnoreCase("take")) {
                        SCore.get().getEM().takeBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), amount);
                        Integer tBalance = SCore.get().getEM().getBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType));
                        if (tBalance < 0) {
                            SCore.get().getEM().setBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), 0);
                        }
                        
                        SCore.debug("&eHas restado el balance &f(" + strWallet + "&f) &edel jugador &f" + playerName + " &een &f" + Tools.get().formatMoney(amount) + "&e, Total en balance: " + Tools.get().formatMoney(tBalance) + "&e.");
                        target.sendMessage(Tools.get().Text("&eEl servidor a restado &f" + Tools.get().formatMoney(amount) + " a tu &f" + strWallet + " balance&e. Total en " + strWallet + " balance: &f" + Tools.get().formatMoney(tBalance) + "&e."));
                        Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    } 
                }                
            } else {
                Player p = (Player)sender;
                
                if (!p.hasPermission("j3survivalcore.command.economy")) {
                    p.sendMessage(Tools.get().Text("&cNo tienes permisos para ejecutar ese comando."));
                    return true;
                }
                
                if (args.length == 0) {
                    p.sendMessage(Tools.get().Text("&6Economy Correct Usages:"));
                    p.sendMessage(Tools.get().Text("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fReset Player Balance: &e/eco reset [silvers/golds] [player]"));
                    return true;
                }
                
                if (!args[0].equalsIgnoreCase("set") && !args[0].equalsIgnoreCase("add") && args[0].equalsIgnoreCase("take") && args[0].equalsIgnoreCase("reset")) {
                    p.sendMessage(Tools.get().Text("&6Economy Correct Usages:"));
                    p.sendMessage(Tools.get().Text("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fReset Player Balance: &e/eco reset [silvers/golds] [player]"));
                    return true;
                }
                
                if (!args[1].equalsIgnoreCase("silvers") && !args[1].equalsIgnoreCase("golds")) {
                    p.sendMessage(Tools.get().Text("&6Economy Correct Usages:"));
                    p.sendMessage(Tools.get().Text("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]"));
                    p.sendMessage(Tools.get().Text("&fReset Player Balance: &e/eco reset [silvers/golds] [player]"));
                    return true;
                }
                String walletType = args[1].toUpperCase();                
                String strWallet = (walletType.equalsIgnoreCase("SILVERS") ? "Silver's" : "Gold's");
                
                String playerName = args[2];
                Player target = Bukkit.getPlayer(playerName);
                if (target == null) {
                    p.sendMessage(Tools.get().Text("&cPlayer &e" + playerName + " &cnot is online!"));
                    return true;
                }
                
                if (args[0].equalsIgnoreCase("reset")) {
                    SCore.get().getEM().setBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), 0);
                    
                    p.sendMessage(Tools.get().Text("&cHas reiniciado el balance &f(" + strWallet + "&f) &cdel jugador &f" + playerName + "&c."));
                    target.sendMessage(Tools.get().Text("&cEl servidor ha reiniciado tu " + strWallet + " balance."));
                    Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);                
                    return true;
                } else {
                    if (args.length <= 3) {
                        p.sendMessage(Tools.get().Text("&6Economy Correct Usages:"));
                        p.sendMessage(Tools.get().Text("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]"));
                        p.sendMessage(Tools.get().Text("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]"));
                        p.sendMessage(Tools.get().Text("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]"));
                        return true;
                    }
                    
                    if (!Tools.get().isInt(args[3])) {
                        p.sendMessage(Tools.get().Text("&6Economy Correct Usages:"));
                        p.sendMessage(Tools.get().Text("&fSet Player Balance: &e/eco set [silvers/golds] [player] [value]"));
                        p.sendMessage(Tools.get().Text("&fAdd Player Balance: &e/eco add [silvers/golds] [player] [value]"));
                        p.sendMessage(Tools.get().Text("&fTake Player Balance: &e/eco take [silvers/golds] [player] [value]"));
                        return true;
                    }
                    Integer amount = Integer.valueOf(args[3]);
                    if (amount < 0) {
                        amount = 0;
                    }
                    
                    if (args[0].equalsIgnoreCase("set")) {
                        SCore.get().getEM().setBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), amount);
                        
                        p.sendMessage(Tools.get().Text("&eHas colocado el balance &f(" + strWallet + "&f) &edel jugador &f" + playerName + " &een &f" + Tools.get().formatMoney(amount) + "&e."));
                        target.sendMessage(Tools.get().Text("&eEl servidor ha modificado tu &f" + strWallet + " balance &ea &f" + Tools.get().formatMoney(amount) + "&e."));
                        Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f); 
                        
                    } else if (args[0].equalsIgnoreCase("add")) {
                        SCore.get().getEM().addBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), amount);
                        Integer tBalance = SCore.get().getEM().getBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType));
                        
                        p.sendMessage(Tools.get().Text("&eHas sumado el balance &f(" + strWallet + "&f) &edel jugador &f" + playerName + " &een &f" + Tools.get().formatMoney(amount) + "&e. Total en balance: " + Tools.get().formatMoney(tBalance) + "&e."));
                        target.sendMessage(Tools.get().Text("&eEl servidor a agregado &f" + Tools.get().formatMoney(amount) + " a tu &f" + strWallet + " balance&e. Total en " + strWallet + " balance: &f" + Tools.get().formatMoney(tBalance) + "&e."));
                        Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f); 
                        
                    } else if (args[0].equalsIgnoreCase("take")) {
                        SCore.get().getEM().takeBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), amount);
                        Integer tBalance = SCore.get().getEM().getBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType));
                        if (tBalance < 0) {
                            SCore.get().getEM().setBalance(target, EconomyManager.WALLET_TYPE.valueOf(walletType), 0);
                        }
                        
                        p.sendMessage(Tools.get().Text("&eHas restado el balance &f(" + strWallet + "&f) &edel jugador &f" + playerName + " &een &f" + Tools.get().formatMoney(amount) + "&e, Total en balance: " + Tools.get().formatMoney(tBalance) + "&e."));
                        target.sendMessage(Tools.get().Text("&eEl servidor a restado &f" + Tools.get().formatMoney(amount) + " a tu &f" + strWallet + " balance&e. Total en " + strWallet + " balance: &f" + Tools.get().formatMoney(tBalance) + "&e."));
                        Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    } 
                }
            }
            return true;
        }
        
        return true;
    }
    
}
