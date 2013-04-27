package Interface.Components;

import Interface.ActionEvent;
import java.util.ArrayList;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Gage
 */
public abstract class Component 
{
    private int AbsoluteX, AbsoluteY, X, Y, Width, Height, ContentWidth, ContentHeight, XPadding, YPadding, Radius;
    private ArrayList<ActionEvent> ActionEvents = new ArrayList<>();
    private boolean isEnabled;
    public boolean hasFocus;
    private String Content = "";
    
    public void doAction(String eventType)
    {
        if(isEnabled)
            for(ActionEvent actionEvent : ActionEvents)
                if(actionEvent != null && actionEvent.getEventType().equals(eventType))
                    actionEvent.actionPerformed();
    }
    
    public void stopAction()
    {
        
    }
    
    public void mousePressed(int X, int Y)
    {
        
    }
    
    public void keyPressed(int Key, char Char)
    {
        
    }
    
    public void addActionEvent(ActionEvent actionEvent)
    {
        if(!ActionEvents.contains(actionEvent))
            ActionEvents.add(actionEvent);
    }
    
    public void setContent(String Content)
    {
        this.Content = Content;
    }
    
    public String getContent()
    {
        return Content;
    }
    
    public int getContentWidth()
    {
        return ContentWidth;
    }
    
    public void setContentWidth(int ContentWidth)
    {
        this.ContentWidth = ContentWidth;
    }
    
    public int getContentHeight()
    {
        return ContentHeight;
    }
    
    public void setContentHeight(int ContentHeight)
    {
        this.ContentHeight = ContentHeight;
    }
    
    public boolean isFitToContent()
    {
        if(Width == -1 || Height == -1)
            return true;
        else
            return false;
    }
    
    public boolean isEnabled()
    {
        return isEnabled;
    }
    
    public void setEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }
    
    public void setAbsoluteLocation(int X, int Y, int Radius)
    {
        this.AbsoluteX = X;
        this.AbsoluteY = Y;
           
        this.Radius = Radius;        
    }
    
    public void setLocation(int X, int Y)
    {
        this.X = X;
        this.Y = Y;
    }
    
    public void setSize(int Width, int Height)
    {
        this.Width = Width;
        this.Height = Height;
    }
    
    public int getRadius() 
    {
        return Radius;
    }

    public int getAbsoluteX()
    {
        return AbsoluteX;
    }
    
    public int getAbsoluteY()
    {
        return AbsoluteY;
    }
    
    public int getX()
    {
        return X;
    }

    public int getY() 
    {
        return Y;
    }

    public int getWidth() 
    {
        return Width;
    }

    public int getHeight() 
    {
        return Height;
    }
    
    public int getXPadding()
    {
        return XPadding;
    }
    
    public int getYPadding()
    {
        return YPadding;
    }
    
    public void setXPadding(int Padding)
    {
        this.YPadding = Padding;
    }
    
    public void setYPadding(int Padding)
    {
        this.YPadding = Padding;
    }
    
    public void setPadding(int Padding)
    {
        this.XPadding = Padding;
        this.YPadding = Padding;
    }
    
    public boolean Contains(int PointX, int PointY)
    {
        if(isFitToContent())
        {
            if(PointX >= AbsoluteX && PointY >= AbsoluteY && PointX <= (AbsoluteX + ContentWidth + XPadding) && PointY <= (AbsoluteY + ContentHeight + YPadding))
                return true;
            else
                return false;
        }
        else
        {
            if(PointX >= AbsoluteX && PointY >= AbsoluteY && PointX <= (AbsoluteX + Width + XPadding) && PointY <= (AbsoluteY + Height + YPadding))
                return true;
            else
                return false;
        }
    }
    
    public abstract void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight);
}