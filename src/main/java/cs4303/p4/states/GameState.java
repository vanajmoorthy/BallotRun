package cs4303.p4.states;

import processing.core.PApplet;

public abstract class GameState {
    public abstract GameState draw(PApplet sketch);

    public abstract void keyPressed(PApplet sketch);

    public abstract void keyReleased(PApplet sketch);

    public abstract void mousePressed(PApplet sketch);

    public abstract void mouseReleased(PApplet sketch);

    public abstract void update(float deltaTime);

}
