package cs4303.p4.map;

import cs4303.p4.physics.BoundingBox;
import cs4303.p4.physics.Collidable;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

// Structure for a graph node (platform)
//extends collidable to make the graph usable for collision detection
public class Node extends Collidable{
    int x, y; // Position of the platform
    List<Node> neighbors; // Adjacent platforms within jump range

    Node(int x, int y,int cellSize) {
        //creates a collidable object with the pixel location of the platform
        super(x,y);
        //creates a bounding box for the node
        BoundingBox box = new BoundingBox(new PVector(x*cellSize,y*cellSize),cellSize,cellSize);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(box);
        super.setBounds(b);

        this.x = x;
        this.y = y;
        neighbors = new ArrayList<>();
    }

    // Add a neighbor node if it's within jump range
    void addNeighbor(Node other) {
        if (Math.abs(this.x - other.x) <= 2 && Math.abs(this.y - other.y) <= 2) { // Assuming jump range of 2 cells
            neighbors.add(other);
        }
    }
}