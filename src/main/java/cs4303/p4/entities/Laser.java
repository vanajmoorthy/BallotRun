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

    public Laser(PApplet parent, float x, float y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.active = false;
        this.timer = 0;
    }

    public void update(float deltaTime) {
        timer += deltaTime;
        if (timer >= interval) {
            active = !active; // Toggle laser activity
            timer = 0; // Reset timer
        }
    }

    public boolean checkCollision(Player player) {
        if (!active)
            return false; // Do not check collisions if the laser is not active
        for (BoundingBox box : player.getBounds()) {
            if (lineIntersectsRect(x, y, x, parent.height, box)) {
                return true; // Return true if any part of the player intersects the active laser
            }
        }
        return false;
    }

    public void draw(float cameraX) {
        if (active) { // Only draw if the laser is active
            parent.stroke(255, 0, 0); // Red color for the laser
            parent.strokeWeight(2); // Set the line thickness for visibility
            float adjustedX = x - cameraX; // Adjust position by camera offset
            parent.line(adjustedX, 0, adjustedX, parent.height); // Draw a vertical line from top to bottom
        }
    }

    private boolean lineIntersectsRect(float x1, float y1, float x2, float y2, BoundingBox box) {
        // Check if any of the rectangle's vertical boundaries are within the x range of
        // the line
        return (box.getLocation().x <= x1 && box.getLocation().x + box.getWidth() >= x1)
                && (y1 <= box.getLocation().y + box.getHeight() && y2 >= box.getLocation().y);
    }
}
