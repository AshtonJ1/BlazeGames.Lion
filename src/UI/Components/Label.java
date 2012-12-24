package UI.Components;

import Game.Scene;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ashton
 */

public class Label extends Component
{
    private boolean isMouseHover;
    private Color backgroundColor = Color.transparent, backgroundHoverColor = Color.transparent, borderColor = Color.black, foregroundColor = Color.white;
    
    public Label(String Content, int X, int Y, int Width, int Height, int XPadding, int YPadding, boolean Enabled)
    {
        setContent(Content);
        setLocation(X, Y);
        setSize(Width, Height);
        setXPadding(XPadding);
        setYPadding(YPadding);
        setEnabled(Enabled);
    }
    
    public Label(String Content, int X, int Y, int Width, int Height)
    {
        this(Content, X, Y, Width, Height, 0, 0, true);
    }
    
    public Label(String Content, int X, int Y)
    {
        this(Content, X, Y, -1, -1, 20, 14, true);
    }
    
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public void setBorderColor(Color borderColor)
    {
        this.borderColor = borderColor;
    }

    public Color getForegroundColor()
    {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor)
    {
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundHoverColor()
    {
        return backgroundHoverColor;
    }

    public void setBackgroundHoverColor(Color backgroundHoverColor)
    {
        this.backgroundHoverColor = backgroundHoverColor;
    }
        
    @Override
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        Font font = g.getFont();
        setContentWidth(font.getWidth(getContent()));
        setContentHeight(font.getHeight(getContent()));
        
        int MouseX = Scene.getInstance().input.getMouseX();
        int MouseY = Scene.getInstance().input.getMouseY();
        
        
        if(Contains(MouseX, MouseY))
            isMouseHover = true;
        else
            isMouseHover = false;
        
        if(isFitToContent())
        {
            if(isMouseHover)
            {
                g.setColor(backgroundHoverColor);
                g.fillRect(getAbsoluteX(), getAbsoluteY(), getContentWidth() + getXPadding(), getContentHeight() + getYPadding());
                g.setColor(foregroundColor);
                g.drawString(getContent(), getAbsoluteX() + (((getContentWidth() + getXPadding()) - getContentWidth()) / 2), getAbsoluteY() + (((getContentHeight() + getYPadding()) - getContentHeight()) / 2));
            }
            else
            {
                g.setColor(backgroundColor);
                g.fillRect(getAbsoluteX(), getAbsoluteY(), getContentWidth() + getXPadding(), getContentHeight() + getYPadding());
                g.setColor(foregroundColor);
                g.drawString(getContent(), getAbsoluteX() + (((getContentWidth() + getXPadding()) - getContentWidth()) / 2), getAbsoluteY() + (((getContentHeight() + getYPadding()) - getContentHeight()) / 2));
            }
        }
        else
        {
            if(isMouseHover)
            {
                g.setColor(backgroundHoverColor);
                g.fillRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
                g.setColor(foregroundColor);
                g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());              
            }
            else
            {
                g.setColor(backgroundColor);
                g.fillRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
                g.setColor(foregroundColor);
                g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());
            }
        }
    }
}
