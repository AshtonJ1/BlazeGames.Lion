package UI;

import Game.Program;
import Game.Scene;
import UI.Components.Component;
import java.awt.Font;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author Gage Orsburn
 */
public class Window
{
    private float X, Y;
    private int Width, Height, FocusedComponent = -1;
    public int StartX, StartY;
    public boolean isFirstRender, isVisible, isComponentActive = false, hasFocus = false, showMin, showClose, titleVisible = true;

    public boolean isMouseDown, isPinned;
    
    private String WindowTitle;

    private ArrayList<Component> Components = new ArrayList<>();
    private ArrayList<ActionEvent> ActionEvents = new ArrayList<>();
    public Input input;
    
    private TrueTypeFont titleFont;
    private Color titleColor;
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, boolean isPinned, boolean showMin, boolean showClose, boolean titleVisible)
    {
        this.titleFont = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 17), true);
        this.titleColor = new Color(194, 192, 180);
        this.titleVisible = titleVisible;
        this.WindowTitle = WindowTitle;
        
        setLocation(X, Y);
        setSize(Width, Height);   
        
        this.isFirstRender = true;
        this.isVisible = true;
        this.isMouseDown = false;
        this.isPinned = isPinned;
        this.showMin = showMin;
        this.showClose = showClose;
        
        input = Scene.getInstance().input;
    }
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, boolean isPinned, boolean showMin, boolean showClose)
    {
        this(WindowTitle, X, Y, Width, Height, isPinned, showMin, showClose, true);
    }
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, boolean isPinned)
    {
        this(WindowTitle, X, Y, Width, Height, isPinned, false, true, true);
    }
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height)
    {
        this(WindowTitle, X, Y, Width, Height, false, false, true, true);
    }
    
    public boolean hasFocus()
    {
        return hasFocus;
    }

    public void setFocused(boolean hasFocus) 
    {
        this.hasFocus = hasFocus;
    }
    
    public boolean isMouseDown()
    {
        return isMouseDown;
    }
    
    public int getWidth()
    {
        return Width;
    }
    
    public int getHeight()
    {
        return Height;
    }
    
    public void doAction(String eventType)
    {
        if(isVisible)
            for(ActionEvent actionEvent : ActionEvents)
                if(actionEvent != null && actionEvent.getEventType().equals(eventType))
                    actionEvent.actionPerformed();
    }
    
    public Window addActionEvent(ActionEvent actionEvent)
    {
        if(!ActionEvents.contains(actionEvent))
            ActionEvents.add(actionEvent);
        
        return this;
    }
    
    public void requestFocus(Component component)
    {
        for(int i = 0; i < Components.size(); i++)
            if(Components.get(i).equals(component))
            {
                Components.get(i).hasFocus = true;
                
                if(FocusedComponent != i)
                    FocusedComponent = i;
            }
            else
                Components.get(i).hasFocus = false;
    }
    
    public void checkComponents(int X, int Y)
    {
        boolean insideComponent = false;
        
        for(Component component : Components)
            if(component.Contains(X, Y))
                insideComponent = true;
        
        if(!insideComponent)
        {
            FocusedComponent = -1;
        
            for(Component component : Components)
                if(component.hasFocus)
                    component.hasFocus = false;
        }
    }
    
    public void onClick(int ActionX, int ActionY, int button, int clickCount)
    {      
        checkComponents(ActionX, ActionY);
        
        for(Component component : Components)
            if(component.Contains(ActionX, ActionY))
            {
                requestFocus(component);
                
                component.doAction("onClick");
                component.doAction("onClick" + clickCount);
                
                if(button == Input.MOUSE_LEFT_BUTTON)
                {
                    component.doAction("onLeftClick");
                    component.doAction("onLeftClick" + clickCount);
                }
                else if(button == Input.MOUSE_MIDDLE_BUTTON)
                {
                    component.doAction("onMiddleClick");
                    component.doAction("onMiddleClick" + clickCount);
                }
                else if(button == Input.MOUSE_RIGHT_BUTTON)
                {
                    component.doAction("onRightClick");
                    component.doAction("onRightClick" + clickCount);
                }
            }
    }
    
    public void stopAction(int ActionX, int ActionY)
    {
        for(Component component : Components)
                component.stopAction();
        
        isComponentActive = false;
    }
    
    public void mousePressed(int X, int Y)
    {
        checkComponents(X, Y);
        
        for(Component component : Components)
            if(component.Contains(X, Y))
            {
                requestFocus(component);
                component.mousePressed(X, Y);
                isComponentActive = true;
            }
    }
    
    public void keyPressed(int Key, char Char)
    {
        if(FocusedComponent != -1 && Components.size() > 0)
        {
            if(Key == Input.KEY_TAB)
            {
                FocusedComponent++;
                
                if(FocusedComponent < Components.size())
                {
                    Component component = Components.get(FocusedComponent);
                    requestFocus(component);
                }
                else
                {
                    FocusedComponent = 0;
                    Component component = Components.get(FocusedComponent);
                    requestFocus(component);
                }
            }
            else
            {
                Component component = Components.get(FocusedComponent);
                component.keyPressed(Key, Char);
            }
        }
    }
    
    public Window addComponent(Component component)
    {
        Components.add(component);
        return this;
    }
    
    public Window removeComponent(Component component)
    {
        Components.remove(component);
        return this;
    }
    
    public Window setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
        return this;
    }
    
    public boolean isVisible()
    {
        return isVisible;
    }
    
    public Window setPinned(boolean isPinned)
    {
        this.isPinned = isPinned;
        return this;
    }
    
    public boolean isPinned()
    {
        return isPinned;
    }
    
    public final Window setLocation(float X, float Y)
    {
        this.X = X;
        this.Y = Y;
        
        this.StartX = (int)X;
        this.StartY = (int)Y;
        
        return this;
    }
    
    public final Window setSize(int Width, int Height)
    {
        if(Width < WindowManager.wndBgTopLeft.getWidth() + WindowManager.wndBgTopCenter.getWidth() + WindowManager.wndBgTopRight.getWidth())
            this.Width = WindowManager.wndBgTopLeft.getWidth() + WindowManager.wndBgTopCenter.getWidth() + WindowManager.wndBgTopRight.getWidth();
        else
            this.Width = Width;
        
        if(Height < WindowManager.wndBgTopLeft.getHeight() + WindowManager.wndBgCenterLeft.getHeight() + WindowManager.wndBgBottomLeft.getHeight())
            this.Height = WindowManager.wndBgTopLeft.getHeight() + WindowManager.wndBgCenterLeft.getHeight() + WindowManager.wndBgBottomLeft.getHeight();
        else
            this.Height = Height;
        
        return this;
    }
    
    public Window Move(int X, int Y)
    {
        this.X += X;
        this.Y += Y;
        
        StartX = input.getMouseX();
        StartY = input.getMouseY();
        
        isFirstRender = false;
        
        return this;
    }
    
    public boolean Contains(int PointX, int PointY)
    {
        if(PointX >= X + 5 && PointY >= Y + 5 && PointX <= ((X - 10) + Width) && PointY <= ((Y - 10) + Height))
            return true;
        else
            return false;
    }
    
    public boolean MenuContains(int PointX, int PointY)
    {
        if(PointX >= X + 5 && PointY >= Y + 5 && PointX <= ((X - 10) + Width) && PointY <= (Y + 40))
            return true;
        else
            return false;
    }
    
    public Window Center()
    {
        this.setLocation((Program.Application.getWidth() / 2) - (this.getWidth() / 2), (Program.Application.getHeight() / 2) - (this.getHeight() / 2));
        return this;
    }
    
    public void Render(Graphics g)
    {
        if(isVisible)
        {
            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
            {
                if(Contains(input.getMouseX(), input.getMouseY()))
                    WindowManager.requestFocus(this);
                
                if((MenuContains(input.getMouseX(), input.getMouseY()) || isMouseDown) && !isComponentActive && hasFocus)
                {
                    isMouseDown = true;
                    
                    if(!isPinned)
                    {
                        if(isFirstRender)
                        {
                            StartX = input.getMouseX();
                            StartY = input.getMouseY();
                        }

                        Move(input.getMouseX() - StartX, input.getMouseY() - StartY);
                    }
                }
            }
            else
            {
                isMouseDown = false;
                isFirstRender = true;
            }
            
            WindowManager.wndBg.draw(X + 14, Y + 14, Width - 28, Height - 28);
            
            WindowManager.wndBgTopLeft.draw(X, Y);
            WindowManager.wndBgTopCenter.draw(X + WindowManager.wndBgTopLeft.getWidth(), Y, Width - (WindowManager.wndBgTopLeft.getWidth() + WindowManager.wndBgTopRight.getWidth()), WindowManager.wndBgTopCenter.getHeight());
            WindowManager.wndBgTopRight.draw((X + Width) - WindowManager.wndBgTopRight.getWidth(), Y);
            
            WindowManager.wndBgCenterLeft.draw(X, Y + WindowManager.wndBgTopLeft.getHeight(), WindowManager.wndBgCenterLeft.getWidth(), Height - (WindowManager.wndBgTopLeft.getHeight() + WindowManager.wndBgBottomLeft.getHeight()));
            WindowManager.wndBgCenterRight.draw((X + Width) - WindowManager.wndBgCenterRight.getWidth(), Y + WindowManager.wndBgTopRight.getHeight(), WindowManager.wndBgCenterRight.getWidth(), Height - (WindowManager.wndBgTopRight.getHeight() + WindowManager.wndBgBottomRight.getHeight()));
            
            WindowManager.wndBgBottomLeft.draw(X, (Y + Height) - WindowManager.wndBgBottomLeft.getHeight());
            WindowManager.wndBgBottomCenter.draw(X + WindowManager.wndBgBottomLeft.getWidth(), (Y + Height) - WindowManager.wndBgBottomCenter.getHeight(), Width - (WindowManager.wndBgBottomLeft.getWidth() + WindowManager.wndBgBottomRight.getWidth()), WindowManager.wndBgBottomCenter.getHeight());
            WindowManager.wndBgBottomRight.draw((X + Width) - WindowManager.wndBgBottomRight.getWidth(), (Y + Height) - WindowManager.wndBgBottomRight.getHeight());
            
            if(showMin)
                WindowManager.wndBtnMin.draw((X + Width - (WindowManager.wndBtnClose.getWidth() + 13)) - WindowManager.wndBtnMin.getWidth(), Y + 12);
            
            if(showClose)
                WindowManager.wndBtnClose.draw((X + Width - 15) - WindowManager.wndBtnClose.getWidth(), Y + 12);
            
            if(titleVisible)
                titleFont.drawString(X + ((getWidth() / 2) - (titleFont.getWidth(WindowTitle) / 2)), Y + 15, WindowTitle, new Color(194, 192, 180));
            
            //g.setColor(Color.gray);
            //g.drawString(this.WindowTitle, X + ((getWidth() / 2) - (g.getFont().getWidth(WindowTitle) / 2)), Y + 15);
            
            for(Component component : Components)
            {
                component.Render(g, this.X + 20, this.Y + 40, this.Width - 40, this.Height - 60);
                component.doAction("onRender");
            }
            
            doAction("onRender");
        }
    }
    
    public Component[] getComponents()
    {
        return Components.toArray(new Component[Components.size()]);
    }
    
    public String getWindowTitle()
    {
        return WindowTitle;
    }

    public Window setWindowTitle(String WindowTitle)
    {
        this.WindowTitle = WindowTitle;
        return this;
    }
    
    public boolean isShowMin()
    {
        return showMin;
    }

    public Window setShowMin(boolean showMin)
    {
        this.showMin = showMin;
        return this;
    }

    public boolean isShowClose()
    {
        return showClose;
    }

    public Window setShowClose(boolean showClose)
    {
        this.showClose = showClose;
        return this;
    }
    
    public Color getTitleColor()
    {
        return titleColor;
    }

    public void setTitleColor(Color titleColor)
    {
        this.titleColor = titleColor;
    }

    public boolean isTitleVisible()
    {
        return titleVisible;
    }

    public void setTitleVisible(boolean titleVisible)
    {
        this.titleVisible = titleVisible;
    }

    public float getX()
    {
        return X;
    }

    public void setX(float X)
    {
        this.X = X;
    }

    public float getY()
    {
        return Y;
    }

    public void setY(float Y)
    {
        this.Y = Y;
    }
}