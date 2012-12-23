/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import java.nio.ByteBuffer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.ImageData;

/**
 *
 * @author Ashton
 */
public class GraphicsImage implements ImageData
{
    private ByteBuffer byteBuffer;
    private int Width, Height;
    
    public GraphicsImage(Graphics g, Camera Cam)
    {
        byteBuffer = ByteBuffer.allocateDirect(Cam.getCamWidth() * Cam.getCamHeight() *20);
        g.getArea((int)Cam.GetXPos(), (int)Cam.GetYPos(), Cam.getCamWidth(), Cam.getCamHeight(), byteBuffer);
        Width = Cam.getCamWidth();
        Height = Cam.getCamHeight();
    }
    
    @Override
    public int getDepth()
    {
       return 4;
    }

    @Override
    public int getWidth()
    {
        return Width;
    }

    @Override
    public int getHeight()
    {
        return Height;
    }

    @Override
    public int getTexWidth()
    {
        return Width;
    }

    @Override
    public int getTexHeight()
    {
        return Height;
    }

    @Override
    public ByteBuffer getImageBufferData()
    {
        return byteBuffer;
    }
    
}
