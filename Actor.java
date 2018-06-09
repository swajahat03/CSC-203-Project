public interface Actor {
    void executeActivity( WorldModel world,
                         ImageStore imageStore, EventScheduler scheduler);

    int getActionPeriod();
}
