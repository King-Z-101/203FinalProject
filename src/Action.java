/**
 * An action that can be taken by an entity
 */
public final class Action {
    public ActionKind kind;
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Action(ActionKind kind, Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

//    public void executeAction(EventScheduler scheduler) { // goes into action class becuase it take in action class as an input)
//        switch (this.kind) {
//            case ACTIVITY:
//                this.executeActivityAction(scheduler);
//                break;
//
//            case ANIMATION:
//                this.executeAnimationAction(scheduler);
//                break;
//        }
//    }
}
