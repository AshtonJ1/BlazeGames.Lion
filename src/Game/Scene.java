package Game;

import Core.*;
import Core.Movement.*;
import Input.KeyAdapter;
import Input.MouseAdapter;
import Network.ClientSocket;
import UI.ActionEvent;
import UI.Components.Button;
import UI.Components.ImageContainer;
import UI.Components.Textbox;
import UI.Window;
import UI.WindowManager;
import java.util.HashMap;
import java.util.Random;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Gageypoo
 */

public class Scene extends BasicGame
{
    private static Scene Instance;
    public static Scene getInstance()
    {
        if(Instance == null)
            Instance = new Scene();
        
        return Instance;
    }
    
    public TiledMap mapDefault;
    public Camera Cam;
    
    public Player player;
    public HashMap<Integer, Player> players = new HashMap();
    public Monster[] mobs;
    public GameContainer gc;
    
    private Image fullMapImage;
    
    private Window wnd, wndMiniMap;
    
    public static Image PlayerImage;
    
    public Input input;
    
    Random random = new Random();
    
    private Scene()
    {
        super("Blaze Games Lion's Beard");
    }
    
    @Override
    public void init(GameContainer gc) throws SlickException
    {
        this.gc = gc;
        
        gc.getGraphics().setAntiAlias(true);
        
        fullMapImage = new Image("Resource/Maps/testmap.png");
        
        PlayerImage = new Image("Resource/Sprite/Player.png");
        input = gc.getInput();
        input.addMouseListener(new MouseAdapter());
        input.addKeyListener(new KeyAdapter());
        
        Program.CSocket = new ClientSocket();
        
        mapDefault = new TiledMap("Resource/Maps/testmap.tmx", "Resource/TileSet");
        
        player = new Player(PlayerImage);
        player.setCurrentMap(mapDefault);
        mobs = new Monster[5];
        
        for(int i = 0; i < mobs.length - 1; i++)
        {
            int RandomNum = random.nextInt(3) + 1;
            
            if(RandomNum == 1)
                mobs[i] = new Monster("Resource/Sprite/Monsters/Blue.png", 32, 32);
            else if(RandomNum == 2)
                mobs[i] = new Monster("Resource/Sprite/Monsters/Skeleton.png", 32, 32);
            else
                mobs[i] = new Monster("Resource/Sprite/Monsters/Ghost.png", 32, 32);
            
            mobs[i].setCurrentMap(mapDefault);
            
            int x = random.nextInt(100) + 32;
            int y = random.nextInt(100) + 32;
            
            int TileID = mapDefault.getTileId((int)(x / 32), (int)(y / 32), mapDefault.getLayerIndex("NoWalk"));
            String NoWalk = mapDefault.getTileProperty(TileID, "nowalk", "false");

            if(!NoWalk.toLowerCase().equals("true"))
                mobs[i].setPos(x, y);
            else
                mobs[i].setPos(2500, 2500);
        }

        try
        {
            mobs[mobs.length - 1] = new Monster("Resource/Sprite/Monsters/Skeleton.png", 32, 32);
            mobs[mobs.length - 1].setCurrentMap(mapDefault);
            mobs[mobs.length - 1].setPos(2080, 2432);
            mobs[mobs.length - 1].setScale(3);
        }
        catch(Exception e)
        {
            
        }
        
          
        player.setPos(220, 270);
        //player.setScale(.75F);
             
        Cam = new Camera(mapDefault.getWidth() * 32, mapDefault.getHeight() * 32);
        
        new Thread(new PlayerMovement()).start();
        new Thread(new EntityMovement()).start();
        
        /*wnd = new Window("la", 200, 200, 200, 200);
        
        Button myButton = new Button("Button", 50, 50);
        myButton.setPadding(15);
        
        myButton.addActionEvent(new ActionEvent()
        {
            @Override
            public void actionPerformed() 
            {
                //player.WalkTo(500, 500);
                System.out.println("You clicked a button.");
            }
        });
        
        Textbox myTextbox = new Textbox(5, 100);
        
        
        wnd.addComponent(myButton);
        wnd.addComponent(myTextbox);
        
        WindowManager.addWindow(wnd);*/
        
        currentMapImage = fullMapImage.getSubImage(-(int)Cam.GetXPos(), -(int)Cam.GetYPos(), Cam.getCamWidth(), Cam.getCamHeight()).getScaledCopy(.25F);
        wndMiniMap = new Window("Minimap", Program.Application.getWidth() - (currentMapImage.getWidth() + 14 + 5), 5, currentMapImage.getWidth() + 14, currentMapImage.getHeight() + 36);
        wndMiniMap.addComponent(new ImageContainer(currentMapImage, 0, 0));
        WindowManager.addWindow(wndMiniMap);
        
        wnd = new Window("Login", 200, 200, 200, 135);
        Textbox txtBox = new Textbox(10, 10, 165, 20, false);
        wnd.addComponent(txtBox);
        
        Textbox pass = new Textbox(10, 40, 165, 20, true);
        pass.addActionEvent(new ActionEvent("onReturn")
        {
            @Override
            public void actionPerformed() 
            {
                player.WalkTo(500, 500);
            }
        });
        wnd.addComponent(pass);
        Button tmp = new Button("Login", 10, 70, 165, 20);
        tmp.addActionEvent(new ActionEvent("onLeftClick")
        {
            @Override
            public void actionPerformed() {
                player.WalkTo(500, 500);
            }
        });
        tmp.addActionEvent(new ActionEvent("onRightClick")
        {
            @Override
            public void actionPerformed() {
                wnd.setVisible(false);
            }
        });
        
        wnd.addComponent(tmp);
        WindowManager.addWindow(wnd);
    }
    
    @Override
    public void update(GameContainer gc, int i) throws SlickException
    {
        
    }
    
    private Image currentMapImage;
    
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException 
    {
        if(Program.isRunning)
        {
            Cam.Render(g);
            mapDefault.render(0, 0);

            for(Monster mob : mobs)
                if(Cam.isEntityInCamera(mob))
                    mob.Draw();

            for(Player plyer : players.values())
                plyer.Draw();

            player.Draw();

            currentMapImage = fullMapImage.getSubImage(-(int)Cam.GetXPos(), -(int)Cam.GetYPos(), Cam.getCamWidth(), Cam.getCamHeight()).getScaledCopy(.25F);
            ((ImageContainer)wndMiniMap.getComponents()[0]).setImage(currentMapImage);
            
            g.resetTransform();

            g.setColor(Color.white);
            g.drawString("X: " + player.getX() + " Y: " + player.getY(), 10, 30);
            g.drawString("Health: ", 10, 50);
            g.setColor(Color.red);
            g.fillRect(80, 55, 100, 10);
            
            for(Window window : WindowManager.Windows)
                if(window.isVisible())
                    window.Render(g);
        }
    }
}
