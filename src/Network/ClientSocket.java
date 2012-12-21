package Network;

import Core.Direction;
import Core.Player;
import Game.Scene;
import naga.NIOService;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.packetreader.RawPacketReader;
import naga.packetwriter.RawPacketWriter;

/**
 *
 * @author Ashton
 */

public class ClientSocket 
{
    private NIOService nioService;
    private NIOSocket nioSocket;
    
    public ClientSocket()
    {
        try
        {
            nioService = new NIOService();
            nioSocket = nioService.openSocket("184.155.26.226", 26061);
            
            nioSocket.setPacketReader(RawPacketReader.INSTANCE);
            nioSocket.setPacketWriter(RawPacketWriter.INSTANCE);
            
            nioSocket.listen(new SocketObserver()
            {
                @Override
                public void connectionOpened(NIOSocket nios) 
                {
                    System.out.println("Opened");
                    Send(Packet.New(0x000001));
                }

                @Override
                public void connectionBroken(NIOSocket nios, Exception excptn) 
                {
                    
                }

                @Override
                public void packetReceived(NIOSocket nios, byte[] bytes)
                {
                    Packet[] paks = Packet.SplitPacket(bytes);
                    for(Packet pak : paks)
                    {
                        int PacketHeader = pak.readInteger();
                        if(PacketHeader == 0x100010)
                        {
                            try
                            {
                                //Needs to be called on the scene thread
                                
                                int PlayerID = pak.readInteger();
                            
                                Player player = new Player(Scene.PlayerImage);
                                player.setCurrentMap(Scene.getInstance().mapDefault);
                                player.setPos(100, 100);
                                
                                Scene.getInstance().players.put(PlayerID, player);
                                System.out.println("Create Player " + PlayerID);
                            }
                            catch(Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        else
                        if(PacketHeader == 0x100020)
                        {
                            int PlayerID = pak.readInteger();
                            float X = pak.readFloat(), Y = pak.readFloat();
                            Direction direction = Direction.fromInteger(pak.readInteger());
                            
                            System.out.println(PlayerID);
                            
                            Player player = Scene.getInstance().players.get(PlayerID);
                            player.WalkTo(X, Y);
                            
                        }
                        else
                        {
                            System.out.println("Unknown Packet: " + PacketHeader);
                        }
                    }
                };

                @Override
                public void packetSent(NIOSocket nios, Object o)
                {
                    System.out.println("sent");
                }
            });
            
            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                      while (true)
                          nioService.selectBlocking();
                    }
                    catch (Exception e) { e.printStackTrace(); }
                }
            }.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void Send(final Packet packet)
    {
        try
        {
            new Thread()
            {
                @Override
                public void run()
                {
                      nioSocket.write(packet.getData());
                }
            }.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } 
    }
}
