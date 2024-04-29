package cs4303.p4.states;

import cs4303.p4.Entity;
import cs4303.p4.Player;
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
        player = new Player(50, 50);
        level = new Level(sketch);

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
            //TODO SCALE TO SCREEN SIZE
            PVector left = new PVector(-10,0);
            player.applyForce(left);
        }

        if(d_pressed){
            PVector right = new PVector(10,0);
            player.applyForce(right);
        }
    }

    public void update() {
        level.updateCamera(); // Update the camera position
        movePlayer();
        player.update();



    }
    //  for(BoundingBox b :player.getBounds()){
    //    b.moveBox(player.getVelocity());
    //}

    /**
     * Checks if the player collides with the map
     * apply force depending on the direction of the collision
     * @return true on collision
     */
    private boolean wallCollisions(){
        //move the bounding box of the player

        for(Node n: level.getNodes()){
            for(Entity e : this.entities){
                if(e.Collision(n)){
                    //work out direction between e and n
                    if(e.getLocation().x < n.getLocation().x){
                        //entity on the left of the wall

                    }

                    if(e.getLocation().x > n.getLocation().x){
                        //entity on the right of the wall
                    }

                    if(e.getLocation().y < n.getLocation().y){
                        //entity on the top of the wall
                    }
                    if(e.getLocation().y > n.getLocation().y){
                        //entity below the wall
                    }


                }
            }
        }
        return false;
    }
}
