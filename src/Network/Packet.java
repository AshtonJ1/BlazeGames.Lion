// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported
// License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/.
// Copyright 2012 Blaze Games

package Network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * Packet to package data to be sent over the TCP Protocol. 
 * Packets MUST be read in the same order they were written.
 * 
 * @author Gage Orsburn
 * @version 2.3.1
 */

public final class Packet 
{
    /**
     * Bit order enumeration.
     */
    public enum Endian 
    {
        /**
         * Order bytes by the most significant byte.<br /><br />
         * [Most Significant][...][Least Significant]
         */
        Big(),
        /**
         * Order bytes by the least significant byte.<br /><br />
         * [Least Significant][...][Most Significant]
         */
        Little();
    }
    
    private byte[] Data = null;
    private int Pointer = 0;
    private static Endian Endianess = Endian.Little;
    
    /**
     * Returns a new packet filled with Data. TODO: Finish array types.
     * @param Data A variable of type object... so that literally anything can be placed inside.
     * @return 
     */
    public static Packet New(Object... Data)
    {
        Packet packet = new Packet();
        
        for(Object data : Data)
        {
            switch(data.getClass().getName())
            {
                //Primitive
                case "java.lang.Byte":    packet.addByte((Byte)data);       break;
                case "java.lang.Boolean": packet.addBoolean((Boolean)data); break;
                case "java.lang.Short":   packet.addShort((Short)data);     break;
                case "java.lang.Integer": packet.addInteger((Integer)data); break;
                case "java.lang.Long":    packet.addLong((Long)data);       break;
                case "java.lang.Float":   packet.addFloat((Float)data);     break;
                case "java.lang.Double":  packet.addDouble((Double)data);   break;
                
                //Reference 
                case "java.lang.String":  packet.addString((String)data);   break;
                case "java.lang.Object":                                    break;
                    
                //TODO: Implement.
                //Array
                case "[B":                                                  break;
                case "[Z":                                                  break;
                case "[S":                                                  break;
                case "[I":                                                  break;
                case "[L":                                                  break;
                case "[F":                                                  break;
                case "[D":                                                  break;
                    
                //Need to get string/object array name
                default: System.err.printf("[Packet.New][Invalid Type=%s]", data.getClass().getName()); break;
            }   
        }
        
        return packet;
    }
    
    /**
     * Takes a TCP buffer and splits it up into packets. With the TCP Protocol 
     * a packet is combined into a single buffer if sent so close to another 
     * packet. When dealing with rapid packet transfer we need to use this 
     * method to make sure we have all our packets accounted for.
     * @param Buffer Incoming buffer of data to split.
     * @return An array of all the packets contained in the inputted buffer.
     */
    public static Packet[] SplitPacket(byte[] Buffer)
    {
        ArrayList<Packet> Packets = new ArrayList<>();
        
        int InnerPacketSize;
        int CurrentPointer = 0;
        
        while(true)
        {
            try
            {
                if(Endianess.equals(Endian.Big))
                    InnerPacketSize = (Buffer[CurrentPointer] << 24) + ((Buffer[CurrentPointer+1] & 0XFF) << 16) + ((Buffer[CurrentPointer+2] & 0xFF) << 8) + (Buffer[CurrentPointer+3] & 0xFF);
                else
                    InnerPacketSize = (Buffer[CurrentPointer]) + ((Buffer[CurrentPointer+1]) << 8) + ((Buffer[CurrentPointer+2]) << 16) + (Buffer[CurrentPointer+3] << 24);

                if(InnerPacketSize == 0)
                    break;

                byte[] PacketData = Arrays.copyOfRange(Buffer, CurrentPointer, CurrentPointer+InnerPacketSize+8);
                CurrentPointer += InnerPacketSize + 8;

                Packets.add(new Packet(PacketData));   
            }
            catch(Exception e)
            {
                break;
            }
        }
        
        return Packets.toArray(new Packet[Packets.size()]);
    }
    
    /**
     * Creates a null packet with little endian and 204800 elements.
     */
    public Packet()
    {
        Data = new byte[204800];
        
        addInteger(0);
        addInteger(0);
    }
    
    /**
     * Creates a null packet with little endian and quantity of elements specified.
     * @param Size Quantity of elements to create within packet.
     */
    public Packet(int Size)
    {
        Data = new byte[Size];
        
        addInteger(0);
        addInteger(0);
    }
    
    /**
     * Creates a packet from existing byte array.
     * @param b Byte array to fill packet data with.
     */
    public Packet(byte[] b)
    {
        Data = b;
        
        readInteger();
        readInteger();
    }
    
    /**
     * Creates a packet from an existing packet.
     * @param p Packet to fill new packet with.
     */
    public Packet(Packet p)
    {
        Data = p.getData();
        
        readInteger();
        readInteger();
    }
    
