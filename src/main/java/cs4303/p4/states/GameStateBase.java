package cs4303.p4.states;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import cs4303.p4.entities.Player;
import cs4303.p4._util.Colors;
import cs4303.p4._util.Constants;
import cs4303.p4._util.gui.GestureDetector;
import cs4303.p4._util.gui.ImageController;
import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeController;
import cs4303.p4.attributes.AttributeModifier;
import cs4303.p4.items.Item;
import processing.core.PApplet;
import processing.core.PVector;

public final class GameStateBase extends GameState {
    private Player player;
    private List<Item> items;
    private Item selectedItem;
    private List<GestureDetector> buttons;

    private int cursor = PApplet.ARROW;
    private boolean didStartGame = false;

    // private ImageController imageController;

    // @Autowired
    // public void 

    private final GestureDetector buttonEqip = new GestureDetector(
        (sketch, hitbox, hasHover, hasClick) -> {
            boolean inventoryHasSpace = !player.isInventoryFull();
            boolean isItemEquipped = selectedItem != null && player.getInventory().contains(selectedItem);

            if (hasHover && (isItemEquipped || inventoryHasSpace))
                cursor = PApplet.HAND;

            sketch.fill(
                hasHover && (isItemEquipped || inventoryHasSpace)
                    ? Colors.darkGray.light
                    : Colors.darkGray.primary
            );
            sketch.noStroke();
            sketch.rect(
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height
                        - Constants.Screen.Base.padding - 40,
                Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
                40,
                10
            );

            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
            sketch.fill(Colors.white);
            sketch.text(
                isItemEquipped
                    ? "UNEQUIP"
                    : inventoryHasSpace
                        ? "EQUIP"
                        : "FULL INVENTORY",
                Constants.Screen.Base.selection.x + Constants.Screen.Base.selection.width / 2,
                Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height - Constants.Screen.Base.padding - 20
            );
        },
        (sketch, button) -> {
            boolean inventoryHasSpace = !player.isInventoryFull();
            boolean isItemEquipped = selectedItem != null && player.getInventory().contains(selectedItem);

            if (isItemEquipped) {
                player.removeItem(selectedItem);
                selectedItem = null;
            } else if (inventoryHasSpace) {
                player.addItem(selectedItem);
                selectedItem = null;
            }
        },
        new GestureDetector.Hitbox(
            new PVector(
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height - Constants.Screen.Base.padding - 40),
            new PVector(
                Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
                40)
        )
    );

    private final GestureDetector buttonPlay = new GestureDetector(
        (sketch, hitbox, hasHover, hasClick) -> {
            if (hasHover)
                cursor = PApplet.HAND;

            sketch.fill(
                hasHover
                    ? Colors.darkGray.light
                    : Colors.darkGray.primary
            );
            sketch.noStroke();
            sketch.rect(
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height
                        - Constants.Screen.Base.padding - 40,
                Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
                40,
                10
            );

            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
            sketch.fill(Colors.white);
            sketch.text(
                "PLAY",
                Constants.Screen.Base.selection.x + Constants.Screen.Base.selection.width / 2,
                Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height - Constants.Screen.Base.padding - 20
            );
        },
        (sketch, button) -> {
            didStartGame = true;
        },
        new GestureDetector.Hitbox(
            new PVector(
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height - Constants.Screen.Base.padding - 40
            ),
            new PVector(
                Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
                40
            )
        )
    );

    private final GestureDetector buttonClose = new GestureDetector(
        (sketch, hitbox, hasHover, hasClick) -> {
            int width = 30;
            int height = 30;
            int x = Constants.Screen.width - 2 * Constants.Screen.Base.padding - width;
            int y = Constants.Screen.Base.selection.y + Constants.Screen.Base.padding;
            float padding = 7.5f;

            if (hasHover)
                cursor = PApplet.HAND;

            sketch.fill(
                hasHover
                    ? Colors.darkGray.light
                    : Colors.darkGray.primary
            );
            sketch.noStroke();
            sketch.rect(
                x,
                y,
                width,
                height,
                5
            );

            sketch.stroke(Colors.white);
            sketch.strokeWeight(4);
            sketch.line(
                x + padding,
                y + padding,
                x + width - padding,
                y + height - padding
            );
            sketch.line(
                x + padding,
                y + width - padding,
                x + height - padding,
                y + padding
            );
        },
        (sketch, button) -> {
            selectedItem = null;
        },
        new GestureDetector.Hitbox(
            new PVector(
                    Constants.Screen.width - 2 * Constants.Screen.Base.padding - 30,
                    Constants.Screen.Base.selection.y + Constants.Screen.Base.padding
            ),
            new PVector(30, 30)
        )
    );

