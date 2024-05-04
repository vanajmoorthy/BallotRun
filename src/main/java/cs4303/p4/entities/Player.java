package cs4303.p4.entities;

import java.util.ArrayList;
import java.util.List;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeController;
import cs4303.p4.map.Node;
import cs4303.p4.physics.BoundingBox;
import cs4303.p4._util.Constants;
import cs4303.p4.items.Item;
import processing.core.PApplet;
import processing.core.PVector;

public class Player extends Entity {
    private float cameraOffsetX;

    public Player(float x, float y) {
        super(x, y);
        super.setInventory(new ArrayList<Item>());
        super.setMaxSlots(3);
        super.setMass(Constants.PLAYER.INSTANCE.MASS);

        // initialise mass and acceleration to 0
        super.setAcceleration(new PVector(0, 0));
        super.setVelocity(new PVector(0, 0));
        this.cameraOffsetX = 0;

        // create bounding box
        // TODO change this to player size
        BoundingBox b1 = new BoundingBox(this.getLocation(), 20, 20);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(b1);
        super.setBounds(b);
    }

    public void setCameraOffsetX(float offsetX) {
        this.cameraOffsetX = offsetX;
    }

    @Override
    public void draw(PApplet sketch) {
        sketch.pushMatrix();
        sketch.fill(0, 0, 255); // Blue color for player
        // Calculate player's position relative to camera
        // float screenX = getLocation().x - cameraOffsetX;
        sketch.rect(getLocation().x, getLocation().y, 20, 20); // 20x20 player for now

        sketch.noFill();

        for (BoundingBox b : getBounds()) {
            // float bx = b.getLocation().x - cameraOffsetX;
            sketch.rect(b.getLocation().x, b.getLocation().y, b.getWidth(), b.getHeight());

        }
        sketch.popMatrix();
    }

    public void jump() {
        // TODO take account of screen size in jump
        PVector jump = new PVector(0, -1 * Constants.Screen.height *
                Constants.PLAYER.INSTANCE.JUMP_IMPULSE *
                (AttributeController.getEntityAttributeValue(this, Attribute.JumpHeight)/100));
        System.out.println("JUMPING" + jump);
        super.applyForce(jump);

    }

    @Override
    public void move(List<Node> nodes) {

        super.move(nodes);

        // TODO edge of screen detection
        // Stop the user from moving past the edges of the screen
        // if(super.getLocation().x >= Constants.Screen.width){
        // super.setLocation( new PVector(
        // Constants.Screen.width,super.getLocation().y));
        // }

        // if(super.getLocation().x <= 0){
        // super.setLocation( new PVector( 0,super.getLocation().y));
        // }

    }

    /**
     * Updates the postitions of the player and
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
