package UI.Components;

import Game.Program;
import Game.Scene;
import UI.WindowManager;
import java.awt.MouseInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Gage Orsburn
 */

public class Button extends Component
{
    private int Height = 38;
    private boolean isMouseDown, isMouseHover, stretchX, stretchY;
    private Color backgroundColor = Color.lightGray, backgroundPressedColor = Color.darkGray, backgroundHoverColor = Color.gray, borderColor = Color.black, foregroundColor = Color.white;
    
    public Button(String Content, int X, int Y, int Width, int XPadding, int YPadding, boolean Enabled) throws SlickException
    {
        this.isMouseDown = false;
        
        setContent(Content);
        setLocation(X, Y);
        setSize(Width, Height);
        setXPadding(XPadding);
        setYPadding(YPadding);
        setEnabled(Enabled);
    }
    
    public Button(String Content, int X, int Y, int Width) throws SlickException
    {
        this(Content, X, Y, Width, 0, 0, true);
    }
    
    public Button(String Content, int X, int Y) throws SlickException
    {
        this(Content, X, Y, 100, 20, 14, true);
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
    public final void setSize(int Width, int Height)
    {
        if(Width < WindowManager.btnBgLeft.getWidth() + WindowManager.btnBgCenter.getWidth() + WindowManager.btnBgRight.getWidth())
            Width = WindowManager.btnBgLeft.getWidth() + WindowManager.btnBgCenter.getWidth() + WindowManager.btnBgRight.getWidth();
        
        super.setSize(Width, this.Height);
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
        
        if(stretchX)
            setSize(ParentWidth - (getX() * 2), getHeight());
        
        if(isMouseDown)
        {
            WindowManager.btnBgLeftPressed.draw(getAbsoluteX(), getAbsoluteY());
            WindowManager.btnBgCenterPressed.draw(getAbsoluteX() + WindowManager.btnBgLeft.getWidth(), getAbsoluteY(), getWidth() - (WindowManager.btnBgLeft.getWidth() + WindowManager.btnBgRight.getWidth()), WindowManager.btnBgCenter.getHeight());
            WindowManager.btnBgRightPressed.draw(getAbsoluteX() + (getWidth() - WindowManager.btnBgRight.getWidth()), getAbsoluteY());

            g.setColor(foregroundColor);
            g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding() + 1, getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding() + 1);
        }
        else if(isMouseHover)
        {
            WindowManager.btnBgLeftHover.draw(getAbsoluteX(), getAbsoluteY());
            WindowManager.btnBgCenterHover.draw(getAbsoluteX() + WindowManager.btnBgLeft.getWidth(), getAbsoluteY(), getWidth() - (WindowManager.btnBgLeft.getWidth() + WindowManager.btnBgRight.getWidth()), WindowManager.btnBgCenter.getHeight());
            WindowManager.btnBgRightHover.draw(getAbsoluteX() + (getWidth() - WindowManager.btnBgRight.getWidth()), getAbsoluteY());

            g.setColor(foregroundColor);
            g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());
        }
        else
        {
            WindowManager.btnBgLeft.draw(getAbsoluteX(), getAbsoluteY());
            WindowManager.btnBgCenter.draw(getAbsoluteX() + WindowManager.btnBgLeft.getWidth(), getAbsoluteY(), getWidth() - (WindowManager.btnBgLeft.getWidth() + WindowManager.btnBgRight.getWidth()), WindowManager.btnBgCenter.getHeight());
            WindowManager.btnBgRight.draw(getAbsoluteX() + (getWidth() - WindowManager.btnBgRight.getWidth()), getAbsoluteY());
            
            g.setColor(foregroundColor);
            g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());
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
