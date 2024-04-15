package cs4303.p4;

import java.util.ArrayList;
import java.util.EnumMap;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import cs4303.p4.items.Item;
import lombok.Getter;
import processing.core.PVector;

@Getter
public class Entity extends Collidable{

    private EnumMap<Attribute, AttributeModifier> baseAttributes;
    private ArrayList<Item> inventory;

    //values for movement
    //TODO add to constructors
    private PVector acceleration;
    private PVector velocity;

    public PVector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    private float mass;
    public Entity(Game g, float x, float y, EnumMap<Attribute, AttributeModifier> baseAttributes) {
        super(g, x, y);
        this.baseAttributes = baseAttributes;
    }

    public Entity(Game g, float x, float y) {
        this(g, x, y, new EnumMap<Attribute, AttributeModifier>(Attribute.class));
    }

    /**
     * Check for collisions between this entity and another
     * @param e entity colliding with this entity
     * @return true if the collision occurs
     */
    public boolean entityCollision(Entity e){
        return false;
    }

    public void draw(){
    }

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
}
