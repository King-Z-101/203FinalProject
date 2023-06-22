import processing.core.PImage;

import java.util.List;

public class House extends Entity{
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 0;

    public House(String id, Point position, List<PImage> images){

        super(id, position, images, 0);
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new House(id, position, images);
    }
}
