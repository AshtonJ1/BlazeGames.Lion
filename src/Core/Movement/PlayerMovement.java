package Core.Movement;

import Core.Direction;
import Game.Program;
import Game.Scene;
import Network.Packet;
import UI.WindowManager;
import org.newdawn.slick.Input;

/**
 *
 * @author Ashton
 */

public class PlayerMovement implements Runnable
{
    private Scene scene;
    private Input input;
    
    @Override
    public void run() 
    {
        scene = Scene.getInstance();
        input = scene.input;
        int i = 0;
        
        float CurrentX = 0, CurrentY = 0;
        Direction CurrentDirection = Direction.None;
        
        while(Program.isRunning) //game.running
        {
            //if(WindowManager.noFocus())
            //{
                try
                { 
                    if(input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT))
                        scene.player.Speed = 1.5F;
                    else if(input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL))
                        scene.player.Speed = .5F;
                    else
                        scene.player.Speed = 1;

                    Direction dir = Direction.None;

                    if(input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP))
                    {
                        scene.player.AppendWalkTo(0, -scene.player.Speed);
                        dir = Direction.Up;
                    }

                    if(input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN))
                    {
                        scene.player.AppendWalkTo(0, scene.player.Speed);
                        dir = Direction.Down;
                    }

                    if(input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT))
                    {
                        scene.player.AppendWalkTo(-scene.player.Speed, 0);
                        dir = Direction.Left;
                    }

                    if(input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT))
                    {
                        scene.player.AppendWalkTo(scene.player.Speed, 0);
                        dir = Direction.Right;
                    }

                    if(i == 20)
                    {
                        if(CurrentX != scene.player.getX() || CurrentY != scene.player.getY() || CurrentDirection != dir)
                            Program.CSocket.Send(Packet.New(0x000020, scene.player.getX(), scene.player.getY(), dir.getDirectionIndex()));

                        CurrentX = scene.player.getX();
                        CurrentY = scene.player.getY();
                        CurrentDirection = dir;

                        i=-1;
                    }

                    scene.player.AIMove();

                    scene.Cam.CenterOnEntity(scene.player);



                    i++;
                    Thread.sleep(8);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            //}
        }
    }
    
}
