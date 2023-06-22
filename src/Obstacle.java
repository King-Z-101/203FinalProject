import processing.core.PImage;

import java.util.List;

public class Obstacle extends EntityAnimation{
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;

    public Obstacle(String id, Point position, List<PImage> images, double animationPeriod){
        super(id, position, images, animationPeriod);
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Obstacle(id, position, images, this.getAnimationPeriod());
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, animation.createAction(this), getAnimationPeriod());
    }
}
