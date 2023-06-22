/**
 * An action that can be taken by an entity
 */
public abstract class Action {
    //private ActionKind kind;
    private Entity entity;
//    private WorldModel world;
//    private ImageStore imageStore;
//    private int repeatCount;

    public Action(Entity entity) {
        //this.kind = kind;
        this.entity = entity;
//        this.world = world;
//        this.imageStore = imageStore;
//        this.repeatCount = repeatCount;
    }

    public abstract Action createAction(Entity entity);

    public abstract void executeAction(EventScheduler scheduler);


    public Entity getEntity() {
        return entity;
    }

}