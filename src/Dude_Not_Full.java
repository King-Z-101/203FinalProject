import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Dude_Not_Full extends Dude implements transformEntity{
    private int resourceCount;

    public Dude_Not_Full(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
        this.resourceCount = resourceCount;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            if (target instanceof Coin){
                Point obj = target.getPosition();
                for (int x = obj.x -1; x <= obj.x + 1; x++){
                    for (int y = obj.y - 1; y <= obj.y +1; y++){
                        Optional<Entity> potentialRock = world.getOccupant(new Point(x,y));
                        if (potentialRock.isPresent() && potentialRock.get() instanceof Rock){
                            world.removeEntityAt(potentialRock.get().getPosition());
                        }
                    }
                }
                world.removeEntity(scheduler, target);
            }
            this.resourceCount = this.resourceCount + 1;
            if (target instanceof Tree) {
                Tree tree = (Tree) target;
                tree.health = tree.health - 1;
                //tree.setHealth(tree.getHealth() - 1);
            }
            else if (target instanceof Sapling) {
                Sapling sapling = (Sapling)target;
                sapling.health = sapling.health - 1;
                //tree.setHealth(tree.getHealth() - 1);
            }
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> coinTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Coin.class)));
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));
        if (coinTarget.isPresent() && !this.moveTo(world, coinTarget.get(), scheduler))
        {
//            dudeCoin mario = new dudeCoin(dudeCoin.dudeCoin_KEY, this.getPosition(), this.getImages(), this.getAnimationPeriod(),
//                this.getActionPeriod(), Dude.resourceLimit);
//            world.removeEntity(scheduler, this);
//            scheduler.unscheduleAllEvents(this);
//            world.addEntity(mario);
//            mario.scheduleActions(scheduler, world, imageStore);
            Activity activity = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, activity, this.getActionPeriod());
        }
         else if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !this.transformObject(world, scheduler, imageStore)) {
            Activity activity = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, activity, this.getActionPeriod());
        }
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Dude_Not_Full(id, position, images, this.getAnimationPeriod(), this.getActionPeriod(), this.resourceLimit, this.getResourceCount());
    }

    public boolean transformObject(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= Dude.resourceLimit) {
//            System.out.println(this.getResourceCount());
//            System.out.println(Dude.resourceLimit);
            Dude dude = new Dude_Full(this.getId(), this.getPosition(), this.getImages(),
                    this.getAnimationPeriod(), this.getActionPeriod(), Dude.resourceLimit);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public int getResourceCount() {
        return resourceCount;
    }
}
