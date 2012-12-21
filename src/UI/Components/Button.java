package UI.Components;

import Game.Scene;
import UI.ActionEvent;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ashton
 */

public class Button extends Component
{
    private String Content = "";
    
    public Button(String Content, int X, int Y, int Width, int Height, int XPadding, int YPadding, boolean Enabled)
    {
        this.Content = Content;
        
        setXPadding(XPadding);
        setYPadding(YPadding);
        setLocation(X, Y);
        setSize(Width, Height);
        setEnabled(Enabled);
    }
    
    public Button(String Content, int X, int Y, int Width, int Height)
    {
        this(Content, X, Y, Width, Height, 0, 0, true);
    }
    
    public Button(String Content, int X, int Y)
    {
        this(Content, X, Y, -1, -1, 20, 14, true);
    }
    
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        super.setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        Font font = g.getFont();
        int ContentHeight = font.getHeight(Content);
        int ContentWidth = font.getWidth(Content);
        
        if(getWidth() == -1 || getHeight() == -1)
        {
            g.setColor(Color.gray);
            g.fillRect(super.getAbsoluteX(), getAbsoluteY(), ContentWidth + getXPadding(), ContentHeight + getYPadding());
            g.setColor(Color.black);
            g.drawString(Content, getAbsoluteX() + (((ContentWidth + getXPadding()) - ContentWidth) / 2), getAbsoluteY() + (((ContentHeight + getYPadding()) - ContentHeight) / 2));
        }
        else
        {
            g.setColor(Color.gray);
            g.fillRect(super.getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
            g.setColor(Color.black);
            g.drawString(Content, getAbsoluteX() + ((getWidth() - ContentWidth) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - ContentHeight) / 2) + getYPadding());
        }
    }
    
    @Override
    public boolean Contains(int PointX, int PointY)
    {
        if(getWidth() == -1 || getHeight() == -1)
        {
            Font font = Scene.getInstance().gc.getGraphics().getFont();
            int ContentHeight = font.getHeight(Content);
            int ContentWidth = font.getWidth(Content);
            
            if(PointX >= getAbsoluteX() && PointY >= getAbsoluteY() && PointX <= (getAbsoluteX() + ContentWidth + getXPadding()) && PointY <= (getAbsoluteY() + ContentHeight + getYPadding()))
                return true;
            else
                return false;
        }
        else
        {
            if(PointX >= getAbsoluteX() && PointY >= getAbsoluteY() && PointX <= (getAbsoluteX() + getWidth() + getXPadding()) && PointY <= (getAbsoluteY() + getHeight() + getYPadding()))
                return true;
            else
                return false;
        }
    }
}
