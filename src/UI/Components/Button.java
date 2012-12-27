package UI.Components;

import Game.Program;
import Game.Scene;
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
    private Image bgLeft, bgCenter, bgRight, bgLeftHover, bgCenterHover, bgRightHover, bgLeftPressed, bgCenterPressed, bgRightPressed;
    
    public Button(String Content, int X, int Y, int Width, int XPadding, int YPadding, boolean Enabled) throws SlickException
    {
        try
        {
            this.bgLeft = new Image("Resource/UI/btn_1_left.png");
            this.bgCenter = new Image("Resource/UI/btn_1_center.png");
            this.bgRight = new Image("Resource/UI/btn_1_right.png");
            this.bgLeftHover = new Image("Resource/UI/btn_1_left_hover.png");
            this.bgCenterHover = new Image("Resource/UI/btn_1_center_hover.png");
            this.bgRightHover = new Image("Resource/UI/btn_1_right_hover.png");
            this.bgLeftPressed = new Image("Resource/UI/btn_1_left_press.png");
            this.bgCenterPressed = new Image("Resource/UI/btn_1_center_press.png");
            this.bgRightPressed = new Image("Resource/UI/btn_1_right_press.png");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
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
        if(Width < bgLeft.getWidth() + bgCenter.getWidth() + bgRight.getWidth())
            Width = bgLeft.getWidth() + bgCenter.getWidth() + bgRight.getWidth();
        
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
            bgLeftPressed.draw(getAbsoluteX(), getAbsoluteY());
            bgCenterPressed.draw(getAbsoluteX() + bgLeft.getWidth(), getAbsoluteY(), getWidth() - (bgLeft.getWidth() + bgRight.getWidth()), bgCenter.getHeight());
            bgRightPressed.draw(getAbsoluteX() + (getWidth() - bgRight.getWidth()), getAbsoluteY());

            g.setColor(foregroundColor);
            g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding() + 1, getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding() + 1);
        }
        else if(isMouseHover)
        {
            bgLeftHover.draw(getAbsoluteX(), getAbsoluteY());
            bgCenterHover.draw(getAbsoluteX() + bgLeft.getWidth(), getAbsoluteY(), getWidth() - (bgLeft.getWidth() + bgRight.getWidth()), bgCenter.getHeight());
            bgRightHover.draw(getAbsoluteX() + (getWidth() - bgRight.getWidth()), getAbsoluteY());

            g.setColor(foregroundColor);
            g.drawString(getContent(), getAbsoluteX() + ((getWidth() - getContentWidth()) / 2) + getXPadding(), getAbsoluteY() + ((getHeight() - getContentHeight()) / 2) + getYPadding());
        }
        else
        {
            bgLeft.draw(getAbsoluteX(), getAbsoluteY());
            bgCenter.draw(getAbsoluteX() + bgLeft.getWidth(), getAbsoluteY(), getWidth() - (bgLeft.getWidth() + bgRight.getWidth()), bgCenter.getHeight());
            bgRight.draw(getAbsoluteX() + (getWidth() - bgRight.getWidth()), getAbsoluteY());
            
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
