package store.j3studios.plugin.protections;

import java.util.Objects;
import org.bukkit.block.Block;

public class Selection {
    
    private final Block centerPoint;
    
    private Block corner_one;
    private Block corner_two;
    private Block corner_three;
    private Block corner_four;
    
    private Integer radius;
    
    public Selection(Block centerPoint, int radius) {
        this.radius = radius;
        this.centerPoint = centerPoint;
        
        this.getCornerOne(centerPoint, radius);
        this.getCornerTwo(centerPoint, radius);
        this.getCornerThree(centerPoint, radius);
        this.getCornerFour(centerPoint, radius);        
    }

    private void getCornerOne (Block centerPoint, int radius) {
        this.corner_one = centerPoint.getWorld().getBlockAt(centerPoint.getX() - radius, centerPoint.getY(), centerPoint.getZ() - radius);
    }
    
    private void getCornerTwo (Block centerPoint, int radius) {
        this.corner_two = centerPoint.getWorld().getBlockAt(centerPoint.getX() - radius, centerPoint.getY(), centerPoint.getZ() + radius);
    }
    
    private void getCornerThree (Block centerPoint, int radius) {
        this.corner_three = centerPoint.getWorld().getBlockAt(centerPoint.getX() + radius, centerPoint.getY(), centerPoint.getZ() - radius);
    }
    
    private void getCornerFour (Block centerPoint, int radius) {
        this.corner_four = centerPoint.getWorld().getBlockAt(centerPoint.getX() + radius, centerPoint.getY(), centerPoint.getZ() + radius);
    }
    
    public Block getCenterPoint() {
        return centerPoint;
    }

    public Block getCorner_one() {
        return corner_one;
    }

    public Block getCorner_two() {
        return corner_two;
    }

    public Block getCorner_three() {
        return corner_three;
    }

    public Block getCorner_four() {
        return corner_four;
    }

    public Integer getRadius() {
        return radius;
    }
    
    public boolean numberInRange(int target, int starting, int ending) {
        return (target >= starting && target <= ending);
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
    
    public boolean containsBlock(Block block) {
        if (block.getWorld() != this.centerPoint.getWorld())
            return false;
        if (numberInRange(block.getX(), this.getCorner_one().getX(), this.getCorner_four().getX()))
            return numberInRange(block.getZ(), this.getCorner_one().getZ(), this.getCorner_four().getZ());
        return false;
    }
    
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Selection))
            return false;
        Selection selection = (Selection)o;
        return this.getCenterPoint().equals(selection.getCenterPoint());
    }
    
    public int hashCode(){
        return Objects.hash(new Object[] { getCenterPoint()});
    }
}
