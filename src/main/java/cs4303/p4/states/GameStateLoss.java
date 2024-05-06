package cs4303.p4.states;

import java.util.List;

import cs4303.p4.entities.Player;
import cs4303.p4.items.Item;
import lombok.RequiredArgsConstructor;
import processing.core.PApplet;

@RequiredArgsConstructor
public class GameStateLoss extends GameState {
    private final Player player;
    private final List<Item> items;

    private float gameOverDelay = 3.0f; // 3 seconds before resetting
    private float gameOverTimer = 0;

    @Override
    public GameState draw(PApplet sketch) {
        sketch.background(50, 50, 50); // Dark background
        sketch.fill(255, 0, 0); // Red text
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.textSize(32);
        sketch.text("LEVEL FAILED", sketch.width / 2, sketch.height / 2);

        return gameOverTimer >= gameOverDelay
            ? new GameStateBase(player, items)
            : null;
    }

    @Override
    public void keyPressed(PApplet sketch) {

    }

    @Override
    public void keyReleased(PApplet sketch) {

    }

    @Override
    public void mousePressed(PApplet sketch) {

    }

    @Override
    public void mouseReleased(PApplet sketch) {

    }

    @Override
    public void update(float deltaTime) {
        gameOverTimer += deltaTime;
    }
}
