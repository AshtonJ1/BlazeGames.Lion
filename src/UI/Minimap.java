package UI;

import Core.Monster;
import Game.Program;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Ashton
 */
public class Minimap extends Window
{
    private Image fullMapImage, currentMapImage;
    private TiledMap map;
    private int mapX, mapY, mapScale = 6, playerX, playerY;
    private Monster[] monsters;
    
    public Minimap(Image mapImage, TiledMap map)
    {
        super("Minimap", Program.Application.getWidth() - 307, 0, 307, 297, false);
        this.fullMapImage = mapImage;
        
        try
        {
            this.currentMapImage = new Image(230, 230);
        }
        catch (SlickException e){  }
        
        this.map = map;
    }
    
    @Override
    public void Render(Graphics g)
    {
        if(isVisible())
        {
            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
            {
                if(Contains(input.getMouseX(), input.getMouseY()))
                    WindowManager.requestFocus(this);
                
                if((Contains(input.getMouseX(), input.getMouseY()) || isMouseDown) && !isComponentActive && hasFocus)
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
                
            currentMapImage.draw(getX() + 20, getY() + 45);
            WindowManager.wndBgMinimap.draw(getX(), getY());
        }
        
    }
    
    public void Update(Graphics g) throws SlickException
    {
        g.clear();
        g.setColor(Color.black);
        g.fillOval(0, 0, 230, 230);
        g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
        g.drawImage(fullMapImage.getSubImage(mapX, mapY, (int)(230 * mapScale), (int)(230 * mapScale)).getScaledCopy(230, 230), 0, 0);
        
        g.setColor(Color.blue);
        g.fillRect(((playerX - mapX) / mapScale), ((playerY - mapY) / mapScale), 5, 5);
        Rectangle miniMapZone = new Rectangle(mapX, mapY, getWidth() * mapScale, getHeight() * mapScale);
        g.setColor(Color.red);
        for(Monster mob : monsters)
        {
            if(miniMapZone.contains(mob.getX(), mob.getY()))
            {
                g.fillRect(((mob.getX() - mapX) / mapScale), ((mob.getY() - mapY) / mapScale), 5, 5);
            }
        }
        
        g.copyArea(currentMapImage, 0, 0);
        
        g.setDrawMode(Graphics.MODE_NORMAL);
        g.clear();
    }
    
    public void setMapPosition(int playerX, int playerY)
    {
        this.playerX = playerX;
        this.playerY = playerY;
        
        int mapX = playerX - (100 * mapScale);
        int mapY = playerY - (100 * mapScale);

        if(mapX < 0)
            mapX = 0;
        else if(mapX + (int)(230 * mapScale) > map.getWidth() * 32)
            mapX = (map.getWidth() * 32) - (int)(230 * mapScale);
        
        if(mapY < 0)
            mapY = 0;
        else if(mapY + (int)(230 * mapScale) > map.getHeight() * 32)
            mapY = (map.getHeight() * 32) - (int)(230 * mapScale);
        
        this.mapX = mapX;
        this.mapY = mapY;
    }
    
    public void setMonsters(Monster[] monsters)
    {
        this.monsters = monsters;
    }
}
