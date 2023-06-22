import processing.core.PImage;

import java.util.List;

public abstract class EntityAction extends EntityAnimation{
    private double actionPeriod;

    public EntityAction(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod){
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    public abstract void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Activity activity = new Activity(this, world, imageStore);
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, activity.createAction(this), this.actionPeriod);
        scheduler.scheduleEvent(this, animation.createAction(this), getAnimationPeriod());
    }

    public double getActionPeriod() {
        return actionPeriod;
    }
}
