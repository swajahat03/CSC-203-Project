import processing.core.PImage;
import java.util.Random;
import java.util.List;
import java.util.Optional;


public class Vein extends ScheduleActivity implements Entity, Actor{

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final Random rand = new Random();
    private final int ORE_REACH = 1;



    public Vein(String id, Point position, int actionPeriod,
                List<PImage> images) {
        super(id,position,images, actionPeriod);

    }


    public  void executeActivity( WorldModel world,
                                  ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = findOpenAround(world, position);

        if (openPt.isPresent())
        {
            Ore ore = new Ore(ORE_ID_PREFIX + id,
                    openPt.get(), ORE_CORRUPT_MIN +
                    rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    // changed getImages to getImageList
                    imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            scheduleActions( ore,world, scheduler, imageStore);
        }

        scheduler.scheduleEvent( new ActivityAction(this, world, imageStore, 0),
                this.actionPeriod,this);
    }

    public Optional<Point> findOpenAround(WorldModel world, Point pos)
    {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
        {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
            {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (world.withinBounds(newPt) &&
                        !world.isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

}
