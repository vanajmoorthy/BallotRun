package cs4303.p4.states;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeController;
import cs4303.p4.entities.Enemy;
import cs4303.p4.entities.Entity;
import cs4303.p4.entities.Player;
import cs4303.p4.items.Item;
import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4._util.gui.GestureDetector;
import cs4303.p4.map.Level;
import cs4303.p4.map.Node;
import cs4303.p4.map.Tile;
import cs4303.p4.map.TileType;
import cs4303.p4.physics.Bullet;
import cs4303.p4.physics.Projectile;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.util.ResourceUtils;

public final class GameStateGameplay extends GameState {
    @Getter
    private int healthIncrement;
    private Player player;
    private Level level;
    private List<Item> items;
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    // flags to decouple movement
    private boolean w_pressed = false;
    private boolean d_pressed = false;
    private boolean a_pressed = false;
    private boolean s_pressed = false;
    private boolean jumped = false;

    private boolean isPaused = false;
    private boolean didReachBallotBox = false;

    private int cursor = PApplet.ARROW;

    private int currentLevel = 1;

    private float difficultyFactor = 0.0f; // Start with a base difficulty
    private int score = 0;

    private GestureDetector buttonRestart = new GestureDetector((sketch, hitbox, hasHover, hasClick) -> {
        if (hasHover && !isPaused)
            cursor = PApplet.HAND;

        sketch.fill(hasHover ? Colors.darkGray.primary : Colors.darkGray.dark);
        sketch.noStroke();
        sketch.rect(Constants.Screen.width - 10 - 40, Constants.Screen.height - 10 - 40, 40, 40, 10);

        sketch.noFill();
        sketch.stroke(Colors.white);
        sketch.strokeWeight(2);
        sketch.rect(Constants.Screen.width - 10 - 40 + 4, 10 + 4, 32, 32, 6);

        sketch.filter(PApplet.INVERT);
        try {
            sketch.image(sketch.loadImage(ResourceUtils.getFile("classpath:icons/reload.png").getAbsolutePath()),
                    Constants.Screen.width - 10 - 40 + 5, 10 + 5, 30, 30);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sketch.filter(PApplet.INVERT);

    }, (sketch, button) -> {
        if (!isPaused)
            level.restartLevel();
    }, new GestureDetector.Hitbox(new PVector(Constants.Screen.width - 10 - 40, 10), new PVector(40, 40)));

    private GestureDetector buttonPause = new GestureDetector((sketch, hitbox, hasHover, hasClick) -> {
        if (hasHover)
            cursor = PApplet.HAND;

        sketch.fill(hasHover ? Colors.darkGray.primary : Colors.darkGray.dark);
        sketch.noStroke();
        sketch.rect(Constants.Screen.width - 10 - 40 - 50, 10, 40, 40, 10);

        sketch.noFill();
        sketch.stroke(Colors.white);
        sketch.strokeWeight(2);
        sketch.rect(Constants.Screen.width - 10 - 40 - 50 + 4, 10 + 4, 32, 32, 6);

        sketch.filter(PApplet.INVERT);
        try {
            sketch.image(sketch.loadImage(ResourceUtils
                    .getFile(isPaused ? "classpath:icons/play.png" : "classpath:icons/pause.png").getAbsolutePath()),
                    Constants.Screen.width - 10 - 40 - 50 + 5, 10 + 5, 30, 30);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sketch.filter(PApplet.INVERT);

    }, (sketch, button) -> {
        isPaused = !isPaused;
    }, new GestureDetector.Hitbox(new PVector(Constants.Screen.width - 10 - 40 - 50, 10), new PVector(40, 40)));

    public GameStateGameplay(PApplet sketch, Player player, List<Item> items) {

        this.player = player;
        this.items = items;
        startLevel(sketch);
    }

    public void startLevel(PApplet sketch) {
        entities.clear(); // Clear all entities before adding new ones
        int newWidth = (int) (currentLevel / 1.8); // Increase grid width with each level
        player.resetPlayer(); // Reset player's position

        this.healthIncrement = (int) (player.getHealth() * 0.1);
        level = new Level(sketch, difficultyFactor, player, newWidth, this);
        level.buildGraph();
        entities.add(player); // Re-add the player
        entities.addAll(level.getEntities()); // Add new level entities
        didReachBallotBox = false;
        // TODO make this scale
        placeEnemies((int) (5 + difficultyFactor * 2));

    }

    public void placeEnemies(int numberOfEnemies) {
        for (int i = 0; i < numberOfEnemies; i++) {
            Boolean foundTile = false;
            while (!foundTile) {
                Random random = new Random();
                int randomIndex = random.nextInt(level.getNodes().size());
                Node selectedNode = level.getNodes().get(randomIndex);


                if(selectedNode.getY() == 0){
                    continue;
                }

                if(selectedNode.getY() >= level.gridHeight -1 ){
                    continue;
                }
                if(selectedNode.getX() == 0){
                    continue;
                }
                if(selectedNode.getX() >= level.gridWidth -1){
                    continue;
                }

                //only place here if there is no node above it
                Boolean valid = true;
                for (Node n : selectedNode.getNeighbors()) {
                    if (n.getY() == selectedNode.getY() - 1) {
                        // System.out.printf("Unsuitable");
                        valid = false;
                    }
                }
                if(!valid){
                    continue;
                }

                Enemy enemy = new Enemy(selectedNode.getX() * Constants.TILE_SIZE ,
                        (selectedNode.getY() - 1) * Constants.TILE_SIZE + 20, 160,this.difficultyFactor);

                entities.add(enemy);
                enemies.add(enemy);

                foundTile = true;

            }

        }
    }


    private int calculateScore(PApplet sketch) {
        return (int) ((100 * difficultyFactor) + sketch.random(20));
    }

    public GameState draw(PApplet sketch) {
        // draw the player
        sketch.background(Colors.black);
        cursor = PApplet.ARROW;

        float cameraOffset = level.getCameraX();


        level.draw(); // Draw the current view of the level
        // level.drawGraph(sketch);

        for (Entity entity : level.getEntranceAndBallot()) {
            entity.getLocation().x -= cameraOffset;
            entity.draw(sketch);
            entity.getLocation().x += cameraOffset;
        }

        for (Projectile p : projectiles) {
            p.getLocation().x -= cameraOffset;
            p.draw(sketch);
            p.getLocation().x += cameraOffset;
        }


        for (Entity entity : entities) {

            entity.getLocation().x -= cameraOffset;
            entity.draw(sketch);

            if(entity instanceof Player){
                player.cameraOffsetX = cameraOffset;
                System.out.println("before");
                System.out.println("enemies" + enemies.size());
                System.out.println("projectiles" + projectiles.size());
                player.updateAttack(sketch,enemies,projectiles);
                System.out.println("after");
                System.out.println("enemies" + enemies.size());
                System.out.println("projectiles" + projectiles.size());

            }

            entity.getLocation().x += cameraOffset;
        }
        ArrayList<Entity> deleting = new ArrayList<Entity>();
        for(Entity e: entities) {
            if(e instanceof Enemy){
                if(!enemies.contains(e)){
                    deleting.add(e);
                }
            }
        }
        entities.removeAll(deleting);
        if (level.playerOnBallot())
            didReachBallotBox = true;

        update(0.0f);

        sketch.noStroke();
        sketch.fill(Colors.darkGray.dark);
        sketch.rect(0, Constants.Screen.height - Constants.Screen.GamePlay.infoPanelHeight, Constants.Screen.width,
                Constants.Screen.GamePlay.infoPanelHeight);

        buttonRestart.draw(sketch);
        buttonPause.draw(sketch);

        sketch.cursor(cursor);

        if (player.getLocation().y > Constants.Screen.height) {
            System.out.println("y!");
            player.setHealth(0);
        }

        // Display current level and difficulty
        sketch.pushMatrix();
        sketch.fill(255); // White text
        sketch.textSize(14);
        sketch.translate(2, Constants.Screen.height - 45);
        String gameInfo = String.format("Score: %d", score);
        sketch.text(gameInfo, 50, 0);
        sketch.popMatrix();

        sketch.pushMatrix();
        sketch.fill(255, 0, 0); // red
        sketch.translate(10, Constants.Screen.height - 30);
        int numSegs = (int) (player.getHealth() / this.healthIncrement);
        sketch.rect(0, 0, numSegs * 20, 10);
        sketch.popMatrix();

        if (player.getHealth() <= 0) {

            return new GameStateLoss(player, items);
        } else if (didReachBallotBox && level.playerOnEntrance()) {
            score += calculateScore(sketch);
            return new GameStateWin(player, items, score);
        } else {
            return null;
        }

    }

    /**
     * w is jump a is left d is right
     * 
     * @param sketch
     */
    public void keyPressed(PApplet sketch) {
        char key = sketch.key;
        if (key == 'w' || key == 'W') {
            this.w_pressed = true;
        } else if (key == 's' || key == 'S') {
            this.s_pressed = true;
        } else if (key == 'a' || key == 'A') {
            this.a_pressed = true;
        } else if (key == 'd' || key == 'D') {
            this.d_pressed = true;
        } else if (key == ' ') {
            player.startAttack(); // Trigger attack when space is pressed
        }
    }

    public void keyReleased(PApplet sketch) {
        char key = sketch.key;
        if (key == 'w' || key == 'W') {
            this.w_pressed = false;
            // flip jumped
            if (this.jumped == true) {
                this.jumped = false;
            }
        }
        if (key == 's' || key == 'A') {
            this.s_pressed = false;
        }
        if (key == 'a' || key == 'S') {
            this.a_pressed = false;
        }
        if (key == 'd' || key == 'D') {
            this.d_pressed = false;
        }
    }

    public void mousePressed(PApplet sketch) {
        if (buttonRestart.hasFocus(sketch))
            buttonRestart.click(sketch);
        if (buttonPause.hasFocus(sketch))
            buttonPause.click(sketch);
    }

    public void mouseReleased(PApplet sketch) {

    }

    public void movePlayer() {
        if (w_pressed) {
            if (this.jumped == false) {
                player.jump();
                this.jumped = true;
            }

        }

        if (a_pressed) {
            PVector left = new PVector(-1 * Constants.moveIncrement, 0);
            player.applyForce(left);
        }

        if (d_pressed) {

            PVector right = new PVector(Constants.moveIncrement, 0);
            player.applyForce(right);
        }
    }

    public void update(float deltaTime) {
        if (!isPaused) {

            for (Enemy e : enemies) {
                Projectile b = e.fire(player, this.level.getNodes());
                if (b != null) {
                    projectiles.add(b);
                }

            }
            // if any projectiles crash into a wall
            // delete them
            ArrayList<Projectile> toRemove = new ArrayList<Projectile>();
            for (Projectile p : this.projectiles) {
                for (Node n : this.level.getNodes()) {
                    if (p.Collision(n)) {
                        toRemove.add(p);
                    }
                }
            }
            projectiles.removeAll(toRemove);
            toRemove.clear();


            // TODO remove the enemy from the arraylist of enemies when killed
            // check for collisions with the player
            for (Projectile p : this.projectiles) {

                if (player.Collision(p)) {
                    player.setHealth(player.getHealth() - this.healthIncrement);
                    toRemove.add(p);

                }
            }
            projectiles.removeAll(toRemove);
            toRemove.clear();

            for (Projectile p : this.projectiles) {
                p.move();
            }

            level.update(deltaTime);

            movePlayer();
            // player.move(level.getNodes());
            player.move(level.getLevelGrid());

        }
    }

}
