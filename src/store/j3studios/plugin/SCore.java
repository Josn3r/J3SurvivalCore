package store.j3studios.plugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import store.j3studios.plugin.listeners.PlayerListener;

public class SCore extends JavaPlugin {
    
    private static SCore ins;
    
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
    
    public void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }
    
    public void registerCommand(String cmd, CommandExecutor executor) {
        this.getCommand(cmd).setExecutor(executor);
    }
}
