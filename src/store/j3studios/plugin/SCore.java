package store.j3studios.plugin;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import store.j3studios.plugin.commands.essentials.FeedCMD;
import store.j3studios.plugin.commands.essentials.GamemodeCMD;
import store.j3studios.plugin.commands.essentials.HealCMD;
import store.j3studios.plugin.commands.essentials.MessageCMD;
import store.j3studios.plugin.commands.essentials.OnlineCMD;
import store.j3studios.plugin.commands.testcmds;
import store.j3studios.plugin.database.PlayerSQL;
import store.j3studios.plugin.database.ProtectionsSQL;
import store.j3studios.plugin.database.SQL;
import store.j3studios.plugin.listeners.PlayerListener;
import store.j3studios.plugin.managers.ScoreboardManager;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.protections.ProtectionListener;
import store.j3studios.plugin.protections.ProtectionManager;
import store.j3studios.plugin.tasks.ProtectionsTask;
import store.j3studios.plugin.utils.Config;
import store.j3studios.plugin.utils.Tools;
import store.j3studios.plugin.utils.socket.SocketServer;

public class SCore extends JavaPlugin {
    
    private static SCore ins;
    
    public static Config lang,board;
    private SocketServer server;
     
    @Override
    public void onEnable() {
        ins = this;
        
        // LOAD CONFIGURATION
        this.getConfig();
        this.saveDefaultConfig();
        
        lang = new Config(this, "lang");
        board = new Config(this, "scoreboard");
        
        this.registerEvent(new PlayerListener());
        this.registerEvent(new ProtectionListener());
        
        this.registerCommand("testprote", new testcmds());
        
        // ESSENTIALS COMMANDS
        this.registerCommand("gamemode", new GamemodeCMD());
        this.registerCommand("message", new MessageCMD());
        this.registerEvent(new MessageCMD());
        this.registerCommand("list", new OnlineCMD());
        this.registerCommand("heal", new HealCMD());
        this.registerCommand("feed", new FeedCMD());
        
        SQL.get().openConnection();
        //this.socketStart(7777);
         
        // LOAD MANAGEr
        ProtectionManager.get().loadProtections();
        new ProtectionsTask(this).loadHours();
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!PlayerManager.get().doesPlayerExists(p.getUniqueId())) {
                PlayerManager.get().createPlayer(p);
            }
            PlayerSQL.get().createPlayer(p);
            PlayerSQL.get().loadProtectionOwnerUser(p.getUniqueId().toString(), PlayerSQL.get().getUserID(p.getUniqueId().toString()));
            PlayerSQL.get().loadProtectionMemberUser(p.getUniqueId().toString(), PlayerSQL.get().getUserID(p.getUniqueId().toString()));
            ScoreboardManager.get().createScoreboard(p);
        }
        
        this.sendEnableMessage();
    }
    
    @Override
    public void onDisable() {
        if (server != null) {
            server.closeServerSocket();
        }
    }
    
    /*
    */
    
    public static SCore get() {
        return ins;
    }
    
    /*
    */
    
    private void socketStart(Integer port) {
        try {
            this.startServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(SCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void startServerSocket(Integer port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        server = new SocketServer(serverSocket);
        server.start();        
        debug("&aSocket Server started successfully!");
    }
    
    /*
    */
    
    public static void debug (String string) {
        Bukkit.getConsoleSender().sendMessage(Tools.get().Text("&7[&6J3SurvivalCore&7] " + string));
    }
    
    public void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
    
    public void registerCommand(String cmd, CommandExecutor executor) {
        this.getCommand(cmd).setExecutor(executor);
    }
    
    public void sendEnableMessage() {
        debug(" ");
        debug("&aJ3SurvivalCore enabled!");
        debug(" ");
        debug("&fDevelopment by: &eJ3Studios");
        debug("&fProject Version: &ev" + this.getDescription().getVersion());
        debug("&fAuthor: &eJosn3r");
        debug(" ");
    }
    
}
