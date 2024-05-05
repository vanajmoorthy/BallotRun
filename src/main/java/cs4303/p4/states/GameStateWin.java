package cs4303.p4.states;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.util.ResourceUtils;

import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4.entities.Player;
import cs4303.p4.items.Item;
import cs4303.p4.items.ItemType;
import processing.core.PApplet;

public class GameStateWin extends GameState {
    private final Player player;
    private final List<Item> items;
    private final Item item;

    private int tickCounter = 300;

    public GameStateWin(Player player, List<Item> items) {
        this.player = player;
        Item newItem = new Item(ItemType.random());
        items.add(newItem);
        this.items = items;
        this.item = newItem;
    }

    @Override
    public GameState draw(PApplet sketch) {
        sketch.noStroke();
        float radius = (float) Math.hypot(
            Constants.Screen.width / 2,
            Constants.Screen.height / 2
        );

        int rayCount = 100;
        for (int i = 0; i < rayCount; i++) {
            sketch.fill(
                i % 2 == 0
                    ? Colors.darkGray.dark
                    : Colors.darkGray.darker
            );
            sketch.arc(
                Constants.Screen.width / 2,
                Constants.Screen.height / 2,
                radius * 2,
                radius * 2,
                (-tickCounter) + (PApplet.TWO_PI / rayCount) * i,
                (-tickCounter) + (PApplet.TWO_PI / rayCount) * (i + 1)
            );
        }

        sketch.fill(Colors.black & 0xa0ffffff);
        sketch.rect(0, 0, Constants.Screen.width, Constants.Screen.height);

        sketch.rectMode(PApplet.CENTER);

        sketch.noStroke();
        sketch.fill(Colors.darkGray.dark);
        sketch.rect(
            Constants.Screen.width / 2,
            Constants.Screen.height / 2,
            120,
            120,
            20
        );

        sketch.noFill();
        sketch.stroke(item.getType().getRarity().getColor());
        sketch.strokeWeight(2);
        sketch.rect(
            Constants.Screen.width / 2,
            Constants.Screen.height / 2,
            105,
            105,
            15
        );

        sketch.rectMode(PApplet.CORNER);

        try {
            sketch.image(
                sketch.loadImage(
                    ResourceUtils.getFile("classpath:textures/" + item.getType().getId() + ".png").getAbsolutePath()
                ),
                Constants.Screen.width / 2 - 45,
                Constants.Screen.height / 2 - 45,
                90,
                90
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sketch.fill(item.getType().getRarity().getColor());
        sketch.noStroke();
        sketch.textSize(40);
        sketch.textAlign(PApplet.CENTER, PApplet.BOTTOM);
        sketch.text(
            item.getType().getRarity().getDisplayName().toUpperCase(),
            Constants.Screen.width / 2,
            Constants.Screen.height / 2 - 150
        );
        sketch.text(
            "ARTIFACT FOUND",
            Constants.Screen.width / 2,
            Constants.Screen.height / 2 - 100
        );

        sketch.textSize(30);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
        sketch.text(
            item.getType().getDisplayName(),
            Constants.Screen.width / 2,
            Constants.Screen.height / 2 + 80
        );

        return tickCounter-- <= 0
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

    }
    
}
