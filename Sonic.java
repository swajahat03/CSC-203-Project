import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sonic extends AbstractMiner implements AnimatedActor {

    public Sonic(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                 int resourceCount, int resourceLimit) {
        super(id, position, images, actionPeriod, animationPeriod, resourceCount, resourceLimit);
    }



    public boolean moveTo( WorldModel world,
                                  Entity target, EventScheduler scheduler)
    {

       // for (Point p: WorldView.clickPoints){
         //   if (Point.adjacent(p,this.position)) {
           //     world.removeEntity(this);
           // }
      //  }
        if (MinerFull.adjacent(this.getPosition(), target.getPosition()))
        {
            resourceCount += 1;
            world.removeEntity( target);
            scheduler.unscheduleAllEvents( target);
            return true;
        }


        else
        {
            Point nextPos = this.nextPosition( world, target.getPosition());

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity( this, nextPos, world);
            }
            return false;
        }


    }


    public boolean transform( WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);


            return true;
        }

        return false;
    }

    public  void executeActivity(  WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest( position,
                e -> e instanceof MinerFull);

        if (!notFullTarget.isPresent() ||
                !moveTo( world,notFullTarget.get(), scheduler) ||
                !transform(  world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( ActionCreator.createActivityAction(this, world, imageStore),
                    this.actionPeriod, this);
        }
    }

    public static boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }
}