package cs4303.p4.states;

import cs4303.p4.Entity;
import cs4303.p4.Player;
import cs4303.p4._util.Constants;
import cs4303.p4.map.Level;
import cs4303.p4.map.Node;
import cs4303.p4.physics.BoundingBox;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

@Getter
public final class GameStateGameplay extends GameState {
    private Player player;
    private Level level;

    // int cellSize = 40;

    // flags to decouple movement
    private boolean w_pressed = false;
    private boolean d_pressed = false;
    private boolean a_pressed = false;
    private boolean s_pressed = false;
    private ArrayList<Entity> entities =new ArrayList<Entity>();
    public GameStateGameplay(PApplet sketch) {
        // TODO insert a start location
        player = new Player(500, 50);
        level = new Level(sketch);
        level.buildGraph();
        entities.add(player);
    }

    public void draw(PApplet sketch) {
        // draw the player
        sketch.background(200);

        level.draw(); // Draw the current view of the level
        player.setCameraOffsetX(level.getCameraX());
        player.draw(sketch);
        update();
    }

    /**
     * w is jump
     * a is left
     * d is right
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
        if(w_pressed){
            player.jump();

        }

        if(a_pressed){
            PVector left = new PVector(-1*Constants.PLAYER.INSTANCE.X_MOVE * Constants.Screen.width, 0);
            player.applyForce(left);
        }

        if(d_pressed){
            PVector right = new PVector(Constants.PLAYER.INSTANCE.X_MOVE * Constants.Screen.width,0);
            player.applyForce(right);
        }
    }

    public void update() {
        level.updateCamera(); // Update the camera position

        player.update();

        wallCollisions();

        movePlayer();
        player.move();

    }


    /**
     * Checks if the player collides with the map
     * apply force depending on the direction of the collision
     * @return true on collision
     */
    private boolean wallCollisions(){

        //move the bounding box of the player
        for(Entity e : this.entities){
            for(Node n: level.getNodes()){
                if(e.Collision(n)){

                    //work out direction between e and n
                    if(e.getLocation().x < n.getBounds().get(0).getLocation().x){
                        //entity on the left of the wall

                        if(e.getVelocity().x > 0){
                            PVector v = e.getVelocity().copy();
                            v.x = 0;
                            e.setVelocity(v);

                            if(e.getAcceleration().x > 0){
                                PVector a = e.getAcceleration().copy();
                                a.x = 0;
                                e.setAcceleration(a);
                            }

                        }

                    }

                    if(e.getLocation().x >  n.getBounds().get(0).getLocation().x){
                        //on the right

                        if(e.getVelocity().x < 0){
                            PVector v = e.getVelocity().copy();
                            v.x = 0;
                            e.setVelocity(v);

                            if(e.getAcceleration().x < 0){
                                PVector a = e.getAcceleration().copy();
                                a.x = 0;
                                e.setAcceleration(a);
                            }

                        }

                    }

                    if(e.getLocation().y <=  n.getBounds().get(0).getLocation().y){
                        //entity on the top of the wall

                        if(e.getVelocity().y > 0){
                            PVector v = e.getVelocity().copy();
                            v.y = 0;
                            e.setVelocity(v);

                            if(e.getAcceleration().y >= 0){
                                PVector a = e.getAcceleration().copy();
                                a.y = 0;
                                e.setAcceleration(a);
                            }

                        }
                    }

                    if(e.getLocation().y >= n.getBounds().get(0).getLocation().y){
                        //System.out.println("Below the wall");
                        //below the wall
                        if(e.getVelocity().y < 0){
                            PVector v = e.getVelocity().copy();
                            v.y = 0;
                            e.setVelocity(v);

                            if(e.getAcceleration().y <= 0){
                                PVector a = e.getAcceleration().copy();
                                a.y = 0;
                                e.setAcceleration(a);
                            }


                        }
                    }
                }
            }
        }
        return false;
    }
}
