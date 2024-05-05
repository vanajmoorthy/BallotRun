package cs4303.p4._util.gui;

import processing.core.PApplet;
import processing.core.PVector;

public class TimedText {
    String text;
    float duration; // Duration in seconds
    float timer;
    PVector location;
    public boolean active = false;

    public TimedText(String text, float duration, PVector location) {
        this.text = text;
        this.duration = duration;
        this.location = location;
        this.timer = duration;
    }

    public void start() {
        active = true;
    }

    public void update(float deltaTime) {
        if (active) {
            timer -= deltaTime;
            if (timer <= 0) {
                active = false;
                timer = 0;
            }
        }
    }

    public void draw(PApplet sketch) {
        if (active) {
            sketch.pushStyle();

            // Set text properties
            sketch.textSize(28);
            sketch.stroke(255); // Text outline color
            sketch.strokeWeight(2);

            // Calculate text width for background sizing
            float textWidth = sketch.textWidth(text);
            float padding = 10; // Padding around the text

            // Draw background rectangle
            sketch.fill(255, 0, 0, 200); // Background color with some transparency
            sketch.noStroke();
            // Draw the rectangle centered around the text
            sketch.rect(location.x - textWidth / 2 - padding, location.y - sketch.textAscent() + padding,
                    textWidth + 2 * padding, sketch.textAscent() + sketch.textDescent() + padding, 5);

            // Draw text on top of the background
            sketch.fill(255); // Text color
            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);

            sketch.text(text, location.x, location.y);

            sketch.popStyle();
        }
    }

    public boolean isActive() {
        return active;
    }

    public void reset() {
        timer = duration;
        active = true;
    }
}
