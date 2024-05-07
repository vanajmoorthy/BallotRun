package cs4303.p4.entities;

import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4.map.Node;
import cs4303.p4.physics.BoundingBox;
import cs4303.p4.physics.Bullet;
import cs4303.p4.physics.Projectile;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class Enemy extends Entity{
    private int bullet_speed;
    private float range;
    private long lastFireTime;
    private int cameraOffsetX;
    private int size;
    /**
     * Basic constructor 
     * @param x
     * @param y
     */
    public Enemy(float x, float y,float range, float difficulty) {
        super(x, y);
        this.size = 20;
        this.range  = range;
        super.setMass(Constants.PLAYER.INSTANCE.MASS);
        this.bullet_speed = (int) PApplet.map(difficulty, 0, 5, 1, 4);
        // initialise mass and acceleration to 0
        super.setAcceleration(new PVector(0, 0));
        super.setVelocity(new PVector(0, 0));
        this.cameraOffsetX = 0;

        // create bounding box

        BoundingBox b1 = new BoundingBox(this.getLocation(), 20, 20);
        ArrayList<BoundingBox> b = new ArrayList<BoundingBox>();
        b.add(b1);
        super.setBounds(b);
    }

    @Override
    public void draw(PApplet sketch) {
        sketch.pushMatrix();
        sketch.noStroke();
        sketch.fill(Colors.red.primary);
        sketch.rect(getLocation().x, getLocation().y, 20, 20);

        sketch.popMatrix();
    }

    /**
     * Fires a projectile
     * @param direction
     * @return
     */
    public Projectile fire(Player player, List<Node> nodes) {

        long currentTimeMillis = System.currentTimeMillis();
        //fire
        if (currentTimeMillis - lastFireTime > 1500) {
            //check if the player is in range
            if(checkForPlayer(player,nodes)){
                PVector dir = PVector.sub(player.getLocation().copy(),this.getLocation().copy());
                dir.normalize();
                Bullet p = new Bullet(this.getLocation().x - 5, this.getLocation().y, dir,bullet_speed,10);
                this.lastFireTime = currentTimeMillis;
                return p;
            }



        }
        return null;
    }


    /**
     * Use raycasting to detect the player
     * @param player
     * @return
     */
    private boolean checkForPlayer(Player player, List<Node> nodes){
        //get the direction to the player
        PVector dir = PVector.sub(player.getLocation().copy(),this.getLocation().copy());
        if(dir.mag() > this.range){
            return false;
        }
        dir.normalize();

        //startloc offset
        PVector startLoc = new PVector(this.getLocation().x + 5, this.getLocation().y + 5);

        Projectile p = new Projectile(startLoc.x,startLoc.y,dir, 0.5F,1);

        Boolean collision = false;
        Boolean playerInRange = false;
        while (!collision){

            //move the ray
            p.move();

            //check for collision with wall
            for (Node node : nodes){
                if (p.Collision(node)){
                    //projectile hits a wall
                    collision = true;
                }
            }

            //check for collision with player
            if (p.Collision(player)){
                //work out distance between enemy and the player
                PVector vectorToPlayer = PVector.sub(player.getLocation().copy(),this.getLocation().copy());
                float distanceToPlayer = vectorToPlayer.mag();
                if(distanceToPlayer <= this.range){

                    playerInRange = true;

                }
                collision = true;

            }

        }
        return playerInRange;
    }

   }
