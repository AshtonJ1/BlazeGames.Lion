package UI;

import Game.Program;
import Game.Scene;
import UI.Components.Component;
import java.awt.Font;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author Gage Orsburn
 */
public class Window
{
    private float X, Y;
    private int Width, Height, StartX, StartY, FocusedComponent = -1;
    private boolean isFirstRender, isVisible, isMouseDown, isPinned, isComponentActive = false, hasFocus = false, showMin, showClose, titleVisible = true;

    private String WindowTitle;

    private ArrayList<Component> Components = new ArrayList<>();
    private ArrayList<ActionEvent> ActionEvents = new ArrayList<>();
    private Input input;
    
    private Image bgTopLeft, bgTopCenter, bgTopRight, bgCenterLeft, bgCenterRight, bgBottomLeft, bgBottomCenter, bgBottomRight, bg, btnClose, btnMin;
    private TrueTypeFont titleFont;
    private Color titleColor;
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, boolean isPinned, boolean showMin, boolean showClose, boolean titleVisible) throws SlickException
    {
        this.bgTopLeft = new Image("Resource/UI/wnd_top_left.png");
        this.bgTopCenter = new Image("Resource/UI/wnd_top_center.png");
        this.bgTopRight = new Image("Resource/UI/wnd_top_right.png");
        this.bgCenterLeft = new Image("Resource/UI/wnd_center_left.png");
        this.bgCenterRight = new Image("Resource/UI/wnd_center_right.png");
        this.bgBottomLeft = new Image("Resource/UI/wnd_bottom_left.png");
        this.bgBottomCenter = new Image("Resource/UI/wnd_bottom_center.png");
        this.bgBottomRight = new Image("Resource/UI/wnd_bottom_right.png");
        this.bg = new Image("Resource/UI/wnd_bg.png");
        this.btnClose = new Image("Resource/UI/wnd_btn_close.png");
        this.btnMin = new Image("Resource/UI/wnd_btn_min.png");
        
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
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, boolean isPinned, boolean showMin, boolean showClose) throws SlickException
    {
        this(WindowTitle, X, Y, Width, Height, isPinned, showMin, showClose, true);
    }
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height, boolean isPinned) throws SlickException
    {
        this(WindowTitle, X, Y, Width, Height, isPinned, false, true, true);
    }
    
    public Window(String WindowTitle, float X, float Y, int Width, int Height) throws SlickException
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
        if(Width < bgTopLeft.getWidth() + bgTopCenter.getWidth() + bgTopRight.getWidth())
            this.Width = bgTopLeft.getWidth() + bgTopCenter.getWidth() + bgTopRight.getWidth();
        else
            this.Width = Width;
        
        if(Height < bgTopLeft.getHeight() + bgCenterLeft.getHeight() + bgBottomLeft.getHeight())
            this.Height = bgTopLeft.getHeight() + bgCenterLeft.getHeight() + bgBottomLeft.getHeight();
        else
            this.Height = Height;
        
        return this;
    }
    
    private Window Move(int X, int Y)
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
            
            bg.draw(X + 14, Y + 14, Width - 28, Height - 28);
            
            bgTopLeft.draw(X, Y);
            bgTopCenter.draw(X + bgTopLeft.getWidth(), Y, Width - (bgTopLeft.getWidth() + bgTopRight.getWidth()), bgTopCenter.getHeight());
            bgTopRight.draw((X + Width) - bgTopRight.getWidth(), Y);
            
            bgCenterLeft.draw(X, Y + bgTopLeft.getHeight(), bgCenterLeft.getWidth(), Height - (bgTopLeft.getHeight() + bgBottomLeft.getHeight()));
            bgCenterRight.draw((X + Width) - bgCenterRight.getWidth(), Y + bgTopRight.getHeight(), bgCenterRight.getWidth(), Height - (bgTopRight.getHeight() + bgBottomRight.getHeight()));
            
            bgBottomLeft.draw(X, (Y + Height) - bgBottomLeft.getHeight());
            bgBottomCenter.draw(X + bgBottomLeft.getWidth(), (Y + Height) - bgBottomCenter.getHeight(), Width - (bgBottomLeft.getWidth() + bgBottomRight.getWidth()), bgBottomCenter.getHeight());
            bgBottomRight.draw((X + Width) - bgBottomRight.getWidth(), (Y + Height) - bgBottomRight.getHeight());
            
            if(showMin)
                btnMin.draw((X + Width - (btnClose.getWidth() + 13)) - btnMin.getWidth(), Y + 12);
            
            if(showClose)
                btnClose.draw((X + Width - 15) - btnClose.getWidth(), Y + 12);
            
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
}