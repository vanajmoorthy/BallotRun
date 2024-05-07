package cs4303.p4._util.gui;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import processing.core.PApplet;
import processing.core.PImage;

@Component
public final class ImageController {
    private static final Map<String, PImage> cache = new HashMap<String, PImage>();

    public static PImage get(PApplet sketch, String path) {
        if (cache.containsKey(path)) return cache.get(path);

        try {
            String absolutePath = ResourceUtils.getFile(path).getAbsolutePath();
            PImage image = sketch.loadImage(absolutePath);
            cache.put(path, image);
            return image;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
