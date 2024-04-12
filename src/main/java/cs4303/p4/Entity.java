package cs4303.p4;

import java.util.ArrayList;
import java.util.EnumMap;

import cs4303.p4.attributes.Attribute;
import cs4303.p4.attributes.AttributeModifier;
import cs4303.p4.items.Item;
import lombok.Getter;

@Getter
public class Entity extends Collidable{
    private EnumMap<Attribute, AttributeModifier> baseAttributes;
    private ArrayList<Item> inventory;

    public Entity(Game g, float x, float y, EnumMap<Attribute, AttributeModifier> baseAttributes) {
        super(g, x, y);
        this.baseAttributes = baseAttributes;
    }

    public Entity(Game g, float x, float y) {
        this(g, x, y, new EnumMap<Attribute, AttributeModifier>(Attribute.class));
    }

    /**
     * Check for collisions between this entity and another
     * @param e entity colliding with this entity
     * @return true if the collision occurs
     */
    public boolean entityCollision(Entity e){
        return false;
    }
}
