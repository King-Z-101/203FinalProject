import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background {
    private String id;
    private List<PImage> images;
    private int imageIndex = 0;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public PImage getCurrentImage() {
        if (this instanceof Background) {
            return this.images.get(this.imageIndex);
        } else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not supported for %s", this));
        }
    }


}
