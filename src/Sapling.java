import processing.core.PImage;

import java.util.List;

public class Sapling extends EntityAction implements transformEntity{
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH = 0;
    public static final int SAPLING_NUM_PROPERTIES = 1;
    public int health;
    public int healthLimit;


    public Sapling(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int health, int healthLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health += 1;
        if (!this.transformObject(world, scheduler, imageStore)) {
            Activity activity = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, activity, this.getActionPeriod());
        }
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Sapling(id, position, images, this.getAnimationPeriod(), this.getActionPeriod(), this.health, this.healthLimit);
    }
    public int getHealth() {
        return health;
    }

    public boolean transformObject(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        //System.out.println("Health: " + this.health);
        if (this.health <= 0) {
            Stump stump = new Stump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.getHealth() >= this.healthLimit) {
//            Tree tree = new Tree(Tree.TREE_KEY + "_" + this.getId(), this.getPosition(),
//                    imageStore.getImageList(Tree.TREE_KEY), Tree.getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN),
//                    Tree.getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN),
//                    Tree.getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN));
            Tree tree = new Tree(Tree.TREE_KEY + "_" + this.getId(), this.getPosition(),
                    imageStore.getImageList(Tree.TREE_KEY),
                    Tree.getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN),
                    Tree.getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN),
                    Tree.getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
