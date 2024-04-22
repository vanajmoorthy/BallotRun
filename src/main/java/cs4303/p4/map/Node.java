package cs4303.p4.map;

import java.util.ArrayList;
import java.util.List;

// Structure for a graph node (platform)
public class Node {
    int x, y; // Position of the platform
    List<Node> neighbors; // Adjacent platforms within jump range

    Node(int x, int y) {
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