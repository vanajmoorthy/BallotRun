package cs4303.p4;

public class Entity extends Collidable{

    public Entity(Game g, float x, float y) {
        super(g, x, y);
    }

    /**
     * Check for collisions between this entity and another
     * @param e entity colliding with this entity
     * @return true if the collision occurs
     */
    public boolean entityCollision(Entity e){
        return false;
    }
}
