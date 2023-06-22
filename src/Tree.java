import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Tree extends EntityAction implements transformEntity{
    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD = 0;
    public static final int TREE_ACTION_PERIOD = 1;
    public static final int TREE_HEALTH = 2;
    public static final int TREE_NUM_PROPERTIES = 3;

    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    public int health;
    //public static int healthLimit;

    public Tree(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
        //this.healthLimit = healthLimit;
    }

    public static int getIntFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    public static double getNumFromRange(double max, double min) {
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }

    public void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!this.transformObject(world, scheduler, imageStore)) {
            Activity activity = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, activity.createAction(this), this.getActionPeriod());
        }
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Tree(id, position, images, this.getAnimationPeriod(), this.getActionPeriod(), this.health);
    }

    public boolean transformObject(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Stump stump = new Stump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
}
