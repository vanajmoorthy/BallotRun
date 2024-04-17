package cs4303.p4.items;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {
    Chestplate(
        "chestplate",
        Rarity.Common,
        "Chestplate",
        Arrays.asList(),
        getChestplateBaseAttributes()
    ),
    Constinution(
        "constitution",
        Rarity.Legendary,
        "Constitution",
        Arrays.asList(),
        getConstitutionBaseAttributes()
    );

    private final String id;
    private final Rarity rarity;
    private final String displayName;
    private final List<Tuple2<String, Integer>> lore;
    private final EnumMap<Attribute, AttributeModifier> baseAttributes;

    private static EnumMap<Attribute, AttributeModifier> getChestplateBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getConstitutionBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        return base;
    }
}
