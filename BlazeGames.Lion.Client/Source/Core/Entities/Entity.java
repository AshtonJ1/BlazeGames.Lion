package Core.Entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Gage
 */
public class Entity 
{
    private float X = 0, Y = 0, Rotation = 0, Scale = 1;
    private int DirectionIndex = 0;
    
    private Animation TextureAnimation = new Animation();
    public TiledMap CurrentMap;
    
    public TiledMap getCurrentMap()
    {
        return CurrentMap;
    }

    public void setCurrentMap(TiledMap CurrentMap) 
    {
        this.CurrentMap = CurrentMap;
    }
    
    public float getScale()
    {
        return Scale;
    }

    public void setScale(float Scale)
    {
        this.Scale = Scale;
        
    }
    
    public float getHeight()
    {
        return TextureAnimation.getImage(0).getHeight();
    }

    public float getWidth()
    {
        return TextureAnimation.getImage(0).getWidth();
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
    
    public void setPos(float X, float Y)
    {  
        setX(X);
        setY(Y);
    }
    
    public float ToXCenter(float X)
    {
        return X + getWidth() / 2;
    }
    
    public float ToYCenter(float Y)
    {
        return Y + getHeight() / 2;
    }

    public float getRotation()
    {
        return Rotation;
    }

    public void setRotation(float Rotation)
    {
        this.Rotation = Rotation;
    }
    
    public boolean Contains(int PointX, int PointY)
    {
        if(PointX >= X && PointY >= Y && PointX <= ((X) + 32) && PointY <= ((Y) + 32))
            return true;
        else
            return false;
    }
    
    public void Draw()
    {
        
    }
    
    public Entity(String TexturePath) throws SlickException
    {
        
    }
    
    public Entity()
    {
        
    }
}
