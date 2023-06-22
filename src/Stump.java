import processing.core.PImage;

import java.util.List;

public class Stump extends Entity{
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_NUM_PROPERTIES = 0;

    public Stump(String id, Point position, List<PImage> images){
        super(id, position, images, 0);
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Stump(id, position, images);
    }
}
