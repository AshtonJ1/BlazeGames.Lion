package Network;

import naga.NIOService;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.packetreader.RawPacketReader;
import naga.packetwriter.RawPacketWriter;

/**
 *
 * @author Gage
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
            nioSocket = nioService.openSocket("127.0.0.1", 26060);

            nioSocket.setPacketReader(RawPacketReader.INSTANCE);
            nioSocket.setPacketWriter(RawPacketWriter.INSTANCE);

            nioSocket.listen(new SocketObserver()
            {
                @Override
                public void connectionOpened(NIOSocket nios)
                {
                }

                @Override
                public void connectionBroken(NIOSocket nios, Exception excptn)
                {
                }

                @Override
                public void packetReceived(NIOSocket nios, byte[] bytes)
                {
                    Packet[] packets = Packet.SplitPacket(bytes);

                    for (Packet packet : packets)
                    {
                        int PacketHeader = packet.readInteger();

                        switch (PacketHeader)
                        {
                            default:
                                System.out.println("Unknown Packet: " + PacketHeader);
                                break;
                        }
                    }
                }

                @Override
                public void packetSent(NIOSocket nios, Object o)
                {
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
                        {
                            nioService.selectBlocking();
                        }
                    } catch (Exception e)
                    {
                        System.out.println(e);
                    }
                }
            }.start();
        } catch (Exception e)
        {
            System.out.println(e);
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
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}