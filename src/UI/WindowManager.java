package UI;

import java.util.ArrayList;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Gage Orsburn
 */
public class WindowManager
{
    private static ArrayList<Window> Windows = new ArrayList<>(); 

    public static Window getWindow(String windowTitle)
    {
        for(Window wnd : Windows)
            if(wnd.getWindowTitle().equals(windowTitle))
                return wnd;
        
        return null;
    }
    private static int Focused = -1;
    
    public static void addWindow(Window window)
    {
        Windows.add(window); 
    }
    
    public static void removeWindow(Window window)
    {
        Windows.remove(window);
    }
    
    public static Window getFocusedWindow()
    {
        if(!noFocus())
            return Windows.get(Focused);
        else
            return null;
    }
    
    public static void keyPressed(int Key, char Char)
    {
        if(Windows.size() > 0 && Focused != -1)
        {
            Window window = Windows.get(Focused);
            
            if(window != null)
                window.keyPressed(Key, Char);
        }
    }
    
    public static void keyReleased(int Key, char Char)
    {
        
    }
    
    public static void mouseClick(int button, int x, int y, int clickCount)
    {
        CheckWindows(x, y);
        
        if(Windows.size() > 0 && Focused != -1)
        {
            Window window = Windows.get(Focused);
            
            if(window != null)
                window.onClick(x, y, button, clickCount);
        }
    }
    
    public static void mousePressed(int button, int x, int y)
    {
        
        CheckWindows(x, y);
        
        if(Windows.size() > 0 && Focused != -1)
        {
            Window window = Windows.get(Focused);
            
            if(window != null)
                window.mousePressed(x, y);
        }
    }
    
        
    public static void mouseReleased(int button, int x, int y)
    {
        if(Windows.size() > 0 && Focused != -1)
        {
            Window window = Windows.get(Focused);
            
            if(window != null)
                window.stopAction(x, y); 
        }
    } 
    
    private static void CheckWindows(int X, int Y)
    {
        boolean insideWindow = false;
        
        for(Window window : Windows)
            if(window.Contains(X, Y) && window.isVisible())
                insideWindow = true;
        
        if(!insideWindow)
        {
            Focused = -1;
        
            for(Window window : Windows)
            {
                window.checkComponents(X, Y);
                window.setFocused(false);
            }
        }
    }
    
    public static void requestFocus(Window window)
    {
        boolean focusedWindowHasMouseDown = false;    

        if(!noFocus())
            if(Windows.get(Focused).isMouseDown())
                focusedWindowHasMouseDown = true;
        
        if(!focusedWindowHasMouseDown)
        {
            for(int i = 0; i < Windows.size(); i++)
            {
                if(Windows.get(i).equals(window))
                {
                    Windows.get(i).setFocused(true);

                    if(Focused != i)
                        Focused = i;
                }
                else
                    Windows.get(i).setFocused(false);
            }
        }
    }
    
    public static void resetFocus()
    {
        Focused = -1;
    }
    
    public static boolean noFocus()
    {
        if(Focused == -1)
            return true;
        else
            return false;
    }
    
    public static void RenderWindows(Graphics g)
    {
        for(Window window : Windows)
            if(window.isVisible() && !window.hasFocus())
                window.Render(g);
        
        if(!noFocus())
            Windows.get(Focused).Render(g);
    }
}
