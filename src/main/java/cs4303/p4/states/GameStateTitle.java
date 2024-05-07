package cs4303.p4.states;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4._util.gui.GestureDetector;
import cs4303.p4.entities.Player;
import cs4303.p4.items.Item;
import cs4303.p4.items.ItemType;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SoundFile;

public class GameStateTitle extends GameState {
    private final Player player;
    private final List<Item> items;

    private boolean didSwitchState = false;
    PImage playerImage;

    public GameStateTitle(PApplet sketch) {
        try {
            playerImage = sketch
                    .loadImage(ResourceUtils.getFile("classpath:textures/" + "player" + ".png").getAbsolutePath());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.player = new Player(0, 0, playerImage);
        this.items = new ArrayList<Item>();

        items.add(new Item(ItemType.Chestplate));
        items.add(new Item(ItemType.Constinution));
        items.add(new Item(ItemType.ParliamentSword));
        items.add(new Item(ItemType.CongressSword));
        items.add(new Item(ItemType.Bribe));
        items.add(new Item(ItemType.HealthTalisman));
        items.add(new Item(ItemType.GlobalizationCharm));
        items.add(new Item(ItemType.Bill));
        items.add(new Item(ItemType.FancySuit));
        items.add(new Item(ItemType.DeepestConcerns));
        items.add(new Item(ItemType.TradeTreaty));
    }

    private int cursor = PApplet.ARROW;
    private GestureDetector buttonStartGame = new GestureDetector((sketch, hitbox, hasHover, hasClick) -> {
        if (hasHover)
            cursor = PApplet.HAND;

        sketch.fill(hasHover ? Colors.emerald.primary : Colors.emerald.light);
        sketch.noStroke();
        sketch.rect(Constants.Screen.width / 2 - 100, Constants.Screen.height / 2 + 50 - 20, 200, 40, 10);

        sketch.fill(Colors.white);
        sketch.textSize(22);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.text("START GAME", Constants.Screen.width / 2, Constants.Screen.height / 2 + 50 - 20 + 17.5f);
    }, (sketch, button) -> {
        didSwitchState = true;
    }, new GestureDetector.Hitbox(new PVector(Constants.Screen.width / 2 - 100, Constants.Screen.height / 2 + 50 - 20),
            new PVector(200, 40)));

    @Override
    public GameState draw(PApplet sketch) {
        sketch.background(Colors.black);
        cursor = PApplet.ARROW;

        sketch.fill(Colors.white);
        sketch.noStroke();
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.textSize(50);
        sketch.text("BALLOT RUN", Constants.Screen.width / 2, Constants.Screen.height / 2 - 150);

        buttonStartGame.draw(sketch);

        sketch.cursor(cursor);

        return didSwitchState ? new GameStateBase(player, items) : null;
    }

    @Override
    public void keyPressed(PApplet sketch) {

    }

    @Override
    public void keyReleased(PApplet sketch) {

    }

    @Override
    public void mousePressed(PApplet sketch) {
        if (buttonStartGame.hasFocus(sketch))
            buttonStartGame.click(sketch);
    }

    @Override
    public void mouseReleased(PApplet sketch) {

    }

    @Override
    public void update(float deltaTime) {

    }

}
