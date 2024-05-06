package cs4303.p4.entities;

import java.util.ArrayList;
import java.util.List;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeController;
import cs4303.p4.map.Node;
import cs4303.p4.physics.BoundingBox;
import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4.items.Item;
import processing.core.PApplet;
import processing.core.PVector;

public class Player extends Entity {
    private float cameraOffsetX;

    private boolean isAttacking = false;
    private float attackRadius = 50; // Example attack radius
    private float currentAttackRadius = 0;
    private long lastAttackTime = 0; // Track the last attack time
    private static long ATTACK_COOLDOWN; // 500 milliseconds between attacks

    public Player(float x, float y) {
        super(x, y);
        super.setInventory(new ArrayList<Item>());
        super.setMaxSlots(3);
        super.setMass(Constants.PLAYER.INSTANCE.MASS);

        // initialise mass and acceleration to 0
        super.setAcceleration(new PVector(0, 0));
        super.setVelocity(new PVector(0, 0));
        this.cameraOffsetX = 0;


        ATTACK_COOLDOWN = (500 * (long) AttributeController.getEntityAttributeValue(this, Attribute.AttackSpeed))/100;
        // create bounding box
        BoundingBox b1 = new BoundingBox(this.getLocation(), 20, 20);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(b1);
        super.setBounds(b);
    }

    public void setCameraOffsetX(float offsetX) {
        this.cameraOffsetX = offsetX;
    }

    public void startAttack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            isAttacking = true;
            currentAttackRadius = 0; // Reset the radius for animation
            lastAttackTime = currentTime; // Update last attack time
        }
    }

    public void updateAttack(PApplet sketch) {
        if (isAttacking) {
            if (currentAttackRadius < attackRadius) {
                currentAttackRadius += 5; // Increment the radius for animation
                sketch.noFill();
                sketch.stroke(255, 0, 0); // Red color for attack radius
                // Draw ellipse centered on player's center
                sketch.ellipse(getLocation().x + 10, getLocation().y + 10, currentAttackRadius * 2,
                        currentAttackRadius * 2);
            } else {
                isAttacking = false; // Stop the attack
                checkForEnemiesWithinRadius(); // Check for enemies within the radius
            }
        }
    }

    private void checkForEnemiesWithinRadius() {
        // This method would check if any enemy is within the attack radius
        // You will need a reference to the list of enemies or pass it as a parameter
    }

    @Override
    public void draw(PApplet sketch) {
        sketch.pushMatrix();
        sketch.noStroke();
        sketch.fill(Colors.blue.primary); // Blue color for player
        // Calculate player's position relative to camera
        // float screenX = getLocation().x - cameraOffsetX;
        sketch.rect(getLocation().x, getLocation().y, 20, 20); // 20x20 player for now

        // sketch.noFill();
        //
        // for (BoundingBox b : getBounds()) {
        // // float bx = b.getLocation().x - cameraOffsetX;
        // sketch.rect(b.getLocation().x, b.getLocation().y, b.getWidth(),
        // b.getHeight());
        //
        // }
        sketch.popMatrix();
    }

    /**
     * Jump function
     * applies the jump force
     */
    public void jump() {
        PVector jump = new PVector(0, -1 * Constants.Screen.height *
                Constants.PLAYER.INSTANCE.JUMP_IMPULSE *
                (AttributeController.getEntityAttributeValue(this, Attribute.JumpHeight) / 100));
        super.applyForce(jump);

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

    // public boolean isOffMap(float cameraX, int gridWidth, int cellSize, boolean cameraMovingRight) {
    //     float playerX = getLocation().x;

    //     // Check if off the left side
    //     if (cameraMovingRight && playerX < -20 - ) {
    //         return true;
    //     }

    //     // Check if off the right side
    //     if (!cameraMovingRight && playerX > (gridWidth * cellSize)) {
    //         System.out.println("off right");

    //         return true;
    //     }
    //     return false;
    // }

    public void resetPlayer() {
        setLocation(new PVector(0, Constants.Screen.GamePlay.infoPanelHeight));
        setVelocity(new PVector(0, 0));
        setAcceleration(new PVector(0, 0));
        this.cameraOffsetX = 0;
        this.setHealth(Math.round(AttributeController.getEntityAttributeValue(this, Attribute.Health)));
        this.resetBoundingBox();
    }

    public void resetBoundingBox() {
        BoundingBox b1 = new BoundingBox(this.getLocation(), 20, 20);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(b1);
        super.setBounds(b);
    }
}
