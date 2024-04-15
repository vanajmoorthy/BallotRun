package cs4303.p4;

import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
class Collidable {
    private PVector location;

    public Collidable(float x, float y) {
        location = new PVector(x, y);
    }

    /**
     * Checking for collisions with tiles
     * 
     * @return
     */
    public boolean tileCollision() {
        return false;
    }
}