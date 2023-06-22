import processing.core.PImage;

import java.util.List;

public class Rock extends Entity{
    public static final String ROCK_KEY = "rock";
    //public static final int COIN_NUM_PROPERTIES = 0;

    public Rock(String id, Point position, List<PImage> images){

        super(id, position, images, 0);
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Rock(id, position, images);
    }
}
