package cs4303.p4.items;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rarity {
    Common(   "Common",    0xFFFFFFFF),
    Uncommon( "Uncommon",  0xFFFFCC33),
    Rare(     "Rare",      0xFF03C03C),
    Epic(     "Epic",      0xFF246BCE),
    Legendary("Legendary", 0xFF007A74);

    private final String displayName;
    private final int color;
}
