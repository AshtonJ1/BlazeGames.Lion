package Game;

import Network.ClientSocket;
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
            Application.setTargetFrameRate(60);
            Application.setUpdateOnlyWhenVisible(false);
            Application.setAlwaysRender(true);
            Application.setShowFPS(false);
            Application.start();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
