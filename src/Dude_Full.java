import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dude_Full extends Dude implements transformEntity{
    public Dude_Full(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
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
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {  //says 5 usages but shouldn't be one usage for each implementation?
        //If coin is present, this is dude's top priority
        Optional<Entity> coinTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Coin.class)));
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(House.class)));
        if (coinTarget.isPresent() && !this.moveTo(world, coinTarget.get(), scheduler))
        {
            //this.getCurrentImage();
            Activity activity = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, activity, this.getActionPeriod());

        }
        else if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
            this.transformObject(world, scheduler, imageStore);
        } else {
            Activity activity = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, activity, this.getActionPeriod());
        }
    }

    public Entity createEntity(String id, Point position, List<PImage> images) {
//        return new Entity(id, position, images, 0, 0, 0, 0, 0, 0);
        return new Dude_Full(id, position, images, this.getAnimationPeriod(), this.getActionPeriod(), this.resourceLimit);
    }

    public boolean transformObject(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Dude dude = new Dude_Not_Full(this.getId(), this.getPosition(), this.getImages(),
                this.getAnimationPeriod(), this.getActionPeriod(), Dude.resourceLimit, 0);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
        return true;
    }
}
