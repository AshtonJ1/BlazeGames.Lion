package Core;

import java.util.Random;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Gage
 */

public class Monster extends Entity
{
    public float Speed = 1;
    
    private Animation[] spriteAnimation;
    private int direction = 0;
    
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
        startWalking(direction.getDirectionIndex());
    }
    
    public void startWalking(int direction)
    {
        setDirection(direction);
        spriteAnimation[direction].stopAt(-2);
        spriteAnimation[direction].start();
        
        if(direction == 3 && (this.getY() - Speed) >= 0)
            this.setYWalk(this.getY() - Speed);
        if(direction == 0)
            this.setYWalk(this.getY() + Speed);
        if(direction == 1 && this.getX() - Speed >= 0)
            this.setXWalk(this.getX() - Speed);
        if(direction == 2)
            this.setXWalk(this.getX() + Speed);
    }
    
    public void stopWalking(Direction direction)
    {
        spriteAnimation[direction.getDirectionIndex()].stopAt(0);
    }
    
    public void setXWalk(float X)
    {
        int TileID = CurrentMap.getTileId((int)(ToXCenter(X) / 32), (int)(ToYCenter(getY()) / 32), CurrentMap.getLayerIndex("NoWalk"));
        String NoWalk = CurrentMap.getTileProperty(TileID, "nowalk", "false");
        
        if(!NoWalk.toLowerCase().equals("true"))
            setX(X);
    }
    
    public void setYWalk(float Y)
    {
        int TileID = CurrentMap.getTileId((int)(ToXCenter(getX()) / 32), (int)(ToYCenter(Y) / 32), CurrentMap.getLayerIndex("NoWalk"));
        String NoWalk = CurrentMap.getTileProperty(TileID, "nowalk", "false");
        
        if(!NoWalk.toLowerCase().equals("true"))
            setY(Y);
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
    
    int frame = 0;
    int MoveForFrames;
    int WaitForFrames;
    int MobMovePosition;
    public void AIMove()
    {
        
        frame++;
        if(frame == 1)
        {
            MoveForFrames = new Random().nextInt(360) + 120;
            WaitForFrames = new Random().nextInt(240) + 120;
            MobMovePosition = new Random().nextInt(4);
        }
        
        if(frame <= MoveForFrames)
            this.startWalking(MobMovePosition);
        else if(frame >= MoveForFrames + WaitForFrames)
            frame = 0;
        else
            this.stopWalking(Direction.fromInteger(MobMovePosition));
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
        return spriteAnimation[direction].getHeight();
        //return TextureAnimation.getImage(0).getHeight();
    }

    @Override
    public float getWidth()
    {
        return spriteAnimation[direction].getWidth();
        //return TextureAnimation.getImage(0).getWidth();
    }
    
    public Monster(String TexturePath, int EntityWidth, int EntityHeight) throws SlickException
    {
        Image spriteImg = new Image(TexturePath);
        
        int Columns = spriteImg.getWidth() / EntityWidth;
        int Rows = spriteImg.getHeight() / EntityHeight;
        
        spriteAnimation = new Animation[Rows];
        
        for(int i = 0; i < Rows; i++)
            spriteAnimation[i] = new Animation();
            
        Image[][] singleSprite = new Image[Rows][Columns];
        
        for(int i = 0; i < Rows; i++)
        {
            for(int j = 0; j < Columns; j++)
            {
                singleSprite[i][j] =  spriteImg.getSubImage(j * EntityWidth, i * EntityHeight, EntityWidth, EntityHeight);
                spriteAnimation[i].addFrame(singleSprite[i][j], 300);
                spriteAnimation[i].stop();
            }
        }
    }
}