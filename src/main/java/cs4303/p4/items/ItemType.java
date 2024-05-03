package cs4303.p4.items;

import java.util.Arrays;
import java.util.EnumMap;

import cs4303.p4._util.gui.text.RichText;
import cs4303.p4._util.gui.text.TextNode;
import cs4303.p4._util.gui.text.TextSpan;
import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {
    Chestplate(
        "chestplate",
        Rarity.Common,
        "Chestplate",
        new RichText(),
        getChestplateBaseAttributes()
    ),
    ParliamentSword(
        "sword_parliament",
        Rarity.Epic,
        "The Parliament",
        new RichText(),
        getParliamentSwordBaseAttributes()
    ),
    CongressSword(
        "sword_congress",
        Rarity.Rare,
        "Sword of Congress",
        new RichText(),
        getCongressSwordBaseAttributes()
    ),
    Constinution(
        "constitution",
        Rarity.Legendary,
        "Constitution",
        new RichText(
            Arrays.<TextNode>asList(
                new TextSpan("In the annals of history, the Constitution stands as a revered relic, forged from the unity and wisdom of a nation in transition. Crafted through collective collaboration, it embodies the aspirations and values of its people, weaving a tapestry of governance from the tumult of revolution or the ashes of conflict. A testament to resilience and shared vision, its words serve as a beacon of hope for generations, guiding the path towards a brighter future.", null)
            )
        ),
        getConstitutionBaseAttributes()
    );

    private final String id;
    private final Rarity rarity;
    private final String displayName;
    private final RichText lore;
    private final EnumMap<Attribute, AttributeModifier> baseAttributes;

    private static EnumMap<Attribute, AttributeModifier> getChestplateBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Defence, new AttributeModifier(Attribute.Defence, 30));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getConstitutionBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Health, new AttributeModifier(Attribute.Health, 200));
        base.put(Attribute.Defence, new AttributeModifier(Attribute.Defence, 75));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getParliamentSwordBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Strength, new AttributeModifier(Attribute.Strength, 50));
        base.put(Attribute.AttackSpeed, new AttributeModifier(Attribute.AttackSpeed, -20));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getCongressSwordBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Strength, new AttributeModifier(Attribute.Strength, 10));
        base.put(Attribute.AttackSpeed, new AttributeModifier(Attribute.AttackSpeed, 25));
        return base;
    }
}
