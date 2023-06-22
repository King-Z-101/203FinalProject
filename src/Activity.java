public class Activity extends Action{
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity entity, WorldModel world,ImageStore imageStore) {
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    public Action createAction(Entity entity) {
        return new Activity(entity, world, imageStore);
    }

//    public void executeAction(EventScheduler scheduler) {
//
//        if (this.getEntity().getClass().equals(Sapling.class)) {
//            Sapling.executeEntity(this.getWorld(), this.getImageStore(), scheduler);
//        } else if (this.getEntity().getClass().equals(Tree.class)) {
//            Tree.executeEntity(this.getWorld(), this.getImageStore(), scheduler);
//        } else if (this.getEntity().getClass().equals(Fairy.class)) {
//            Fairy.executeEntity(this.getWorld(), this.getImageStore(), scheduler);
//        } else if (this.getEntity().getClass().equals(Dude_Not_Full.class)) {
//            Dude_Not_Full.executeEntity(this.getWorld(), this.getImageStore(), scheduler);
//        } else if (this.getEntity().getClass().equals(Dude_Full.class)) {
//            Dude_Full.executeEntity(this.getWorld(), this.getImageStore(), scheduler);
//        } else {
//            throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.getEntity().getClass()));
//        }
//    }

    public void executeAction(EventScheduler scheduler) {
        if (getEntity() == null) {
            return;
        }
        Class<? extends Entity> temp = getEntity().getClass();
        //System.out.println("Temp class: " + temp);
        if (temp.equals(Sapling.class)) {
            ((Sapling) getEntity()).executeEntity(getWorld(), getImageStore(), scheduler);
        } else if (temp.equals(Tree.class)) {
            ((Tree) getEntity()).executeEntity(getWorld(), getImageStore(), scheduler);
        } else if (temp.equals(Fairy.class)) {
            ((Fairy) getEntity()).executeEntity(getWorld(), getImageStore(), scheduler);
        } else if (temp.equals(Dude_Not_Full.class)) {
            ((Dude_Not_Full) getEntity()).executeEntity(getWorld(), getImageStore(), scheduler);
        } else if (temp.equals(Dude_Full.class)) {                  ///never reaching this part
            //System.out.println("Entering Dude_Full if statement");
            ((Dude_Full) getEntity()).executeEntity(getWorld(), getImageStore(), scheduler);
        } else {
            throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", getEntity().getClass()));
        }
    }


    public WorldModel getWorld() {
        return world;
    }

    public ImageStore getImageStore() {
        return imageStore;
    }
}
