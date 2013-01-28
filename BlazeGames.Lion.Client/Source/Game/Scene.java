package Game;

import Core.Camera;
import Core.Entities.Monster;
import Core.Entities.Player;
import Core.Movement.EntityMovement;
import Core.Movement.PlayerMovement;
import Input.KeyAdapter;
import Input.MouseAdapter;
import Interface.ActionEvent;
import Interface.Components.Button;
import Interface.Components.Textbox;
import Interface.Window;
import Interface.WindowManager;
import Interface.Windows.Minimap;
import Network.ClientSocket;
import java.util.HashMap;
import java.util.Random;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Gage
 */
public class Scene extends BasicGame
{
    // <editor-fold desc="Singleton" defaultstate="collapsed">
    private static Scene Instance;
    
    public static Scene getInstance()
    {
        if(Instance == null)
            Instance = new Scene();
        
        return Instance;
    }
    // </editor-fold>
    
    // <editor-fold desc="Entity" defaultstate="collapsed">
    public static Image PlayerImage;
    public Player myPlayer;
    public HashMap<Integer, Player > Players  = new HashMap();
    public HashMap<Integer, Monster> Monsters = new HashMap();
    
    public int MonsterIndex = -1, PlayerIndex = -1;
    // </editor-fold>
    
    // <editor-fold desc="Windows" defaultstate="collapsed">
    public Window wndLogin;
    public Minimap wndMiniMap;
    // </editor-fold>
    
    public GameContainer gameContainer;
    public Input gameInput;
    public Camera gameCamera;
    public TiledMap gameMap;
    public boolean gameStarted = false;
    public float bgAlpha = 1F;
    
    private Random random = new Random();
    private Image backgroundImage;
    
    public Scene()
    {
        super("Lion");
    }
    
    @Override
    public void init(GameContainer gc) throws SlickException
    {
        this.gameContainer = gc;
        gc.getGraphics().setAntiAlias(true);
        WindowManager.init();
        
        PlayerImage = new Image("Resource/Sprite/Player.png");
        backgroundImage = new Image("Resource/Images/background.png");
        
        gameInput = gc.getInput();
        gameInput.addMouseListener(new MouseAdapter());
        gameInput.addKeyListener(new KeyAdapter());
        
        Program.CSocket = new ClientSocket();
        
        gameMap = new TiledMap("Resource/Maps/testmap.tmx", "Resource/TileSet");
        
        myPlayer = new Player(PlayerImage);
        myPlayer.setCurrentMap(gameMap);
        myPlayer.setPos(220, 270);
        
        gameCamera = new Camera(gameMap.getWidth() * 32, gameMap.getHeight() * 32);
        
        new Thread(new PlayerMovement()).start();
        new Thread(new EntityMovement()).start();
        
        wndMiniMap = new Minimap(new Image("Resource/Maps/testmap.png"), gameMap);
        wndMiniMap.setMapPosition((int)myPlayer.getXCenter(), (int)myPlayer.getYCenter());
        WindowManager.addWindow(wndMiniMap);
        
        wndLogin = new Window("Login", 200, 200, 250, 175);
        
        Textbox txtAccount = new Textbox(15, 10, 165, false);
        txtAccount.setStretchX(true);
        txtAccount.addActionEvent(new ActionEvent("onReturn")
        {
            @Override
            public void actionPerformed() 
            {
                gameStarted = true;
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
                gameStarted = true;
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
                gameStarted = true;
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
        wndMiniMap.setMonsters(Monsters.values().toArray(new Monster[Monsters.size()]));
        wndMiniMap.setMapPosition((int)myPlayer.getXCenter(), (int)myPlayer.getYCenter());
        wndMiniMap.Update(gc.getGraphics());
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        if(Program.isRunning)
        {
            if(!gameStarted)
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
            
            gameCamera.Render(g);
            gameMap.render(0, 0);
            
            for(Monster monster : Monsters.values())
                if(gameCamera.isEntityInCamera(monster))
                    monster.Draw();
            
            for(Player player : Players.values())
                player.Draw();
            
            if(MonsterIndex != -1)
                g.drawRect(Monsters.get(MonsterIndex).getX()-1, Monsters.get(MonsterIndex).getY() -1, 
                        Monsters.get(MonsterIndex).getWidth() + 2, Monsters.get(MonsterIndex).getHeight() + 2);
                
                myPlayer.Draw();
                g.setDrawMode(Graphics.MODE_NORMAL);
                g.resetTransform();
            
            if(gameStarted && bgAlpha > 0)
                bgAlpha -= 0.02;
            if(!gameStarted && bgAlpha < 1)
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
