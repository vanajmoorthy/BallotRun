package cs4303.p4.entities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cs4303.p4._util.Constants;
import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import cs4303.p4.items.Item;
import cs4303.p4.map.Node;
import cs4303.p4.physics.BoundingBox;
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
    private @Getter @Setter PVector acceleration;
    private @Getter @Setter PVector velocity;
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
     * Applies gravity
     */
    public void applyGravity(){

        // gravity
        PVector gravity = new PVector(0, Constants.Screen.height * Constants.gravity);
        this.applyForce(gravity);

    }

    /**
     * Updates the position
     */
    public void move(List<Node> nodes){

        // drag
        this.applyDrag();

        // Update velocity based on acceleration
        PVector prospectiveV = PVector.add(this.getVelocity(),this.getAcceleration());
        //update the positions of the bounding boxes
        for(BoundingBox b : super.getBounds()){
            b.moveBox(prospectiveV);
        }
        //check for collisions between the new location and the map
        for(Node n: nodes){
            //if theres a collision change how much movement occurs
            if(this.Collision(n)){

                //distance the velocity tried to move
                //float DistanceTravelled = prospectiveV.mag();

                //find the distance between the current location and the collision with
                //bounding box`
                //BoundingBox b = this.getBoundingBox(n);
                //float distToBox = b.getDistanceToBox(this);

                //IF THERES A COLLISION JUST MOVE BY A THIRD THE DISTANCE
                PVector newv = PVector.div(prospectiveV,3);
                this.setVelocity(newv);

                this.getAcceleration().mult(0);

                // Update position based on velocity
                super.setLocation(PVector.add(super.getLocation(),this.getVelocity()));

                //move the bounding box
                PVector reverse = PVector.mult(prospectiveV,-1);
                //update the positions of the bounding boxes
                for(BoundingBox b : super.getBounds()){
                    b.moveBox(reverse);
                }

                //update the positions of the bounding boxes
                for(BoundingBox b : super.getBounds()){
                    b.moveBox(this.getVelocity());
                }
                return;
            }
        }

        //update the velocity
        this.setVelocity(PVector.add(this.getVelocity(),this.getAcceleration()));

        this.getAcceleration().mult(0);

        // Update position based on velocity
        super.setLocation(PVector.add(super.getLocation(),this.getVelocity()));

    }

    /**
     * Apply a force to the object
     * updates the acceleration
     * @param force
     */
    public void applyForce(PVector force){
        PVector f = force.copy();
        f.div(this.mass);
        PVector a = PVector.add(this.getAcceleration(),f);
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
        float drag = (float) (velocity.mag() * velocity.mag() * -2.5 * Constants.airResistance);
        velocity.normalize();
        velocity.mult(drag);
        this.applyForce(velocity);
    }

    /**
     * Get the grid y of the previous tile
     * @return
     */
    public int getTileY(){

        return (int) this.getLocation().y / Constants.TILE_SIZE;
    }

    /**
     * Get the grid y of the previous tile
     * @return
     */
    public int getTileX(){

        return (int) this.getLocation().x / Constants.TILE_SIZE;
    }
}
