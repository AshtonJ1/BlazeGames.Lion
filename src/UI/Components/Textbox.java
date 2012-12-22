package UI.Components;

import org.newdawn.slick.Graphics;

/**
 *
 * @author Gage
 */

public class Textbox extends Component
{
    //private boolean isPassword;
    //private char[] Text;
    
    public Textbox(int X, int Y, int Width, int Height)
    {
        setLocation(X, Y);
        setSize(Width, Height);
        setEnabled(true);
    }
    
    public Textbox(int X, int Y)
    {
        this(X, Y, 20, 100);
    }
    
    //need to do keyevents
    
    @Override
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        //draw border
        //draw white rect
        //set color to font text color
        //draw getContent()
    }
}
