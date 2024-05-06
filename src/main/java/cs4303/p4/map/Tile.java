package cs4303.p4.map;

import cs4303.p4._util.Colors;
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
    public void draw(PApplet sketch, int x, int y, float lerp) {
        if (type == TileType.PLATFORM) {
            sketch.stroke(Colors.darkGray.light);
            sketch.strokeWeight(1.5f);
            sketch.fill(Colors.darkGray.lighter);
            sketch.rect(
                x * cellSize + lerp * cellSize,
                y * cellSize,
                cellSize,
                cellSize,
                2
            );
        }
    }
}
