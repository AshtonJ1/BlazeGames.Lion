package UI.Components;

import Game.Program;
import Game.Scene;
import java.awt.MouseInfo;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Gage Orsburn
 */

public class Button extends Component
{
    private boolean isMouseDown, isMouseHover, stretchX, stretchY;
    private Color backgroundColor = Color.lightGray, backgroundPressedColor = Color.darkGray, backgroundHoverColor = Color.gray, borderColor = Color.black, foregroundColor = Color.white;
    
    public Button(String Content, int X, int Y, int Width, int Height, int XPadding, int YPadding, boolean Enabled)
    {
        this.isMouseDown = false;
        
        setContent(Content);
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
    public void mousePressed(int X, int Y)
    {
        isMouseDown = true;
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
            if(stretchX)
                setContentWidth((ParentWidth - (getX() * 2)) + getXPadding());
            if(stretchY)
                setContentHeight((ParentHeight - (getY() * 2)) + getYPadding());
        
            if(isMouseDown)
            {
                g.setColor(backgroundPressedColor);
                g.fillRect(getAbsoluteX() - 1, getAbsoluteY() - 1, getContentWidth() + getXPadding() - 1, getContentHeight() + getYPadding() - 1);
                g.setColor(foregroundColor);
                g.drawString(getContent(), getAbsoluteX() + (((getContentWidth() + getXPadding()) - getContentWidth()) / 2), getAbsoluteY() + (((getContentHeight() + getYPadding()) - getContentHeight()) / 2));
            }
            else if(isMouseHover)
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
            if(stretchX)
                setSize(ParentWidth - (getX() * 2), getHeight());
            if(stretchY)
                setSize(getWidth(), ParentHeight - (getY() * 2));
        
            if(isMouseDown)
            {
                g.setColor(backgroundPressedColor);
                g.fillRect(getAbsoluteX() + 1, getAbsoluteY() + 1, getWidth(), getHeight());
                g.setColor(foregroundColor);
                g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding() + 1, getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding() + 1);
            }
            else if(isMouseHover)
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
    
    public Color getBackgroundPressedColor()
    {
        return backgroundPressedColor;
    }

    public void setBackgroundPressedColor(Color backgroundPressedColor)
    {
        this.backgroundPressedColor = backgroundPressedColor;
    }

    public Color getBackgroundHoverColor()
    {
        return backgroundHoverColor;
    }

    public void setBackgroundHoverColor(Color backgroundHoverColor)
    {
        this.backgroundHoverColor = backgroundHoverColor;
    }
    
        public boolean isStretchX()
    {
        return stretchX;
    }

    public void setStretchX(boolean stretchX)
    {
        this.stretchX = stretchX;
    }

    public boolean isStretchY()
    {
        return stretchY;
    }

    public void setStretchY(boolean stretchY)
    {
        this.stretchY = stretchY;
    }
}
