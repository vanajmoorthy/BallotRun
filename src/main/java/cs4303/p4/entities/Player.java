package cs4303.p4.entities;

import java.util.ArrayList;
import java.util.List;

import cs4303.p4.entities.Entity;
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
        float screenX = getLocation().x - cameraOffsetX;
        sketch.rect(screenX, getLocation().y, 20, 20); // 20x20 player for now

        sketch.noFill();
        for (BoundingBox b : getBounds()) {
            float bx = b.getLocation().x - cameraOffsetX;
            sketch.rect(bx, b.getLocation().y, b.getWidth(), b.getHeight());
        }
        sketch.popMatrix();
    }

    public void jump() {
        // TODO take account of screen size in jump
        PVector jump = new PVector(0, -1 * Constants.Screen.height * Constants.PLAYER.INSTANCE.JUMP_IMPULSE);
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

}
