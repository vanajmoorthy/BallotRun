package cs4303.p4.entities;

import java.util.ArrayList;

import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4.items.Item;
import cs4303.p4.physics.BoundingBox;
import processing.core.PApplet;
import processing.core.PVector;

public class Entrance extends Entity {
    public Entrance(float x, float y) {
        super(x, y);
        ArrayList<BoundingBox> bounds = new ArrayList<BoundingBox>();
        bounds.add(new BoundingBox(
            super.getLocation(),
            30,
            45
        ));
        super.setBounds(bounds);
        super.setAcceleration(new PVector(0, 0));
        super.setVelocity(new PVector(0, 0));
        super.setInventory(new ArrayList<Item>());
        super.setMaxSlots(3);
        super.setMass(Constants.PLAYER.INSTANCE.MASS);
    }

    @Override
    public void draw(PApplet sketch) {
        sketch.fill(Colors.lime.darker);
        sketch.noStroke();
        sketch.rect(
            super.getLocation().x,
            super.getLocation().y,
            30,
            45
        );
    }
}
