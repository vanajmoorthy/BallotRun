package cs4303.p4.map;

import cs4303.p4._util.Colors;
import processing.core.PApplet;
import processing.core.PImage; // Import the PImage class

public class Tile {
    private int cellSize;
    private TileType type; // Type of the tile (0: empty, 1: platform, 2: enemy/treasure)
    private PImage platformImage; // Image for platform tile

    // Constructor
    public Tile(int cellSize, PImage platformImage) {
        this.cellSize = cellSize;
        this.type = TileType.EMPTY; // Default to empty
        this.platformImage = platformImage;
    }

    // Setter for the tile type
    public void setType(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    // Draw method for the tile
    public void draw(PApplet sketch, int x, int y, float lerp) {
        if (type == TileType.PLATFORM) {
            sketch.image(platformImage, x * cellSize + lerp * cellSize, y * cellSize, cellSize, cellSize);
        } else if (type == TileType.TEST) {
            sketch.stroke(Colors.red.light);
            sketch.strokeWeight(1.5f);
            sketch.fill(Colors.red.lighter);
            sketch.rect(x * cellSize + lerp * cellSize, y * cellSize, cellSize, cellSize, 2);
        }
    }
}
