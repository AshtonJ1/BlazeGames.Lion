/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.Components;

import Game.Scene;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Ashton
 */
public class ImageContainer extends Component
{
    private Image image;
    public ImageContainer(Image image, int X, int Y)
    {
        this.image = image;
        setLocation(X, Y);
        setSize(image.getWidth(), image.getHeight());
    }
    
    @Override
    public void Render(Graphics g, float ParentX, float ParentY, int ParentWidth, int ParentHeight)
    {
        setAbsoluteLocation((int)ParentX+getX(), (int)ParentY+getY(), 0);
        g.drawImage(image, getAbsoluteX(), getAbsoluteY());
        Scene s = Scene.getInstance();
        //g.drawImage(Scene.PlayerImage.getScaledCopy(5, 5), getAbsoluteX() + (s.player.getX()*.060F), getAbsoluteY() + (s.player.getY()*.060F));
        g.fillRect(getAbsoluteX() + (s.player.getXCenter()*.06F), getAbsoluteY() + (s.player.getYCenter()*.06F), 5, 5);
        //System.out.println(getAbsoluteX() + (s.player.getX()));
    }
    
    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }
}
