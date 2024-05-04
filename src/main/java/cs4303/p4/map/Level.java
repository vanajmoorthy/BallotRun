package cs4303.p4.map;

import cs4303.p4._util.Constants;
import cs4303.p4.entities.Player;
import lombok.Getter;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int cellSize;
    private int gridWidth;
    private int gridHeight;
    private Tile[][] levelGrid;
    private PApplet parent;

    @Getter
    private float cameraX;
    @Getter
    private float cameraSpeed = 2.5f;
    @Getter
    private boolean cameraMovingRight = true;
    @Getter
    private boolean cameraStill = false;

    @Getter
    private List<Node> nodes;
    private Player player;

    private float cameraDelayTime = 3.5f; // delay in seconds before camera starts moving
    private float cameraDelayElapsed = 0; // time elapsed since the level started
    @Getter
    private boolean cameraDelayCompleted = false; // has the delay completed?

    public Level(PApplet p, float difficultyFactor, Player player) {
        this.cellSize = Constants.TILE_SIZE;
        this.parent = p;
        this.gridWidth = (p.width / cellSize) * 2;
        this.gridHeight = p.height / cellSize;
        levelGrid = new Tile[gridHeight][gridWidth];
        initializeGrid();
        this.player = player;
        generateLevel(difficultyFactor);
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

    void generateLevel(float difficultyFactor) {
        int lastPlatformX = 0; // Keep track of the last platform's x-coordinate

        // Ensure the player can always progress by creating a base path
        for (int x = 1; x < gridWidth - 1; x++) {
            // Every few cells, guarantee a platform within jump reach
            if ((x - lastPlatformX) >= 2 + (int) (difficultyFactor * 2)) { // Increase the gap between platforms based
                                                                           // on difficulty
                levelGrid[gridHeight - 3][x].setType(TileType.PLATFORM);
                lastPlatformX = x; // Update the last platform position
            }
        }

        // Additional random platforms for complexity (could overlap the base path)
        for (int x = 1; x < gridWidth - 1; x++) {
            for (int y = gridHeight - 5; y > 1; y -= 2) {
                if (parent.random(1) < 0.1 / difficultyFactor) { // Decrease the probability of random platforms based
                                                                 // on difficulty
                    int platformWidth = (int) (2 + parent.random(4 - (int) (difficultyFactor * 2))); // Vary platform
                                                                                                     // width based on
                                                                                                     // difficulty
                    for (int i = 0; i < platformWidth; i++) {
                        if (x + i < gridWidth) {
                            levelGrid[y][x + i].setType(TileType.PLATFORM);
                        }
                    }
                }
            }
        }

        // Create the platforms that can be jumped onto from below
        for (int x = 1; x < gridWidth - 1; x++) {
            // Start from ground level and check upwards, ignore top two rows
            for (int y = gridHeight - 1; y > 1; y -= 2) {
                if (parent.random(1) < 0.3 / difficultyFactor) { // Decrease the probability of platforms based on
                                                                 // difficulty
                    int platformHeight = (int) (1 + parent.random(4 - (int) (difficultyFactor * 2))); // Vary platform
                                                                                                      // height based on
                                                                                                      // difficulty
                    for (int i = 0; i < platformHeight; i++) {
                        if (y - i > 0) {
                            levelGrid[y - i][x].setType(TileType.PLATFORM);
                        }
                    }

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

        // Find the highest platform in the leftmost column
        int highestPlatformY = -1;
        for (int y = 0; y < gridHeight; y++) {
            if (levelGrid[y][0].getType() == TileType.PLATFORM) {
                highestPlatformY = y;
                break; // Break at the first platform found from the top down
            }
        }

        // Set the start tile
        if (highestPlatformY != -1) {
            levelGrid[highestPlatformY][0].setType(TileType.START);
        } else {
            // If no platform exists, choose a random position in the leftmost column
            int randomY = PApplet.floor(parent.random(2, gridHeight));
            levelGrid[randomY][0].setType(TileType.START);
            System.out.println("No platform found in the leftmost column. Setting start tile at random position.");
        }

        // Finding the rightmost column that contains at least one platform
        int rightMostPlatformY = -1;
        for (int y = gridHeight - 1; y >= 0; y--) {
            if (levelGrid[y][gridWidth - 1].getType() == TileType.PLATFORM) {
                rightMostPlatformY = y - 1; // Get the row just above the found platform
                break;
            }
        }

        // Place the ballot on the lowest platform in the rightmost column, if available
        if (rightMostPlatformY != -1) {
            levelGrid[rightMostPlatformY][gridWidth - 1].setType(TileType.BALLOT);

            // Ensure there is no platform directly above the ballot for
            // accessibility
            if (rightMostPlatformY > 0) {
                levelGrid[rightMostPlatformY - 1][gridWidth - 1].setType(TileType.EMPTY);
            }

            // Add additional tiles for accessibility
            if (rightMostPlatformY + 1 < gridHeight) {
                levelGrid[rightMostPlatformY + 1][gridWidth - 1].setType(TileType.PLATFORM);
                // Additional platforms to the left and right, if space allows
                if (gridWidth - 2 >= 0) {
                    levelGrid[rightMostPlatformY + 1][gridWidth - 2].setType(TileType.PLATFORM);
                }
                if (gridWidth < gridWidth) {
                    levelGrid[rightMostPlatformY + 1][gridWidth].setType(TileType.PLATFORM);
                }
            }
        } else {
            // Fallback if no platform is available in the last column
            // Place the ballot on a default position or handle the absence of a suitable
            // location
            int ballotX = gridWidth - 1; // Random position 4 tiles before the end
            int ballotY = gridHeight - 2; // Second last row, assuming it's accessible
            if (ballotX < gridWidth) {
                levelGrid[ballotY][ballotX].setType(TileType.BALLOT);
                levelGrid[ballotY - 1][ballotX].setType(TileType.EMPTY);
                levelGrid[ballotY][ballotX - 1].setType(TileType.EMPTY);
                levelGrid[ballotY - 1][ballotX - 1].setType(TileType.EMPTY);

                for (int i = 1; i <= 3; i++) { // Create a path of platforms leading to the ballot
                    if (ballotX - i > 0) {
                        levelGrid[ballotY + 1][ballotX - i].setType(TileType.PLATFORM);
                    }
                }
            }
            System.out.println("No suitable platform found in the rightmost column for the ballot.");
        }

        // Add enemies, etc
        int numObstacles = (int) (10 * difficultyFactor); // Increase the number of obstacles based on difficulty
        for (int i = 0; i < numObstacles; i++) {
            int randX = (int) parent.random(gridWidth);
            int randY = (int) parent.random(gridHeight - 2); // Avoid placing items on the ground
            if (levelGrid[randY][randX].getType() == TileType.EMPTY) {
                levelGrid[randY][randX].setType(TileType.ENTITY); // Enemy, hazard, or treasure
            }
        }
    }

    public void buildGraph() {
        nodes = new ArrayList<>(); // Initialize the graph nodes list

        // Iterate over levelGrid and add platforms as nodes
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (levelGrid[y][x].getType() == TileType.PLATFORM || levelGrid[y][x].getType() == TileType.START) {
                    Node newNode = new Node(x, y, cellSize);
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

    // Method to draw the graph
    public void drawGraph(PApplet sketch) {
        sketch.stroke(255, 0, 0); // Set line color to red for visibility
        sketch.strokeWeight(2); // Set line thickness

        for (Node node : nodes) {
            float nodeScreenX = node.getX() * cellSize - cameraX;
            float nodeScreenY = node.getY() * cellSize;

            // Draw connections to neighbors
            for (Node neighbor : node.getNeighbors()) {
                float neighborScreenX = neighbor.getX() * cellSize - cameraX;
                float neighborScreenY = neighbor.getY() * cellSize;

                // Draw a line between the current node and its neighbor
                sketch.line(nodeScreenX + cellSize / 2, nodeScreenY + cellSize / 2,
                        neighborScreenX + cellSize / 2, neighborScreenY + cellSize / 2);
            }
        }
    }

    // Call this method in main game loop
    public void updateCamera(float deltaTime) { // Assume deltaTime is passed in seconds
        // Update the elapsed time only if the delay is not completed

        if (!cameraDelayCompleted) {
            cameraDelayElapsed += deltaTime;
            if (cameraDelayElapsed >= cameraDelayTime) {
                cameraDelayCompleted = true; // Set the delay as completed
            }

        }

        // Proceed with camera movement only if the delay is completed
        if (cameraDelayCompleted) {
            if (cameraMovingRight) {
                for (Node n : nodes) {
                    n.updateBoundingBoxes(cameraSpeed, cameraMovingRight, cameraStill);
                }
                if (cameraX < (gridWidth * cellSize) - parent.width) {
                    cameraX += cameraSpeed; // Move camera right until the end
                } else {
                    // Hold the camera at the end of the level
                    cameraX = (gridWidth * cellSize) - parent.width;
                    // Check if player has reached the last accessible tile
                    cameraStill = true;
                    if (playerOnBallot()) {
                        System.out.println("on ballot!");
                        cameraStill = false;
                        cameraMovingRight = false; // Reverse the camera direction

                    }
                }
            } else {
                if (cameraX > 0) {
                    cameraX -= cameraSpeed; // Move camera left until the start
                    for (Node n : nodes) {
                        n.updateBoundingBoxes(cameraSpeed, cameraMovingRight, cameraStill);
                    }
                } else {
                    // Hold the camera at the start of the level
                    cameraX = 0;
                }
            }
        }
    }

    private boolean playerOnBallot() {
        float playerX = player.getLocation().x;
        float playerY = player.getLocation().y;

        // Convert player position to grid coordinates
        int gridX = (int) ((playerX + cameraX) / cellSize);
        int gridY = (int) (playerY / cellSize);

        // Debug output
        System.out.println("Player grid position: " + gridX + ", " + gridY);

        // Check grid boundaries
        if (gridX >= 0 && gridX < gridWidth && gridY >= 0 && gridY < gridHeight) {
            return levelGrid[gridY][gridX].getType() == TileType.BALLOT;
        }
        return false; // Return false if out of bounds
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
