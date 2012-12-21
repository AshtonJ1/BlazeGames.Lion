package Core;

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
    private int Direction = 0;
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
        //return spriteAnimation[Direction].getHeight();
        return TextureAnimation.getImage(0).getHeight();
    }

    public float getWidth()
    {
        //return spriteAnimation[Direction].getWidth();
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
        //this.Texture.setRotation(Rotation);
    }
    
    public void setTexture(String TexturePath) throws SlickException
    {
        //TextureAnimation.addFrame(new Image(TexturePath), 1000);
    }
    
    public void addFrame(String TexturePath, int duration) throws SlickException
    {
        //TextureAnimation.addFrame(new Image(TexturePath), duration);
    }
    
    public void Draw()
    {
        //float X = (this.X + TextureAnimation.getWidth() / 2) - (TextureAnimation.getWidth() / 2 * Scale);
        //float Y = (this.Y + TextureAnimation.getHeight() / 2) - (TextureAnimation.getHeight() / 2 * Scale);
        
        //float X = (this.X + Sprites.getSubImage(0, 0, 32, 42).getWidth() / 2) - (Sprites.getSubImage(0, 0, 32, 42).getWidth() / 2 * Scale);
        //float Y = (this.Y + Sprites.getSubImage(0, 0, 32, 42).getHeight() / 2) - (Sprites.getSubImage(0, 0, 32, 42).getHeight() / 2 * Scale);
        
        //Sprites.getSubImage(0, 0, 32, 42).draw(X, Y);
        //TextureAnimation.draw(X, Y, TextureAnimation.getWidth() * Scale, TextureAnimation.getHeight() * Scale);
        
        /*float X = (this.X + spriteAnimation[Direction].getWidth() / 2) - (spriteAnimation[Direction].getWidth() / 2 * Scale);
        float Y = (this.Y + spriteAnimation[Direction].getHeight() / 2) - (spriteAnimation[Direction].getHeight() / 2 * Scale);
        
        spriteAnimation[Direction].draw(X, Y, spriteAnimation[Direction].getWidth() * Scale, spriteAnimation[Direction].getHeight() * Scale);*/
        //spriteAnimation[Direction].s
    }
    
    public Entity(String TexturePath) throws SlickException
    {
        //TextureAnimation.addFrame(new Image(TexturePath), 1000);
        //Sprites = new SpriteSheet(TexturePath, 37, 46);
        
        //TextureAnimation = new Animation(Sprites, 200);
    }
    
    public Entity()
    {
        
    }
}