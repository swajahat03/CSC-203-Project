import processing.core.PImage;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class AbstractMiner extends ScheduleAnimation{

    int resourceCount;
    int resourceLimit;

    public AbstractMiner(String id, Point position, List<PImage> images, int actionPeriod,
                         int animationPeriod, int resourceCount, int resourceLimit){
        super(id,position,images,actionPeriod,animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public Point nextPosition( WorldModel world,
                               Point destPos)
    {
       /* int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied( newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied( newPos))
            {
                newPos = this.position;
            }
        }

        return newPos; */

        AStarPathingStrategy SingleStep = new AStarPathingStrategy();
        Predicate<Point> passThrough = p ->!world.isOccupied(p) && world.withinBounds(p);
        BiPredicate<Point, Point> withinReach = (Point p1, Point p2) -> OreBlob.adjacent(p1,p2);
        List<Point> path = SingleStep.computePath(getPosition(), destPos, passThrough, withinReach, PathingStrategy.Pot_Neighbors);

        if (path == null) {
            return this.getPosition();
        } else if(path.size() == 0) {
            return this.getPosition();
        }
        return path.get(0);
    }




}
