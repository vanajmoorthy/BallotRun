package cs4303.p4;

import processing.core.PApplet;
import processing.core.PVector;

public class Player extends Entity {
    public Player(float x, float y) {
        super(x, y);
        super.setMass(Constants.PLAYER.INSTANCE.MASS);
        //initialise mass and acceleration to 0
        super.setAcceleration(new PVector(0,0));
        super.setVelocity(new PVector(0,0));
    }

    @Override
    public void draw(PApplet sketch) {
        sketch.pushMatrix();
        sketch.fill(0, 0, 255); // Set fill color to blue
        //TODO FIX THE SIZE OF THE PLAYER
        sketch.rect(super.getLocation().x,super.getLocation().y,10,10);
        sketch.popMatrix();
    }

    public void jump(){

        //TODO take account of screen size in jump
        PVector jump = new PVector(0,Constants.PLAYER.INSTANCE.JUMP_IMPULSE);
        super.applyForce(jump);

    }

    @Override
    public void update(){

        //update acceleration by applying resistance to it
        //gravity
        //TODO take screen size into account
        PVector gravity = new PVector(0,Constants.gravity);
        super.applyForce(gravity);

        //drag
        //TODO add drag



        // Update velocity based on acceleration
        super.setVelocity(super.getVelocity().add(super.getAcceleration()));

        // Update position based on velocity
        super.setLocation(super.getLocation().add(super.getVelocity()));


    }
}
