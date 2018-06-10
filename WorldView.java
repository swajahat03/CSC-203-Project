import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class WorldView
{
   public PApplet screen;
   public WorldModel world;
   public int tileWidth;
   public int tileHeight;
   public Viewport viewport;
   public static final String RAINBOW_ID = "rainbow";

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }


   public static int clamp(int value, int low, int high)
   {
      return Math.min(high, Math.max(value, low));
   }

   public void shiftView( int colDelta, int rowDelta)
   {
      int newCol = clamp(this.viewport.col + colDelta, 0,
              this.world.numCols - this.viewport.numCols);
      int newRow = clamp(this.viewport.row + rowDelta, 0,
              this.world.numRows - this.viewport.numRows);
       viewport.shift( newCol, newRow);
   }

   public void drawBackground()
   {
      for (int row = 0; row < this.viewport.numRows; row++)
      {
         for (int col = 0; col < this.viewport.numCols; col++)
         {
            Point worldPoint = this.viewport.viewportToWorld( col, row);
            Optional<PImage> image = world.getBackgroundImage(worldPoint);
            if (image.isPresent())
            {
               this.screen.image(image.get(), col * this.tileWidth,
                       row * this.tileHeight);
            }
         }
      }
   }

   public  void drawEntities()
   {
      for (Entity entity : this.world.entities)
      {
         Point pos = entity.getPosition();

         if (viewport.contains( pos))
         {
            Point viewPoint = this.viewport.worldToViewport( pos.x, pos.y);
            this.screen.image(entity.getCurrentImage(),
                    viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
         }
      }
   }

   public void drawViewport()
   {
      drawBackground();
      drawEntities();
   }

   public void threatLevelMidnight(int x, int y, ImageStore imageStore){
      List<PImage> rainbowImageList = imageStore.getImageList(RAINBOW_ID);
      Point p = viewport.viewportToWorld(x, y);
      clickPoints.add(p);

      world.drawMichaelScarn(x,y,viewport, rainbowImageList); //Change here for different shapes on click
      world.drawMichaelScarn(x,y+1,viewport,rainbowImageList);
      world.drawMichaelScarn(x+1,y+1,viewport,rainbowImageList);
      world.drawMichaelScarn(x,y-1,viewport,rainbowImageList);
      world.drawMichaelScarn(x+1,y-1,viewport,rainbowImageList);
      world.drawMichaelScarn(x-1,y,viewport,rainbowImageList);
      world.drawMichaelScarn(x-1,y+1,viewport,rainbowImageList);
      world.drawMichaelScarn(x+1,y,viewport,rainbowImageList);
      world.drawMichaelScarn(x-1,y-1,viewport,rainbowImageList);
   }



   public static List<Point> clickPoints = new ArrayList<>();


  // public void spawnSonic(Sonic sonic, Point click, EventScheduler sched, ImageStore imageStore) {
    //  Create.createSonic("sonic",4, 5,imageStore.getImageList("sonic"),click);
   //}


}
