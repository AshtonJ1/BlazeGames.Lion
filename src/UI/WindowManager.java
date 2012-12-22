package UI;

import java.util.ArrayList;

/**
 *
 * @author Gage Orsburn
 */
public class WindowManager
{
    public static ArrayList<Window> Windows = new ArrayList<>(); 
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
                window.doAction(x, y);
        }
    }
    
    public static void mousePressed(int button, int x, int y)
    {
        
        /*CheckWindows(x, y);
        
        if(Windows.size() > 0 && Focused != -1)
        {
            Window window = Windows.get(Focused);
            
            if(window != null)
                window.doAction(x, y);
        }*/
    }
    
        
    public static void mouseReleased(int button, int x, int y)
    {
        /*if(Windows.size() > 0 && Focused != -1)
        {
            Window window = Windows.get(Focused);
            
            if(window != null)
                window.stopAction(x, y); 
        }*/
    }
    
    private static void CheckWindows(int X, int Y)
    {
        boolean insideWindow = false;
        
        for(Window window : Windows)
            if(window.Contains(X, Y))
                insideWindow = true;
        
        if(!insideWindow)
            Focused = -1;
    }
    
    public static void requestFocus(Window window)
    {
        for(int i = 0; i < Windows.size(); i++)
            if(Windows.get(i).equals(window))
                if(Focused != i)
                    Focused = i;
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
}
