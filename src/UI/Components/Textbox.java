package UI.Components;

import Game.Scene;
import java.awt.MouseInfo;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Gage
 */

public class Textbox extends Component
{
    private boolean isPassword, stretchX, stretchY;

    private int cursorPosition;
    private Color backgroundColor = Color.white, borderColor = Color.black, foregroundColor = Color.black;
    
    public Textbox(int X, int Y, int Width, int Height, boolean isPassword)
    {
        this.isPassword = isPassword;
        
        setLocation(X, Y);
        setSize(Width, Height);
        setEnabled(true);
        
        cursorPosition = 0;
    }
    
    public Textbox(int X, int Y, int Width, int Height)
    {
        this(X, Y, Width, Height, false);
    }
    
    public Textbox(int X, int Y)
    {
        this(X, Y, 100, 20, false);
    }
    
    @Override
    public void keyPressed(int Key, char Char)
    {
        if(Key == Input.KEY_BACK)
        {
            if(getContent().length() > 0 && cursorPosition > 0)
            {
                String preCursorText = getContent().substring(0, cursorPosition - 1);
                String postCursorText = getContent().substring(cursorPosition, getContent().length());
                
                setContent(preCursorText + postCursorText);
                cursorPosition--;
            }
        }
        else if(Key == Input.KEY_DELETE)
        {
            if(getContent().length() > 0 && cursorPosition < getContent().length())
            {
                String preCursorText = getContent().substring(0, cursorPosition);
                String postCursorText = getContent().substring(cursorPosition + 1, getContent().length());
                
                setContent(preCursorText + postCursorText);
            }
        }
        else if(Key == Input.KEY_LEFT)
        {
            /*if(cursorPosition > 0)
                cursorPosition--;*/
        }
        else if(Key == Input.KEY_RIGHT)
        {
            /*if(cursorPosition < getContent().length())
                cursorPosition++;*/
        }
        else if(Key == Input.KEY_ENTER)
        {
           doAction("onReturn");
        }
        else if(Char != Character.UNASSIGNED)
        {
                String preCursorText = getContent().substring(0, cursorPosition);
                String postCursorText = getContent().substring(cursorPosition, getContent().length());

                setContent(preCursorText + Char + postCursorText);

                cursorPosition++;
        }
    }
    
    private int currentFrame = 0;
    
    @Override
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        currentFrame++;
        
        if(currentFrame == 120)
            currentFrame = 1;
        
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        if(stretchX)
            setSize(ParentWidth - (getX() * 2), getHeight());
        if(stretchY)
            setSize(getWidth(), ParentHeight - (getY() * 2));
        
        g.setColor(borderColor);
        g.drawRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
        g.setColor(backgroundColor);
        g.fillRect(getAbsoluteX() + 2, getAbsoluteY() + 2, getWidth() - 3, getHeight() - 3);
        g.setColor(foregroundColor);
        
        Font font = g.getFont();
        
        String drawContent = getContent();
        if(isPassword)
            drawContent = drawContent.replaceAll("[^*]", "*");
        
        int startDrawContent = 0;
        
        if(font.getWidth(getContent()) > getWidth() - 4)
        {
            String content = getContent();
            
            
            for(int i = 1; i <= content.length(); i++)
            {
                String newDrawContent = content.substring((content.length() - i), content.length());
                
                if(isPassword)
                    newDrawContent = newDrawContent.replaceAll("[^*]", "*");
                
                if(font.getWidth(newDrawContent) > getWidth() - 4)
                {
                    break;
                }
                else
                {
                    startDrawContent = content.length() - i;
                    drawContent = newDrawContent;
                }
            }
            
            //System.out.println(startDrawContent);
        }
        
        if(hasFocus && currentFrame < 60)
        {
            if(cursorPosition == 0)
                g.drawLine(getAbsoluteX() + 3, getAbsoluteY() + 3, getAbsoluteX() + 3, (getAbsoluteY() + getHeight()) - 3);
            else
            {
                int textWidth = g.getFont().getWidth(getContent().substring(startDrawContent, cursorPosition));
                g.drawLine(getAbsoluteX() + textWidth + 2, getAbsoluteY() + 3, getAbsoluteX() + textWidth + 2, (getAbsoluteY() + getHeight()) - 3);
            }
        }
        
        g.drawString(drawContent, getAbsoluteX() + 3, getAbsoluteY() + 1);
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
