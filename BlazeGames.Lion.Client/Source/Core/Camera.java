package Core;

import Core.Entities.Entity;
import Game.Program;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Gage
 */
public class Camera 
{
    private float X = 0, Y = 0;
    private int mapWidth, mapHeight;
    private Rectangle cameraZone;
    
    public Camera(int mapWidth, int mapHeight)
    {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        
        cameraZone = new Rectangle(0, 0, Program.Application.getWidth()+20, Program.Application.getHeight()+20);
    }
    
    public void setCamSize(float Width, float Height)
    {
        cameraZone.setWidth(Width);
        cameraZone.setHeight(Height);
    }
    
    public int getCamWidth()
    {
        return (int)cameraZone.getWidth();
    }
    
    public int getCamHeight()
    {
        return (int)cameraZone.getHeight();
    }
    
    public void Render(Graphics g)
    {
        g.translate((int)this.X, (int)this.Y);
        cameraZone.setX(-X-20);
        cameraZone.setY(-Y-20);
    }
    
    public boolean isEntityInCamera(Entity entity)
    {         
        if(cameraZone.contains(entity.getX(), entity.getY()))
            return true;
        else
            return false;
    }
    
    public void UpdatePos(float X, float Y)
    {
        int ProgramWidth = Program.Application.getWidth();
        int ProgramHeight = Program.Application.getHeight();
        
        if(X - ProgramWidth / 2 + 16 < 0)
            this.X = 0;
    	else if(X + ProgramWidth / 2 + 16 > mapWidth)
            this.X = -mapWidth + ProgramWidth;
    	else
            this.X = -X + ProgramWidth / 2 - 16;
 
    	if(Y - ProgramHeight / 2 + 16 < 0)
            this.Y = 0;
    	else if(Y + ProgramHeight / 2 + 16 > mapHeight)
            this.Y = -mapHeight + ProgramHeight;
    	else
            this.Y = -Y + ProgramHeight / 2 - 16;
    }
    
    public void CenterOnEntity(Entity entity)
    { 
        UpdatePos(entity.getX(), entity.getY());
    }
    
    public float GetXPos()
    {
        return X;
    }

    public float GetYPos()
    {
        return Y;
    }
}
