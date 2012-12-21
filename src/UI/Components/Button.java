package UI.Components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Gage Orsburn
 */

public class Button extends Component
{
    private String Content = "";
    private boolean isMouseDown;
    
    public Button(String Content, int X, int Y, int Width, int Height, int XPadding, int YPadding, boolean Enabled)
    {
        this.Content = Content;
        this.isMouseDown = false;
        
        setLocation(X, Y);
        setSize(Width, Height);
        setXPadding(XPadding);
        setYPadding(YPadding);
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
    
    @Override
    public void doAction()
    {
        isMouseDown = true;
        super.doAction();
        
    }
    
    @Override
    public void stopAction() 
    {
        isMouseDown = false;
    }
    
    @Override
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        Font font = g.getFont();
        setContentWidth(font.getWidth(Content));
        setContentHeight(font.getHeight(Content));
         
        if(isFitToContent())
        {
            g.setColor(Color.gray);
            g.fillRect(getAbsoluteX(), getAbsoluteY(), getContentWidth() + getXPadding(), getContentHeight() + getYPadding());
            g.setColor(Color.black);
            g.drawString(Content, getAbsoluteX() + (((getContentWidth() + getXPadding()) - getContentWidth()) / 2), getAbsoluteY() + (((getContentHeight() + getYPadding()) - getContentHeight()) / 2));
        }
        else
        {
            g.setColor(Color.gray);
            g.fillRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
            g.setColor(Color.black);
            g.drawString(Content, getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());
        }
        
        /*if(isFitToContent())
        {
            if(!isMouseDown)
            {
                g.setColor(Color.gray);
                g.fillRect(super.getAbsoluteX(), getAbsoluteY(), getContentWidth() + getXPadding(), getContentHeight() + getYPadding());
                g.setColor(Color.black);
                g.drawString(Content, getAbsoluteX() + (((getContentWidth() + getXPadding()) - getContentWidth()) / 2), getAbsoluteY() + (((getContentHeight() + getYPadding()) - getContentHeight()) / 2));
            }
            else
            {
                g.setColor(Color.gray);
                g.fillRect(getAbsoluteX() - 1, getAbsoluteY() - 1, getContentWidth() + getXPadding() - 1, getContentHeight() + getYPadding() - 1);
                g.setColor(Color.black);
                g.drawString(Content, getAbsoluteX() + (((getContentWidth() + getXPadding()) - getContentWidth()) / 2), getAbsoluteY() + (((getContentHeight() + getYPadding()) - getContentHeight()) / 2));
            }
        }
        else
        {
            if(!isMouseDown)
            {
                g.setColor(Color.gray);
                g.fillRect(super.getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
                g.setColor(Color.black);
                g.drawString(Content, getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());
            }
            else
            {
                g.setColor(Color.gray);
                g.fillRect(super.getAbsoluteX() + 1, getAbsoluteY() + 1, getWidth() + 1, getHeight() + 1);
                g.setColor(Color.black);
                g.drawString(Content, getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());
            }  
        }*/
    }
}
