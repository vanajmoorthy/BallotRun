package cs4303.p4.physics;

import processing.core.PVector;

public class Bullet extends Projectile{
    /**
     * Construct
     *
     * @param x
     * @param y
     * @param Direction projectile moves in
     * @param speed
     * @param size      projectiles are squares, this is the length of the sides
     */
    public Bullet(float x, float y, PVector Direction, float speed, int size) {
        super(x, y, Direction, speed, size);
    }

    /**
     * bullet been hit
     * should destroy the object
     */
    public void attacked(){

    }
}
