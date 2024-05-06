package cs4303.p4.physics;

import cs4303.p4._util.Colors;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
@Getter
@Setter
public class Projectile extends Collidable{
    private int size;
    private PVector Direction;
    private float speed;
    /**
     * Construct
     * @param x
     * @param y
     * @param Direction projectile moves in
     * @param speed
     * @param size projectiles are squares, this is the length of the sides
     */
    public Projectile(float x, float y, PVector Direction , float speed, int size) {
        super(x, y);

        this.Direction = Direction;
        this.speed = speed;
        this.size = size;

        BoundingBox b1 = new BoundingBox(this.getLocation(), size, size);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(b1);
        super.setBounds(b);

    }

    public void draw(PApplet sketch){
        sketch.pushMatrix();
        sketch.noStroke();
        sketch.fill(Colors.red.dark);
        sketch.rect(getLocation().x, getLocation().y, this.size, this.size);
        sketch.popMatrix();
    }

    /**
     * Move the projectile and its bounding box
     */
    public void move(){
        this.setLocation(PVector.add(this.getLocation(),( PVector.mult(this.Direction,(this.speed)))));
        for(BoundingBox b : this.getBounds()){
            b.moveBox(PVector.mult(this.Direction,(this.speed)));

        }
    }



}
