package cs4303.p4._util.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

@Getter
@AllArgsConstructor
public final class GestureDetector {
    @AllArgsConstructor
    public static class Hitbox {
        protected final PVector pos;
        protected final PVector size;
    }

    @FunctionalInterface
    public static interface GestureInterface {
        public void handle(
            PApplet sketch
        );
    }

    @FunctionalInterface
    public static interface DrawInterface {
        public void handle(
            PApplet sketch,
            Hitbox hitbox,
            boolean hasHover,
            boolean hasClick
        );
    }

    private final DrawInterface draw;
    private final GestureInterface onClick;
    private final Hitbox hitbox;

    public void draw(PApplet sketch) {
        draw.handle(sketch, hitbox, isFocused(sketch), sketch.mousePressed);
    }

    public boolean isFocused(PApplet sketch) {
        return
            sketch.mouseX >= hitbox.pos.x &&
            sketch.mouseY >= hitbox.pos.y &&
            sketch.mouseX <= hitbox.pos.x + hitbox.size.x &&
            sketch.mouseY <= hitbox.pos.y + hitbox.size.y;
    }

    public void click(PApplet sketch) {
        onClick.handle(sketch);
    }
}
