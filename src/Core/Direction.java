package Core;

/**
 *
 * @author Ashton
 */

public enum Direction 
{
    None(-1),
    Up(3),
    Down(0),
    Left(1),
    Right(2);
    
    private int DirectionIndex;
    
    public int getDirectionIndex()
    {
        return DirectionIndex;
    }
    
    Direction(int DirectionIndex)
    {
        this.DirectionIndex = DirectionIndex;
    }
    
    public static Direction fromInteger(int i)
    {
        switch(i)
        {
            case -1: return Direction.None;
            case 0: return Direction.Down;
            case 1: return Direction.Left;
            case 2: return Direction.Right;
            case 3: return Direction.Up;
            default: return null;
        }
    }
}
