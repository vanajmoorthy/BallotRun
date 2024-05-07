package cs4303.p4.items;

import cs4303.p4._util.Colors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rarity {
    Common(   "Common",    Colors.white, 0.33f),
    Uncommon( "Uncommon",  Colors.lime.light, 0.66f),
    Rare(     "Rare",      Colors.amber.primary, 1.15f),
    Epic(     "Epic",      Colors.pink.darker, 1.35f),
    Legendary("Legendary", Colors.teal.dark, 1.66f);

    private final String displayName;
    private final int color;
    private final float difficultyModifier;
}
