package cs4303.p4.items;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Item {
    private final ItemType type;
    private final Date dateObtained;

    public Item(ItemType type) {
        this(type, new Date());
    }
}
