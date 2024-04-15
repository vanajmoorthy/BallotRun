package cs4303.p4;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import cs4303.p4.states.GameState;
import cs4303.p4.states.GameStateGameplay;
import processing.core.PApplet;

@SpringBootApplication
public class Game extends PApplet {
    private GameState state;

    public static void main(String[] args) {
        String[] appletArgs = new String[] { "Game" };
        Game sketch = new Game();
        PApplet.runSketch(appletArgs, sketch);
    }

    @Override
    public void settings() {
        size(1000, 600);
    }

    @Override
    public void setup() {
        state = new GameStateGameplay(this);
    }

    @Override
    public void keyPressed() {
        state.keyPressed(this);
    }

    @Override
    public void keyReleased() {
        state.keyReleased(this);
    }

    @Override
    public void draw() {
        state.draw(this);
    }
}
