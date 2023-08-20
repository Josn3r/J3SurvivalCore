package store.j3studios.plugin;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import store.j3studios.plugin.listeners.PlayerListener;
import store.j3studios.plugin.utils.Tools;
import store.j3studios.plugin.utils.socket.SocketServer;

public class SCore extends JavaPlugin {
    
    private static SCore ins;
    
    private SocketServer server;
    
    @Override
    public void onEnable() {
        ins = this;
        
        this.registerEvent(new PlayerListener());
    }
    
    @Override
    public void onDisable() {
        
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
}
