package cs4303.p4;

import processing.core.PVector;

public class Player extends Entity{


    public Player(Game g, float x, float y) {
        super(g, x, y);
        super.setMass(Contants.PLAYER.INSTANCE.MASS);
        //initialise mass and acceleration to 0
        super.setAcceleration(new PVector(0,0));
        super.setVelocity(new PVector(0,0));
    }

    @Override
    public void draw() {
        super.game.pushMatrix();
        super.game.fill(0, 0, 255); // Set fill color to blue
        //TODO FIX THE SIZE OF THE PLAYER
        super.game.rect(super.getLocation().x,super.getLocation().y,10,10);
        super.game.popMatrix();
    }

    public void jump(){

        //TODO take account of screen size in jump
        PVector jump = new PVector(0,Contants.PLAYER.INSTANCE.JUMP_IMPULSE);
        super.applyForce(jump);

    }

    @Override
    public void update(){

        //update acceleration by applying resistance to it
        //gravity
        //TODO take screen size into account
        PVector gravity = new PVector(0,Contants.gravity);
        super.applyForce(gravity);

        //drag
        //TODO add drag



        // Update velocity based on acceleration
        super.setVelocity(super.getVelocity().add(super.getAcceleration()));

        // Update position based on velocity
        super.setLocation(super.getLocation().add(super.getVelocity()));


    }
}
