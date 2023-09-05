package store.j3studios.plugin.managers;

import dev.lone.itemsadder.api.CustomStack;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import store.j3studios.plugin.SCore;
import store.j3studios.plugin.utils.ItemBuilder;
import store.j3studios.plugin.utils.Tools;

public class MagicManager {
    
    private static MagicManager ins;
    
    public static MagicManager get() {
        if (ins == null)
            ins = new MagicManager();
        return ins;
    }
    
    public void orbitalStrike(Player p, Location loc){
        this.orbitalStrikePart1(p, loc);
    }   
    
    public void healingEffect (Player player, Double HEAL_RADIUS) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 2));
        Location center = player.getLocation().add(0.0, 1.0, 0.0);

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (otherPlayer != player && player.getLocation().distance(otherPlayer.getLocation()) <= HEAL_RADIUS) {
                //otherPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 1));
                if ((otherPlayer.getHealth()+0.2) < 20.0) {
                    Tools.get().playSound(otherPlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    otherPlayer.setHealth(otherPlayer.getHealth()+0.2);
                    Tools.get().sendActionBar(otherPlayer, Tools.get().Text("&6Estás siendo curado por &f" + player.getName()));
                } else {
                    otherPlayer.setHealth(20.0);
                }
            }
        }
        
        double radius = 10.0;                        
        for (double i = 0; i <= Math.PI; i += Math.PI / 15) {
            double y = radius * Math.cos(i) + 1.5;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 30) {
                double x = radius * Math.cos(a) * Math.sin(i);
                double z = radius * Math.sin(a) * Math.sin(i);
                center.add(x, y, z);
                center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center, 1);
                center.subtract(x, y, z);
            }
        }
    }
    
    public void ceguera(Player player){
        ArrayList<Player> reg = new ArrayList<>();
        
        new BukkitRunnable() {
            // Number of points to display, evenly spaced around the circle's radius
            int circlePoints = 3;
            // How fast should the particles rotate around the center beam
            int rotationSpeed = 20;
            double radius = 2.5;
            Location startLoc = player.getEyeLocation();
            World world = startLoc.getWorld();
            final Vector dir = player.getLocation().getDirection().normalize().multiply(1);
            final double pitch = (startLoc.getPitch() +90.0F) * 0.017453292F;
            final double yaw = -startLoc.getYaw() * 0.017453292F;
            // Particle offset increment for each loop
            double increment = (2 * Math.PI) / rotationSpeed;
            double circlePointOffset = 0; // This is used to rotate the circle as the beam progresses
            int beamLength = 30;
            double radiusShrinkage = radius / (double) ((beamLength + 2) / 2);
            @Override
            public void run() {
                beamLength--;
                if(beamLength < 1){
                    this.cancel();
                }
                for (int i = 0; i < circlePoints; i++) {
                    double x =  radius * Math.cos(2 * Math.PI * i / circlePoints + circlePointOffset);
                    double z =  radius * Math.sin(2 * Math.PI * i / circlePoints + circlePointOffset);

                    Vector vec = new Vector(x, 0, z);
                    rotateAroundAxisX(vec, pitch);
                    rotateAroundAxisY(vec, yaw);

                    startLoc.add(vec);
                    Tools.get().playParticle(Particle.SMOKE_NORMAL, startLoc, 10, 0.05d, 0.05d, 0.05);
                    startLoc.subtract(vec);
                }
                // Always spawn a center particle in the same direction the player was facing.
                startLoc.add(dir);
                Tools.get().playParticle(Particle.CLOUD, startLoc, 3, 0.25d, 0.25d, 0.25);
                for (Entity entity : world.getNearbyEntities(startLoc, 2.5, 2.5, 2.5)) {
                    if (entity instanceof Player) {
                        // Ignore player that initiated the shot
                        if (entity == player) {
                            continue;
                        }
                        // Found a target entity. Stop searching
                        Player target = (Player)entity; // Save/lock the target
                        if (!reg.contains(target)) {
                            reg.add(target);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3));
                            // BLOCK_NOTE_BLOCK_BASS
                            Tools.get().playSound(target, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        }
                        
                        
                        break;
                    }
                }                
                startLoc.subtract(dir);

                // Shrink each circle radius until it's just a point at the end of a long swirling cone
                radius -= radiusShrinkage;
                if (radius < 0) {
                    this.cancel();
                }

                // Rotate the circle points each iteration, like rifling in a barrel
                circlePointOffset += increment;
                if (circlePointOffset >= (2 * Math.PI)) {
                    circlePointOffset = 0;
                }
                startLoc.add(dir);
            }
        }.runTaskTimer(SCore.get(), 0, 3);
    }
    
    /*
    
    */
    
    public void orbitalStrikePart1 (Player player, Location loc){
        Tools.get().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5.0f, 5.0f);
        Tools.get().playParticle(Particle.EXPLOSION_HUGE, loc, 1, 0.5d, 0.5d, 0.5d);
        
        ArrayList<Player> damageRecibe = new ArrayList<>();
        new BukkitRunnable() {
            double radius = 1.5d;
            int beamLength = 10;
            @Override
            public void run() {
                beamLength--;
                if(beamLength < 1){
                    this.cancel();
                    return;
                }
                
                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    if (otherPlayer != player && !damageRecibe.contains(otherPlayer) && loc.distance(otherPlayer.getLocation()) <= 6) {
                        otherPlayer.damage(2.5d);
                        //otherPlayer.setVelocity(otherPlayer.getVelocity().add(player.getLocation().getDirection().multiply(1.5).setY(0.5)));
                        otherPlayer.setVelocity(loc.toVector().subtract(otherPlayer.getLocation().toVector()).normalize().multiply(-0.75).setY(0.25));
                        
                        Tools.get().playSound(otherPlayer, Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
                        Tools.get().sendActionBar(otherPlayer, Tools.get().Text("&6Recibiste daño por &f" + player.getName()));
                        damageRecibe.add(otherPlayer);
                    }
                }
                
                radius += 0.5d;                
                orbitalStrikePart2(loc, radius);
            }
        }.runTaskTimer(SCore.get(), 0, 1);
    }
    
    public void orbitalStrikePart2 (Location loc, Double ratio) {        
        int circlePoints = 20;
        double radius = ratio;        
        Location playerLoc = loc;
        World world = playerLoc.getWorld();
        double increment = (2 * Math.PI) / circlePoints;
        for (int i = 0; i < circlePoints; i++) {
            for (double y = 0.0; y < 1.0; y+=0.5) {
                double angle = i * increment;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector vec = new Vector(x, y, z);
                playerLoc.add(vec);
                world.spawnParticle(Particle.FLAME, playerLoc, 0);
                playerLoc.subtract(vec);
            }
        }     
        
    }
    
    public void slash(Player p) {
        if (taskSlash1 != 0) {
            return;
        }
        
        Vector direction = p.getLocation().getDirection().multiply(3).setY(0.5);
        Location inFront = p.getLocation().add(direction);
        ArmorStand stand = (ArmorStand)p.getWorld().spawnEntity(inFront, EntityType.ARMOR_STAND);
        stand.setInvulnerable(true);
        stand.setInvisible(true);
        stand.setGravity(false);
        
        taskSlash1 = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            Integer i = 1;
            @Override
            public void run() {
                CustomStack strike = CustomStack.getInstance("survivalrp:strike_slash_" + i);
                ItemStack item = strike.getItemStack();
                stand.setItemInHand(item);                
                ++i;
                if (i > 6) {
                    stopTask(taskSlash1);
                    SCore.debug("Task stopped");
                    stand.remove();
                    taskSlash1 = 0;
                }
            }            
        }, 0, 1);
        
        /*Location loc = player.getEyeLocation();
        slash_1(player, loc);
        player.setVelocity(player.getLocation().getDirection().multiply(1.0));
        Bukkit.getScheduler().runTaskLater(SCore.get(), () -> {
            Location loc1 = player.getEyeLocation();
            slash_2(player, loc1);
            player.setVelocity(player.getLocation().getDirection().multiply(1.0));
        
        }, 10L);*/
    }
    
    private int taskSlash1 = 0;
    public void slash_1(final Player p, final Location loc) {
        if (taskSlash1 != 0) {
            return;
        }
        taskSlash1 = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            final double pitch = 0.0F; //(loc.getPitch() + 19.0F) * 0.017453292F; // ARRIBA - ABAJO
            final double yaw = -loc.getYaw() * 0.017453292F; // IZQ - DER
            double radius = 3.0;
            double alpha = 0;            
            @Override
            public void run() {
                for (int i = 0; i<3; ++i) {
                    alpha += Math.PI / 16;
                    double x = radius * Math.cos(alpha);
                    double z = radius * Math.sin(alpha);
                    Vector vec = new Vector(x, Math.sin(alpha+1.5)-1, z);
                    rotateAroundAxisX(vec, pitch);
                    rotateAroundAxisY(vec, yaw);
                    loc.add(vec); 
                    DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 10, 0), 1.0F);            
                    p.spawnParticle(Particle.REDSTONE, loc, 15, 0.025, 0.025, 0.025, 0.1f, dustOptions);
                    checkEntityRadius(p, loc, 1.0);
                    loc.subtract(vec);
                    
                    if (alpha >= Math.PI) {
                        break;
                    }
                } 
                if (alpha >= Math.PI) {
                    stopTask(taskSlash1);
                    taskSlash1 = 0;
                }
            }
        }, 0L, 1L);      
    }
    
    private int taskSlash2 = 0;
    public void slash_2(final Player p, final Location loc) {
        if (taskSlash2 != 0) {
            return;
        }        
        taskSlash2 = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            //final Location loc = p.getEyeLocation();
            
            final double pitch = 0.0F; //(loc.getPitch() + 19.0F) * 0.017453292F; // ARRIBA - ABAJO
            final double yaw = (-loc.getYaw()-90) * 0.017453292F; // IZQ - DER
            double radius = 3.0;
            double alpha = 0;            
            @Override
            public void run() {
                for (int i = 0; i<3; ++i) {
                    alpha += Math.PI / 16;
                    double x = radius * Math.sin(alpha);
                    double z = radius * Math.cos(alpha);
                    Vector vec = new Vector(x, Math.cos(alpha-1.5)-1, z);
                    rotateAroundAxisX(vec, pitch);
                    rotateAroundAxisY(vec, yaw);
                    loc.add(vec); 
                    DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 10, 0), 1.0F);            
                    p.spawnParticle(Particle.REDSTONE, loc, 15, 0.025, 0.025, 0.025, 0.1f, dustOptions);
                    checkEntityRadius(p, loc, 1.0);
                    loc.subtract(vec);
                    
                    if (alpha >= Math.PI) {
                        break;
                    }
                } 
                if (alpha >= Math.PI) {
                    stopTask(taskSlash2);
                    taskSlash2 = 0;
                }
            }
        }, 0L, 1L); 
    }
    
    /*
    
    */
    
    private final Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private final Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }
    
    public void stopTask (Integer taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }
    
    public void checkEntityRadius (Player p, Location loc, Double radius) {
        ArrayList<Entity> reg = new ArrayList<>();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
            if (entity instanceof LivingEntity) {
                if (entity == p) {
                    continue;
                }
                
                Entity ent = entity;
                if (!reg.contains(ent)) {
                    reg.add(ent);
                    ((Damageable)ent).damage(2, p);
                    ent.setVelocity(entity.getVelocity().add(loc.getDirection().normalize().multiply(0.75)));
                }
            }
        }
    }
}
