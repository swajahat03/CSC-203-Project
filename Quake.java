import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Quake extends ScheduleAnimation implements AnimatedActor {


    public Quake( String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id,position,images,actionPeriod,animationPeriod);}


    public  void executeActivity( WorldModel world,
                                  ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity( this);
    }

}
