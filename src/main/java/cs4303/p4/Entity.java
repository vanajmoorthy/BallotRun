package cs4303.p4;

import java.util.ArrayList;
import java.util.EnumMap;

import cs4303.p4._util.Constants;
import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import cs4303.p4.items.Item;
import cs4303.p4.physics.Collidable;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PVector;

@Getter
@Setter
public abstract class Entity extends Collidable {
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



    public abstract void draw(PApplet sketch);

    /**
     * Updates the velocity
     */
    public void update(){
    }

    /**
     * Updates the position
     */
    public void move(){

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

    /**
     * Apply drag to an entity
     * applies it to the acceleration of the object
     */
    public void applyDrag(){
        PVector velocity = this.getVelocity().copy();
        float drag = (float) (velocity.mag() * velocity.mag() * 0.5F * Constants.airResistance);
        velocity.normalize();
        velocity.mult(-drag);
        this.setAcceleration(this.getAcceleration().add(velocity));
    }
}
