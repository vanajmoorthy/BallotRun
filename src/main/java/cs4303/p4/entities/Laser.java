package cs4303.p4.entities;

import cs4303.p4._util.Constants;
import cs4303.p4.physics.BoundingBox;
import processing.core.PApplet;

public class Laser {
    private float x, y; // Position of the laser
    private boolean active; // Is the laser currently active (shining)
    private float timer; // Timer to control flashing
    private float interval = 2.0f; // Interval in seconds between flashes
    private PApplet parent; // Reference to the PApplet for drawing
    private float dashLength = 10; // Length of each dash
    private float dashSpace = 10; // Space between dashes
    private boolean hasHitPlayer; // Flag to check if laser has already hit the player

    public Laser(PApplet parent, float x, float y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.active = false;
        this.hasHitPlayer = false;
        this.timer = 0;
    }

    public void update(float deltaTime) {
        timer += deltaTime;
        if (timer >= interval) {
            active = !active; // Toggle laser activity
            timer = 0;
            hasHitPlayer = false; // Reset the hit flag whenever the laser toggles state
        }
    }

    public boolean checkCollision(Player player) {
        if (!active || hasHitPlayer) {
            return false; // Do not check collisions if the laser is not active or has already hit
        }
        for (BoundingBox box : player.getBounds()) {
            if (lineIntersectsRect(x, y, x, parent.height, box)) {
                hasHitPlayer = true; // Mark that the laser has hit the player this cycle
                return true; // Return true if any part of the player intersects the active laser
            }
        }
        return false;
    }

    public void draw(float cameraX) {
        float adjustedX = x - cameraX;
        if (active) {
            parent.stroke(255, 0, 0); // Red color for danger
            parent.strokeWeight(2);
            drawDashedLine(adjustedX);
        } else {
            parent.stroke(255, 255, 255, 120); // White color for inactive state
            parent.strokeWeight(2);
            drawDashedLine(adjustedX);
        }
    }

    private void drawDashedLine(float adjustedX) {
        float y = 0;
        while (y < parent.height) {
            parent.line(adjustedX, y, adjustedX, y + dashLength);
            y += dashLength + dashSpace;
        }
    }

    private boolean lineIntersectsRect(float x1, float y1, float x2, float y2, BoundingBox box) {
        return (box.getLocation().x <= x1 && box.getLocation().x + box.getWidth() >= x1)
                && (y1 <= box.getLocation().y + box.getHeight() && y2 >= box.getLocation().y);
    }
}
