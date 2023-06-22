import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy extends Moveable{
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD = 0;
    public static final int FAIRY_ACTION_PERIOD = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;
//    private static final PathingStrategy Fairy_PATHING = new
//            SingleStepPathingStrategy();
    private static final PathingStrategy Fairy_PATHING = new
            AstarPathingStrategy();

    public Fairy(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod, Fairy_PATHING);
    }

    private static boolean neighbors(Point p1, Point p2)   {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }

//    public Point nextPosition(WorldModel world, Point destPos) {
//        List<Point> path = Fairy_PATHING.computePath(getPosition(), destPos,
//                p -> world.withinBounds(p) && !world.isOccupied(p), //canPassTrough
//                //Point::adjacent, //withinReach
//                (p1, p2) -> neighbors(p1,p2),//withinReach
//                PathingStrategy.CARDINAL_NEIGHBORS);
//        if (path.size() == 0)
//            return this.getPosition();
//        else
//            return path.get(0);
//    }

//    public Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
//    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
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
        Optional<Entity> fairyTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = new Sapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos,
                        imageStore.getImageList(Sapling.SAPLING_KEY),
                        Sapling.SAPLING_ACTION_ANIMATION_PERIOD,
                        Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_HEALTH, Sapling.SAPLING_HEALTH_LIMIT);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }
        Activity activity = new Activity(this, world, imageStore);
        scheduler.scheduleEvent(this, activity.createAction(this), this.getActionPeriod());
    }


    public Entity createEntity(String id, Point position, List<PImage> images) {
        return new Fairy(id, position, images, this.getActionPeriod(), this.getAnimationPeriod());
    }
}
