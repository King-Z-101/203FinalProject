//import processing.core.PImage;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//public class dudeCoin extends Dude implements transformEntity{
//    public static final String dudeCoin_KEY = "dudeCoin";
//    public dudeCoin(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int resourceLimit) {
//        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
//    }
//
//    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
//        if (this.getPosition().adjacent(target.getPosition())) {
////            this.resourceCount = this.resourceCount + 1;
//            if (target instanceof Coin) {
//                Point obj = target.getPosition();
//                for (int x = obj.x - 1; x <= obj.x + 1; x++) {
//                    for (int y = obj.y - 1; y <= obj.y + 1; y++) {
//                        Optional<Entity> potentialRock = world.getOccupant(new Point(x, y));
//                        if (potentialRock.get() instanceof Rock) {
//                            world.removeEntityAt(potentialRock.get().getPosition());
//                        }
//                    }
//                }
//                world.removeEntity(scheduler, target);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void executeEntity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Entity> coinTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Coin.class)));
////        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));
////        if (coinTarget.isPresent())
////        {
////            dudeCoin mario = new dudeCoin(dudeCoin.dudeCoin_KEY, this.getPosition(), this.getImages(), this.getAnimationPeriod(),
////                    this.getActionPeriod(), Dude.resourceLimit);
////            world.removeEntity(scheduler, this);
////            scheduler.unscheduleAllEvents(this);
////            world.addEntity(mario);
////            mario.scheduleActions(scheduler, world, imageStore);
////            this.moveTo(world, coinTarget.get(), scheduler);
////        }
//        if (coinTarget.isEmpty() || !this.moveTo(world, coinTarget.get(), scheduler) || !this.transformObject(world, scheduler, imageStore)) {
//            Activity activity = new Activity(this, world, imageStore);
//            scheduler.scheduleEvent(this, activity, this.getActionPeriod());
//        }
//    }
//
//    public boolean transformObject(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//            Dude dude = new Dude_Not_Full(this.getId(), this.getPosition(), this.getImages(),
//                    this.getAnimationPeriod(), this.getActionPeriod(), Dude.resourceLimit, 0);
//
//            world.removeEntity(scheduler, this);
//            scheduler.unscheduleAllEvents(this);
//
//            world.addEntity(dude);
//            dude.scheduleActions(scheduler, world, imageStore);
//
//            return true;
//    }
//
//
//}
