package Core.Movement;

import Game.Program;
import Game.Scene;
import Interface.WindowManager;
import Network.Packet;
import org.newdawn.slick.Input;

/**
 *
 * @author Gage
 */
public class PlayerMovement implements Runnable
{

    private Scene scene;
    private Input input;

    @Override
    public void run()
    {
        scene = Scene.getInstance();
        input = scene.gameInput;

        int i = 0;

        float CurrentX = 0, CurrentY = 0;
        Direction CurrentDirection = Direction.None;

        while (Program.isRunning)
        {
            try
            {
                Direction dir = Direction.None;

                if (WindowManager.noFocus())
                {
                    if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT))
                    {
                        scene.myPlayer.Speed = 1.5F;
                    } 
                    else if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL))
                    {
                        scene.myPlayer.Speed = .5F;
                    } 
                    else
                    {
                        scene.myPlayer.Speed = 1;
                    }

                    if (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP))
                    {
                        scene.myPlayer.AppendWalkTo(0, -scene.myPlayer.Speed);
                        dir = Direction.Up;
                    }

                    if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN))
                    {
                        scene.myPlayer.AppendWalkTo(0, scene.myPlayer.Speed);
                        dir = Direction.Down;
                    }

                    if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT))
                    {
                        scene.myPlayer.AppendWalkTo(-scene.myPlayer.Speed, 0);
                        dir = Direction.Left;
                    }

                    if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT))
                    {
                        scene.myPlayer.AppendWalkTo(scene.myPlayer.Speed, 0);
                        dir = Direction.Right;
                    }
                }

                if (i == 20)
                {
                    if (CurrentX != scene.myPlayer.getX() || CurrentY != scene.myPlayer.getY() || CurrentDirection != dir)
                    {
                        Program.CSocket.Send(Packet.New(0x000020, scene.myPlayer.getX(), scene.myPlayer.getY(), dir.getDirectionIndex()));
                    }

                    CurrentX = scene.myPlayer.getX();
                    CurrentY = scene.myPlayer.getY();
                    CurrentDirection = dir;

                    i = -1;
                }

                scene.myPlayer.AIMove();
                scene.gameCamera.CenterOnEntity(scene.myPlayer);

                i++;
                Thread.sleep(8);
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}