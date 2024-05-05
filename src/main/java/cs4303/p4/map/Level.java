package cs4303.p4.map;

import cs4303.p4._util.gui.TimedText;
import cs4303.p4._util.Constants;
import cs4303.p4.entities.BallotBox;
import cs4303.p4.entities.Entity;
import cs4303.p4.entities.Entrance;
import cs4303.p4.entities.Player;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

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
    private BallotBox ballotBox;
    private Entrance entrance;
    @Getter
    private final List<Entity> entities;

    private float cameraDelayTime = 3.5f; // delay in seconds before camera starts moving
    private float cameraDelayElapsed = 0; // time elapsed since the level started
    @Getter
    private boolean cameraDelayCompleted = false;

    @Getter
    private float postBallotDelayTime = 3.0f; // Delay in seconds before camera moves back
    private float postBallotDelayElapsed = 0; // Time elapsed after reaching the ballot
    private boolean ballotReached = false;

    private boolean gameOver = false;

    private TimedText ballotMessage;
    private TimedText startingMessage;

    public Level(PApplet p, float difficultyFactor, Player player) {
        this.cellSize = Constants.TILE_SIZE;
        this.parent = p;
        this.gridWidth = (p.width / cellSize) * 2;
        this.gridHeight = p.height / cellSize;
        levelGrid = new Tile[gridHeight][gridWidth];
        initializeGrid();
        this.player = player;
        this.entities = new ArrayList<Entity>();
        generateLevel(difficultyFactor);
        this.ballotMessage = new TimedText("Now go back to your start position", postBallotDelayTime,
                new PVector(Constants.Screen.width / 2, Constants.Screen.height / 2));

        this.startingMessage = new TimedText("Starting...", cameraDelayTime,
                new PVector(parent.width / 2, parent.height / 2));
        this.startingMessage.start();
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
        // Spawn a platform if leftmost column has no tiles
        if (highestPlatformY == -1) {
            int randomY = PApplet.floor(parent.random(2, gridHeight));
            levelGrid[randomY][0].setType(TileType.PLATFORM);
            highestPlatformY = randomY;
            System.out.println("No platform found in the leftmost column. Setting start tile at random position.");
        }
        // Spawn entrance
        Entrance entrance = new Entrance(
                Constants.TILE_SIZE / 2,
                (highestPlatformY) * Constants.TILE_SIZE);
        entrance.getLocation().x -= entrance.getBounds().get(0).getWidth() / 2;
        entrance.getLocation().y -= entrance.getBounds().get(0).getHeight();
        entities.add(entrance);
        this.entrance = entrance;

        // Find the highest platform in the rightmost column
        highestPlatformY = -1;
        for (int y = 0; y < gridHeight; y++) {
            if (levelGrid[y][gridWidth - 1].getType() == TileType.PLATFORM) {
                highestPlatformY = y;
                break; // Break at the first platform found from the top down
            }
        }
        // Spawn a platform if rightmost column has no tiles
        if (highestPlatformY == -1) {
            int randomY = PApplet.floor(parent.random(2, gridHeight));
            levelGrid[randomY][gridWidth - 1].setType(TileType.PLATFORM);
            highestPlatformY = randomY;
            System.out.println("No platform found in the rightmost column. Setting start tile at random position.");
        }
        // Spawn ballot box
        BallotBox ballotBox = new BallotBox(
                (gridWidth - 1) * Constants.TILE_SIZE + Constants.TILE_SIZE / 2,
                (highestPlatformY) * Constants.TILE_SIZE);
        ballotBox.getLocation().x -= ballotBox.getBounds().get(0).getWidth() / 2;
        ballotBox.getLocation().y -= ballotBox.getBounds().get(0).getHeight();
        entities.add(ballotBox);
        this.ballotBox = ballotBox;

        // // Add enemies, etc
        // int numObstacles = (int) (10 * difficultyFactor); // Increase the number of
        // obstacles based on difficulty
        // for (int i = 0; i < numObstacles; i++) {
        // int randX = (int) parent.random(gridWidth);
        // int randY = (int) parent.random(gridHeight - 2); // Avoid placing items on
        // the ground
        // if (levelGrid[randY][randX].getType() == TileType.EMPTY) {
        // levelGrid[randY][randX].setType(TileType.ENTITY); // Enemy, hazard, or
        // treasure
        // }
        // }
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

    public void update(float deltaTime) {
        if (!gameOver) {
            if (!cameraDelayCompleted) {
                cameraDelayElapsed += deltaTime;
                startingMessage.update(deltaTime);
                if (cameraDelayElapsed >= cameraDelayTime) {
                    cameraDelayCompleted = true;
                    startingMessage.active = false; // Ensure the starting message is deactivated
                }
            }

            if (cameraDelayCompleted) {
                updateCamera(deltaTime);
            }

            // Check if the player is on the ballot and update the ballot message
            // accordingly
            if (playerOnBallot()) {
                if (!ballotMessage.isActive()) {
                    ballotMessage.reset();
                    ballotMessage.start();
                }
            }

            ballotMessage.update(deltaTime); // Update the ballot message
            checkPlayerPosition();
        }
    }

    private void checkPlayerPosition() {
        if (player.isOffMap(cameraX, gridWidth, cellSize, cameraMovingRight)) {
            gameOver = true;
            // restartLevel();
            player.setHealth(0);
        }
    }

    private void drawRestartButton() {
        parent.fill(255, 0, 0);
        parent.rect(parent.width - 110, 10, 100, 30);
        parent.fill(255);
        parent.textAlign(PApplet.CENTER);
        parent.text("Restart", parent.width - 60, 30);
    }

    public void checkRestartButtonPressed(float mouseX, float mouseY) {
        if (mouseX >= parent.width - 80 && mouseX <= parent.width - 10 &&
                mouseY >= 10 && mouseY <= 40) {
            System.out.println("pressed!");

            restartLevel();
        }
    }

    private void restartLevel() {
        for (Node node : nodes) {
            node.resetBoundingBox(); // Reset each node's bounding boxes to the start positions
        }

        player.resetPlayer(); // Reset player's position
        player.resetBoundingBox();

        for (Entity entity : entities) {
            entity.getLocation().x += cameraX;
        }

        cameraX = 0;
        cameraMovingRight = true;
        cameraStill = false;

        gameOver = false; // Reset game over state
        cameraDelayElapsed = 0; // Reset camera delay elapsed time
        cameraDelayCompleted = false; // Reset camera delay completed flag
        updateCamera(0); // Call with deltaTime 0 or a small value to initialize the state.

        // Reset and start the starting message
        startingMessage.reset();
        startingMessage.start();

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

                        cameraMovingRight = false;
                        ballotReached = true; // Set the flag to indicate ballot reached
                    }
                }
            } else {
                if (ballotReached) {
                    postBallotDelayElapsed += deltaTime;
                    if (postBallotDelayElapsed >= postBallotDelayTime) {
                        cameraStill = false;
                        ballotReached = false; // Reset the flag
                        postBallotDelayElapsed = 0; // Reset the elapsed time
                    }
                } else {
                    if (cameraX > 0) {
                        cameraX -= cameraSpeed; // Move camera left until the start
                        for (Node n : nodes) {
                            n.updateBoundingBoxes(cameraSpeed, cameraMovingRight, cameraStill);
                        }
                    } else {
                        cameraX = 0;
                    }
                }
            }
        }
    }

    public boolean playerOnBallot() {
        return player.getLocation().dist(ballotBox.getLocation()) < 20;
    }

    public boolean playerOnEntrance() {
        return player.getLocation().dist(entrance.getLocation()) < 20;
    }

    public void draw() {
        if (!gameOver) {
            drawMap();
            if (!cameraDelayCompleted && startingMessage.isActive()) {
                startingMessage.draw(parent);
            }

            if (ballotMessage.isActive()) { // Check if ballot message is active before drawing
                ballotMessage.draw(parent);
            }
        }
        drawRestartButton();
    }

    // Modify the draw method to offset tiles based on the camera position
    public void drawMap() {

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
