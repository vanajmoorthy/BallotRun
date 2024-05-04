package cs4303.p4.attributes;

import cs4303.p4._util.Colors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
    Health(     '❤', Colors.red.dark, "Health",       10, AttributeModifierBehavior.ADD),
    Defence(    '❂', Colors.green.dark, "Defence",      0, AttributeModifierBehavior.ADD),
    Strength(   '❁', Colors.rose.darker, "Strength",     100, AttributeModifierBehavior.ADD),
    AttackSpeed('⚔', Colors.yellow.light, "Attack Speed", 100, AttributeModifierBehavior.ADD),
    Speed(      '✧', Colors.slate.lighter, "Speed",        100, AttributeModifierBehavior.ADD),
    JumpHeight( '⇑', Colors.sky.primary, "Jump Height",  100, AttributeModifierBehavior.ADD),
    Luck(       '✯', Colors.emerald.dark, "Luck",         0, AttributeModifierBehavior.ADD);

    private final char symbol;
    private final int color;
    private final String displayName;
    private final float defaultValue;
    private final AttributeModifierBehavior defaultBehavior;
}
