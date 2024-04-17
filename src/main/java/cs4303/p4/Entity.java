package cs4303.p4;

import java.util.ArrayList;
import java.util.EnumMap;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import cs4303.p4.items.Item;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PVector;

@Getter
@Setter
public abstract class Entity extends Collidable{
    private EnumMap<Attribute, AttributeModifier> baseAttributes;
    private ArrayList<Item> inventory;
    private int maxSlots;

    //values for movement
    //TODO add to constructors
    private PVector acceleration;
    private PVector velocity;
    private float mass;

    public Entity(float x, float y, EnumMap<Attribute, AttributeModifier> baseAttributes) {
        super(x, y);
        this.baseAttributes = baseAttributes;
    }

    public Entity(float x, float y) {
        this(x, y, new EnumMap<Attribute, AttributeModifier>(Attribute.class));
    }

    /**
     * Check for collisions between this entity and another
     * @param e entity colliding with this entity
     * @return true if the collision occurs
     */
    public boolean entityCollision(Entity e){
        return false;
    }

    public abstract void draw(PApplet sketch);

    public void update(){
    }

    /**
     * Apply a force to the object
     * updates the acceleration
     * @param force
     */
    public void applyForce(PVector force){
        PVector f = force.copy();
        f.div(this.mass);
        PVector a = this.getAcceleration();
        a.add(f);
        this.setAcceleration(a);

    }

    public boolean addItem(Item item) {
        if (inventory.contains(item)) return false;
        if (inventory.size() >= maxSlots) return false;

        inventory.add(item);
        return true;
    }
}
