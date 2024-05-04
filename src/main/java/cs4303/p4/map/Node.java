package cs4303.p4.map;

import cs4303.p4.physics.BoundingBox;
import cs4303.p4.physics.Collidable;
import lombok.Getter;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Node extends Collidable {
    @Getter
    int x, y; // Position of the platform
    @Getter
    List<Node> neighbors; // Adjacent platforms within jump range
    private int cellSize;

    Node(int x, int y, int cellSize) {
        super(x, y);
        this.cellSize = cellSize;
        BoundingBox box = new BoundingBox(new PVector(x * cellSize, y * cellSize), cellSize, cellSize);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(box);
        super.setBounds(b);
        this.x = x;
        this.y = y;
        neighbors = new ArrayList<>();
    }

    void addNeighbor(Node other) {
        if (Math.abs(this.x - other.x) <= 2 && Math.abs(this.y - other.y) <= 2) {
            neighbors.add(other);
        }
    }

    public PVector getAdjustedPosition(float cameraSpeed) {
        return new PVector(cameraSpeed, 0);
    }

    public void updateBoundingBoxes(float cameraSpeed, boolean cameraMovingRight, boolean cameraStill) {
        System.out.println("moving right: " + cameraMovingRight);
        System.out.println("still: " + cameraStill);
        if (cameraMovingRight && !cameraStill) {
            PVector adjustedPos = getAdjustedPosition(cameraSpeed);
            for (BoundingBox b : getBounds()) {
                PVector currLocation = b.getLocation().copy();

                PVector newLocation = PVector.sub(currLocation, adjustedPos);
                b.setLocation(newLocation);
            }
        } else if (cameraStill) {

        } else {
            PVector adjustedPos = getAdjustedPosition(-cameraSpeed);
            for (BoundingBox b : getBounds()) {
                PVector currLocation = b.getLocation().copy();
                System.out.println(currLocation);
                System.out.println(adjustedPos);

                PVector newLocation = PVector.sub(currLocation, adjustedPos);
                b.setLocation(newLocation);
            }
        }

    }
}