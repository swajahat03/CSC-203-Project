import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Ore extends ScheduleActivity implements Actor, Entity {

    private static final Random rand = new Random();
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;


    public Ore(String id, Point position, int actionPeriod,
               List<PImage> images) {
        super(id, position,images,actionPeriod);
    }



    public  void executeActivity(  WorldModel world,
                                  ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = Create.createOreBlob(this.id + BLOB_ID_SUFFIX,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(Functions.BLOB_KEY),pos);


        world.addEntity(blob);
        VirtualWorld.scheduleActions( world, scheduler, imageStore);
    }

}


