package UI.Components;

import UI.ActionEvent;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ashton
 */

public class Button extends Component
{
    private String Content = "";
    
    public Button(String Content, int X, int Y, int Width, int Height, boolean Enabled)
    {
        this.Content = Content;
        
        setLocation(X, Y);
        setSize(Width, Height);
        setEnabled(Enabled);
    }
    
    public Button(String Content, int X, int Y, int Width, int Height)
    {
        this(Content, X, Y, Width, Height, true);
    }
    
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        super.setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        g.setColor(Color.gray);
        g.fillRect(super.getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
        g.setColor(Color.black);
        g.drawString(Content, getAbsoluteX() + getXMargin(), getAbsoluteY() + getYMargin());
    }
}
