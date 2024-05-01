package cs4303.p4.physics;

import processing.core.PVector;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Collidable {
    private PVector location;
    private ArrayList<BoundingBox> bounds;

    /**
     * Constructor
     * Override this method to create custom bounding boxes
     * @param x the x position
     * @param y the y position
     */
    public Collidable(float x, float y) {
        location = new PVector(x, y);

    }

    /**
     * Checking for collisions
     * @param c the object to check for collisions with
     * @return true if collision
     */
    public boolean Collision(Collidable c) {
        return false;
    }
}