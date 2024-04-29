package cs4303.p4.physics;

import processing.core.PVector;
import lombok.Getter;


public class BoundingBox {
    private  @Getter PVector location;
    private @Getter int width;
    private @Getter int height;

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



}
