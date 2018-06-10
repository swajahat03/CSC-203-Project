import processing.core.PImage;

import java.util.List;

public class Create {
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Create(){

    }

    public static Entity createMinerNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new MinerNotFull(id, position, images, actionPeriod, animationPeriod, 0, resourceLimit);
    }



    public static Obstacle createObstacle(String id,
                                          List<PImage> images, Point pos)
    {
        return new Obstacle(id, pos, images);
    }

    public static OreBlob createOreBlob(String id, int actionPeriod, int animationPeriod, List<PImage> images, Point pos)
    {
        return new OreBlob(id, pos, animationPeriod, actionPeriod ,images );
    }


    public static Entity createSonic(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new Sonic(id, position, images, actionPeriod, animationPeriod, 0, resourceLimit);
    }


    public static Ore createOre(String id, int actionPeriod, Point pos,
                                List<PImage> images)
    {
        return new Ore(id, pos, actionPeriod, images );
    }

    public static Quake createQuake(List<PImage> images, Point pos)
    {
        return new Quake(QUAKE_ID, pos, images,
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Blacksmith createBlacksmith(String id, List<PImage> images, Point pos)
    {
        return new Blacksmith(id, pos, images);
    }

    public static Vein createVein(String id, int actionPeriod, Point pos,
                                  List<PImage> images)
    {
        return new Vein(id, pos,  actionPeriod, images);
    }

    public static MinerFull createMinerFull(String id, int resourceLimit, int actionPeriod, int animationPeriod, Point pos,
                                            List<PImage> images)
    {
        return new MinerFull(id, pos, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

}
