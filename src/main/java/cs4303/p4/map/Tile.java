package cs4303.p4.map;

import processing.core.PApplet;

public class Tile {
    private int cellSize;
    private TileType type; // Type of the tile (0: empty, 1: platform, 2: enemy/treasure)

    // Constructor
    public Tile(int cellSize) {
        this.cellSize = cellSize;
        this.type = TileType.EMPTY; // Default to empty
    }

    // Setter for the tile type
    public void setType(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    // Draw method for the tile
    public void draw(PApplet sketch, int x, int y) {
        // Draw based on type
        switch (type) {
            case PLATFORM:
                // Draw platform
                sketch.rect(x * cellSize, y * cellSize, cellSize, cellSize);
                break;
            case ENTITY:
                // Draw enemy or treasure
                sketch.ellipse(x * cellSize + cellSize / 2, y * cellSize + cellSize / 2, cellSize / 2, cellSize / 2);
                break;
            default:
                break;
        }
    }
}
