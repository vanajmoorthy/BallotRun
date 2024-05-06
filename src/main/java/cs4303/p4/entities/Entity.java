package cs4303.p4.entities;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cs4303.p4._util.Constants;
import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeController;
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

    // values for movement
    // TODO add to constructors
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


    public boolean isInventoryFull() {
        return inventory.size() >= maxSlots;
    }


    public abstract void draw(PApplet sketch);

    /**
     * Applies gravity
     */
    public void applyGravity() {

        // gravity
        PVector gravity = new PVector(0, Constants.Screen.height * Constants.gravity);
        this.applyForce(gravity);

    }

    /**
     * Updates the position
     */
    public void move(List<Node> nodes) {
        System.out.println(this.getVelocity());
        //gravity
        this.applyGravity();

        //drag
        this.applyDrag();

        // Update velocity based on acceleration
        PVector prospectiveV = PVector.add(this.getVelocity(),this.getAcceleration());


        // update the positions of the bounding boxes
        for (BoundingBox b : super.getBounds()){
            b.moveBox(prospectiveV);

        }

        // check for collisions between the new location and the map
        for (Node n : nodes) {
            // if theres a collision change how much movement occurs
            if (this.Collision(n)) {
                //create an entity to move
                BoundingBox bCopy = new BoundingBox(
                        this.getLocation().copy(),this.getBounds().get(0).getWidth(),this.getBounds().get(0).getHeight());

                ArrayList<BoundingBox> bnds = new ArrayList<BoundingBox>();
                bnds.add(bCopy);
                Entity temp = new Entity(this.getLocation().copy().x,this.getLocation().copy().y) {
                    @Override
                    public void draw(PApplet sketch) {

                    }
                };

                temp.setBounds(bnds);


                //normalise and flip the direction
                System.out.println("actual velocity" + prospectiveV);
                PVector unitOfVelocity = prospectiveV.copy().normalize().mult(-1);
                //PVector unitOfVelocity = prospectiveV.copy().mult((float) -0.01);

                System.out.println("unitOfVelocity: " + unitOfVelocity);
                // Round each component of the vector up to the nearest integer
                int roundedX = (int) (unitOfVelocity.x < 0 ? Math.round(unitOfVelocity.x) : Math.round(unitOfVelocity.x));
                int roundedY = (int) (unitOfVelocity.y < 0 ? Math.round(unitOfVelocity.y) : Math.round(unitOfVelocity.y));
                int roundedZ = (int) (unitOfVelocity.z < 0 ? Math.round(unitOfVelocity.z) : Math.round(unitOfVelocity.z));

                // Create a new PVector with rounded componentsdddd
                PVector roundedVector = new PVector(roundedX, roundedY, roundedZ);
               // System.out.println("the rounded vector" + roundedVector);
                PVector trackingMovement = new PVector(0,0);

                //move it backwards  by the unit vector till no collision
                Boolean collide = true;
                while(collide) {

                    bCopy.moveBox(roundedVector);
                    PVector tempLoc = temp.getLocation();
                    temp.setLocation(tempLoc.add(roundedVector));
                    trackingMovement.add(roundedVector);

                    if(!temp.Collision(n)){
                        //figure out if the collision is with a node below
                        BoundingBox b = n.getBoundingBox(this);

                        collide = false;
                    }
                }

                PVector v = PVector.add(prospectiveV,trackingMovement);



                this.setVelocity(v);


                // move the bounding box back to its original position
                PVector reverse = PVector.mult(prospectiveV.copy(), -1);
                for (BoundingBox x : super.getBounds()) {
                    x.moveBox(reverse);
                }

                // Update position based on velocity
                super.setLocation(PVector.add(super.getLocation(), this.getVelocity()));

                // update the positions of the bounding boxes to how much movement is occuring
                for (BoundingBox c : super.getBounds()) {
                    c.moveBox(this.getVelocity());
                }
                this.setAcceleration(new PVector(0,0));
                this.setVelocity(new PVector(0,0));
                return;
            }
        }

        // update the velocity
        this.setVelocity(PVector.add(this.getVelocity(), this.getAcceleration()));

        // Update position based on velocity
        super.setLocation(PVector.add(super.getLocation(), this.getVelocity()));

        this.setAcceleration(new PVector(0,0));
    }

    /**
     * Apply a force to the object
     * updates the acceleration
     * 
     * @param force
     */
    public void applyForce(PVector force) {
        PVector f = force.copy();
        f.div(this.mass);
        PVector a = PVector.add(this.getAcceleration(), f);
        this.setAcceleration(a);

    }

    public boolean addItem(Item item) {
        if (inventory.contains(item))
            return false;
        if (inventory.size() >= maxSlots)
            return false;
        inventory.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }

    /**
     * Apply drag to an entity
     * applies it to the acceleration of the object
     */
    public void applyDrag() {
        PVector velocity = this.getVelocity().copy();
        float drag = (float) (velocity.mag() * velocity.mag() * -2.5 * Constants.airResistance);
        velocity.normalize();
        velocity.mult(drag);
        this.applyForce(velocity);
    }

    /**
     * Get the grid y of the previous tile
     * 
     * @return
     */

    public int getTileY() {

        return (int) this.getLocation().y / Constants.TILE_SIZE;
    }

    /**
     * Get the grid y of the previous tile
     * 
     * @return
     */

    public int getTileX() {

        return (int) this.getLocation().x / Constants.TILE_SIZE;
    }
}
