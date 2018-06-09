import processing.core.PImage;
import java.util.function.Predicate;
import java.util.*;

final class WorldModel
{
   public int numRows;
   public int numCols;
   public Background background[][];
   public Entity occupancy[][];
   public Set<Entity> entities;
    public static final String RAINBOW_NAME = "rainbow_default";

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

    public void tryAddEntity( Entity entity)
    {
        if (isOccupied(entity.getPosition()))
        {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity( entity);
    }

    public boolean withinBounds( Point pos)
    {
        return pos.y >= 0 && pos.y < this.numRows &&
                pos.x >= 0 && pos.x < this.numCols;
    }

    public boolean isOccupied( Point pos)
    {
        return withinBounds( pos) &&
                getOccupancyCell( pos) != null;
    }

    public Optional<Entity> nearestEntity(List<Entity> entities,
                                          Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity other : entities)
            {
                int otherDistance = other.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }


    public Optional<Entity> findNearest(Point pos,
                                        Predicate<Entity> isTarget)
    {List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities)
        {
            if (isTarget.test(entity))
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);

    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */
    public  void addEntity( Entity entity)
    {
        if (this.withinBounds( entity.getPosition()))
        {
            setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos, WorldModel world)
    {
        Point oldPos = entity.getPosition();
        if (world.withinBounds( pos) && !pos.equals(oldPos))
        {
            world.setOccupancyCell( oldPos, null);
            world.removeEntityAt( pos);
            world.setOccupancyCell( pos, entity);
            entity.setPosition(pos) ;
        }
    }

    public void removeEntity( Entity entity)
    {
        removeEntityAt( entity.getPosition());
    }

    public void removeEntityAt( Point pos)
    {
        if (withinBounds( pos)
                && getOccupancyCell( pos) != null)
        {
            Entity entity = getOccupancyCell( pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition(new Point(-1,-1));
            this.entities.remove(entity);
            setOccupancyCell( pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (withinBounds( pos))
        {
            return Optional.of(getBackgroundCell(pos).getCurrentImage(getBackgroundCell( pos)));
        }
        else
        {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos,
                                     Background background)
    {
        if (withinBounds(pos))
        {
            setBackgroundCell( pos, background);
        }
    }

    public  Optional<Entity> getOccupant(Point pos)
    {
        if (isOccupied( pos))
        {
            return Optional.of(getOccupancyCell( pos));
        }
        else
        {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos)
    {
        return this.occupancy[pos.y][pos.x];
    }

    public  void setOccupancyCell( Point pos,
                                        Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public  Background getBackgroundCell( Point pos)
    {
        return this.background[pos.y][pos.x];
    }

    public  void setBackgroundCell( Point pos,
                                         Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public Set<Entity> getEntities(){
        return entities;
    }

    public void drawMichaelScarn(int x, int y, Viewport view, List<PImage> listImage){
        Point worldPoint = view.viewportToWorld(x, y);

        if(withinBounds(worldPoint)) {
            setBackgroundCell(worldPoint, new Background(RAINBOW_NAME, listImage));
        }
    }


}
