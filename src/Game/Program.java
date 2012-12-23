package Game;

import Network.*;
import org.newdawn.slick.AppGameContainer;

/**
 *
 * @author Gage
 */

public class Program
{
    public static AppGameContainer Application;
    public static boolean isFullscreen = false, isRunning = true;
    public static ClientSocket CSocket;
    
    public static void main(String[] Arguements) throws InterruptedException
    {
        try
        {
            Application = new AppGameContainer(Scene.getInstance());
            Application.setDisplayMode(800, 600, false);
            //Application.setTargetFrameRate(120);
            Application.setUpdateOnlyWhenVisible(false);
            Application.setAlwaysRender(true);
            //Application.setVSync(true);
            Application.start();
            //Application.setVerbose(true);
        }
        catch(Exception e)
        {
            //System.out.println(e.getMessage());
        }
    }
}
