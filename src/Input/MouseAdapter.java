package Input;

import Game.*;
import UI.WindowManager;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

/**
 *
 * @author Ashton
 */

public class MouseAdapter implements MouseListener
{
        @Override
        public void mouseWheelMoved(int change)
        {
        
        }
        
        @Override
        public void mouseClicked(int button, int x, int y, int clickCount)
        {
            WindowManager.mouseClick(button, x, y, clickCount);
            
            if(button == Input.MOUSE_LEFT_BUTTON && WindowManager.noFocus())
            {
                Scene scene = Scene.getInstance();
                scene.player.WalkTo(-scene.Cam.GetXPos() + x, -scene.Cam.GetYPos() + y);
            }
        }
        
        @Override
        public void mousePressed(int button, int x, int y)
        {
            //WindowManager.mousePressed(button, x, y);
        }
        
        @Override
        public void mouseReleased(int button, int x, int y) 
        {
            WindowManager.mouseReleased(button, x, y);
        }
        
        @Override
        public void mouseMoved(int oldx, int oldy, int newx, int newy) 
        {
        
        }
        
        @Override
        public void mouseDragged(int oldx, int oldy, int newx, int newy)
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
