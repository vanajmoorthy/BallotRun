package cs4303.p4.items;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.concurrent.ThreadLocalRandom;

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
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("Molded from the steely resolve of countless campaigns, this chestplate absorbs the impact of direct confrontations, reducing damage taken and symbolizing the shield of conviction against adversity.", null)
        )),
        getChestplateBaseAttributes()
    ),
    ParliamentSword(
        "sword_parliament",
        Rarity.Epic,
        "The Parliament",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("A robust gavel once wielded in the grand halls of The Parliament, this artifact unifies voices of reason and passion. It amplifies your influence, allowing commands and pleas to resonate further and gives you strength. However, due to legislative gridlock, it makes you slower.", null)
        )),
        getParliamentSwordBaseAttributes()
    ),
    CongressSword(
        "sword_congress",
        Rarity.Rare,
        "Sword of Congress",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("This blade was smithed in the crucible of heated debates and sharp wit. The Sword of Congress cuts through deception and slices away the chains of oppression with each swing, delivering justice with precision. It gives you strength and slower attack speed.", null)
        )),
        getCongressSwordBaseAttributes()
    ),
    GlobalizationCharm(
        "globalization_charm",
        Rarity.Rare,
        "Globalization Charm",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("Forged from the essence of interconnected markets, this charm widens your perspective, revealing hidden paths and shortcuts. It represents the inevitable tide of shared destinies and mutual benefits and gives you more attack speed and strength.", null)
        )),
        getGlobalizationCharmBaseAttributes()
    ),
    HealthTalisman(
        "health_talisman",
        Rarity.Uncommon,
        "Health Talisman",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("Carved from ancient woods harboring the spirits of healing, this talisman slowly restores vitality, embodying the resilience and recovery of nature itself.", null)
        )),
        getHealthTalismanBaseAttributes()
    ),
    Bribe(
        "bribe",
        Rarity.Uncommon,
        "Bribe",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("A discreet pouch filled with untraceable currency, the Bribe can turn foes into temporary allies. Use it wisely, as the power of corruption is seductive and may lead to unexpected consequences like reduced luck.", null)
        )),
        getBribeBaseAttributes()
    ),
    Bill(
        "bill",
        Rarity.Rare,
        "Bill",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("Inked with the urgency of change, the Bill is a powerful document that can reshape reality in minor ways. When activated, it momentarily alters the game's rules, providing luck to the bearer. However, due to legislative gridlock, it makes you slower.", null)
        )),
        getBillBaseAttributes()
    ),
    FancySuit(
        "fancy_suit",
        Rarity.Uncommon,
        "Fancy Suit",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("Tailored by a master from threads of influence and charm, wearing this Fancy Suit can open locked doors and gates, as guards and enemies hesitate to halt such an impressive figure.", null)
        )),
        getFancySuitAttributes()
    ),
    TradeTreaty(
        "trade_treaty",
        Rarity.Epic,
        "Trade Treaty",
        new RichText(Arrays.<TextNode>asList(
            new TextSpan("Bound in the leather of diplomacy, the Trade Treaty is a scroll granting economic foresight.", null)
        )),
        getTradeTreatyAttributes()
    ),
    DeepestConcerns(
        "sword_deepest_concerns",
        Rarity.Legendary,
        "Deepest Concerns",
        new RichText(
            Arrays.<TextNode>asList(
                new TextSpan("We are deeply concerned about democratic backsliding in our nation.", null)
            )
        ),
        getDeepestConcernsAttributes()
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

    public static ItemType random() {
        int itemI = ThreadLocalRandom.current().nextInt(0, ItemType.values().length);
        return ItemType.values()[itemI];
    }

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
        base.put(Attribute.Strength, new AttributeModifier(Attribute.Strength, 35));
        base.put(Attribute.AttackSpeed, new AttributeModifier(Attribute.AttackSpeed, -10));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getGlobalizationCharmBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Strength, new AttributeModifier(Attribute.Strength, 10));
        base.put(Attribute.Defence, new AttributeModifier(Attribute.Defence, -30));
        base.put(Attribute.Speed, new AttributeModifier(Attribute.AttackSpeed, 45));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getHealthTalismanBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Health, new AttributeModifier(Attribute.Health, 50));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getBribeBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Luck, new AttributeModifier(Attribute.Luck, -20));
        base.put(Attribute.Strength, new AttributeModifier(Attribute.Strength, 20));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getBillBaseAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Luck, new AttributeModifier(Attribute.Luck, 20));
        base.put(Attribute.Speed, new AttributeModifier(Attribute.Speed, -20));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getFancySuitAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Health, new AttributeModifier(Attribute.Health, 20));
        base.put(Attribute.Defence, new AttributeModifier(Attribute.Defence, 40));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getTradeTreatyAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Health, new AttributeModifier(Attribute.Health, 40));
        base.put(Attribute.Speed, new AttributeModifier(Attribute.Speed, 20));
        return base;
    }

    private static EnumMap<Attribute, AttributeModifier> getDeepestConcernsAttributes() {
        EnumMap<Attribute, AttributeModifier> base = new EnumMap<Attribute, AttributeModifier>(Attribute.class);
        base.put(Attribute.Strength, new AttributeModifier(Attribute.Strength, 50));
        base.put(Attribute.Defence, new AttributeModifier(Attribute.Defence, -40));
        return base;
    }
}
