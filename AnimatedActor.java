public interface AnimatedActor extends Animated, Actor{
    void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    int getAnimationPeriod();
    void nextImage();
    int getActionPeriod();


}
