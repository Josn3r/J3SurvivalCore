package store.j3studios.plugin.managers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;

public class ScoreboardManager {
	
    private static ScoreboardManager sm;
    
    public static ScoreboardManager get() {
        if (sm == null) {
            sm = new ScoreboardManager();
        } 	
        return sm;
    }

    public void loadScoreboard() {
    	
    }
    
    public String Text(String s) {
        return ChatColor.translateAlternateColorCodes((char)'&', s);
    }

    public void createScoreboard(final Player p) {
    	SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
    	sp.createScoreboard(p);
    }
       
    public String vars(Player p, String s) {
    	SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
    	
    	String withVars = s
    			// NORMAL VARIABLES
    			.replaceAll("<ONLINE>", ""+Bukkit.getOnlinePlayers().size())
    			.replaceAll("<PING>", ""+p.getPing())
    			;    	
    	
    	withVars = PlaceholderAPI.setPlaceholders(p, withVars);
    	return withVars;
    }
    
    public String getName(Player p) {
        String title = SCore.board.getString("scoreboard.title");
        return title;
    }

    public List<String> getLines(Player p) {
    	List<String> str = SCore.board.getStringList("scoreboard.lines");
    	return str;
    }
    
}

