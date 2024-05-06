package cs4303.p4;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import cs4303.p4._util.Constants;
import cs4303.p4.states.GameState;
import cs4303.p4.states.GameStateTitle;
import processing.core.PApplet;

@SpringBootApplication
public class Game extends PApplet {
    private GameState state;

    private float lastFrameTime;

    public static void main(String[] args) {
        String[] appletArgs = new String[] { "Game" };
        Game sketch = new Game();
        PApplet.runSketch(appletArgs, sketch);
    }

    @Override
    public void settings() {
        smooth(8);

        size(
                Math.max(Constants.Screen.width, Constants.Screen.minWidth),
                Math.max(Constants.Screen.height, Constants.Screen.minHeight));
    }

    @Override
    public void setup() {
        state = new GameStateTitle();

        // TODO MAKE THIS NOT A COMMENT
        frameRate(30); // Set the frame rate to 30 fps

        lastFrameTime = millis();

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
    public void mousePressed() {
        state.mousePressed(this);
    }

    @Override
    public void mouseReleased() {
        state.mouseReleased(this);
    }

    @Override
    public void draw() {
        float currentTime = millis();
        float deltaTime = (currentTime - lastFrameTime) / 1000.0f; // convert milliseconds to seconds
        lastFrameTime = currentTime;

        state.update(deltaTime);
        GameState newState = state.draw(this);
        if (newState != null)
            state = newState;
    }
}