    /**
     * Creates a null packet with 2048 elements, with endian specified.
     * @param b Specifies big or little endian.
     */
    public Packet(Endian e)
    {
        Data = new byte[2048];
        Endianess = e;
        
        addInteger(0);
        addInteger(0);
    }
    
    /**
     * Creates a null packet with quantity of elements and endian specified.
     * @param Size Quantity of elements to create within packet.
     * @param b Specifies big or little endian.
     */
    public Packet(int Size, Endian e)
    {
        Data = new byte[Size];
        Endianess = e;
        
        addInteger(0);
        addInteger(0);
    }
    
    /**
     * Creates a packet from existing byte array while specifying endian.
     * @param b Byte array to fill packet data with.
     * @param c Specifies big or little endian.
     */
    public Packet(byte[] b, Endian e)
    {
        Data = b;
        Endianess = e;
        
        readInteger();
        readInteger();
    }
    
    /**
     * Creates a packet from an existing packet.
     * @param p Packet to fill new packet with.
     * @param b Specifies big or little endian.
     */
    public Packet(Packet p, Endian e)
    {
        Data = p.getData();
        Endianess = e;
        
        readInteger();
        readInteger();
    }
    
    /**
     * Returns data in packet as a byte array.
     * @return All elements in packet as a byte array.
     */
    public byte[] getData()
    {
        return Arrays.copyOf(Data, getLength());
    }
    
    /**
     * Returns the length of the bytes used in the byte array.
     * @return The length of Data[] as an integer.
     */
    public int getLength()
    {
        return Pointer + 8;
    }
    
    /**
     * Returns data in packet as a string.
     * @return All elements in packet as a string in the format of [Pointer:Byte]
     */
    @Override
    public String toString()
    {
        String tmp = "";
        
        for(int i = 0; i < getLength(); i++)
        {
            tmp += String.format("[%d:%s]", i,Byte.toString(Data[i]));
        }
        
        return tmp;
    }
    
    /**
     * Sets endian to big.
     */
    public void setBigEndian()
    {
        Endianess = Endian.Big;
    }
    
    /**
     * Sets endian to little.
     */
    public void setLittleEndian()
    {
        Endianess = Endian.Little;
    }
    
    /**
     * Adds byte to packet.
     * @param b Byte to add to packet.
     */
    public void addByte(byte b)
    {
        Data[Pointer] = b;
        Pointer++;      
        
        updateChecksum();
    }
    
    /**
     * Adds boolean to packet.
     * @param b Boolean to add to packet.
     */
    public void addBoolean(boolean b)
    {
        Data[Pointer] = (byte)(b ? 1 : 0);
        Pointer++;
        
        updateChecksum();
    }
    
    /**
     * Adds short to packet.
     * @param s Short to add to packet.
     */
    public void addShort(short s)
    {
        if(Endianess == Endian.Big)
        {
            addByte((byte)(s >> 8));
            addByte((byte)(s     ));
        }
        else
        {
            addByte((byte)(s     ));
            addByte((byte)(s >> 8));
        }
    }
    
    /**
     * Adds character to packet.
     * @param c Character to add to packet.
     */
    public void addCharacter(char c)
    {
        addByte((byte)c);
    }
    
    /**
     * Adds integer to packet.
     * @param i Integer to add to packet.
     */
    public void addInteger(int i)
    {
        if(Endianess == Endian.Big)
        {
            addByte((byte)(i >> 24));
            addByte((byte)(i >> 16));
            addByte((byte)(i >> 8 ));
            addByte((byte)(i      ));
        }
        else
        {
            addByte((byte)(i      ));
            addByte((byte)(i >> 8 ));
            addByte((byte)(i >> 16));
            addByte((byte)(i >> 24));
        }
    }
    
    /**
     * Adds float to packet.
     * @param f Float to add to packet.
     */
    public void addFloat(float f)
    {
        addInteger(Float.floatToRawIntBits(f));
    }
    
    /**
     * Adds long to packet.
     * @param l Long to add to packet.
     */
    public void addLong(long l)
    {
        if(Endianess == Endian.Big)
        {
            addByte((byte)(l >> 56));
            addByte((byte)(l >> 48));
            addByte((byte)(l >> 40));
            addByte((byte)(l >> 32));
            addByte((byte)(l >> 24));
            addByte((byte)(l >> 16));
            addByte((byte)(l >> 8 ));
            addByte((byte)(l      ));
        }
        else
        {
            addByte((byte)(l      ));
            addByte((byte)(l >> 8 ));
            addByte((byte)(l >> 16));
            addByte((byte)(l >> 24));
            addByte((byte)(l >> 32));
            addByte((byte)(l >> 40));
            addByte((byte)(l >> 48));
            addByte((byte)(l >> 56));
        }
    }
    
    /**
     * Adds double to packet.
     * @param d Double to add to packet.
     */
    public void addDouble(double d)
    {
        addLong(Double.doubleToRawLongBits(d));
    }
    
