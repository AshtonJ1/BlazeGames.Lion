package Utility;

import Game.Scene;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;

/**
 *
 * @author Gage
 */
public class ScreenCap 
{
   public static void TakeScreenShot(int X, int Y, int Width, int Height)
   {
       try
       {
           Image screenShot = new Image(Width, Height);
           Scene.getInstance().gameContainer.getGraphics().copyArea(screenShot, X, Y);
           
           DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss");
           Date date = new Date();
           String screenName = dateFormat.format(date);
           
           OutputStream outputStream = new FileOutputStream("C://Users/Gage/Desktop/ScreenShots/" + screenName + ".png");
           ImageOut.write(screenShot, ImageOut.PNG, outputStream);
           screenShot.destroy();
           outputStream.close();
       }
       catch(SlickException | IOException e)
       {
           System.out.println(e);
       }
   }
}
