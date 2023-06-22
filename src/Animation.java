public class Animation extends Action{
    private int repeatCount;

    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    public Action createAction(Entity entity) {
        return new Animation(entity, repeatCount);
    }

    public void executeAction(EventScheduler scheduler) {
        this.getEntity().nextImage();

        if (this.getRepeatCount() != 1) {
            Animation animation = new Animation(super.getEntity(), Math.max(this.getRepeatCount() - 1, 0));
            scheduler.scheduleEvent(this.getEntity(), animation.createAction(this.getEntity()), this.getEntity().getAnimationPeriod());   //potential error
        }
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}
