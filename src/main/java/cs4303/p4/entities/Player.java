package cs4303.p4.entities;

import java.util.ArrayList;
import java.util.List;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeController;
import cs4303.p4.map.Level;
import cs4303.p4.map.Node;
import cs4303.p4.map.TileType;
import cs4303.p4.physics.BoundingBox;
import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4.items.Item;
import cs4303.p4.physics.Projectile;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import static processing.core.PApplet.constrain;
import static processing.core.PApplet.dist;

public class Player extends Entity {
    public float cameraOffsetX;

    private boolean isAttacking = false;
    private float attackRadius = 100; // Example attack radius
    private float currentAttackRadius = 0;
    private long lastAttackTime = 0; // Track the last attack time
    private static long ATTACK_COOLDOWN; // 500 milliseconds between attacks
    private int size;
    private boolean canJump = true;
    PImage image;

    public Player(float x, float y, PImage image) {
        super(x, y);
        super.setInventory(new ArrayList<Item>());
        super.setMaxSlots(3);
        super.setMass(Constants.PLAYER.INSTANCE.MASS);

        // initialise mass and acceleration to 0
        super.setAcceleration(new PVector(0, 0));
        super.setVelocity(new PVector(0, 0));
        this.cameraOffsetX = 0;
        this.size = 20;
        this.image = image;

        ATTACK_COOLDOWN = (500 * (long) AttributeController.getEntityAttributeValue(this, Attribute.AttackSpeed)) / 100;
        // create bounding box
        BoundingBox b1 = new BoundingBox(this.getLocation(), size, size);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(b1);
        super.setBounds(b);
    }

    public void startAttack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            isAttacking = true;
            currentAttackRadius = 0; // Reset the radius for animation
            lastAttackTime = currentTime; // Update last attack time
        }
    }

    public void updateAttack(PApplet sketch, ArrayList<Enemy> enemies, ArrayList<Projectile> bullets) {
        if (isAttacking) {
            if (currentAttackRadius < attackRadius) {
                currentAttackRadius += 5; // Increment the radius for animation
                sketch.noFill();
                sketch.stroke(255, 0, 0); // Red color for attack radius
                // Draw ellipse centered on player's center
                sketch.ellipse(getLocation().x + 10, getLocation().y + 10, currentAttackRadius, currentAttackRadius);
            } else {
                isAttacking = false; // Stop the attack

            }
            checkForEnemiesWithinRadius(enemies, bullets); // Check for enemies within the radius
        }
    }

    private void checkForEnemiesWithinRadius(ArrayList<Enemy> enemies, ArrayList<Projectile> bullets) {
        // This method would check if any enemy is within the attack radius
        // You will need a reference to the list of enemies or pass it as a parameter
        ArrayList<Enemy> toRemove = new ArrayList<>();
        for (Enemy e : enemies) {
            if (isWithinRadius(e.getLocation().x, e.getLocation().y, e.getSize(), this.getLocation().x + 10,
                    this.getLocation().y + 10, currentAttackRadius)) {
                toRemove.add(e);
            }
        }
        ArrayList<Projectile> removals = new ArrayList<>();
        for (Projectile p : bullets) {
            if (isWithinRadius(p.getLocation().x, p.getLocation().y, p.getSize(), this.getLocation().x + 10,
                    this.getLocation().y + 10, currentAttackRadius)) {
                removals.add(p);
            }
        }
        enemies.removeAll(toRemove);
        bullets.removeAll(removals);

    }

    boolean isWithinRadius(float squareX, float squareY, float squareSize, float radiusX, float radiusY, float radius) {
        PVector squareCentre = new PVector(squareX - this.cameraOffsetX + squareSize / 2, squareY + squareSize / 2);
        PVector radiusCentre = new PVector(radiusX, radiusY);

        PVector squareToRadius = PVector.sub(radiusCentre, squareCentre);

        if (squareToRadius.mag() <= radius) {
            return true;

        } else {
            return false;
        }
    }

    @Override
    public void draw(PApplet sketch) {
        try {
            sketch.image(image, getLocation().x, getLocation().y, size, size); // Draw the image at
        } catch (Exception e) {
            sketch.pushMatrix();
            sketch.noStroke();
            sketch.fill(Colors.blue.primary); // Blue color for player
            sketch.rect(getLocation().x, getLocation().y, size, size);

            sketch.popMatrix();
        }

    }

    /**
     * Jump function applies the jump force
     */
    public void jump() {
        if (canJump && isGrounded()) {
            PVector jumpForce = new PVector(0, -Constants.PLAYER.INSTANCE.JUMP_IMPULSE
                    * (AttributeController.getEntityAttributeValue(this, Attribute.JumpHeight) / 100));
            super.applyForce(jumpForce);
            canJump = false; // Player cannot jump again until they land
        }
        canJump = true;

    }

    public boolean isGrounded() {
        int belowY = (int) ((getLocation().y + size) / Constants.TILE_SIZE) + 1;
        int playerX = (int) (getLocation().x / Constants.TILE_SIZE);

        // Ensure the indexes are within the bounds of the level grid
        if (playerX < 0 || playerX >= Level.getLevelGrid()[0].length || belowY >= Level.getLevelGrid().length) {
            return false; // Return false if out of bounds
        }

        // Check if the tile below the player is a platform
        if (Level.getLevelGrid()[belowY][playerX].getType() == TileType.PLATFORM) {
            // Check if the bottom of the player's bounding box is within a certain distance
            // of the ground
            float bottomY = getLocation().y + size;
            float groundY = belowY * Constants.TILE_SIZE;
            float groundThreshold = 5; // Adjust this threshold as needed
            return bottomY >= groundY - groundThreshold;
        } else {
            return false;
        }
    }

    /**
     * Updates the postitions of the player and its bounding boxes
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

    public void resetPlayer() {
        setLocation(new PVector(0, 0));
        setVelocity(new PVector(0, 0));
        setAcceleration(new PVector(0, 0));
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
