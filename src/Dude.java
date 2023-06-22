import processing.core.PImage;

import java.util.List;

public abstract class Dude extends Moveable{
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;
    public static int resourceLimit;
    private static final PathingStrategy Dude_PATHING = new
            AstarPathingStrategy();
//    private static final PathingStrategy Dude_PATHING = new
//            SingleStepPathingStrategy();



    public Dude(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod, Dude_PATHING);
        this.resourceLimit = resourceLimit;
    }

//    public Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
//    }
}
