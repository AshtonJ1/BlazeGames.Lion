package Game;

import Core.*;
import Core.Movement.*;
import Input.*;
import UI.*;
import UI.Components.*;
import java.util.*;
import org.newdawn.slick.*;
import Network.ClientSocket;
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
    
    public boolean inGame = false;
    public float bgAlpha = 1F;
    
    public TiledMap mapDefault;
    public Camera Cam;
    
    public Player player;
    public HashMap<Integer, Player> players = new HashMap();
    public Monster[] mobs;
    public GameContainer gc;
    
    public Window wndLogin;
    public Minimap wndMiniMap;
    
    public static Image PlayerImage;
    
    public Input input;
    
    private Random random = new Random();
    private Image backgroundImage;

    private Scene()
    {
        super("Blaze \"Lion\"");
    }
    
    @Override
    public void init(GameContainer gc) throws SlickException
    {
        this.gc = gc;
        gc.getGraphics().setAntiAlias(true);
        WindowManager.init();
        
        PlayerImage = new Image("Resource/Sprite/Player.png");
        backgroundImage = new Image("Resource/Images/background.png");
        
        input = gc.getInput();
        input.addMouseListener(new MouseAdapter());
        input.addKeyListener(new KeyAdapter());
        
        Program.CSocket = new ClientSocket();
        
        mapDefault = new TiledMap("Resource/Maps/testmap.tmx", "Resource/TileSet");
        
        player = new Player(PlayerImage);
        player.setCurrentMap(mapDefault);
        mobs = new Monster[50];
        
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
            
            int x = random.nextInt(3100) + 32;
            int y = random.nextInt(3100) + 32;
            
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
             
        Cam = new Camera(mapDefault.getWidth() * 32, mapDefault.getHeight() * 32);
        
        new Thread(new PlayerMovement()).start();
        new Thread(new EntityMovement()).start();
        
        wndMiniMap = new Minimap(new Image("Resource/Maps/testmap.png"), mapDefault);
        wndMiniMap.setMapPosition((int)player.getXCenter(), (int)player.getYCenter());
        WindowManager.addWindow(wndMiniMap);
        
        wndLogin = new Window("Login", 200, 200, 250, 175);
        
        Textbox txtAccount = new Textbox(15, 10, 165, false);
        txtAccount.setStretchX(true);
        txtAccount.addActionEvent(new ActionEvent("onReturn")
        {
            @Override
            public void actionPerformed() 
            {
                inGame = true;
                wndLogin.setVisible(false);
                Program.Application.setShowFPS(true);
            }
        });
        
        Textbox txtPassword = new Textbox(15, 40, 165, true);
        txtPassword.setStretchX(true);
        txtPassword.addActionEvent(new ActionEvent("onReturn")
        {
            @Override
            public void actionPerformed() 
            {
                inGame = true;
                wndLogin.setVisible(false);
                Program.Application.setShowFPS(true);
            }
        });
        
        Button btnLogin = new Button("Login", 15, 70, 165);
        btnLogin.setStretchX(true);
        btnLogin.addActionEvent(new ActionEvent("onLeftClick")
        {
            @Override
            public void actionPerformed()
            {
                inGame = true;
                wndLogin.setVisible(false);
                Program.Application.setShowFPS(true);
            }
        });
        
        wndLogin.addComponent(txtAccount).
            addComponent(txtPassword).
            addComponent(btnLogin).
            Center().
            setShowMin(true);
        WindowManager.addWindow(wndLogin);
    }
    
    @Override
    public void update(GameContainer gc, int i) throws SlickException
    {
        wndMiniMap.setMonsters(mobs);
        wndMiniMap.setMapPosition((int)player.getXCenter(), (int)player.getYCenter());
        wndMiniMap.Update(gc.getGraphics());
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException 
    {
        if(Program.isRunning)
        {
            if(!inGame)
            {
                if(wndMiniMap.isVisible())
                    wndMiniMap.setVisible(false);
                if(!wndLogin.isVisible())
                    wndLogin.setVisible(true);
            }
            else
            {
                if(!wndMiniMap.isVisible())
                    wndMiniMap.setVisible(true);
                if(wndLogin.isVisible())
                    wndLogin.setVisible(false);
            }
            
            Cam.Render(g);         
                mapDefault.render(0, 0);

                for(Monster mob : mobs)
                    if(Cam.isEntityInCamera(mob))
                        mob.Draw();

                for(Player plyer : players.values())
                    plyer.Draw();

                player.Draw();

                g.setDrawMode(Graphics.MODE_NORMAL);
                
                g.resetTransform();
            
            if(inGame)
            {
                g.setColor(Color.white);
                g.drawString("X: " + player.getX() + " Y: " + player.getY(), 10, 30);
                g.drawString("Health: ", 10, 50);
                g.setColor(Color.red);
                g.fillRect(80, 55, 100, 10);
            }
            
            if(inGame && bgAlpha > 0)
                bgAlpha -= 0.02;
            if(!inGame && bgAlpha < 1)
                bgAlpha += 0.02;
            
            if(bgAlpha > 0)
            {
                Image bg = backgroundImage.getScaledCopy(Program.Application.getWidth(), Program.Application.getHeight());
                bg.setAlpha(bgAlpha);
                g.drawImage(bg, 0, 0);
            }
            
            WindowManager.RenderWindows(g);
        }
    }
}
