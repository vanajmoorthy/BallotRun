package cs4303.p4;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import processing.core.PApplet;

@SpringBootApplication
public class Game extends PApplet {

    //flags to decouple movement
    boolean w_pressed = false;
    boolean d_pressed = false;
    boolean a_pressed = false;
    boolean s_pressed = false;

    Player player;

	public static void main(String[] args) {
        String[] appletArgs = new String[] { "Game" };
        Game sketch = new Game();
        PApplet.runSketch(appletArgs, sketch);
    }

    @Override
    public void settings() {

    }

    @Override
    public void setup() {
        //initialise the player
        //TODO insert a start location
        player = new Player(this,100,100);
    }

    @Override
    public void keyPressed() {
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
            println("Spacebar pressed!");
        }

    }

    @Override
    public void keyReleased(){
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

    @Override
    public void draw() {
        //draw the player
        player.draw();
    }


    public void movePlayer(){

    }


    public void update(){

    }

}

