package cs4303.p4.states;

import cs4303.p4.Player;
import cs4303.p4.map.Level;
import lombok.Getter;
import processing.core.PApplet;

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

    public GameStateGameplay(PApplet sketch) {
        // TODO insert a start location
        player = new Player(100, 100);
        level = new Level(sketch);
    }

    public void draw(PApplet sketch) {
        // draw the player
        sketch.background(200);
        // graphics.beginDraw();
        level.draw();
        // graphics.endDraw();
        // image(graphics, 0, 0); // Draw the PGraphics object to the screen
        player.draw(sketch);

        sketch.background(200);
        sketch.clear();

        level.updateCamera(); // Update the camera position
        level.draw(); // Draw the current view of the level
        player.draw(sketch);
    }

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
        } else if (key == ' ') {
            player.jump();
            PApplet.println("Spacebar pressed!");
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

    }

    public void update() {

    }
}
