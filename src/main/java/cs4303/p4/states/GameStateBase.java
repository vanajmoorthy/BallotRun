package cs4303.p4.states;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Item> items;
    private Item selectedItem;
    private List<GestureDetector> buttons;

    public GameStateBase(Player player, List<Item> items) {
        this.player = player;
        this.items = items;
        this.buttons = new ArrayList<GestureDetector>();
        final List<Item> itemsSorted = items
                .stream()
                .sorted(
                        (a, b) -> {
                            int rarity = -a.getType().getRarity().compareTo(b.getType().getRarity());
                            if (rarity != 0)
                                return rarity;
                            return a.getType().getDisplayName().compareTo(b.getType().getDisplayName());
                        })
                .collect(Collectors.toList());
        for (int i = 0; i < items.size(); i++) {
            Item item = itemsSorted.get(i);
            final int offset = i * 60;

            buttons.add(
                    new GestureDetector(
                            (sketch, hitbox, hasHover, hasClick) -> {
                                sketch.fill(
                                        hasHover
                                                ? Colors.darkGray.primary
                                                : Colors.darkGray.dark);
                                sketch.noStroke();
                                sketch.rect(10 + offset, 10, 50, 50, 10);

                                sketch.noFill();
                                sketch.stroke(item.getType().getRarity().getColor());
                                sketch.strokeWeight(2);
                                sketch.rect(10 + offset + 5, 10 + 5, 40, 40, 5);
                            },
                            (sketch, button) -> {
                                // PApplet.println(item.getType().getDisplayName());
                                selectedItem = item;
                            },
                            new GestureDetector.Hitbox(
                                    new PVector(10 + offset, 10),
                                    new PVector(50, 50))));
        }

        buttons.add(
                new GestureDetector(
                        (sketch, hitbox, hasHover, hasClick) -> {
                            if (selectedItem != null) {
                                sketch.fill(
                                        hasHover
                                                ? Colors.darkGray.primary
                                                : Colors.darkGray.light);
                                sketch.noStroke();
                                sketch.rect(
                                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                                        Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height
                                                - Constants.Screen.Base.padding - 40,
                                        Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
                                        40,
                                        10);
                            }
                        },
                        (sketch, button) -> {
                            if (selectedItem != null) {
                                player.addItem(selectedItem);
                            }
                        },
                        new GestureDetector.Hitbox(
                                new PVector(
                                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                                        Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height
                                                - Constants.Screen.Base.padding - 40

                                ),
                                new PVector(
                                        Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
                                        40))));
    }

    public void draw(PApplet sketch) {
        // screen
        sketch.fill(Colors.black);
        sketch.noStroke();
        sketch.rect(0, 0, Constants.Screen.width, Constants.Screen.height);

        // selected item
        sketch.fill(Colors.darkGray.dark);
        sketch.noStroke();
        sketch.rect(
                Constants.Screen.Base.selection.x,
                Constants.Screen.Base.selection.y,
                Constants.Screen.Base.selection.width,
                Constants.Screen.Base.selection.height,
                15);

        sketch.textSize(20);
        if (selectedItem != null) {
            final int itemIconSize = 70;

            // item icon
            sketch.fill(Colors.darkGray.primary);
            sketch.noStroke();
            sketch.rect(
                    Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                    Constants.Screen.Base.selection.y + Constants.Screen.Base.padding,
                    itemIconSize,
                    itemIconSize,
                    10);

            sketch.noFill();
            sketch.stroke(selectedItem.getType().getRarity().getColor());
            sketch.strokeWeight(2);
            sketch.rect(
                    Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 5,
                    Constants.Screen.Base.selection.y + Constants.Screen.Base.padding + 5,
                    itemIconSize - 10,
                    itemIconSize - 10,
                    5);

            // item title
            sketch.textSize(35);
            sketch.textAlign(PApplet.LEFT, PApplet.TOP);
            sketch.fill(selectedItem.getType().getRarity().getColor());
            sketch.noStroke();
            sketch.text(
                    selectedItem.getType().getDisplayName(),
                    Constants.Screen.Base.selection.x + 2 * Constants.Screen.Base.padding + itemIconSize,
                    Constants.Screen.Base.selection.y + Constants.Screen.Base.padding);

            sketch.textSize(20);
            sketch.textAlign(PApplet.LEFT, PApplet.BOTTOM);
            sketch.text(
                    selectedItem.getType().getRarity().getDisplayName().toUpperCase(),
                    Constants.Screen.Base.selection.x + 2 * Constants.Screen.Base.padding + itemIconSize,
                    Constants.Screen.Base.selection.y + Constants.Screen.Base.padding + itemIconSize);

            sketch.textAlign(PApplet.LEFT, PApplet.TOP);

            // attributes
            EnumMap<Attribute, AttributeModifier> itemAttributes = selectedItem.getType().getBaseAttributes();
            int i = 0;
            for (AttributeModifier modifier : itemAttributes.values()) {
                sketch.fill(modifier.getAttribute().getColor());
                sketch.text(
                        modifier.getAttribute().getSymbol(),
                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                        Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i);

                float offset = sketch.textWidth(modifier.getAttribute().getSymbol() + " ");
                sketch.text(
                        modifier.getAttribute().getDisplayName(),
                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offset,
                        Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i);

                offset += sketch.textWidth(modifier.getAttribute().getDisplayName());
                sketch.fill(Colors.white);
                sketch.text(
                        ": ",
                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offset,
                        Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i);

                offset += sketch.textWidth(": ");
                sketch.fill(modifier.getValue() >= 0 ? Colors.white : Colors.red.darker);
                sketch.text(
                        Math.round(modifier.getValue()),
                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offset,
                        Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i);

                i++;
            }

            // lore
            selectedItem.getType().getLore().draw(
                    sketch,
                    Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                    Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * (i + 1),
                    Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
                    20);
        } else {
            sketch.noStroke();
            sketch.fill(Colors.neutral.dark);
            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
            sketch.text(
                    "SELECT AN ITEM",
                    Constants.Screen.Base.selection.x + Constants.Screen.Base.selection.width / 2,
                    Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height / 2);
        }

        // all items
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
    }

    public void keyPressed(PApplet sketch) {

    }

    public void update(float deltaTime) {

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
