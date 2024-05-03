package cs4303.p4.physics;

import lombok.Setter;
import processing.core.PVector;

import java.util.ArrayList;
import lombok.Getter;

public class Collidable {
    private @Getter @Setter PVector location;
    private @Getter @Setter ArrayList<BoundingBox> bounds;

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
     * NOTE returns true the moment a collision is found
     * if all collisions are needed override
     * Checking for collisions between the bounding boxes
     * @param c the object to check for collisions with
     * @return true if collision
     */
    public boolean Collision(Collidable c) {
        for (BoundingBox b : bounds) {
            for (BoundingBox b2 : c.getBounds()) {
                if(b.getLocation().x < b2.getLocation().x + b2.getWidth() &&
                        b.getLocation().x + b.getWidth() > b2.getLocation().x &&
                        b.getLocation().y < b2.getLocation().y + b2.getHeight() &&
                        b.getLocation().y + b.getHeight() > b2.getLocation().y){
                    return true;
                }
            }
        }
        return false;
    }

    public BoundingBox getBoundingBox(Collidable c){
        for (BoundingBox b : bounds) {
            for (BoundingBox b2 : c.getBounds()) {
                if(b.getLocation().x < b2.getLocation().x + b2.getWidth() &&
                        b.getLocation().x + b.getWidth() > b2.getLocation().x &&
                        b.getLocation().y < b2.getLocation().y + b2.getHeight() &&
                        b.getLocation().y + b.getHeight() > b2.getLocation().y){
                    return b2;
                }
            }
        }
        return null;
    }
}