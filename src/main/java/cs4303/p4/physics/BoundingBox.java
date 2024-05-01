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
}
