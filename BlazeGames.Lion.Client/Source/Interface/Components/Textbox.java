package Interface.Components;

import Game.Scene;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Gage
 */
public class Textbox extends Component
{
    private boolean isPassword, stretchX, selected = false;

    private int cursorPosition, Height = 27;
    private Color foregroundColor = Color.gray;
    private Image bgLeft, bgCenter, bgRight;
    
    public Textbox(int X, int Y, int Width, boolean isPassword) throws SlickException
    {
        bgLeft = new Image("Resource/Interface/txtbox_left.png");
        bgCenter = new Image("Resource/Interface/txtbox_center.png");
        bgRight = new Image("Resource/Interface/txtbox_right.png");
        
        this.isPassword = isPassword;
        
        setLocation(X, Y);
        setSize(Width, Height);
        setEnabled(true);
        
        cursorPosition = 0;
    }
    
    public Textbox(int X, int Y, int Width) throws SlickException
    {
        this(X, Y, Width, false);
    }
    
    public Textbox(int X, int Y) throws SlickException
    {
        this(X, Y, 100, false);
    }
    
    @Override
    public final void setSize(int Width, int Height)
    {
        if(Width < bgLeft.getWidth() + bgCenter.getWidth() + bgRight.getWidth())
            Width = bgLeft.getWidth() + bgCenter.getWidth() + bgRight.getWidth();
        
        super.setSize(Width, this.Height);
    }
    
    @Override
    public void keyPressed(int Key, char Char)
    {
        if(Key == Input.KEY_BACK)
        {
            if(getContent().length() > 0 && cursorPosition > 0)
            {
                if(!selected)
                {
                    String preCursorText = getContent().substring(0, cursorPosition - 1);
                    String postCursorText = getContent().substring(cursorPosition, getContent().length());

                    setContent(preCursorText + postCursorText);
                    cursorPosition--;
                }
                else
                {
                    setContent("");
                    cursorPosition = 0;
                    selected = !selected;
                }
            }
        }
        else if(Scene.getInstance().gameInput.isKeyDown(Input.KEY_LCONTROL) || Scene.getInstance().gameInput.isKeyDown(Input.KEY_RCONTROL))
        {
            if(Key == Input.KEY_A)
            {
                selected = true;
            }
            else if(Key == Input.KEY_C)
            {
                if(selected)
                {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(getContent()), null);
                }
            }
            else if(Key == Input.KEY_V)
            {
                try
                {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    String data = (String)clipboard.getData(DataFlavor.stringFlavor);

                    setContent(getContent() + data);
                    cursorPosition += data.length();
                }
                catch(Exception e) { }
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
           selected = false;
           doAction("onReturn");
        }
        else if(Char != Character.UNASSIGNED)
        {
            if(selected)
            {
                setContent("");
                cursorPosition = 0;    
                selected = !selected;
            }
            
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
        
        if(currentFrame == 60)
            currentFrame = 1;
        
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        
        if(stretchX)
            setSize(ParentWidth - (getX() * 2), getHeight());
        
        bgLeft.draw(getAbsoluteX(), getAbsoluteY());
        bgCenter.draw(getAbsoluteX() + bgLeft.getWidth(), getAbsoluteY(), getWidth() - (bgLeft.getWidth() + bgRight.getWidth()), bgCenter.getHeight());
        bgRight.draw(getAbsoluteX() + (getWidth() - bgRight.getWidth()), getAbsoluteY());
        
        //g.setColor(borderColor);
        //g.drawRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
        //g.setColor(backgroundColor);
        //g.fillRect(getAbsoluteX() + 2, getAbsoluteY() + 2, getWidth() - 3, getHeight() - 3);
        
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
        
        if(hasFocus && currentFrame < 30)
        {
            if(cursorPosition == 0)
                g.drawLine(getAbsoluteX() + 5, getAbsoluteY() + 3, getAbsoluteX() + 5, (getAbsoluteY() + getHeight()) - 3);
            else
            {
                int textWidth = g.getFont().getWidth(drawContent);
                g.drawLine(getAbsoluteX() + textWidth + 4, getAbsoluteY() + 3, getAbsoluteX() + textWidth + 4, (getAbsoluteY() + getHeight()) - 3);
            }
        }
        
        g.drawString(drawContent, getAbsoluteX() + 5, getAbsoluteY() + 4);
          
        if(selected)
        {
            //{ A = 75, R = 13, G = 97, B = 173 }
            g.setColor(new Color(13,97,173,75));
            g.fillRect(getAbsoluteX() + 4, getAbsoluteY() + 5, font.getWidth(drawContent), getHeight() - 10);
        }
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
}