package store.j3studios.plugin.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.google.common.base.Strings;
import java.io.IOException;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import store.j3studios.plugin.SCore;

public class Tools {
    
    private static Tools ins;
    
    public static Tools get() {
        if (ins == null) {
            ins = new Tools();
        }
        return ins;
    }
    
    public String Text(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    } 
    
    /*
    
    */
	
    public String formatMoney (Integer balance) {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###.##");
        return format.format(balance);
    }
	
    public String formatMoney (Double balance) {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###.##");
        return format.format(balance);
    }
    
    public Double calcPercent (Double value, Double percent) {
        double impuestos = -(100-percent);
    	double calculo = (value * (100 + (impuestos)) / 100);
    	return calculo;
    }
	
    public String findDifference(String start_date, String end_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            String difference;
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);
            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = difference_In_Time / 1000L % 60L;
            long difference_In_Minutes = difference_In_Time / 60000L % 60L;
            long difference_In_Hours = difference_In_Time / 3600000L % 24L;
            long difference_In_Days = difference_In_Time / 86400000L % 365L;
	      
            if (difference_In_Days == 0L) {
                if (difference_In_Minutes == 0L) {
                    difference = difference_In_Seconds + " : sec";
                } else if (difference_In_Hours == 0L) {
                    difference = difference_In_Minutes + " : min";
                } else {
                    difference = difference_In_Hours + " : hours";
                } 
            } else {
                difference = difference_In_Days + " : days";
            } 
            return difference;
        } catch (ParseException e) {
            return null;
        } 
    }
	
    public boolean dateValidator(String format) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(format);
            return true;
        } catch (ParseException e) {
        }
        return false;
    }
	
    public String getFormatTime (Integer timer) {
        int hours = timer / 3600;
        int secondsLeft = timer - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        
        if(hours >= 1) {
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";
        }
        if(minutes >= 1) {
        	if (minutes >= 10) {
                formattedTime += minutes + ":";
        	} else {
        		formattedTime += "0";
        		formattedTime += minutes + ":";
        	}
            if (seconds < 10)
            	formattedTime += "0";
            formattedTime += seconds;
        } else {
        	formattedTime += seconds + "s" ;
        }
        return formattedTime;
    }

    public String formatLocation (Location loc) {
        String format = loc.getWorld().getName() + " - X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ();
        return format;
    }
    
    /*
    MESSAGES TOOLS
    */
    
    public String compileWords(String[] args, int index) {
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString().trim();
    }
	
    public void sendClickeableMessage (Player p, String message, String event) {
    	ComponentBuilder mensaje = new ComponentBuilder (message);
    	BaseComponent[] msg = mensaje.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, event)).create();    	
    	p.spigot().sendMessage(msg);
    }
	
    public void clearChat (Player p, Integer lines) {
        for (int i = 0; i < lines; ++i) {
            p.sendMessage(" ");
        }
    }
	
    public void sendTitle(Player player, String msgTitle, String msgSubTitle, int fadein, int ticks, int fadeout) {
        String title = msgTitle; String subTitle = msgSubTitle;
        /*if (KB.get().papi) {
            title = PlaceholderAPI.setPlaceholders(player, title);
            subTitle = PlaceholderAPI.setPlaceholders(player, subTitle);
        }*/
        player.sendTitle(Text(title), Text(subTitle), fadein, ticks, fadeout);
    }

    public void sendActionBar(Player player, String message) {
    	String text = message;
    	/*if (KB.get().papi) {
            text = PlaceholderAPI.setPlaceholders(player, text);    	
    	}*/
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Text(text)));
    }
    
    public void playSound(Player player, Sound sound, Float volumen, Float pitch) {
        player.playSound(player.getLocation(), sound, volumen, pitch);
    }
    
    public void playSound(Location loc, Sound sound, Float volumen, Float pitch) {
        loc.getWorld().playSound(loc, sound, volumen, pitch);
    }
	
    
    public void playParticle (Particle particle, Location loc, Integer cantidad, Double offSetX, Double offSetY, Double offSetZ) {
        loc.getWorld().spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), cantidad, offSetX, offSetY, offSetZ, 0.01d, null, false);
    }
	
    public void playParticle (Particle particle, Player p, Integer cantidad, Double offSetX, Double offSetY, Double offSetZ) {
        p.getLocation().getWorld().spawnParticle(particle, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), cantidad, offSetX, offSetY, offSetZ, 0.01d, null, false);
    }
	
    public void playPathParticle (Particle particle, Location loc, Integer cantidad, Double offSetX, Double offSetY, Double offSetZ) {
        loc.getWorld().spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), cantidad, offSetX, offSetY, offSetZ, 0.01d, null, false);
    }
	
    /*
    / LOCATION TOOLS
    */
    
    public void teleportTo (Player p, Location loc) {
        if (loc != null) {
            p.teleport(loc.add(0.5, 0.0, 0.5));
        } else {
            p.sendMessage(Text("&cThat location does exists or not configure..."));
        }
    }
    
    public String setLocToString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    public String setLocToString(Player p) {
        Location loc = p.getLocation();
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," +  loc.getYaw() + "," + loc.getPitch();
    }
	
    public Location setStringToLoc(String path, Boolean isBlock) {
        Location loc = null;
        if (path == null) {
        	return loc;
        }
        String[] locs = path.split(",");        
        if (isBlock) {
            loc = new Location(Bukkit.getWorld(locs[0]), (double)Integer.parseInt(locs[1]), (double)Integer.parseInt(locs[2]), (double)Integer.parseInt(locs[3]));
        } else {
            loc = new Location(Bukkit.getWorld(locs[0]), (double)Integer.parseInt(locs[1]), (double)Integer.parseInt(locs[2]), (double)Integer.parseInt(locs[3]), (float)Float.valueOf(locs[4]), (float)Float.valueOf(locs[5]));
        }
        return loc;
    }
    
    //
    
    public void clearPlayer (Player p, GameMode gameMode) {
    	SCore.get().getServer().getScheduler().runTask(SCore.get(), () -> {
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setExp(0.0f);
            p.setTotalExperience(0);
            p.setLevel(0);
            p.setFireTicks(0);
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setGameMode(gameMode);
            for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                p.removePotionEffect(potionEffect.getType());
            }
            if (p.getOpenInventory() != null){
                p.closeInventory();
            }
        });
    }
    
	//
	// SAVE A CONFIG / YML FILE
	//
	
    public void save(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
        }
    }
    
    //
    // CHECK IF ARGUMENT IS A INT.
    //
    
    public boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
    
    public boolean isDouble(String s) {
	    try {
	        Double.parseDouble(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
    
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }
      
    // PROGRESS BAR
	
    public String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        return Strings.repeat("" + completedColor + symbol, progressBars) + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
	
    public String getProgressBar(Double current, Double max, Double totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        Double percent = current / max;
       int progressBars = (int) (totalBars * percent);
       return Strings.repeat("" + completedColor + symbol, progressBars) + Strings.repeat("" + notCompletedColor + symbol, (int) (totalBars - progressBars));
    }
     
    // CENTERED MESSAGE
 	
 
    private final static int CENTER_PX = 154;
    public void sendCenteredMessage(Player player, String message){
 		if(message == null || message.equals(""))
 			player.sendMessage("");	
 			
 		message = ChatColor.translateAlternateColorCodes('&', message);
 		
 		int messagePxSize = 0;
 		boolean previousCode = false;
 		boolean isBold = false;
 	 
 		for(char c : message.toCharArray()){
 			if(c == '§'){
 				previousCode = true;
 			}else if(previousCode == true){
 				previousCode = false;
                                isBold = c == 'l' || c == 'L';
 			}else{
 				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
 				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
 				messagePxSize++;
 			}
 		}
 		
 		int halvedMessageSize = messagePxSize / 2;
 		int toCompensate = CENTER_PX - halvedMessageSize;
 		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
 		int compensated = 0;
 		StringBuilder sb = new StringBuilder();
 		while(compensated < toCompensate){
 			sb.append(" ");
 			compensated += spaceLength;
 		}	
 		player.sendMessage(sb.toString() + message);
 	}
 	     
    
}
