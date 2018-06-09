import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends AbstractMiner implements Entity, AnimatedActor {

    public MinerFull(String id, Point position,
                     List<PImage> images, int resourceLimit, int resourceCount,
                     int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod,resourceCount,resourceLimit);
    }

    public boolean moveToFull( WorldModel world,
                          Entity target, EventScheduler scheduler) {
        if (Point.adjacent(position, target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());


            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get());
                }
                world.moveEntity( target, nextPos, world);
            }
            return false;
        }
    }

    public boolean transformFull( WorldModel world,
                             EventScheduler scheduler, ImageStore imageStore) {

        if (resourceCount >= resourceLimit)


        {
            Entity miner = Create.createMinerNotFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod,this.animationPeriod,
                    this.images);

            world.removeEntity( miner);
            scheduler.unscheduleAllEvents( miner);

            world.addEntity( miner);
            scheduleActions( miner, world, scheduler, imageStore);
            return true;
        }
        return false;

    }



    public void executeActivity(  WorldModel world,
                                 ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest( position,
                e -> e instanceof Ore);

        if (fullTarget.isPresent() &&
                moveToFull(  world, fullTarget.get(), scheduler))
        {
            transformFull(  world, scheduler, imageStore);
        }
        else
        {
            //eventscheduler or scheduler
            scheduler.scheduleEvent(new ActivityAction(this, world, imageStore, 0), this.actionPeriod, this);
        }

    }







}
