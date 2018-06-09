import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

final class Functions
{
   public static final Random rand = new Random();
   // public static final String BLOB_KEY = "blob";
   // public static final String BLOB_ID_SUFFIX = " -- blob";
   //public static final int BLOB_PERIOD_SCALE = 4;
   //public static final int BLOB_ANIMATION_MIN = 50;
   //public static final int BLOB_ANIMATION_MAX = 150;

   //public static final String ORE_ID_PREFIX = "ore -- ";
   //public static final int ORE_CORRUPT_MIN = 20000;
   //public static final int ORE_CORRUPT_MAX = 30000;
   //public static final int ORE_REACH = 1;

   //public static final String QUAKE_KEY = "quake";
   //public static final String QUAKE_ID = "quake";
   //public static final int QUAKE_ACTION_PERIOD = 1100;
   //public static final int QUAKE_ANIMATION_PERIOD = 100;
   //public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

   public static final int COLOR_MASK = 0xffffff;
   public static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   public static final int PROPERTY_KEY = 0;

   public static final String MINER_KEY = "miner";
   public static final String OBSTACLE_KEY = "obstacle";
   public static final String ORE_KEY = "ore";
   public static final String SMITH_KEY = "blacksmith";
   public static final String VEIN_KEY = "vein";
   public static final String BGND_KEY = "background";
   public static final String QUAKE_KEY = "quake";
   public static final String BLOB_KEY = "blob";
   private static final String SONIC_KEY = "sonic";


   public static void loadImages(Scanner in, ImageStore imageStore,
      PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            processImageLine(imageStore.images, in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
               lineNumber));
         }
         lineNumber++;
      }
   }

   public static void processImageLine(Map<String, List<PImage>> images,
      String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   public static List<PImage> getImages(Map<String, List<PImage>> images,
      String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   public static void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }


   public static void load(Scanner in, WorldModel world, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public static boolean processLine(String line, WorldModel world,
      ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
         case BGND_KEY:
            return WorldLoader.parseBackground(properties, world, imageStore);
         case MINER_KEY:
            return WorldLoader.parseMiner(properties, world, imageStore);
         case OBSTACLE_KEY:
            return WorldLoader.parseObstacle(properties, world, imageStore);
         case ORE_KEY:
            return WorldLoader.parseOre(properties, world, imageStore);
         case SMITH_KEY:
            return WorldLoader.parseSmith(properties, world, imageStore);
         case VEIN_KEY:
            return WorldLoader.parseVein(properties, world, imageStore);
         }
      }

      return false;
   }





}
