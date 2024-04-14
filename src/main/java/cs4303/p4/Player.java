package cs4303.p4;

public class Player extends Entity{
    public Player(Game g, float x, float y) {
        super(g, x, y);
    }

    @Override
    public void draw() {
        super.game.pushMatrix();
        super.game.fill(0, 0, 255); // Set fill color to blue
        //TODO FIX THE SIZE OF THE PLAYER
        super.game.rect(super.location.x,super.location.y,10,10);
        super.game.popMatrix();
    }

    public void jump(){

    }

    @Override
    public void update(){

    }
}
