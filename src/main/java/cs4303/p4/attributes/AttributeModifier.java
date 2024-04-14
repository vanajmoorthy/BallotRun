package cs4303.p4.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class AttributeModifier {
    private final Attribute attribute;
    private final AttributeModifierBehavior behavior;
    private final float value;

    public AttributeModifier(Attribute attribute, float value) {
        this(attribute, attribute.getDefaultBehavior(), value);
    }
}
