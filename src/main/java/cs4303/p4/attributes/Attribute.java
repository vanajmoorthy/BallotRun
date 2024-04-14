package cs4303.p4.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attribute {
    Health(     '❤', 0xFFB31B1B, "Health",       10, AttributeModifierBehavior.ADD),
    Defence(    '❂', 0xFF00693E, "Defence",      0, AttributeModifierBehavior.ADD),
    Strength(   '❁', 0xFFEC5800, "Strength",     1, AttributeModifierBehavior.ADD),
    AttackSpeed('⚔', 0xFFFFBF00, "Attack Speed", 1, AttributeModifierBehavior.MULTIPLY),
    Speed(      '✧', 0xFFF8F8FF, "Speed",        1, AttributeModifierBehavior.MULTIPLY),
    JumpHeight( '⇑', 0xFF568203, "Jump Height",  1, AttributeModifierBehavior.MULTIPLY),
    Luck(       '✯', 0xFF318CE7, "Luck",         0, AttributeModifierBehavior.ADD);

    private final char symbol;
    private final int color;
    private final String displayName;
    private final float defaultValue;
    private final AttributeModifierBehavior defaultBehavior;
}
