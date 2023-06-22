import processing.core.PImage;

import java.util.List;

public abstract class Moveable extends EntityAction{
//    public Moveable(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
//        super(kind, id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
//    }
    private PathingStrategy strategy;

    public Moveable(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, PathingStrategy pStrategy) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.strategy = pStrategy;
    }

    //public abstract Point nextPosition(WorldModel world, Point destPos);
    protected Point nextPosition(WorldModel world, Point destPos) {
        List<Point> path = new AstarPathingStrategy().computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p), //canPassTrough
                //Point::adjacent, //withinReach
                (p1, p2) -> neighbors(p1,p2),//withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.size() == 0)
            return this.getPosition();
        else
            return path.get(0);
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    private static boolean neighbors(Point p1, Point p2)   {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }


}
