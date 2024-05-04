package cs4303.p4.states;

import cs4303.p4.entities.Entity;
import cs4303.p4.entities.Player;
import cs4303.p4.items.Item;
import cs4303.p4._util.Constants;
import cs4303.p4.map.Level;
import cs4303.p4.map.Node;
import cs4303.p4.physics.BoundingBox;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class GameStateGameplay extends GameState {
    private Player player;
    private Level level;
    private List<Item> items;

    // int cellSize = 40;

    // flags to decouple movement
    private boolean w_pressed = false;
    private boolean d_pressed = false;
    private boolean a_pressed = false;
    private boolean s_pressed = false;
    private boolean jumped = false;

    private ArrayList<Entity> entities = new ArrayList<Entity>();

    public GameStateGameplay(PApplet sketch) {
        // TODO insert a start location
        player = new Player(500, 50);
        level = new Level(sketch, 1.2f, player);
        level.buildGraph();
        entities.add(player);
    }

    public void draw(PApplet sketch) {
        // draw the player
        sketch.background(200);
        level.draw(); // Draw the current view of the level

        if (level.isCameraDelayCompleted()) {
            boolean cameraMoving = level.isCameraMovingRight() || level.getCameraX() > 0;
            player.updatePosition(level.getCameraSpeed(), cameraMoving, level.isCameraMovingRight(),
                    level.isCameraStill());
        }

        player.draw(sketch);

        for (Node n : level.getNodes()) {
            for (BoundingBox b : n.getBounds()) {
                sketch.rect(b.getLocation().x, b.getLocation().y, Constants.TILE_SIZE, Constants.TILE_SIZE);
            }
        }
        update(0.0f);
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
        if (key == 'w') {
            this.w_pressed = true;
        } else if (key == 's') {
            this.s_pressed = true;
        } else if (key == 'a') {
            this.a_pressed = true;
        } else if (key == 'd') {
            this.d_pressed = true;
        }
    }

    public void keyReleased(PApplet sketch) {
        char key = sketch.key;
        if (key == 'w') {
            this.w_pressed = false;
            // flip jumped
            if (this.jumped == true) {
                this.jumped = false;
            }
        }
        if (key == 's') {
            this.s_pressed = false;
        }
        if (key == 'a') {
            this.a_pressed = false;
        }
        if (key == 'd') {
            this.d_pressed = false;
        }
    }

    public void mousePressed(PApplet sketch) {

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
        level.updateCamera(deltaTime); // Update the camera position

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

    @Override
    public GameState switchState(PApplet sketch) {
        return new GameStateBase(player, items);
    }
}
