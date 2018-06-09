public class ActionCreator {


    public static Action createAnimationAction(Animated entity, int repeatCount)
    {
        return new AnimationAction(entity, null, null, repeatCount);
    }

    public static Action createActivityAction(Actor entity, WorldModel world,
                                              ImageStore imageStore)
    {
        return new ActivityAction( entity, world, imageStore, 0);
    }


}

