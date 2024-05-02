package cs4303.p4.items;

import cs4303.p4._util.Colors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rarity {
    Common(   "Common",    Colors.white),
    Uncommon( "Uncommon",  Colors.lime.light),
    Rare(     "Rare",      Colors.amber.primary),
    Epic(     "Epic",      Colors.pink.darker),
    Legendary("Legendary", Colors.teal.dark);

    private final String displayName;
    private final int color;
}
