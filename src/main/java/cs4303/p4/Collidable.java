package cs4303.p4;

import processing.core.PVector;

class Collidable {
    PVector location;
    Game game;

    public Collidable(Game g, float x, float y){
        location = new PVector(x,y);
        this.game = g;
    }


    /**
     * Checking for collisions with tiles
     * @return
     */
    public boolean tileCollision(){
        return false;
    }






}