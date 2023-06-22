import processing.core.PImage;

import java.util.List;

public abstract class EntityAnimation extends Entity{
    //private static double animationPeriod;

//    public EntityAnimation(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
//        super(kind, id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
//    }

    public EntityAnimation(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images, animationPeriod);
        //this.animationPeriod = animationPeriod;
    }


}
