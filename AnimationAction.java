public class AnimationAction implements Action {
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;
    private Animated entity;

    public AnimationAction (Animated entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (this.repeatCount != 1)
        {

            scheduler.scheduleEvent(  ActionCreator.createAnimationAction(entity,
                    Math.max(repeatCount - 1, 0)),entity.getAnimationPeriod(), (Entity)entity);
        }
    }

}
