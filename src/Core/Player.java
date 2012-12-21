package Core;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Gage
 */

public class Player extends Entity
{   
    public float Speed = 1;
    
    private SpriteSheet spriteSheet = null;
    private Animation[] spriteAnimation;
    private int direction = 0;
    
    public float getXCenter()
    {
        return getX() + getWidth() / 2;
    }
    
    public float getYCenter()
    {
         return getY() + getHeight() / 2;       
    }
    
    public int getDirection()
    {
        return direction;
    }
    
    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
    public void startWalking(Direction direction)
    {
        setDirection(direction.getDirectionIndex());
        spriteAnimation[direction.getDirectionIndex()].stopAt(-2);
        spriteAnimation[direction.getDirectionIndex()].start();
        
        StopWalkTo();
        
        if(direction == Direction.Up && (this.getY() - Speed) >= 0)
            this.setYWalk(this.getY() - Speed);
        if(direction == Direction.Down)
            this.setYWalk(this.getY() + Speed);
        if(direction == Direction.Left && this.getX() - Speed >= 0)
            this.setXWalk(this.getX() - Speed);
        if(direction == Direction.Right)
            this.setXWalk(this.getX() + Speed);
    }
    
    public void stopWalking(Direction direction)
    {
        if(WalkToX == -1 && WalkToY == -1)
            spriteAnimation[direction.getDirectionIndex()].stopAt(2);
    }
    
    @Override
    public void Draw()
    {
        float X = (getX() + spriteAnimation[direction].getWidth() / 2) - (spriteAnimation[direction].getWidth() / 2 * getScale());
        float Y = (getY() + spriteAnimation[direction].getHeight() / 2) - (spriteAnimation[direction].getHeight() / 2 * getScale());
        
        spriteAnimation[direction].draw(X, Y, spriteAnimation[direction].getWidth() * getScale(), spriteAnimation[direction].getHeight() * getScale());
    }
    
    @Override
    public float getHeight()
    {
        return spriteAnimation[direction].getHeight() * getScale();
        //return TextureAnimation.getImage(0).getHeight();
    }

    @Override
    public float getWidth()
    {
        return spriteAnimation[direction].getWidth();
        //return TextureAnimation.getImage(0).getWidth();
    }
    
    @Override
    public void setPos(float X, float Y)
    {
        setX(X);
        setY(Y);
        
        System.out.println("X: " + X + "Y:" + Y);
    }
    
    public void setXWalk(float X)
    {
        int TileID = CurrentMap.getTileId((int)(ToXCenter(X) / 32), (int)(getYCenter() / 32), CurrentMap.getLayerIndex("NoWalk"));
        String NoWalk = CurrentMap.getTileProperty(TileID, "nowalk", "false");
        String Warp = CurrentMap.getTileProperty(TileID, "warp", "false");
        
        if(!Warp.equals("false"))
            setPos(Float.parseFloat(Warp.split(",")[0]), Float.parseFloat(Warp.split(",")[1]));
        
        if(!NoWalk.toLowerCase().equals("true"))
            setX(X);
    }
    
    public void setYWalk(float Y)
    {
        int TileID = CurrentMap.getTileId((int)(getXCenter() / 32), (int)(ToYCenter(Y) / 32), CurrentMap.getLayerIndex("NoWalk"));
        String NoWalk = CurrentMap.getTileProperty(TileID, "nowalk", "false");
        String Warp = CurrentMap.getTileProperty(TileID, "warp", "false");
        
        if(!Warp.equals("false"))
            setPos(Float.parseFloat(Warp.split(",")[0]), Float.parseFloat(Warp.split(",")[1]));
        
        if(!NoWalk.toLowerCase().equals("true"))
            setY(Y);
    }
    
