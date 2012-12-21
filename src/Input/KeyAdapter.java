package Input;

import Game.Program;
import Game.Scene;
import UI.WindowManager;
import java.awt.Toolkit;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Ashton
 */

public class KeyAdapter implements KeyListener
{
    @Override
    public void keyPressed(int key, char c)
    {
        //WindowManager.keyPressed(key, c);
        
        if(key == Input.KEY_ESCAPE)
            WindowManager.resetFocus();
            //Program.Application.exit();

        if(key == Input.KEY_F11)
        {
            try
            {
                if(Program.isFullscreen)
                {
                    Program.Application.setDisplayMode(800, 600, false);
                    Program.isFullscreen = false;
                    Scene.getInstance().Cam.setCamSize(800, 600);
                }
                else
                {
                    Program.Application.setDisplayMode(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, true);
                    Program.isFullscreen = true;
                    Scene.getInstance().Cam.setCamSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
                }
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
