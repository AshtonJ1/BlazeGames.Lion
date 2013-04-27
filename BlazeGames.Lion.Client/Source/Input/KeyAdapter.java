package Input;

import Game.Program;
import Game.Scene;
import Interface.Window;
import Interface.WindowManager;
import Utility.ScreenCap;
import java.awt.Toolkit;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Gage
 */
public class KeyAdapter implements KeyListener
{
    @Override
    public void keyPressed(int key, char c)
    {
        if(!WindowManager.noFocus())
        {
            WindowManager.keyPressed(key, c);
        }
        else
        {
            if(key == Input.KEY_F5)
                ScreenCap.TakeScreenShot(0, 0, Program.Application.getWidth(), Program.Application.getHeight());
        }
        
        if(key == Input.KEY_M)
            Scene.getInstance().wndMiniMap.setVisible(!Scene.getInstance().wndMiniMap.isVisible());
        
        if(key == Input.KEY_ESCAPE)
        {
            Scene.getInstance().MonsterIndex = -1;
            Scene.getInstance().PlayerIndex = -1;
        }

        if(key == Input.KEY_F11)
        {
            try
            {
                if(Program.isFullscreen)
                {
                    Program.Application.setDisplayMode(800, 600, false);
                    Program.isFullscreen = false;
                    Scene.getInstance().gameCamera.setCamSize(800, 600);
                }
                else
                {
                    Program.Application.setDisplayMode(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, true);
                    Program.isFullscreen = true;
                    Scene.getInstance().gameCamera.setCamSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
                }
                
                WindowManager.getWindow("Login").Center();
                Window Minimap = WindowManager.getWindow("Minimap");
                Minimap.setLocation(Program.Application.getWidth() - Minimap.getWidth(), Minimap.getY());
            }
            catch(SlickException e){}
        }
    }

    @Override
    public void keyReleased(int key, char c)
    {
        WindowManager.keyReleased(key, c);
    }

    @Override
    public void setInput(Input input)
    {
        
    }

    @Override
    public boolean isAcceptingInput()
    {
        return true;
    }

    @Override
    public void inputEnded()
    {
        
    }

    @Override
    public void inputStarted()
    {
        
    }
}
