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

import static processing.core.PApplet.constrain;

@Getter
@Setter
public abstract class Entity extends Collidable {
    private EnumMap<Attribute, AttributeModifier> baseAttributes;
    private ArrayList<Item> inventory;
    private int maxSlots;
    private int health = 1;

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
        // gravity
        this.applyGravity();

        // drag
        this.applyDrag();

        // work out the new velocity
        PVector prospectiveV = PVector.add(this.getVelocity(), this.getAcceleration());
        float constrainedX = constrain(prospectiveV.x, -30, 30);
        float constrainedY = constrain(prospectiveV.y, -30, 30);
        float constrainedZ = constrain(prospectiveV.z, -30, 30);

        PVector constrainedProspectiveV = new PVector(constrainedX, constrainedY, constrainedZ);

        // update the positions of the bounding boxes
        for (BoundingBox b : super.getBounds()) {
            b.moveBox(constrainedProspectiveV);

        }

        // check for collisions between the new location and the map
        for (Node n : nodes) {
            // if theres a collision change how much movement occurs
            if (this.Collision(n)) {
                // create an entity to move
                BoundingBox bCopy = new BoundingBox(
                        this.getBounds().get(0).getLocation().copy(), this.getBounds().get(0).getWidth(),
                        this.getBounds().get(0).getHeight());

                ArrayList<BoundingBox> bnds = new ArrayList<BoundingBox>();
                bnds.add(bCopy);
                Entity temp = new Entity(this.getLocation().copy().x, this.getLocation().copy().y) {
                    @Override
                    public void draw(PApplet sketch) {

                    }
                };

                temp.setBounds(bnds);

                // get a unit vector which is a fraction of a pixel to move the bounding box
                // back by
                PVector unitOfVelocity = constrainedProspectiveV.copy().normalize().mult((float) -0.01);

                // track the cumulative distance the boc was moved backwards
                PVector trackingMovement = new PVector(0, 0);

                // move it backwards by the unit vector till no collision
                Boolean collide = true;
                while (collide) {

                    // move the entity
                    bCopy.moveBox(unitOfVelocity);
                    PVector tempLoc = temp.getLocation();
                    temp.setLocation(tempLoc.add(unitOfVelocity));
                    trackingMovement.add(unitOfVelocity);

                    if (!temp.Collision(n)) {
                        // figure out if the collision is with a node below
                        BoundingBox b = n.getBoundingBox(this);
                        if (b.getLocation().y > this.getLocation().y) {
                            // must be below
                            // eliminate the x direction of tracking movement
                            trackingMovement = new PVector((float) (0.05 * trackingMovement.x), trackingMovement.y);

                        }
                        collide = false;

                    }
                }

                PVector v = PVector.add(constrainedProspectiveV, trackingMovement);

                this.setVelocity(v);

                // move the bounding box back to its original position
                PVector reverse = PVector.mult(constrainedProspectiveV.copy(), -1);

                // move the bounding box back to its original position
                for (BoundingBox x : super.getBounds()) {
                    x.moveBox(reverse);
                }

                // Update position based on velocity
                super.setLocation(PVector.add(super.getLocation(), this.getVelocity()));

                // update the positions of the bounding boxes to how much movement is occuring
                for (BoundingBox c : super.getBounds()) {
                    c.moveBox(this.getVelocity());
                }
                this.setAcceleration(new PVector(0, 0));
                return;
            }
        }

        // update the velocity
        this.setVelocity(constrainedProspectiveV);

        // Update position based on velocity
        super.setLocation(PVector.add(super.getLocation(), this.getVelocity()));

        this.setAcceleration(new PVector(0, 0));
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

    public void setVelocity(PVector v) {
        // Ensure no component of prospectiveV exceeds 35
        float constrainedX = constrain(v.x, -30, 30);
        float constrainedY = constrain(v.y, -30, 30);
        float constrainedZ = constrain(v.z, -30, 30);

        PVector constrainedProspectiveV = new PVector(constrainedX, constrainedY, constrainedZ);
        this.velocity = constrainedProspectiveV;
    }

    /**
     * Updates the postitions of the entity and
     * its bounding boxes
     * 
     * @param offset the camera offset
     */
    public void moveWithCamera(float deltaX, boolean cameraMoving, boolean cameraMovingRight, boolean cameraStill) {
        PVector location = getLocation();
        if (cameraMoving) {
            if (cameraMovingRight && !cameraStill) {
                location.x -= deltaX * 2; // Move player horizontally with the camera when moving right
            } else if (cameraStill) {
                location.x = location.x;
            } else {
                location.x += deltaX * 2; // Move player in the opposite direction when camera moves left
            }
            setLocation(location);

            // Also move the bounding boxes if necessary
            for (BoundingBox b : getBounds()) {
                if (cameraMovingRight && !cameraStill) {
                    b.moveBox(new PVector(-deltaX * 2, 0));
                } else if (cameraStill) {
                    b.moveBox(new PVector(0, 0));

                } else {
                    b.moveBox(new PVector(deltaX * 2, 0));
                }
            }
        }

    }
}
