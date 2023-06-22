import processing.core.PImage;

import java.util.List;

public class Coin extends EntityAnimation{
    public static final String COIN_KEY = "coin";
    public static final double COIN_ANIMATION_PERIOD = 0.5;
    public static final int COIN_NUM_PROPERTIES = 1;
    public Coin(String id, Point position, List<PImage> images, double animationPeriod){

        super(id, position, images, animationPeriod);
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Coin(id, position, images, this.getAnimationPeriod());
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, animation.createAction(this), getAnimationPeriod());
    }




}
