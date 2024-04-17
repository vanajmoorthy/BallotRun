package cs4303.p4._util.gui.text;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TextSpan extends TextNode {
    private final String text;
    private final int color;
    private final boolean bold;
    private final boolean italic;

    public TextSpan(String text, int color) {
        this(text, color, false, false);
    }
}
