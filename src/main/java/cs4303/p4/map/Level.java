package cs4303.p4.map;

import cs4303.p4._util.gui.TimedText;
import cs4303.p4.Game;
import cs4303.p4._util.Constants;
import cs4303.p4.entities.BallotBox;
import cs4303.p4.entities.Entity;
import cs4303.p4.entities.Entrance;
import cs4303.p4.entities.Laser;
import cs4303.p4.entities.Player;
import cs4303.p4.physics.BoundingBox;
import cs4303.p4.states.GameStateGameplay;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Level {
    private int cellSize;

    public static int gridWidth;

    public static int gridHeight;

    static @Getter private Tile[][] levelGrid;
    private PApplet parent;

    @Getter
    private float cameraX;
    @Getter
    private float cameraSpeed = 0.9f;
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
    @Getter
    private final List<Entity> entranceAndBallot;

    private float cameraDelayTime = 3.0f; // delay in seconds before camera starts moving
    private float cameraDelayElapsed = 0; // time elapsed since the level started
    @Getter
    private boolean cameraDelayCompleted = false;

    @Getter
    private float postBallotDelayTime = 3.0f; // Delay in seconds before camera moves back
    private float postBallotDelayElapsed = 0; // Time elapsed after reaching the ballot
    private boolean ballotReached = false;

    private TimedText ballotMessage;
    private TimedText startingMessage;
    private List<Laser> lasers;
    private int healthIncrement;

    public Level(PApplet p, float difficultyFactor, Player player, int width, GameStateGameplay gamePlay) {
        this.cellSize = Constants.TILE_SIZE;
        this.parent = p;

        Level.gridWidth = (p.width / cellSize) * Math.max(width, 2);
        Level.gridHeight = (Constants.Screen.height - Constants.Screen.GamePlay.infoPanelHeight) / cellSize;

        levelGrid = new Tile[gridHeight][gridWidth];

        initializeGrid();
        this.player = player;
        this.entities = new ArrayList<Entity>();
        this.entranceAndBallot = new ArrayList<Entity>();

        generateLevel(difficultyFactor);
        this.ballotMessage = new TimedText("Now go back to your start position", postBallotDelayTime,
                new PVector(Constants.Screen.width / 2, Constants.Screen.height / 2));

        this.startingMessage = new TimedText("Starting...", cameraDelayTime,
                new PVector(parent.width / 2, parent.height / 2));
        this.startingMessage.start();

        lasers = new ArrayList<>();
        generateLasers();
        this.healthIncrement = gamePlay.getHealthIncrement();

    }

    private void generateLasers() {
        int numLasers = gridWidth / 10; // Number of lasers to add
        int buffer = 10; // Number of cells to skip from the start
        int minSpacing = 5; // Minimum number of cells between each laser

        Random rand = new Random();
        List<Integer> laserPositions = new ArrayList<>(); // Track the grid positions of lasers

        for (int i = 0; i < numLasers; i++) {
            int x;
            do {
                // Generate a position ensuring it's beyond the buffer and spaced by minSpacing
                x = (rand.nextInt(gridWidth - buffer - minSpacing) + buffer);
            } while (!isPositionValid(x, laserPositions, minSpacing));

            laserPositions.add(x); // Store the position
            lasers.add(new Laser(parent, x * cellSize, 0)); // Create a new laser at this position
        }
    }

    private boolean isPositionValid(int currentPosition, List<Integer> positions, int minSpacing) {
        // Check if the new position is at least minSpacing cells away from existing
        // lasers
        for (int pos : positions) {
            if (Math.abs(pos - currentPosition) < minSpacing) {
                return false;
            }
        }
        return true;
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

        // Reset the grid with empty tiles first
        initializeGrid();

        // Dynamic parameters based on difficulty
        int maxIslands = 40; // More islands at easier levels
        int minIslands = 5; // Fewer islands at harder levels
        int numIslands = (int) PApplet.map(difficultyFactor, 0, 5, minIslands, maxIslands);

        int numSectionsX = 3; // More sections for higher difficulty
        int numSectionsY = 2; // More vertical sections as difficulty
                              // increases

        int sectionWidth = gridWidth / numSectionsX;
        int sectionHeight = gridHeight / numSectionsY;
        ArrayList<PVector> islandCenters = new ArrayList<>();
        // Generate islands within each section
        for (int i = 0; i < numIslands; i++) {
            int minIslandWidth = 3;
            int maxIslandWidth = 5;
            int islandWidth = (int) parent.random(minIslandWidth, maxIslandWidth);

            // Choose a random section
            int sectionIndex = i % (numSectionsX * numSectionsY);
            int sectionX = sectionIndex % numSectionsX;
            int sectionY = sectionIndex % numSectionsY;

            // Calculate random position within the chosen section
            int xPosition = (int) parent.random(sectionX * sectionWidth, (sectionX + 1) * sectionWidth - islandWidth);
            int yPosition = (int) parent.random(sectionY * sectionHeight, (sectionY + 1) * sectionHeight - 1);
            islandCenters.add(new PVector(xPosition + islandWidth / 2, yPosition));

            yPosition = (int) PApplet.map(yPosition, 0, 10, 0, 12);
            // Gaussian distribution parameters
            float sigma = islandWidth / 3.0f; // Controls the spread of the bell curve
            float mean = xPosition + islandWidth / 2.0f; // Center of the island

            for (int x = xPosition; x < xPosition + islandWidth; x++) {
                levelGrid[yPosition][x].setType(TileType.PLATFORM);
                int gaussianHeight = (int) (Math.exp(-0.5 * Math.pow((x - mean) / sigma, 2)) * 3); // Scale factor for
                                                                                                   // height

                for (int y = yPosition + 1; y <= yPosition + gaussianHeight && y < gridHeight; y++) {
                    levelGrid[y][x].setType(TileType.PLATFORM);
                }
            }
        }

        // Identify isolated islands and connect them
        connectIslands(islandCenters);

        // Ensure starting and ending platforms are accessible
        ensureAccessibility();

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
        Entrance entrance = new Entrance(Constants.TILE_SIZE / 2, (highestPlatformY) * Constants.TILE_SIZE);
        entrance.getLocation().x -= entrance.getBounds().get(0).getWidth() / 2;
        entrance.getLocation().y -= entrance.getBounds().get(0).getHeight();
        entranceAndBallot.add(entrance);
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
        BallotBox ballotBox = new BallotBox((gridWidth - 1) * Constants.TILE_SIZE + Constants.TILE_SIZE / 2,
                (highestPlatformY) * Constants.TILE_SIZE);
        ballotBox.getLocation().x -= ballotBox.getBounds().get(0).getWidth() / 2;
        ballotBox.getLocation().y -= ballotBox.getBounds().get(0).getHeight();
        entranceAndBallot.add(ballotBox);
        this.ballotBox = ballotBox;

        // Find the starting position (entrance) and the ending position (ballot box)
        int startX = (int) entrance.getLocation().x / cellSize;
        int startY = (int) entrance.getLocation().y / cellSize;
        int endX = (int) ballotBox.getLocation().x / cellSize;
        int endY = (int) ballotBox.getLocation().y / cellSize;

        // Generate a traversable path from the ballot box to the start position
        boolean[][] visited = new boolean[gridHeight][gridWidth];
        List<int[]> path = generateTraversablePath(startX, startY, endX, endY);
        List<int[]> path2 = generateTraversablePath(0, gridHeight, endX, endY);

        for (int[] step : path) {
            int x = step[0];
            int y = step[1];
            if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
                levelGrid[y][x].setType(TileType.EMPTY); // Adjust the tile type
                if (y - 1 > 0) {
                    levelGrid[y - 1][x].setType(TileType.EMPTY); // Set the adjacent tile for
                    // thickness
                }
            }
        }
        for (int[] step : path2) {
            int x = step[0];
            int y = step[1];
            if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
                levelGrid[y][x].setType(TileType.EMPTY); // Adjust the tile type
                if (y - 1 > 0) {
                    levelGrid[y - 1][x].setType(TileType.EMPTY); // Set the adjacent tile for
                    // thickness
                }
            }
        }

        // Modify the level grid based on the generated path

        if (path != null) {
            for (int[] coordinates : path) {
                int x = coordinates[0];
                int y = coordinates[1];

                if (levelGrid[y][x].getType() != TileType.PLATFORM) {
                    // Add platforms below the path if not on an island
                    for (int i = y + 1; i < Math.min(gridHeight, y + 3); i++) {
                        levelGrid[i][x].setType(TileType.PLATFORM);
                    }
                } else {
                    // Carve out sections of the islands if the path is on an island
                    for (int i = y - 1; i >= Math.max(0, y - 2); i--) {
                        levelGrid[i][x].setType(TileType.EMPTY);
                    }
                }
            }

        }

        // Additional parameters for column clearing
        int minClearColumns = 2; // Minimum number of columns to clear
        int maxClearColumns = 5; // Maximum number of columns to clear
        int numClearedColumns = (int) PApplet.map(difficultyFactor, 0, 5, minClearColumns, maxClearColumns);

        // Randomly choose columns to clear
        Random random = new Random();
        ArrayList<Integer> clearedColumns = new ArrayList<>();
        for (int i = 0; i < numClearedColumns; i++) {
            int randomColumn = random.nextInt(gridWidth);
            // Ensure the column is not on the edges (entrance/exit)
            while (randomColumn == 0 || randomColumn == gridWidth - 1 || clearedColumns.contains(randomColumn)) {
                randomColumn = random.nextInt(gridWidth);
            }
            clearedColumns.add(randomColumn);
        }

        // Clear the chosen columns
        for (int clearedColumn : clearedColumns) {
            for (int y = 0; y < gridHeight; y++) {
                levelGrid[y][clearedColumn].setType(TileType.EMPTY);
            }
        }

        // Modify the level grid based on the generated path (considering cleared
        // columns)
        if (path != null) {
            for (int[] coordinates : path) {
                int x = coordinates[0];
                int y = coordinates[1];

                // Skip cleared columns while carving out sections for the path
                if (!clearedColumns.contains(x)) {
                    if (levelGrid[y][x].getType() != TileType.PLATFORM) {
                        // Add platforms below the path if not on an island
                        for (int i = y + 1; i < Math.min(gridHeight, y + 3); i++) {
                            levelGrid[i][x].setType(TileType.PLATFORM);
                        }
                    } else {
                        // Carve out sections of the islands if the path is on an island
                        for (int i = y - 1; i >= Math.max(0, y - 2); i--) {
                            levelGrid[i][x].setType(TileType.EMPTY);
                        }
                    }
                }
            }
        }

        // After placing the ballot box in the grid
        int ballotX = (int) ballotBox.getLocation().x / cellSize; // Convert position to grid coordinates
        int ballotY = (int) ballotBox.getLocation().y / cellSize;

        // Ensure the tile with the ballot box is empty
        levelGrid[ballotY][ballotX].setType(TileType.EMPTY);

        // Clear tiles to the left of the ballot box and ensure there are platforms
        // underneath
        int tilesToClear = 7; // Number of tiles to clear to the left of the ballot box
        for (int i = 0; i < tilesToClear; i++) {
            int clearX = ballotX - 1 - i; // Calculate the x-coordinate of the tile to clear
            if (clearX >= 0) { // Check bounds
                levelGrid[ballotY][clearX].setType(TileType.EMPTY);
                // Ensure there is a platform right underneath the cleared tile if within bounds
                if (ballotY + 1 < gridHeight) {
                    levelGrid[ballotY + 1][clearX].setType(TileType.PLATFORM);
                }
            }
        }

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

    private void ensureAccessibility() {
        // Example function to ensure that the first and last platforms are reachable
        levelGrid[1][1].setType(TileType.PLATFORM); // Starting platform
        levelGrid[gridHeight - 2][gridWidth - 2].setType(TileType.PLATFORM); // Ending platform
    }

    private List<int[]> generateTraversablePath(int startX, int startY, int endX, int endY) {
        List<int[]> path = new ArrayList<>();
        int currentX = startX;
        int currentY = startY;
        int verticalDirection = 1; // Start moving upward

        // Establish boundaries for vertical movement
        int maxY = gridHeight - 2;
        int minY = 1;
        int maxHorizontalSteps = 5;
        int minHorizontalSteps = 1;
        int horizontalSteps = (int) (Math.random() * (maxHorizontalSteps - minHorizontalSteps + 1))
                + minHorizontalSteps; // Random steps between min and max

        while (currentX != endX || currentY != endY) {
            // Check for horizontal movement remaining
            if (horizontalSteps > 0) {
                int horizontalStep = Integer.compare(endX, currentX);
                currentX += horizontalStep;
                horizontalSteps--;
            } else {
                // Determine next vertical position within boundaries
                int nextY = currentY + verticalDirection;
                if (nextY > maxY || nextY < minY) {
                    verticalDirection *= -1;
                    nextY = currentY + verticalDirection;
                }

                // Apply the vertical movement and reset horizontal steps
                currentY = nextY;
                horizontalSteps = (int) (Math.random() * (maxHorizontalSteps - minHorizontalSteps + 1))
                        + minHorizontalSteps;
            }

            // Add the current position to the path
            path.add(new int[] { currentX, currentY });

            // Safety measure to prevent infinite loops
            if (path.size() > gridWidth * gridHeight) {
                break;
            }
        }

        return path;
    }

    private void connectIslands(ArrayList<PVector> islandCenters) {
        boolean[] visited = new boolean[islandCenters.size()];
        visited[0] = true;

        for (int i = 0; i < islandCenters.size() - 1; i++) {
            int start = -1, end = -1;
            double closestDist = Double.MAX_VALUE;

            for (int j = 0; j < islandCenters.size(); j++) {
                if (!visited[j])
                    continue;

                for (int k = 0; k < islandCenters.size(); k++) {
                    if (visited[k] || j == k)
                        continue;

                    double dist = Math.sqrt(Math.pow(islandCenters.get(j).x - islandCenters.get(k).x, 2)
                            + Math.pow(islandCenters.get(j).y - islandCenters.get(k).y, 2));
                    if (dist < closestDist) {
                        closestDist = dist;
                        start = j;
                        end = k;
                    }
                }
            }

            if (start != -1 && end != -1) {
                visited[end] = true;
                generatePath((int) islandCenters.get(start).x, (int) islandCenters.get(start).y,
                        (int) islandCenters.get(end).x, (int) islandCenters.get(end).y);
            }
        }
    }

    private void generatePath(int startX, int startY, int endX, int endY) {
        // Simple line drawing algorithm to connect two points
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        int sx = startX < endX ? 1 : -1;
        int sy = startY < endY ? 1 : -1;

        int err = (dx > dy ? dx : -dy) / 2;
        int e2;

        while (true) {
            levelGrid[startY][startX].setType(TileType.PLATFORM); // Set the path
            if (startX == endX && startY == endY)
                break;
            e2 = err;
            if (e2 > -dx) {
                err -= dy;
                startX += sx;
            }
            if (e2 < dy) {
                err += dx;
                startY += sy;
            }
        }
    }

    public void buildGraph() {
        nodes = new ArrayList<>(); // Initialize the graph nodes list

        // Iterate over levelGrid and add platforms as nodes
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (levelGrid[y][x].getType() == TileType.PLATFORM) {
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
                sketch.line(nodeScreenX + cellSize / 2, nodeScreenY + cellSize / 2, neighborScreenX + cellSize / 2,
                        neighborScreenY + cellSize / 2);
            }
        }
    }

    public void update(float deltaTime) {
        if (!cameraDelayCompleted) {
            cameraDelayElapsed += deltaTime;
            startingMessage.update(deltaTime);
            if (cameraDelayElapsed >= cameraDelayTime) {
                cameraDelayCompleted = true;
                startingMessage.active = false;
            }
        }

        if (cameraDelayCompleted) {
            updateCamera(deltaTime);
        }

        if (playerOnBallot()) {
            if (!ballotMessage.isActive()) {
                ballotMessage.reset();
                ballotMessage.start();
            }
        }

        ballotMessage.update(deltaTime);
        checkPlayerPosition();

        for (Laser laser : lasers) {
            laser.update(deltaTime); // Update each laser
            if (laser.checkCollision(player)) {
                System.out.println("player health before hit" + player.getHealth());

                player.setHealth(player.getHealth() - this.healthIncrement * 2);
                System.out.println("player health after hit" + player.getHealth());
            }
        }
    }

    private void checkPlayerPosition() {
        if (!cameraStill && (player.getLocation().x >= 20 + Constants.Screen.width + cameraX
                || player.getLocation().x < cameraX - 1)) {
            player.setHealth(0);
        }
    }

    public void restartLevel() {
        for (Node node : nodes) {
            node.resetBoundingBox(); // Reset each node's bounding boxes to the start positions
        }

        player.resetPlayer(); // Reset player's position
        player.resetBoundingBox();

        cameraX = 0;
        cameraMovingRight = true;
        cameraStill = false;

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
        BoundingBox boxPlayer = player.getBounds().get(0);
        BoundingBox boxBallotBox = ballotBox.getBounds().get(0);
        return boxPlayer.getLocation().x + boxPlayer.getWidth() >= boxBallotBox.getLocation().x
                && boxPlayer.getLocation().y + boxPlayer.getHeight() >= boxBallotBox.getLocation().y
                && boxBallotBox.getLocation().x + boxBallotBox.getWidth() >= boxPlayer.getLocation().x
                && boxBallotBox.getLocation().x + boxBallotBox.getHeight() >= boxPlayer.getLocation().x;
    }

    public boolean playerOnEntrance() {
        BoundingBox boxPlayer = player.getBounds().get(0);
        BoundingBox boxEntrance = entrance.getBounds().get(0);
        return boxPlayer.getLocation().x + boxPlayer.getWidth() >= boxEntrance.getLocation().x
                && boxPlayer.getLocation().y + boxPlayer.getHeight() >= boxEntrance.getLocation().y
                && boxEntrance.getLocation().x + boxEntrance.getWidth() >= boxPlayer.getLocation().x
                && boxEntrance.getLocation().x + boxEntrance.getHeight() >= boxPlayer.getLocation().x;
    }

    public void draw() {
        drawMap();
        if (!cameraDelayCompleted && startingMessage.isActive()) {
            startingMessage.draw(parent);
        }
        if (ballotMessage.isActive()) {
            ballotMessage.draw(parent);
        }

        for (Laser laser : lasers) {
            laser.draw(cameraX); // Pass the current camera offset
        }

    }

    // Modify the draw method to offset tiles based on the camera position
    public void drawMap() {

        // Only draw the part of the level that's currently within the camera's view
        int startCol = (int) (cameraX / cellSize);
        int endCol = Math.min(startCol + parent.width / cellSize + 1, gridWidth);

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
