package store.j3studios.plugin.managers;

import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import store.j3studios.plugin.player.PlayerManager;
import store.j3studios.plugin.player.SPlayer;
import store.j3studios.plugin.utils.Tools;

public class MagicManager {
    
    private static MagicManager ins;
    
    public static MagicManager get() {
        if (ins == null)
            ins = new MagicManager();
        return ins;
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
                                
        for (double i = 0; i <= Math.PI; i += Math.PI / 15) {
            double y = HEAL_RADIUS * Math.cos(i) + 1.5;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 15) {
                double x = HEAL_RADIUS * Math.cos(a) * Math.sin(i);
                double z = HEAL_RADIUS * Math.sin(a) * Math.sin(i);
                center.add(x, y, z);
                center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center, 1);
                center.subtract(x, y, z);
            }
        }
    }
    
    //
    
    private int taskSlash1 = 0;
    public void slash(Player p) {
        if (taskSlash1 != 0) {
            return;
        }
        
        Vector direction = p.getLocation().getDirection().multiply(4).setY(0.5);
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
                
                checkEntityRadius(p, inFront, 2.0, 2.0);
                if (i > 6) {
                    stopTask(taskSlash1);
                    stand.remove();
                    taskSlash1 = 0;
                }
            }            
        }, 0, 1);
    }
    
    int testStartTask = 0;
    public void test (Player player) {
        SPlayer sp = PlayerManager.get().getPlayer(player.getUniqueId());
        
        testStartTask = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            int tick = 1;
            @Override
            public void run() {
                if (tick <= 4) {
                    switch (tick) {
                        case 1:
                            test1(player);
                        case 2:
                            test2(player);
                        case 3:
                            test3(player);
                        case 4:
                            test4(player);
                    }
                } else {
                    String progress = new Tools().getProgressBar(sp.getMana(), 20.0, 20.0, '|', ChatColor.YELLOW, ChatColor.GRAY);
                    new Tools().sendActionBar(player, "&6&lMANÁ &7- &e"+progress);

                    sp.setMana(sp.getMana()-0.50);                
                    if (sp.getMana() <= 0.0) {
                        stopTask(testStartTask);
                        
                        stopTask(heal1);
                        stopTask(heal2);
                        stopTask(heal3);
                        stopTask(heal4);
                        
                        sp.setMana(20.0);
                    }
                    
                    stopTask(testStartTask);
                }
                ++tick;
            }
        }, 0L, 13L);
    }
    
    private int heal1 = 0;
    private int heal2 = 0;
    private int heal3 = 0;
    private int heal4 = 0;
    public void test1 (final Player player) {        
        final float radius = 5.0f;
        final float radPerSec = 5.0f;
        final float radPerTick = radPerSec / 40f;
        
        final CustomEntity ce = CustomEntity.spawn("survivalrp:test", player.getLocation());
                       
        heal1 = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            int tick = 0;
            @Override
            public void run() {
                ++tick;
                Location loc1 = getLocationAroundCircle(player.getLocation(), radius, radPerTick*tick);
                ce.teleport(loc1);
                
                double y = player.getLocation().getY();
                for (double i = y; i < (y+5); ++i) {
                    new Tools().playParticle(Particle.VILLAGER_HAPPY, loc1, 1, 0.01, 5.00, 0.01);
                }                
            }
        }, 0L, 1L);
    }
    public void test2 (final Player player) {        
        final float radius = 5.0f;
        final float radPerSec = 5.0f;
        final float radPerTick = radPerSec / 40f;
        
        final CustomEntity ce = CustomEntity.spawn("survivalrp:test", player.getLocation());
                       
        heal2 = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            int tick = 0;
            @Override
            public void run() {
                ++tick;
                Location loc1 = getLocationAroundCircle(player.getLocation(), radius, radPerTick*tick);
                ce.teleport(loc1);
                
                double y = player.getLocation().getY();
                for (double i = y; i < (y+5); ++i) {
                    new Tools().playParticle(Particle.VILLAGER_HAPPY, loc1, 1, 0.01, 5.00, 0.01);
                }                
            }
        }, 0L, 1L);
    }
    public void test3 (final Player player) {        
        final float radius = 5.0f;
        final float radPerSec = 5.0f;
        final float radPerTick = radPerSec / 40f;
        
        final CustomEntity ce = CustomEntity.spawn("survivalrp:test", player.getLocation());
                       
        heal3 = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            int tick = 0;
            @Override
            public void run() {
                ++tick;
                Location loc1 = getLocationAroundCircle(player.getLocation(), radius, radPerTick*tick);
                ce.teleport(loc1);
                
                double y = player.getLocation().getY();
                for (double i = y; i < (y+5); ++i) {
                    new Tools().playParticle(Particle.VILLAGER_HAPPY, loc1, 1, 0.01, 5.00, 0.01);
                }                
            }
        }, 0L, 1L);
    }
    public void test4 (final Player player) {        
        final float radius = 5.0f;
        final float radPerSec = 5.0f;
        final float radPerTick = radPerSec / 40f;
        
        final CustomEntity ce = CustomEntity.spawn("survivalrp:test", player.getLocation());
                       
        heal4 = SCore.get().getServer().getScheduler().scheduleSyncRepeatingTask(SCore.get(), new Runnable() {
            int tick = 0;
            @Override
            public void run() {
                ++tick;
                Location loc1 = getLocationAroundCircle(player.getLocation(), radius, radPerTick*tick);
                ce.teleport(loc1);
                
                double y = player.getLocation().getY();
                for (double i = y; i < (y+5); ++i) {
                    new Tools().playParticle(Particle.VILLAGER_HAPPY, loc1, 1, 0.01, 5.00, 0.01);
                }                
            }
        }, 0L, 1L);
    }
    
    /*
    
    */
    
    public Location getLocationAroundCircle(Location center, double radius, double angleInRadian) {
        double x = center.getX() + radius * Math.cos(angleInRadian);
        double z = center.getZ() + radius * Math.sin(angleInRadian);
        double y = center.getY();
        
        Location loc = new Location(center.getWorld(), x, y, z);
        Vector difference = center.toVector().clone().subtract(loc.toVector());
        loc.setDirection(difference);        
        return loc;
    }
    
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
    
    public void checkEntityRadius (Player p, Location loc, Double radius, Double damage) {
        ArrayList<Entity> reg = new ArrayList<>();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
            if (entity instanceof LivingEntity) {
                if (entity == p) {
                    continue;
                }
                
                Entity ent = entity;
                if (!reg.contains(ent)) {
                    reg.add(ent);
                    ((Damageable)ent).damage(damage, p);
                    ent.setVelocity(entity.getVelocity().add(loc.getDirection().normalize().multiply(0.75)));
                }
            }
        }
    }
}
