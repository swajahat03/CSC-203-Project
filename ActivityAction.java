public class ActivityAction implements Action {

    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;
    private Actor entity;


    public ActivityAction(Actor entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.world = world;
        this.imageStore = imageStore;
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        if(entity instanceof MinerFull) {
            entity.executeActivity(world, imageStore, scheduler);
        } else if (entity.getClass() == MinerNotFull.class) {
            entity.executeActivity(world,
                    imageStore, scheduler);
        } else if (entity.getClass() == Ore.class) {
            entity.executeActivity(world, imageStore,
                    scheduler);
        } else if (entity.getClass() == OreBlob.class) {
            entity.executeActivity(world,
                    imageStore, scheduler);
        } else if (entity.getClass() == Quake.class) {
            entity.executeActivity(world, imageStore,
                    scheduler);
        } else if(entity.getClass() == Vein.class) {
            entity.executeActivity(world, imageStore,
                    scheduler);
        }
       else if(entity.getClass() == Sonic.class) {
           entity.executeActivity(world, imageStore,
                   scheduler);
       }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("executeActivity not supported for %s",
                            entity.getClass()));
        }
    }

}
