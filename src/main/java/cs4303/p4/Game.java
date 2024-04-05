package cs4303.p4;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import processing.core.PApplet;

@SpringBootApplication
public class Game extends PApplet {

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

    }

    @Override
    public void keyPressed() {

    }

    @Override
    public void draw() {

    }
}