    private GestureDetector[] buttonsInventory = new GestureDetector[3];

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
                }
            )
            .collect(Collectors.toList());
        for (int i = 0; i < items.size(); i++) {
            Item item = itemsSorted.get(i);

            int itemsPerLine = (Constants.Screen.Base.storage.width - Constants.Screen.Base.padding) /
                    (50 + Constants.Screen.Base.padding);
            int offsetX = (i % itemsPerLine) * (50 + Constants.Screen.Base.padding);
            int offsetY = (i / itemsPerLine) * (50 + Constants.Screen.Base.padding);

            buttons.add(
                new GestureDetector(
                    (sketch, hitbox, hasHover, hasClick) -> {
                        if (hasHover)
                            cursor = PApplet.HAND;

                        sketch.fill(
                            hasHover
                                ? Colors.darkGray.primary
                                : Colors.darkGray.dark
                        );
                        sketch.noStroke();
                        sketch.rect(10 + offsetX, 10 + offsetY, 50, 50, 10);

                        sketch.noFill();
                        sketch.stroke(item.getType().getRarity().getColor());
                        sketch.strokeWeight(2);
                        sketch.rect(10 + offsetX + 5, 10 + offsetY + 5, 40, 40, 5);

                        // TODO: this is no good but it works
                        try {
                            sketch.image(
                                sketch.loadImage(
                                    ResourceUtils
                                        .getFile("classpath:textures/" + item.getType().getId() + ".png")
                                        .getAbsolutePath()
                                    // new ClassPathResource("classpath:textures/" + item.getType().getId() + ".png").getFilename()
                                ),
                                10 + offsetX + 9,
                                10 + offsetY + 9,
                                32,
                                32
                            );
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    },
                    (sketch, button) -> {
                        // PApplet.println(item.getType().getDisplayName());
                        selectedItem = item;
                    },
                    new GestureDetector.Hitbox(
                        new PVector(10 + offsetX, 10 + offsetY),
                        new PVector(50, 50)
                    )
                )
            );
        }

        for (int j = 0; j < buttonsInventory.length; j++) {
            final int i = j;
            final int offsetY = i * 55 + 50;
            buttonsInventory[i] = new GestureDetector(
                (sketch, hitbox, hasHover, hasClick) -> {
                    Item item = player.getInventory().size() > player.getMaxSlots() - 1 - i
                        ? player.getInventory().get(player.getMaxSlots() - 1 - i)
                        : null;

                    if (hasHover && item != null)
                        cursor = PApplet.HAND;

                    sketch.fill(
                        hasHover && item != null
                            ? Colors.darkGray.light
                            : Colors.darkGray.primary
                    );
                    sketch.noStroke();
                    sketch.rect(
                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                        Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY,
                        50,
                        50,
                        10
                    );

                    sketch.noFill();
                    sketch.stroke(
                        item != null
                            ? item.getType().getRarity().getColor()
                            : Colors.darkGray.lighter
                    );
                    sketch.strokeWeight(2);
                    sketch.rect(
                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 5,
                        Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY + 5,
                        40,
                        40,
                        5
                    );

                    if (item != null) {
                        // TODO: this is no good but it works
                        try {
                            sketch.image(
                                sketch.loadImage(
                                        ResourceUtils
                                                .getFile(
                                                        "classpath:textures/" + item.getType().getId() + ".png")
                                                .getAbsolutePath()),
                                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 9,
                                Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY + 9,
                                32,
                                32
                            );
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    sketch.fill(
                        item != null
                            ? item.getType().getRarity().getColor()
                            : Colors.darkGray.lighter
                    );
                    sketch.noStroke();
                    sketch.textAlign(PApplet.LEFT, PApplet.CENTER);
                    sketch.text(
                        item != null
                            ? item.getType().getDisplayName()
                            : "Empty",
                        Constants.Screen.Base.selection.x + 2 * Constants.Screen.Base.padding + 50,
                        Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY + 22.5f);

                    if (hasHover && item != null) {
                        sketch.fill(Colors.black & 0x88FFFFFF);
                        sketch.noStroke();
                        sketch.rect(
                            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                            Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY,
                            50,
                            50,
                            10
                        );

                        sketch.noFill();
                        sketch.stroke(Colors.white);
                        sketch.strokeWeight(4);
                        sketch.line(
                            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 15,
                            Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY + 15,
                            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 15 + 20,
                            Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY + 15 + 20
                        );
                        sketch.line(
                            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 15,
                            Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY + 15 + 20,
                            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 15 + 20,
                            Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY + 15
                        );
                    }
                },
                (sketch, button) -> {
                    Item item = player.getInventory().size() > player.getMaxSlots() - 1 - i
                        ? player.getInventory().get(player.getMaxSlots() - 1 - i)
                        : null;

                    if (item != null) {
                        player.removeItem(item);
                    }
                },
                new GestureDetector.Hitbox(
                    new PVector(
                        Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                        Constants.Screen.height - 3 * Constants.Screen.Base.padding - 40 - offsetY
                    ),
                    new PVector(
                        50,
                        50
                    )
                )
            );
        }
    }

    public GameState draw(PApplet sketch) {
        // screen
        cursor = PApplet.ARROW;
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
            15
        );

        sketch.textSize(20);
        if (selectedItem != null) {
            drawSelectedItem(sketch);
        } else {
            drawInventory(sketch);
        }

        // all items
        for (GestureDetector button : buttons) {
            button.draw(sketch);
        }

        sketch.cursor(cursor);
        return didStartGame
            ? new GameStateGameplay(sketch, player, items)
            : null;
    }

    private void drawInventory(PApplet sketch) {
        // sketch.noStroke();
        // sketch.fill(Colors.neutral.dark);
        // sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        // sketch.text(
        // "SELECT AN ITEM",
        // Constants.Screen.Base.selection.x + Constants.Screen.Base.selection.width /
        // 2,
        // Constants.Screen.Base.selection.y + Constants.Screen.Base.selection.height /
        // 2
        // );

        sketch.textAlign(PApplet.LEFT, PApplet.CENTER);
        sketch.textSize(20);

        int offsetY = 0;
        for (Attribute attribute : Attribute.values()) {
            sketch.fill(attribute.getColor());
            sketch.text(
                attribute.getSymbol(),
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + offsetY
            );

            float offsetX = sketch.textWidth(attribute.getSymbol() + " ");
            sketch.text(
                attribute.getDisplayName(),
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offsetX,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + offsetY
            );

            offsetX += sketch.textWidth(attribute.getDisplayName());
            sketch.fill(Colors.white);
            sketch.text(
                ": ",
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offsetX,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + offsetY
            );

            offsetX += sketch.textWidth(": ");

            float value = AttributeController.getEntityAttributeValue(player, attribute);

            sketch.fill(value >= 0 ? Colors.white : Colors.red.darker);
            sketch.text(
                Math.round(value),
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offsetX,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + offsetY
            );

            offsetY += 22;
        }

        for (GestureDetector button : buttonsInventory) {
            button.draw(sketch);
        }

        buttonPlay.draw(sketch);
    }

    private void drawSelectedItem(PApplet sketch) {
        final int itemIconSize = 70;

        // item icon
        sketch.fill(Colors.darkGray.primary);
        sketch.noStroke();
        sketch.rect(
            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
            Constants.Screen.Base.selection.y + Constants.Screen.Base.padding,
            itemIconSize,
            itemIconSize,
            10
        );

        sketch.noFill();
        sketch.stroke(selectedItem.getType().getRarity().getColor());
        sketch.strokeWeight(2);
        sketch.rect(
            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 5,
            Constants.Screen.Base.selection.y + Constants.Screen.Base.padding + 5,
            itemIconSize - 10,
            itemIconSize - 10,
            5
        );

        // sketch.image(
        //     imageController.get(sketch, "classpath:textures/" + selectedItem.getType().getId() + ".png"),
        //     Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 10,
        //     Constants.Screen.Base.selection.y + Constants.Screen.Base.padding + 10,
        //     itemIconSize - 20,
        //     itemIconSize - 20
        // );

        // TODO: this is no good but it works
        try {
            sketch.image(
                sketch.loadImage(
                    ResourceUtils
                        .getFile("classpath:textures/" + selectedItem.getType().getId() + ".png")
                        .getAbsolutePath()),
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + 10,
                Constants.Screen.Base.selection.y + Constants.Screen.Base.padding + 10,
                itemIconSize - 20,
                itemIconSize - 20
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // item title
        sketch.textSize(35);
        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        sketch.fill(selectedItem.getType().getRarity().getColor());
        sketch.noStroke();
        sketch.text(
            selectedItem.getType().getDisplayName(),
            Constants.Screen.Base.selection.x + 2 * Constants.Screen.Base.padding + itemIconSize,
            Constants.Screen.Base.selection.y + Constants.Screen.Base.padding
        );

        sketch.textSize(20);
        sketch.textAlign(PApplet.LEFT, PApplet.BOTTOM);
        sketch.text(
            selectedItem.getType().getRarity().getDisplayName().toUpperCase(),
            Constants.Screen.Base.selection.x + 2 * Constants.Screen.Base.padding + itemIconSize,
            Constants.Screen.Base.selection.y + Constants.Screen.Base.padding + itemIconSize
        );

        sketch.textAlign(PApplet.LEFT, PApplet.TOP);

        // attributes
        EnumMap<Attribute, AttributeModifier> itemAttributes = selectedItem.getType().getBaseAttributes();
        List<AttributeModifier> modifiers = itemAttributes.values()
            .stream()
            .sorted((a, b) -> a.getAttribute().compareTo(b.getAttribute()))
            .collect(Collectors.toList());
        int i = 0;
        sketch.textSize(20);
        for (AttributeModifier modifier : modifiers) {
            sketch.fill(modifier.getAttribute().getColor());
            sketch.text(
                modifier.getAttribute().getSymbol(),
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i
            );

            float offset = sketch.textWidth(modifier.getAttribute().getSymbol() + " ");
            sketch.text(
                modifier.getAttribute().getDisplayName(),
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offset,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i
            );

            offset += sketch.textWidth(modifier.getAttribute().getDisplayName());
            sketch.fill(Colors.white);
            sketch.text(
                ": ",
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offset,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i
            );

            offset += sketch.textWidth(": ");
            sketch.fill(modifier.getValue() >= 0 ? Colors.white : Colors.red.darker);
            sketch.text(
                Math.round(modifier.getValue()),
                Constants.Screen.Base.selection.x + Constants.Screen.Base.padding + offset,
                Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * i
            );

            i++;
        }

        // lore
        selectedItem.getType().getLore().draw(
            sketch,
            Constants.Screen.Base.selection.x + Constants.Screen.Base.padding,
            Constants.Screen.Base.selection.y + 2 * Constants.Screen.Base.padding + itemIconSize + 22 * (i + 1),
            Constants.Screen.Base.selection.width - 2 * Constants.Screen.Base.padding,
            20
        );

        buttonEqip.draw(sketch);
        buttonClose.draw(sketch);
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

        if (selectedItem != null) {
            if (buttonEqip.hasFocus(sketch))
                buttonEqip.click(sketch);
            if (buttonClose.hasFocus(sketch))
                buttonClose.click(sketch);
        } else {
            for (GestureDetector button : buttonsInventory) {
                if (button.hasFocus(sketch))
                    button.click(sketch);
            }
            if (buttonPlay.hasFocus(sketch))
                buttonPlay.click(sketch);
        }
    }

    public void mouseReleased(PApplet sketch) {

    }

    @Override
    public void update(float deltaTime) {

    }
}
