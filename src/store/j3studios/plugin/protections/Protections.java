package store.j3studios.plugin.protections;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Protections {
    
    private String uuid;
    private Location loc;
    private Integer size;
    
    private OfflinePlayer owner;
    private UUID ownerUUID;
    private ArrayList<String> members;
    private final Selection selection;

    public Protections(String protectionUUID, Player owner, ArrayList<String> members, Selection selection) {
        this.uuid = protectionUUID;
        
        this.owner = owner;
        this.ownerUUID = owner.getUniqueId();
        this.members = members;
        
        this.selection = selection;
        this.loc = selection.getCenterPoint().getLocation();
        this.size = selection.getRadius();
    }
    
    public Protections(String protectionUUID, String ownerUUID, ArrayList<String> members, Selection selection) {
        this.uuid = protectionUUID;
        
        this.owner = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID));
        this.ownerUUID = UUID.fromString(ownerUUID);
        this.members = members;
        
        this.selection = selection;
        this.loc = selection.getCenterPoint().getLocation();
        this.size = selection.getRadius();
    }
    
    public boolean overlaps(Selection comparator) {
        if (this.containsBlock(comparator.getCorner_one()))
            return true;
        if (this.containsBlock(comparator.getCorner_two()))
            return true;
        if (this.containsBlock(comparator.getCorner_three()))
            return true;
        return this.containsBlock(comparator.getCorner_four());
    }
    
    public boolean containsBlock (Block block) {
        if (block.getWorld() != this.loc.getWorld())
            return false;
        if (numberInRange(block.getX(), selection.getCorner_one().getX(), selection.getCorner_four().getX()))
            return numberInRange(block.getZ(), selection.getCorner_one().getZ(), selection.getCorner_four().getZ());
        return false;
    }
    
    public boolean numberInRange(int target, int starting, int ending) {
        return (target >= starting && target <= ending);
    }

    public String getUuid() {
        return uuid;
    }

    public Location getLoc() {
        return loc;
    }

    public Integer getSize() {
        return size;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public String getMembersList() {
        String str = "";
        for (String list : getMembers()) {
            String name = list.split(" : ")[1];
            str = str + name + ", ";
        }
        if (str.contains(", ")) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
    
    public Selection getSelection() {
        return selection;
    }
    
    //
    
    public boolean equals (Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Protections))
            return false;
        Protections region = (Protections)o;
        return this.getOwner().equals(region.getOwner());
    }
    
    public boolean containsPlayer (Player player) {
        return this.containsBlock(player.getWorld().getBlockAt(player.getLocation()));
    }
    
}
