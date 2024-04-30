package cs4303.p4.states;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cs4303.p4.entities.Player;
import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4._util.gui.GestureDetector;
import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import cs4303.p4.items.Item;
import processing.core.PApplet;
import processing.core.PVector;

public final class GameStateBase extends GameState {
    private Player player;
    private Item selectedItem;
    private List<GestureDetector> buttons;

    public GameStateBase(Player player) {
        this.player = player;
        this.buttons = new ArrayList<GestureDetector>();
        for (int i = 0; i < player.getInventory().size(); i++) {
            Item item = player.getInventory().get(i);
            final int offset = i * 60;

            buttons.add(new GestureDetector(
                    (sketch, hitbox, hasHover, hasClick) -> {
                        sketch.fill(
                                hasHover
                                        ? Colors.night.lighter
                                        : Colors.night.primary);
                        sketch.noStroke();
                        sketch.rect(10 + offset, 10, 50, 50, 10);

                        sketch.noFill();
                        sketch.stroke(item.getType().getRarity().getColor());
                        sketch.strokeWeight(2);
                        sketch.rect(10 + offset + 5, 10 + 5, 40, 40, 5);
                    },
                    (sketch) -> {
                        // PApplet.println(item.getType().getDisplayName());
                        selectedItem = item;
                    },
                    new GestureDetector.Hitbox(
                            new PVector(10 + offset, 10),
                            new PVector(50, 50))));
        }
    }

    public void draw(PApplet sketch) {
        int itemX = Constants.Screen.Base.inventoryWidth - 300;

        sketch.fill(Colors.night.darker);
        sketch.noStroke();
        sketch.rect(0, 0, itemX, Constants.Screen.Base.inventoryHeight);

        boolean isAnyButtonFocused = false;
        for (GestureDetector button : buttons) {
            button.draw(sketch);
            if (button.hasFocus(sketch))
                isAnyButtonFocused = true;
        }
        sketch.cursor(
                isAnyButtonFocused
                        ? PApplet.HAND
                        : PApplet.ARROW);

        sketch.fill(Colors.night.dark);
        sketch.noStroke();
        sketch.rect(itemX, 0, 300, Constants.Screen.Base.inventoryHeight);

        sketch.textSize(20);
        if (selectedItem != null) {
            EnumMap<Attribute, AttributeModifier> itemAttributes = selectedItem.getType().getBaseAttributes();
            int i = 0;
            for (AttributeModifier modifier : itemAttributes.values()) {
                sketch.fill(modifier.getAttribute().getColor());
                sketch.text(modifier.getAttribute().getSymbol(), itemX + 10, 50 + 22 * i);

                float offset = sketch.textWidth(modifier.getAttribute().getSymbol() + " ");
                sketch.text(modifier.getAttribute().getDisplayName(), itemX + 10 + offset, 50 + 22 * i);

                offset += sketch.textWidth(modifier.getAttribute().getDisplayName());
                sketch.fill(Colors.platinum.dark);
                sketch.text(": " + Math.round(modifier.getValue()), itemX + 10 + offset, 50 + 22 * i);

                i++;
            }

            selectedItem.getType().getLore().draw(
                    sketch,
                    itemX + 10,
                    50 + 22 * (i + 1),
                    300 - 20,
                    20);
        }
    }

    public void keyPressed(PApplet sketch) {

    }

    public void keyReleased(PApplet sketch) {

    }

    public void mousePressed(PApplet sketch) {
        for (GestureDetector button : buttons) {
            if (button.hasFocus(sketch))
                button.click(sketch);
        }
    }

    public void mouseReleased(PApplet sketch) {

    }
}
