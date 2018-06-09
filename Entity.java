import processing.core.PImage;

import java.util.List;


public interface Entity {

        PImage getCurrentImage();

        void setPosition(Point p);

        Point getPosition();

        List<PImage> getImages();


    }
