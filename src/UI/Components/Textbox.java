package UI.Components;

import Game.Scene;
import org.newdawn.slick.Color;
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
        this(X, Y, 100, 20, true);
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
        g.drawRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
        g.setColor(Color.white);
        g.fillRect(getAbsoluteX() + 1, getAbsoluteY() + 1, getWidth() - 1, getHeight() - 1);
        g.setColor(Color.black);
        
        //we want to only draw the substring of the content that is able to fit inside the textbox
        //the substring should start at the end, not the beginning for example if string is coconuts
        //it should draw conuts instead of coconu
        
        if(isPassword)
            g.drawString(getContent().replaceAll("[^*]", "*"), getAbsoluteX() + 3, getAbsoluteY() + 3);
        else
            g.drawString(getContent(), getAbsoluteX(), getAbsoluteY());
    }
}
