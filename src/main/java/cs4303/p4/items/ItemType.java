package cs4303.p4.items;

import java.util.EnumMap;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {
    Chestplate(
        "Chestplate",
        getChestplateBaseAttributes()
    );

    private final String displayName;
    private final EnumMap<Attribute, AttributeModifier> baseAttributes;

    private static EnumMap<Attribute, AttributeModifier> getChestplateBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        return base;
    }
}
