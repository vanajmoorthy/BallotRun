package cs4303.p4;

import lombok.Getter;
import processing.core.PApplet;

// Assumes player can jump 2 "cells"
@Getter
public class Level {
    private int cellSize; // Size of each cell in the grid
    private int gridWidth;
    private int gridHeight;
    private int[][] levelGrid;
    private PApplet parent;

    // Constructor
    public Level(int cellSize, PApplet p) {
        this.cellSize = cellSize;
        this.parent = p;
        this.gridWidth = p.width / cellSize;
        this.gridHeight = p.height / cellSize;
        this.levelGrid = new int[gridHeight][gridWidth];
        initializeGrid();
        generateLevel();
    }

    // Initialize the level grid
    private void initializeGrid() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                levelGrid[y][x] = 0; // 0 represents an empty space
            }
        }
    }

    public void generateLevel() {
        // Create the solid ground on the bottom
        for (int x = 0; x < gridWidth; x++) {
            levelGrid[gridHeight - 1][x] = 1;
        }

        // Create the platforms that can be jumped onto from below
        for (int x = 1; x < gridWidth - 1; x++) {
            for (int y = gridHeight - 3; y > 0; y -= 2) { // Start from second row above the ground and check upwards
                if (parent.random(1) < 0.3) { // With a 30% probability, place a platform
                    levelGrid[y][x] = 1;

                    // Ensure there is a platform within 2 spaces to the left or right for
                    // reversibility
                    if (x > 1 && levelGrid[y][x - 2] == 0 && parent.random(1) < 0.5) {
                        levelGrid[y][x - 2] = 1;
                    }
                    if (x < gridWidth - 2 && levelGrid[y][x + 2] == 0 && parent.random(1) < 0.5) {
                        levelGrid[y][x + 2] = 1;
                    }
                }
            }
        }

        // Add enemies or treasures: placeholder
        for (int i = 0; i < 10; i++) {
            int randX = (int) parent.random(gridWidth);
            int randY = (int) parent.random(gridHeight - 2); // Avoid placing items on the ground
            if (levelGrid[randY][randX] == 0) {
                levelGrid[randY][randX] = 2; // Enemy or treasure
            }
        }
    }

    // Draw the level
    public void draw(PApplet sketch) {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (levelGrid[y][x] == 1) {
                    // Draw platform
                    sketch.fill(0xFFFFFFFF);
                    sketch.stroke(0xFF000000);
                    sketch.rect(x * 40, y * 40, 40, 40);
                } else if (levelGrid[y][x] == 2) {
                    // Draw enemy or treasure: placeholder
                    sketch.fill(0xFF555555);
                    sketch.stroke(0xFF000000);
                    sketch.ellipse(x * 40 + 20, y * 40 + 20, 20, 20);
                }
            }
        }
    }
}
