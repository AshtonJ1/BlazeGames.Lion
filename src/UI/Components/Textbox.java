package UI.Components;

import Game.Scene;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Gage
 */

public class Textbox extends Component
{
    private boolean isPassword;
    //private char[] Text;
    
    public Textbox(int X, int Y, int Width, int Height, boolean isPassword)
    {
        this.isPassword = isPassword;
        
        setLocation(X, Y);
        setSize(Width, Height);
        setEnabled(true);
    }
    
    public Textbox(int X, int Y, int Width, int Height)
    {
        this(X, Y, Width, Height, false);
    }
    
    public Textbox(int X, int Y)
    {
        this(X, Y, 20, 100, true);
    }
    
    @Override
    public void keyPressed(int Key, char Char)
    {
        //exclude any special keys, crtl, shift and so on
        
        if(Key == Input.KEY_BACK)
        {
            if(getContent().length() > 0)
                setContent(getContent().substring(0, getContent().length() - 1));
        }
        else
        {
            setContent(getContent() + Char);
        }
    }
    
    @Override
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        //draw border
        //draw white rect
        //set color to font text color
        
        if(isPassword)
            g.drawString(getContent().replaceAll("[^*]", "*"), getAbsoluteX(), getAbsoluteY());
        else
            g.drawString(getContent(), getAbsoluteX(), getAbsoluteY());
    }
}
