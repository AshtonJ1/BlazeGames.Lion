package Core.Movement;

import Core.Monster;
import Core.Player;
import Game.Program;
import Game.Scene;

/**
 *
 * @author Ashton
 */

public class EntityMovement implements Runnable
{
    private Scene scene;

    @Override
    public void run() 
    {
        scene = Scene.getInstance();

        while(Program.isRunning) //game.running
        {
            try
            {
                for(Monster mob : scene.mobs)
                    mob.AIMove();
                
                for(Player player : scene.players.values())
                    player.AIMove();
                
                Thread.sleep(8);
            }
            catch(Exception e)
            {
                
            }
        }
    }
    
}
