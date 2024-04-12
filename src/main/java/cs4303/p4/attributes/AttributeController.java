package cs4303.p4.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import cs4303.p4.Entity;
import cs4303.p4.attributes.exceptions.InvalidBaseBehaviourException;
import cs4303.p4.items.Item;

public final class AttributeController {
    public static Collection<AttributeModifier> collectEntityAttributeValues(Entity entity, Attribute attribute) {
        List<AttributeModifier> attributeValues = new ArrayList<AttributeModifier>();

        if (entity.getBaseAttributes().containsKey(attribute)) {
            attributeValues.add(entity.getBaseAttributes().get(attribute));
        }

        entity.getInventory()
            .stream()
            .forEach((item) -> {
                if (item.getType().getBaseAttributes().containsKey(attribute)) {
                    attributeValues.add(item.getType().getBaseAttributes().get(attribute));
                }
            });

        return attributeValues;
    }

    public static float getEntityAttributeValue(Entity entity, Attribute attribute) {
        AttributeModifier baseModifier = entity.getBaseAttributes().get(attribute);
        float value = (baseModifier != null) ? baseModifier.getValue() : attribute.getDefaultValue();

        for (Item item : entity.getInventory()) {
            AttributeModifier itemModifier = item.getType().getBaseAttributes().get(attribute);
            if (itemModifier != null) {
                switch (itemModifier.getBehavior()) {
                    case ADD: value += itemModifier.getValue(); break;
                    case MULTIPLY: value *= itemModifier.getValue(); break;
                    case BASE: throw new InvalidBaseBehaviourException("item attributes");
                }
            }
        }
        
        return value;
    }

    public static EnumMap<Attribute, Float> getEntityAttributeValues(Entity entity) {
        EnumMap<Attribute, Float> atrributeValues = new EnumMap<Attribute, Float>(Attribute.class);

        for (Attribute attribute : Attribute.values()) {
            atrributeValues.put(attribute, getEntityAttributeValue(entity, attribute));
        }

        return atrributeValues;
    }
}
