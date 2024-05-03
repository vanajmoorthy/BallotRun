package cs4303.p4.physics;

import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
public class BoundingBox {
    private PVector location;
    private int width;
    private int height;

    /**
 * Constructs a new BoundingBox object with the specified location, width, and height.
 *
 * @param location The top-left corner of the bounding box in the 2D space.
 * @param width     The width of the bounding box.
 * @param height    The height of the bounding box.
 */
    public BoundingBox(PVector location, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }

    /**
     * Moves the box by the move vector
     * @param move
     */
    public void moveBox(PVector move){
        this.location.add(move);
    }



    // public float getDistanceToBox(Collidable c){
    //     // Calculate distances to the closest points on each edge of the rectangle
    //     float distToLeft = dist(c.getLocation().x, c.getLocation().y, this.getLocation().x, constrain(c.getLocation().y, this.getLocation().y, this.getLocation().y + this.height));
    //     float distToRight = dist(c.getLocation().x, c.getLocation().y, this.getLocation().x + this.width, constrain(c.getLocation().y, this.getLocation().y, this.getLocation().y + this.height));
    //     float distToTop = dist(c.getLocation().x, c.getLocation().y, constrain(c.getLocation().x, this.getLocation().x, this.getLocation().x + this.width), this.getLocation().y);
    //     float distToBottom = dist(c.getLocation().x, c.getLocation().y, constrain(c.getLocation().x, this.getLocation().x, this.getLocation().x + this.width), this.getLocation().y + this.height);

    //     // Find the minimum distance among these distances
    //     return min(distToLeft, min(distToRight, min(distToTop, distToBottom)));
    // }



}
