package cs4303.p4.map;

import cs4303.p4.Constants;
import processing.core.PApplet;
import processing.core.PGraphics;

// TODO: make tile object with literally just cell size of tile and draw method
// Implement sliding window

// Assumes player can jump 2 "cells"
public class Level {
    private int cellSize; // Size of each cell in the grid
    private int gridWidth;
    private int gridHeight;
    private Tile[][] levelGrid;
    private PGraphics graphics;
    private PApplet parent;
    private int cameraX; // Camera offset on the X-axis
    private final int cameraSpeed = 1; // Speed at which the camera moves to the right

    // Constructor
    public Level(PGraphics g, PApplet p) {
        this.cellSize = Constants.TILE_SIZE;
        this.graphics = g;
        this.parent = p;
        this.gridWidth = (p.width / cellSize) * 2;
        this.gridHeight = p.height / cellSize;
        levelGrid = new Tile[gridHeight][gridWidth];
        initializeGrid();
        generateLevel();
    }

    private void initializeGrid() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                levelGrid[y][x] = new Tile(cellSize, graphics); // Initialize each Tile object
            }
        }
    }

    void generateLevel() {
        // Create the solid ground on the bottom
        for (int x = 0; x < gridWidth; x++) {
            levelGrid[gridHeight - 1][x].setType(TileType.PLATFORM);
        }

        // Create the platforms that can be jumped onto from below
        for (int x = 1; x < gridWidth - 1; x++) {
            for (int y = gridHeight - 3; y > 0; y -= 2) { // Start from second row above the ground and check upwards
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

    // Call this method in main game loop
    public void updateCamera() {
        // Increment the camera position by the camera speed each frame
        // Ensure the camera does not go past the end of the level
        cameraX = Math.min(cameraX + cameraSpeed, (gridWidth * cellSize) - parent.width);
    }

    // Modify the draw method to offset tiles based on the camera position
    public void draw() {
        // Only draw the part of the level that's currently within the camera's view
        int startCol = cameraX / cellSize;
        int endCol = Math.min(startCol + parent.width / cellSize, gridWidth);

        for (int y = 0; y < gridHeight; y++) {
            for (int x = startCol; x < endCol; x++) {
                // Calculate the on-screen x position, adjusted for the camera's current
                // position
                int screenX = x * cellSize - cameraX;
                if (levelGrid[y][x].getType() != TileType.EMPTY) {
                    levelGrid[y][x].draw(screenX / cellSize, y); // Use Tile's draw method with adjusted screenX
                }
            }
        }
    }
}
