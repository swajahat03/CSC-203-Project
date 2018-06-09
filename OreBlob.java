import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OreBlob extends ScheduleAnimation implements Entity, AnimatedActor {

  //  public List<PImage> images;


    public OreBlob(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id,position,images,actionPeriod,animationPeriod);
    }

    public Point nextPosition(WorldModel world,
                              Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        Optional<Entity> occupant = world.getOccupant( newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant( newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public  void executeActivity( WorldModel world,
                                 ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> blobTarget = world.findNearest(
                this.position, e -> e instanceof Vein);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo( world, blobTarget.get(), scheduler))
            {
                Quake quake = Create.createQuake(imageStore.getImageList(Functions.QUAKE_KEY),tgtPos);
                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                scheduleActions(quake, world,scheduler, imageStore);
            }
        }

        scheduler.scheduleEvent(ActionCreator.createActivityAction(this, world, imageStore),
                nextPeriod,this);

    }

    private  boolean moveTo( WorldModel world,
                            Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity( target);
            scheduler.unscheduleAllEvents( target);
            return true;
        }
        else
        {
            Point nextPos = nextPosition( world, target.getPosition());

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity(this, nextPos, world);
            }
            return false;
        }
    }

    public static boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }
}
