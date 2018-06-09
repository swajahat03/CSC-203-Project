import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends AbstractMiner implements AnimatedActor{

    public MinerNotFull(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                        int resourceCount, int resourceLimit) {
        super(id, position, images, actionPeriod, animationPeriod, resourceCount, resourceLimit);    }

    public boolean moveToNotFull( WorldModel world,
                          Entity target, EventScheduler scheduler)
    {

        for (Point p: WorldView.clickPoints){
            if (Point.adjacent(p,this.position)) {
                world.removeEntity(this);
            }
        }
        if (OreBlob.adjacent(this.getPosition(), target.getPosition()))
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

    public boolean transformNotFull( WorldModel world,
                             EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            Entity miner = Create.createMinerFull(this.id, this.resourceLimit,
                    this.actionPeriod, this.animationPeriod, position,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity( miner);
            scheduleActions(miner, world, scheduler, imageStore);

            return true;
        }

        return false;
    }

    public  void executeActivity(  WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest( position,
                e -> e instanceof Ore);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull( world,notFullTarget.get(), scheduler) ||
                !transformNotFull(  world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( ActionCreator.createActivityAction(this, world, imageStore),
                    this.actionPeriod, this);
        }
    }







}
