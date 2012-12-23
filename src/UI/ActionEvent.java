package UI;

/**
 *
 * @author Gage
 */
public abstract class ActionEvent 
{
    private String eventType;
    
    public ActionEvent(String eventType)
    {
        this.eventType = eventType;
    }
    
    public String getEventType()
    {
        return this.eventType;
    }
    
    public abstract void actionPerformed();
}
