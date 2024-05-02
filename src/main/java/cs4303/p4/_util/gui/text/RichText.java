package cs4303.p4._util.gui.text;

import java.util.List;

import cs4303.p4._util.Colors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import processing.core.PApplet;

@Getter
@AllArgsConstructor
public final class RichText {
    private final List<TextNode> text;

    // maybe return final height of text?
    // maybe add restricted height?
    public void draw(PApplet sketch, float x, float y, float width, float textSize) {
        float currX = x;
        float currY = y;

        float spaceWidth = sketch.textWidth(" ");

        sketch.textSize(textSize);

        for (TextNode node : text) {
            if (node instanceof LineBreak) {
                currX = x;
                currY += textSize * 2;
            } else if (node instanceof TextSpan) {
                Integer nodeColor = ((TextSpan) node).getColor();
                sketch.fill(nodeColor != null ? nodeColor : Colors.neutral.dark);

                String[] words = ((TextSpan) node).getText().split("\\s+");
                for (String word : words) {
                    float wordWidth = sketch.textWidth(word);

                    if (currX != x && currX + wordWidth >= x + width) {
                        currX = x;
                        currY += textSize;
                    }

                    sketch.text(word, currX, currY);
                    currX += wordWidth;
                    currX += spaceWidth;

                    if (currX >= x + width) {
                        currX = x;
                        currY += textSize;
                    }
                }
            }
        }
    }
}
