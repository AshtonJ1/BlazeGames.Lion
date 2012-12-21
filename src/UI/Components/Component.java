package UI.Components;

import UI.ActionEvent;
import java.util.ArrayList;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Ashton
 */

public abstract class Component 
{
    private int AbsoluteX, AbsoluteY, X, Y, Width, Height, XPadding, YPadding, Radius;
    private ArrayList<ActionEvent> ActionEvents = new ArrayList<>();
    private boolean isEnabled;
    
    public void doAction()
    {
        if(isEnabled)
            for(ActionEvent actionEvent : ActionEvents)
                if(actionEvent != null)
                    actionEvent.actionPerformed();
    }
    
    public void addActionEvent(ActionEvent actionEvent)
    {
        if(!ActionEvents.contains(actionEvent))
            ActionEvents.add(actionEvent);
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
        System.out.println(PointX  + " = " + (AbsoluteX + Width + XPadding));
        System.out.println(PointY  + " = " + (AbsoluteY + Height + YPadding));
        
        if(Width == -1 || Height == -1)
        {
            if(PointX >= AbsoluteX && PointY >= AbsoluteY && PointX <= (AbsoluteX + Width + XPadding) && PointY <= (AbsoluteY + Height + YPadding))
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