import processing.core.PImage;

import java.util.List;

public abstract class BasicEntity implements Entity {

    protected int imageIndex = 0;
    protected Point position;
    protected String id;
    protected List<PImage> images;

    public BasicEntity (String id, Point position,
                        List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public PImage getCurrentImage() {

        return (this.getImages().get(imageIndex));

    }

    public List<PImage> getImages() {
        return images;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p) {
        position  = p;
    }



}