    private float WalkToX = -1, WalkToY = -1;
    public void AIMove()
    {
        if(WalkToX == -1 || WalkToY == -1)
            return;
        
        Direction dir = Direction.None;
        
        /*float WalkToXLength = WalkToX - getX();
        float WalkToYLength = WalkToY - getY();
        
        if(WalkToXLength < 0)
            WalkToXLength = -WalkToXLength;
        if(WalkToYLength < 0)
            WalkToYLength = -WalkToYLength;*/

            if(WalkToX != getX())
            {
                if(WalkToX > getX())
                {
                    dir = Direction.Right;
                    if(WalkToX >= getX() + Speed)
                        setXWalk(getX() + Speed);
                    else
                        setXWalk(WalkToX);
                }
                else
                {
                    dir = Direction.Left;
                    if(WalkToX <= getX() - Speed)
                        setXWalk(getX() - Speed);
                    else
                        setXWalk(WalkToX);
                }
            }
            
            if(WalkToY != getY())
            {
                if(WalkToY > getY())
                {
                    dir = Direction.Down;
                    if(WalkToY >= getY() + Speed)
                        setYWalk(getY() + Speed);
                    else
                        setYWalk(WalkToY);
                }
                else
                {
                    dir = Direction.Up;
                    if(WalkToY <= getY() - Speed)
                        setYWalk(getY() - Speed);
                    else
                        setYWalk(WalkToY);
                }
            }
        
        
        if(dir == Direction.None)
        {
            spriteAnimation[getDirection()].stop();
            spriteAnimation[getDirection()].setCurrentFrame(2);
            
            WalkToX = -1;
            WalkToY = -1;
        }
        else
        {
            setDirection(dir.getDirectionIndex());
            spriteAnimation[dir.getDirectionIndex()].stopAt(-2);
            spriteAnimation[dir.getDirectionIndex()].start();
        }
    }
    
    public void WalkTo(float X, float Y)
    {
        WalkToX = X;
        WalkToY = Y;
    }
    
    public void AppendWalkTo(float X, float Y)
    {
        if(WalkToX == -1)
            WalkToX = getX();
        if(WalkToY == -1)
            WalkToY = getY();
        
        WalkToX += X;
        WalkToY += Y;
    }
    
    public void StopWalkTo()
    {
        WalkToX = -1;
        WalkToY = -1;
    }
    
    @Override
    public float ToXCenter(float X)
    {
        return X + getWidth() / 2;
    }
    
    @Override
    public float ToYCenter(float Y)
    {
        return Y + getHeight() / 2;
    }
    
    public Player(Image Texture) throws SlickException
    {
        int Columns = 4;
        int Rows = 4;
        
        int EntityWidth = 37;
        int EntityHeight = 46;
        
        spriteAnimation = new Animation[4];
        
        for(int i = 0; i < 4; i++)
            spriteAnimation[i] = new Animation();
            
        Image[][] singleSprite = new Image[Columns][Rows];
        
        for(int i = 0; i < Rows; i++)
        {
            for(int j = 0; j < Columns; j++)
            {
                singleSprite[i][j] =  Texture.getSubImage(j * EntityWidth, i * EntityHeight, EntityWidth, EntityHeight);
                spriteAnimation[i].addFrame(singleSprite[i][j], 300);
                spriteAnimation[i].stop();
            }
        }
    }
    
    public Player(String TexturePath) throws SlickException
    {
        int Columns = 4;
        int Rows = 4;
        
        int EntityWidth = 37;
        int EntityHeight = 46;
        
        spriteSheet = new SpriteSheet(TexturePath, 148, 190);
        spriteAnimation = new Animation[4];
        
        for(int i = 0; i < 4; i++)
            spriteAnimation[i] = new Animation();
            
        Image[][] singleSprite = new Image[Columns][Rows];
        
        for(int i = 0; i < Rows; i++)
        {
            for(int j = 0; j < Columns; j++)
            {
                singleSprite[i][j] =  spriteSheet.getSubImage(j * EntityWidth, i * EntityHeight, EntityWidth, EntityHeight);
                spriteAnimation[i].addFrame(singleSprite[i][j], 300);
                spriteAnimation[i].stop();
            }
        }
    }
}