    /**
     * Adds string to packet.
     * @param s String to add to packet.
     */
    public void addString(String s)
    {
        char[] charArray = s.toCharArray();
        int Length = charArray.length;
        
        addInteger(Length);
        
        for(char c : charArray) 
        {
            addCharacter(c);
        }
    }
    
    /**
     * Returns byte at current position.
     * @return Byte to be read.
     */
    public byte readByte()
    {
        return Data[Pointer++];
    }
    
    /**
     * Returns boolean at current position.
     * @return Boolean to be read.
     */
    public boolean readBoolean()
    {
        return (Data[Pointer++] == 1 ? true : false);
    }
    
    /**
     * Returns short at current position.
     * @return Short to be read.
     */
    public short readShort()
    {
        if(Endianess == Endian.Big)
        {
            return (short)((short)(readByte() << 8) + (readByte()));
        }
        else
        {
            return (short)((short)(readByte()) + (readByte() << 8));
        }
    }
    
    /**
     * Returns character at current position.
     * @return Character to be read.
     */
    public char readCharacter()
    {
        return (char)readByte();
    }
    
    /**
     * Returns integer at current position.
     * @return Integer to be read.
     */
    public int readInteger()
    {
        if(Endianess == Endian.Big)
        {
            return (readByte() << 24) + ((readByte() & 0xFF) << 16) + ((readByte() & 0xFF) << 8) + ((readByte() & 0xFF));
        }
        else
        {
            return (readByte() & 0xFF) + ((readByte() & 0xFF) << 8) + ((readByte() & 0xFF) << 16) + (readByte() << 24);
        }
    }
    
    /**
     * Returns float at current position.
     * @return Float to be read.
     */
    public float readFloat()
    {
        return Float.intBitsToFloat(readInteger());
    }
    
    /**
     * Returns long at current position.
     * @return Long to be read.
     */
    public long readLong()
    {
        if(Endianess == Endian.Big)
        {
            return ((long)(readInteger()) << 32) + (readInteger() & 0xFFFFFFFFL);
        }
        else
        {
            return (readInteger() & 0xFFFFFFFFL) + ((long)(readInteger()) << 32);
        }
    }
    
    /**
     * Returns float at current position.
     * @return Double to be read.
     */
    public double readDouble()
    {
        return Double.longBitsToDouble(readLong());
    }
    
    /**
     * Returns string at current position.
     * @return String to be read.
     */
    public String readString()
    {
        String temp = "";
        int Length = readInteger();
        
        for(int i = 0; i < Length; i++) 
        {
            temp += readCharacter();
        }
        
        return temp;
    }
    
    /**
     * Checks if the packet is valid and non-modified.
     * @return Returns true if the packet is valid and non-modified.
     */
    public boolean isValid()
    {
        if(Data.length > 8)
        {
            int tmpPointer = Pointer;
            Pointer = 0;
           
            int PacketLength = readInteger();
            int Checksum = readInteger();
            
            Pointer = tmpPointer;
            
            byte[] InnerData = Arrays.copyOfRange(Data, 8, getLength());
            System.out.println("isValid() Length: " + getLength());
            System.out.println("isValid() Checsum: " + getChecksum(InnerData));
            
            if (PacketLength != InnerData.length) 
            {
                return false;
            }
            else if (getChecksum(InnerData) != Checksum)
            {
                    return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return true;
        }
    }
    
    private void updateChecksum()
    {
        if(Data.length > 8)
        {           
            byte[] InnerData = Arrays.copyOfRange(Data, 8, getLength());
            //System.out.println("updateChecksum() Length: " + getLength());
            int Checksum = getChecksum(InnerData);
            //System.out.println("updateChecksum() Checksum: " + Checksum);
            
            if(Endianess == Endian.Big)
            {
                Data[0] = (byte)(InnerData.length >> 24);
                Data[1] = (byte)(InnerData.length >> 16);
                Data[2] = (byte)(InnerData.length >> 8 );
                Data[3] = (byte)(InnerData.length      );  
                Data[4] = (byte)(Checksum >> 24);
                Data[5] = (byte)(Checksum >> 16);
                Data[6] = (byte)(Checksum >> 8 );
                Data[7] = (byte)(Checksum      );
            }
            else
            {
                Data[0] = (byte)(InnerData.length      );
                Data[1] = (byte)(InnerData.length >>  8);
                Data[2] = (byte)(InnerData.length >> 16);
                Data[3] = (byte)(InnerData.length >> 24);  

                Data[4] = (byte)(Checksum      );
                Data[5] = (byte)(Checksum >>  8);
                Data[6] = (byte)(Checksum >> 16);
                Data[7] = (byte)(Checksum >> 24);
            }
        }
    }
    
    private int getChecksum(byte[] byteArray)
    {
        final CRC32 checksum = new CRC32();
        checksum.update(byteArray, 0, byteArray.length);
        return (int)checksum.getValue();
    }
}