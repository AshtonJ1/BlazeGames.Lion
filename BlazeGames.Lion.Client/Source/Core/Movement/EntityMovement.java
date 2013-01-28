package Core.Movement;

import Core.Entities.Monster;
import Core.Entities.Player;
import Game.Program;
import Game.Scene;

/**
 *
 * @author Gage
 */
public class EntityMovement implements Runnable
{
    private Scene scene;
    
    @Override
    public void run()
    {
        scene = Scene.getInstance();
        
        while(Program.isRunning)
        {
            try
            {
                for(Monster monster : scene.Monsters.values())
                    monster.AIMove();
                
                for(Player player : scene.Players.values())
                    player.AIMove();
                
                Thread.sleep(20);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
}