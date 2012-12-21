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
    private int AbsoluteX, AbsoluteY, X, Y, Width, Height, XMargin, YMargin, Radius;
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
    
    public int getXMargin()
    {
        return XMargin;
    }
    
    public int getYMargin()
    {
        return YMargin;
    }
    
    public void setXMargin(int Margin)
    {
        this.YMargin = Margin;
    }
    
    public void setYMargin(int Margin)
    {
        this.YMargin = Margin;
    }
    
    public void setMargin(int Margin)
    {
        this.XMargin = Margin;
        this.YMargin = Margin;
    }
    
    public boolean Contains(int PointX, int PointY)
    {
        if(PointX >= AbsoluteX && PointY >= AbsoluteY && PointX <= (AbsoluteX+Width) && PointY <= (AbsoluteY+Height))
            return true;
        else
            return false;
    }
    
    public abstract void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight);
}