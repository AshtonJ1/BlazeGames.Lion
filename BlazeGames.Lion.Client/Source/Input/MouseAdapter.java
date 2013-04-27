package Input;

import Core.Entities.Monster;
import Core.Entities.Player;
import Game.Scene;
import Interface.WindowManager;
import java.util.Map;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

/**
 *
 * @author Gage
 */
public class MouseAdapter implements MouseListener
{
    private Scene scene;
    
    @Override
    public void mouseWheelMoved(int i)
    {
        
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount)
    {
        if(scene == null)
            scene = Scene.getInstance();
        
        WindowManager.mouseClick(button, x, y, clickCount);
        
        if(WindowManager.noFocus())
        {
            if(button == Input.MOUSE_LEFT_BUTTON)
            {
                boolean EntitySelected = false;
                
                for (Map.Entry<Integer, Monster> entry : scene.Monsters.entrySet())
                {
                    if(!EntitySelected && entry.getValue().Contains((int)(-scene.gameCamera.GetXPos()+x), (int)(-scene.gameCamera.GetYPos()+y)))
                    {
                        scene.MonsterIndex = entry.getKey();
                        EntitySelected = true;    
                    }
                }
                
                for (Map.Entry<Integer, Player> entry : scene.Players.entrySet())
                {
                    if(!EntitySelected && entry.getValue().Contains(x, y))
                    {
                        scene.PlayerIndex = entry.getKey();
                        EntitySelected = true;
                    }
                }
                
                if(!EntitySelected)
                        scene.myPlayer.WalkTo(-scene.gameCamera.GetXPos() + x, -scene.gameCamera.GetYPos() + y);
            }
        }
    }

    @Override
    public void mousePressed(int button, int x, int y)
    {
        WindowManager.mousePressed(button, x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y)
    {
        WindowManager.mouseReleased(button, x, y);
    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3)
    {
        
    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3)
    {
        
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
