package cs4303.p4.map;

import java.util.ArrayList;
import java.util.List;

import cs4303.p4._util.Constants;
import lombok.Getter;
import processing.core.PApplet;

// TODO: make tile object with literally just cell size of tile and draw method
// Implement sliding window

// Assumes player can jump 2 "cells"
public class Level {
    private int cellSize; // Size of each cell in the grid
    private int gridWidth;
    private int gridHeight;
    private Tile[][] levelGrid;
    private PApplet parent;

    private float cameraX; // Camera offset on the X-axis (now float)
    private final float cameraSpeed = 0.3f; // Speed at which the camera moves to the right

    private @Getter List<Node> nodes;

    // Constructor
    public Level(PApplet p) {
        this.cellSize = Constants.TILE_SIZE;
        this.parent = p;
        this.gridWidth = (p.width / cellSize) * 3;
        this.gridHeight = p.height / cellSize;
        levelGrid = new Tile[gridHeight][gridWidth];
        initializeGrid();
        generateLevel();
    }

    public float getCameraX() {
        return cameraX;
    }

    private void initializeGrid() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                levelGrid[y][x] = new Tile(cellSize); // Initialize each Tile object
            }
        }
    }

    public boolean isPlatformAt(float x, float y) {
        int gridX = (int) (x / cellSize);
        int gridY = (int) (y / cellSize);

        // Check bounds
        if (gridX < 0 || gridX >= gridWidth || gridY < 0 || gridY >= gridHeight) {
            return false;
        }

        return levelGrid[gridY][gridX].getType() == TileType.PLATFORM;
    }

    void generateLevel() {
        int lastPlatformX = 0; // Keep track of the last platform's x-coordinate

        // Ensure the player can always progress by creating a base path
        for (int x = 1; x < gridWidth - 1; x++) {
            // Every few cells, guarantee a platform within jump reach
            if ((x - lastPlatformX) >= 2) { // Ensure a platform is placed every two cells
                levelGrid[gridHeight - 3][x].setType(TileType.PLATFORM);
                lastPlatformX = x; // Update the last platform position
            }
        }

        // Additional random platforms for complexity (could overlap the base path)
        for (int x = 1; x < gridWidth - 1; x++) {
            for (int y = gridHeight - 5; y > 1; y -= 2) { // Avoid interfering with the guaranteed path
                if (parent.random(1) < 0.1) { // Less frequent random platforms
                    levelGrid[y][x].setType(TileType.PLATFORM);
                }
            }
        }

        // Create the platforms that can be jumped onto from below
        for (int x = 1; x < gridWidth - 1; x++) {
            // Start from ground level and check upwards, ignore top two rows
            for (int y = gridHeight - 1; y > 1; y -= 2) {
                if (parent.random(1) < 0.3) { // With a 30% probability, place a platform
                    levelGrid[y][x].setType(TileType.PLATFORM);

                    // Ensure there is a platform within 2 spaces to the left or right for
                    // reversibility
                    if (x > 1 && levelGrid[y][x - 2].getType() == TileType.EMPTY && parent.random(1) < 0.5) {
                        levelGrid[y][x - 2].setType(TileType.PLATFORM);
                    }
                    if (x < gridWidth - 2 && levelGrid[y][x + 2].getType() == TileType.EMPTY
                            && parent.random(1) < 0.5) {
                        levelGrid[y][x + 2].setType(TileType.PLATFORM);
                    }
                }
            }
        }

        // Add enemies or treasures: placeholder
        for (int i = 0; i < 10; i++) {
            int randX = (int) parent.random(gridWidth);
            int randY = (int) parent.random(gridHeight - 2); // Avoid placing items on the ground
            if (levelGrid[randY][randX].getType() == TileType.EMPTY) {
                levelGrid[randY][randX].setType(TileType.ENTITY); // Enemy or treasure
            }
        }
    }

    public void buildGraph() {
        nodes = new ArrayList<>(); // Initialize the graph nodes list

        // Iterate over levelGrid and add platforms as nodes
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (levelGrid[y][x].getType() == TileType.PLATFORM) {
                    Node newNode = new Node(x, y,cellSize);
                    // Connect to existing nodes
                    for (Node node : nodes) {
                        node.addNeighbor(newNode);
                        newNode.addNeighbor(node);
                    }
                    nodes.add(newNode); // Add the new node to the list
                }
            }
        }
    }

    // Call this method in main game loop
    public void updateCamera() {
        // Increment the camera position by a fraction of the camera speed each frame
        cameraX += cameraSpeed;

        // Ensure the camera does not go past the end of the level
        cameraX = Math.min(cameraX, (gridWidth * cellSize) - parent.width);
    }

    // Modify the draw method to offset tiles based on the camera position
    public void draw() {
        // Only draw the part of the level that's currently within the camera's view
        int startCol = (int) (cameraX / cellSize);
        int endCol = Math.min(startCol + parent.width / cellSize, gridWidth);

        for (int y = 0; y < gridHeight; y++) {
            for (int x = startCol; x < endCol; x++) {
                // Calculate the on-screen x position, adjusted for the camera's current
                // position using floating-point arithmetic
                float screenX = x * cellSize - cameraX;

                if (levelGrid[y][x].getType() != TileType.EMPTY) {
                    // Use linear interpolation to smoothly transition between cell positions
                    int cellX = (int) (screenX / cellSize);
                    float lerp = (screenX / cellSize) - cellX;
                    levelGrid[y][x].draw(parent, cellX, y, lerp);
                }
            }
        }
    }
}
