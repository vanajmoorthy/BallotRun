package cs4303.p4.states;

import cs4303.p4.entities.Entity;
import cs4303.p4.entities.Player;
import cs4303.p4.items.Item;
import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4._util.gui.GestureDetector;
import cs4303.p4.map.Level;
import cs4303.p4.map.Node;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

@Getter
public final class GameStateGameplay extends GameState {
    private Player player;
    private Level level;
    private List<Item> items;
    private ArrayList<Entity> entities = new ArrayList<Entity>();

    // flags to decouple movement
    private boolean w_pressed = false;
    private boolean d_pressed = false;
    private boolean a_pressed = false;
    private boolean s_pressed = false;
    private boolean jumped = false;

    private boolean didReachBallotBox = false;

    private int cursor = PApplet.ARROW;

    private GestureDetector buttonRestart = new GestureDetector(
        (sketch, hitbox, hasHover, hasClick) -> {
            if (hasHover) cursor = PApplet.HAND;

            sketch.fill(
                hasHover
                    ? Colors.darkGray.primary
                    : Colors.darkGray.dark
            );
            sketch.noStroke();
            sketch.rect(
                Constants.Screen.width - 10 - 40,
                10,
                40,
                40,
                10
            );

            sketch.noFill();
            sketch.stroke(Colors.white);
            sketch.strokeWeight(2);
            sketch.rect(
                Constants.Screen.width - 10 - 40 + 4,
                10 + 4,
                32,
                32,
                6
            );

            sketch.filter(PApplet.INVERT);
            try {
                sketch.image(
                    sketch.loadImage(
                        ResourceUtils.getFile("classpath:icons/reload.png").getAbsolutePath()
                    ),
                    Constants.Screen.width - 10 - 40 + 5,
                    10 + 5,
                    30,
                    30
                );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            sketch.filter(PApplet.INVERT);

        },
        (sketch, button) -> {
            level.restartLevel();
        },
        new GestureDetector.Hitbox(
            new PVector(
                Constants.Screen.width - 10 - 40,
                10
            ),
            new PVector(40, 40)
        )
    );

    public GameStateGameplay(PApplet sketch, Player player, List<Item> items) {
        // TODO insert a start location
        this.player = player;
        this.items = items;
        level = new Level(sketch, 1.2f, player);
        entities.add(player);
        entities.addAll(level.getEntities());
        level.buildGraph();
        player.resetPlayer(); // Reset player's position
    }

    public GameState draw(PApplet sketch) {
        // draw the player
        sketch.background(Colors.black);
        cursor = PApplet.ARROW;
        level.draw(); // Draw the current view of the level
        // level.drawGraph(sketch);

        for (Entity entity : entities) {
            if (level.isCameraDelayCompleted()) {
                boolean cameraMoving = level.isCameraMovingRight() || level.getCameraX() > 0;
                entity.moveWithCamera(level.getCameraSpeed(), cameraMoving, level.isCameraMovingRight(),
                        level.isCameraStill());
            }

            entity.draw(sketch);
        }

        buttonRestart.draw(sketch);

        sketch.cursor(cursor);

        buttonRestart.draw(sketch);

        sketch.cursor(cursor);

        if (level.playerOnBallot()) didReachBallotBox = true;

        // for (Node n : level.getNodes()) {
        // for (BoundingBox b : n.getBounds()) {
        // sketch.stroke(0, 0, 0);
        // sketch.rect(b.getLocation().x, b.getLocation().y, Constants.TILE_SIZE,
        // Constants.TILE_SIZE);
        // }
        // }
        update(0.0f);

        if (player.getLocation().y > Constants.Screen.height)
            player.setHealth(0);

        return player.getHealth() <= 0
                ? new GameStateLoss(player, items)
                : didReachBallotBox && level.playerOnEntrance()
                        ? new GameStateWin(player, items)
                        : null;
    }

    /**
     * w is jump
     * a is left
     * d is right
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
            PVector left = new PVector(-1 * Constants.PLAYER.INSTANCE.X_MOVE * Constants.Screen.width, 0);
            player.applyForce(left);
        }

        if (d_pressed) {

            PVector right = new PVector(Constants.PLAYER.INSTANCE.X_MOVE * Constants.Screen.width, 0);
            player.applyForce(right);
        }
    }

    public void update(float deltaTime) {
        level.update(deltaTime);

        player.applyGravity();

        movePlayer();

        for (Entity e : entities) {
            e.move(level.getNodes());
        }

    }

    /**
     * Checks if the player collides with the map
     * apply force depending on the direction of the collision
     * 
     * @return true on collision
     */
    private void wallCollisions() {

        // move the bounding box of the player
        for (Entity e : this.entities) {
            for (Node n : level.getNodes()) {
                if (e.Collision(n)) {

                    if ((e.getLocation().y < n.getBounds().get(0).getLocation().y) && (e.getTileX() == n.getX())) {
                        // entity on the top of the wall
                        if (e.getVelocity().y >= 0) {
                            PVector v = e.getVelocity().copy();
                            v.y = 0;
                            e.setVelocity(v);

                        }

                        if (e.getAcceleration().y >= 0) {
                            PVector a = e.getAcceleration().copy();
                            a.y = 0;
                            e.setAcceleration(a);
                        }

                        return;
                    }

                    if ((e.getLocation().y > n.getBounds().get(0).getLocation().y) && (e.getTileX() == n.getX())) {
                        System.out.println("below");
                        // below the wall
                        if (e.getVelocity().y < 0) {
                            PVector v = e.getVelocity().copy();
                            v.y = 0;
                            e.setVelocity(v);

                        }

                        if (e.getAcceleration().y < 0) {
                            PVector a = e.getAcceleration().copy();
                            a.y = 0;
                            e.setAcceleration(a);
                        }
                        return;
                    }
                    // work out direction between e and n
                    if ((e.getTileX() < n.getX()) && (e.getTileY() == n.getY())) {

                        // entity on the left of the wall
                        if (e.getVelocity().x >= 0) {
                            PVector v = e.getVelocity().copy();
                            v.x = 0;
                            e.setVelocity(v);

                        }

                        if (e.getAcceleration().x >= 0) {
                            PVector a = e.getAcceleration().copy();
                            a.x = 0;
                            e.setAcceleration(a);
                        }
                        return;
                    }

                    if ((e.getTileX() >= n.getX()) && (e.getTileY() == n.getY())) {
                        // on the right
                        System.out.println("ON THE RIGHT");
                        if (e.getVelocity().x <= 0) {
                            PVector v = e.getVelocity().copy();
                            v.x = 0;
                            e.setVelocity(v);

                        }

                        if (e.getAcceleration().x <= 0) {
                            PVector a = e.getAcceleration().copy();
                            a.x = 0;
                            e.setAcceleration(a);
                        }
                        return;
                    }

                }
            }
        }

    }
}
