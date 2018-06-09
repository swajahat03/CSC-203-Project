import processing.core.PImage;

import java.util.List;


public abstract class ScheduleAnimation extends ScheduleActivity{
    protected int animationPeriod;

    public ScheduleAnimation(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id,position,images,actionPeriod);
        this.animationPeriod = animationPeriod;
    }


    public void scheduleActions(Entity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        scheduler.scheduleEvent( ActionCreator.createActivityAction((Actor)entity, world, imageStore),
                ((AnimatedActor)entity).getActionPeriod(), entity);
        scheduler.scheduleEvent(ActionCreator.createAnimationAction((Animated)entity, ((AnimatedActor)entity).getAnimationPeriod()),
                ((AnimatedActor)entity).getAnimationPeriod(), entity);

    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }



}
