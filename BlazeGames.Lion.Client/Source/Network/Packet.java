// This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported
// License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/.
// Copyright 2013 BlazeSoft
package Network;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * Packet to package data to be sent over the TCP Protocol. Packets MUST be read
 * in the same order they were written.
 *
 * @author Gage Orsburn
 * @version 2.4.1
 */
public final class Packet
{

    /**
     * Bit order enumeration.
     */
    public enum Endian
    {

        /**
         * Order bytes by the most significant byte.<br /><br /> [Most
         * Significant][...][Least Significant]
         */
        Big(),
        /**
         * Order bytes by the least significant byte.<br /><br /> [Least
         * Significant][...][Most Significant]
         */
        Little();
    }
    private byte[] Data = null;
    private int Pointer = 0;
    private static Packet.Endian Endianess = Packet.Endian.Little;

    /**
     * Returns a new packet filled with Data. TODO: Finish array types.
     *
     * @param Data A variable of type object... so that literally anything can
     * be placed inside.
     * @return
     */
    public static Packet New(Object... Data)
    {
        Packet packet = new Packet();

        for (Object data : Data)
        {
            switch (data.getClass().getName())
            {
                //Primitive
                case "java.lang.Byte":
                    packet.addByte((Byte) data);
                    break;
                case "java.lang.Boolean":
                    packet.addBoolean((Boolean) data);
                    break;
                case "java.lang.Short":
                    packet.addShort((Short) data);
                    break;
                case "java.lang.Integer":
                    packet.addInteger((Integer) data);
                    break;
                case "java.lang.Long":
                    packet.addLong((Long) data);
                    break;
                case "java.lang.Float":
                    packet.addFloat((Float) data);
                    break;
                case "java.lang.Double":
                    packet.addDouble((Double) data);
                    break;

                //Reference 
                case "java.lang.String":
                    packet.addString((String) data);
                    break;
                case "java.lang.Object":
                    packet.addObject(data);
                    break;

                //Array
                case "[B":
                    packet.addBytes((byte[]) data);
                    break;
                case "[Z":
                    packet.addBooleans((boolean[]) data);
                    break;
                case "[S":
                    packet.addShorts((short[]) data);
                    break;
                case "[I":
                    packet.addIntegers((int[]) data);
                    break;
                case "[L":
                    packet.addLongs((long[]) data);
                    break;
                case "[F":
                    packet.addFloats((float[]) data);
                    break;
                case "[D":
                    packet.addDoubles((double[]) data);
                    break;

                case "[Ljava.lang.String;":
                    packet.addStrings((String[]) data);
                    break;
                //Need to get object array name

                //Assume unknown type extends an object
                default:
                    packet.addObject(data);
                    break;

                //Debugging purposes
                //default: System.err.printf("[Packet.New][Invalid Type=%s]\n", data.getClass().getName()); break;
            }
        }

        return packet;
    }

    /**
     * Takes a TCP buffer and splits it up into packets. With the TCP Protocol a
     * packet is combined into a single buffer if sent so close to another
     * packet. When dealing with rapid packet transfer we need to use this
     * method to make sure we have all our packets accounted for.
     *
     * @param Buffer Incoming buffer of data to split.
     * @return An array of all the packets contained in the inputted buffer.
     */
    public static Packet[] SplitPacket(byte[] Buffer)
    {
        ArrayList<Packet> Packets = new ArrayList<>();

        int InnerPacketSize;
        int CurrentPointer = 0;

        while (true)
        {
            try
            {
                if (Endianess.equals(Packet.Endian.Big))
                {
                    InnerPacketSize = (Buffer[CurrentPointer] << 24) + ((Buffer[CurrentPointer + 1] & 0XFF) << 16) + ((Buffer[CurrentPointer + 2] & 0xFF) << 8) + (Buffer[CurrentPointer + 3] & 0xFF);
                } else
                {
                    InnerPacketSize = (Buffer[CurrentPointer]) + ((Buffer[CurrentPointer + 1]) << 8) + ((Buffer[CurrentPointer + 2]) << 16) + (Buffer[CurrentPointer + 3] << 24);
                }

                if (InnerPacketSize == 0)
                {
                    break;
                }

                byte[] PacketData = Arrays.copyOfRange(Buffer, CurrentPointer, CurrentPointer + InnerPacketSize + 8);
                CurrentPointer += InnerPacketSize + 8;

                Packets.add(new Packet(PacketData));
            } catch (Exception e)
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
     * Creates a null packet with little endian and quantity of elements
     * specified.
     *
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
     *
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
     *
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
     *
     * @param b Specifies big or little endian.
     */
    public Packet(Packet.Endian e)
    {
        Data = new byte[2048];
        Endianess = e;

        addInteger(0);
        addInteger(0);
    }

    /**
     * Creates a null packet with quantity of elements and endian specified.
     *
     * @param Size Quantity of elements to create within packet.
     * @param b Specifies big or little endian.
     */
    public Packet(int Size, Packet.Endian e)
    {
        Data = new byte[Size];
        Endianess = e;

        addInteger(0);
        addInteger(0);
    }

    /**
     * Creates a packet from existing byte array while specifying endian.
     *
     * @param b Byte array to fill packet data with.
     * @param c Specifies big or little endian.
     */
    public Packet(byte[] b, Packet.Endian e)
    {
        Data = b;
        Endianess = e;

        readInteger();
        readInteger();
    }

    /**
     * Creates a packet from an existing packet.
     *
     * @param p Packet to fill new packet with.
     * @param b Specifies big or little endian.
     */
    public Packet(Packet p, Packet.Endian e)
    {
        Data = p.getData();
        Endianess = e;

        readInteger();
        readInteger();
    }

    /**
     * Returns data in packet as a byte array.
     *
     * @return All elements in packet as a byte array.
     */
    public byte[] getData()
    {
        return Arrays.copyOf(Data, getLength());
    }

    /**
     * Returns the length of the bytes used in the byte array.
     *
     * @return The length of Data[] as an integer.
     */
    public int getLength()
    {
        return Pointer + 8;
    }

    /**
     * Returns data in packet as a string.
     *
     * @return All elements in packet as a string in the format of
     * [Pointer:Byte]
     */
    @Override
    public String toString()
    {
        String tmp = "";

        for (int i = 0; i < getLength(); i++)
        {
            tmp += String.format("[%d:%s]", i, Byte.toString(Data[i]));
        }

        return tmp;
    }

    /**
     * Sets endian to big.
     */
    public void setBigEndian()
    {
        Endianess = Packet.Endian.Big;
    }

    /**
     * Sets endian to little.
     */
    public void setLittleEndian()
    {
        Endianess = Packet.Endian.Little;
    }

    /**
     * Adds byte to packet.
     *
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
     *
     * @param b Boolean to add to packet.
     */
    public void addBoolean(boolean b)
    {
        Data[Pointer] = (byte) (b ? 1 : 0);
        Pointer++;

        updateChecksum();
    }

    /**
     * Adds short to packet.
     *
     * @param s Short to add to packet.
     */
    public void addShort(short s)
    {
        if (Endianess == Packet.Endian.Big)
        {
            addByte((byte) (s >> 8));
            addByte((byte) (s));
        } else
        {
            addByte((byte) (s));
            addByte((byte) (s >> 8));
        }
    }

    /**
     * Adds character to packet.
     *
     * @param c Character to add to packet.
     */
    public void addCharacter(char c)
    {
        addByte((byte) c);
    }

    /**
     * Adds integer to packet.
     *
     * @param i Integer to add to packet.
     */
    public void addInteger(int i)
    {
        if (Endianess == Packet.Endian.Big)
        {
            addByte((byte) (i >> 24));
            addByte((byte) (i >> 16));
            addByte((byte) (i >> 8));
            addByte((byte) (i));
        } else
        {
            addByte((byte) (i));
            addByte((byte) (i >> 8));
            addByte((byte) (i >> 16));
            addByte((byte) (i >> 24));
        }
    }

    /**
     * Adds float to packet.
     *
     * @param f Float to add to packet.
     */
    public void addFloat(float f)
    {
        addInteger(Float.floatToRawIntBits(f));
    }

    /**
     * Adds long to packet.
     *
     * @param l Long to add to packet.
     */
    public void addLong(long l)
    {
        if (Endianess == Packet.Endian.Big)
        {
            addByte((byte) (l >> 56));
            addByte((byte) (l >> 48));
            addByte((byte) (l >> 40));
            addByte((byte) (l >> 32));
            addByte((byte) (l >> 24));
            addByte((byte) (l >> 16));
            addByte((byte) (l >> 8));
            addByte((byte) (l));
        } else
        {
            addByte((byte) (l));
            addByte((byte) (l >> 8));
            addByte((byte) (l >> 16));
            addByte((byte) (l >> 24));
            addByte((byte) (l >> 32));
            addByte((byte) (l >> 40));
            addByte((byte) (l >> 48));
            addByte((byte) (l >> 56));
        }
    }

    /**
     * Adds double to packet.
     *
     * @param d Double to add to packet.
     */
    public void addDouble(double d)
    {
        addLong(Double.doubleToRawLongBits(d));
    }

    /**
     * Adds string to packet.
     *
     * @param s String to add to packet.
     */
    public void addString(String s)
    {
        char[] charArray = s.toCharArray();
        addCharacters(charArray);
        /*int Length = charArray.length;
        
         addInteger(Length);
        
         for(char c : charArray) 
         {
         addCharacter(c);
         }*/
        /*char[] charArray = s.toCharArray();
        
         for(char c : charArray)
         addCharacter(c);
        
         addCharacter('\1');*/
    }

    /**
     * Adds serialized object to the packet. The data is formatted in
     * Blaze.Serialization 1.0.
     *
     * @param o Object to be serialized and added to the packet.
     */
    public void addObject(Object o)
    {
        Field[] fieldArray = o.getClass().getDeclaredFields();

        addString("java");
        addString(o.getClass().getName());
        addInteger(fieldArray.length);

        for (Field field : fieldArray)
        {
            addInteger(field.getModifiers());
            addString(field.getType().getSimpleName());
            addString(field.getName());

            if (field.getModifiers() != Field.PUBLIC)
            {
                field.setAccessible(true);
            }

            try
            {
                switch (field.getType().getSimpleName())
                {
                    case "byte":
                        addByte((byte) field.get(o));
                        break;
                    case "boolean":
                        addBoolean((boolean) field.get(o));
                        break;
                    case "short":
                        addShort((short) field.get(o));
                        break;
                    case "int":
                        addInteger((int) field.get(o));
                        break;
                    case "long":
                        addLong((long) field.get(o));
                        break;
                    case "float":
                        addFloat((float) field.get(o));
                        break;
                    case "double":
                        addDouble((double) field.get(o));
                        break;

                    //We have to check reference types for null.
                    case "String":
                        String string = (String) field.get(o);

                        if (string != null)
                        {
                            addString((String) field.get(o));
                        } else
                        {
                            addString("^NIL");
                        }
                        break;

                    /*case "Object":
                     Object object = field.get(o);
                        
                     if(object != null)
                     {
                     addBoolean(false);
                     addObject(object);
                     }
                     else
                     {
                     addBoolean(true);
                     }
                     break;*/


                    /*default:
                     Object objectChild = field.get(o);
                        
                     if(objectChild != null)
                     {
                     addBoolean(true);
                     addObject(objectChild);
                     }
                     else
                     {
                     addBoolean(false);
                     }
                     break;*/
                    //default: System.out.println(field.getType().getSimpleName()); break;    
                }
            } catch (IllegalArgumentException | IllegalAccessException e)
            {
                System.out.println(e);
            }
        }
    }

    /**
     *
     * @param ba
     */
    public void addBytes(byte[] ba)
    {
        addInteger(ba.length);

        for (byte b : ba)
        {
            addByte(b);
        }
    }

    /**
     *
     * @param ba
     */
    public void addBooleans(boolean[] ba)
    {
        addInteger(ba.length);

        for (boolean b : ba)
        {
            addBoolean(b);
        }
    }

    /**
     *
     * @param sa
     */
    public void addShorts(short[] sa)
    {
        addInteger(sa.length);

        for (short s : sa)
        {
            addShort(s);
        }
    }

    /**
     *
     * @param ca
     */
    public void addCharacters(char[] ca)
    {
        addInteger(ca.length);

        for (char c : ca)
        {
            addByte((byte) c);
        }
    }

    /**
     *
     * @param ia
     */
    public void addIntegers(int[] ia)
    {
        addInteger(ia.length);

        for (int i : ia)
        {
            addInteger(i);
        }
    }

    /**
     *
     * @param la
     */
    public void addLongs(long[] la)
    {
        addInteger(la.length);

        for (long l : la)
        {
            addLong(l);
        }
    }

    /**
     *
     * @param fa
     */
    public void addFloats(float[] fa)
    {
        addInteger(fa.length);

        for (float f : fa)
        {
            addFloat(f);
        }
    }

    /**
     *
     * @param da
     */
    public void addDoubles(double[] da)
    {
        addInteger(da.length);

        for (double d : da)
        {
            addDouble(d);
        }
    }

    /**
     *
     * @param sa
     */
    public void addStrings(String[] sa)
    {
        addInteger(sa.length);

        for (String s : sa)
        {
            addString(s);
        }
    }

    /**
     * Returns byte at current position.
     *
     * @return Byte to be read.
     */
    public byte readByte()
    {
        return Data[Pointer++];
    }

    /**
     * Returns boolean at current position.
     *
     * @return Boolean to be read.
     */
    public boolean readBoolean()
    {
        return (Data[Pointer++] == 1 ? true : false);
    }

    /**
     * Returns short at current position.
     *
     * @return Short to be read.
     */
    public short readShort()
    {
        if (Endianess == Packet.Endian.Big)
        {
            return (short) ((short) (readByte() << 8) + (readByte()));
        } else
        {
            return (short) ((short) (readByte()) + (readByte() << 8));
        }
    }

    /**
     * Returns character at current position.
     *
     * @return Character to be read.
     */
    public char readCharacter()
    {
        return (char) readByte();
    }

    /**
     * Returns integer at current position.
     *
     * @return Integer to be read.
     */
    public int readInteger()
    {
        if (Endianess == Packet.Endian.Big)
        {
            return (readByte() << 24) + ((readByte() & 0xFF) << 16) + ((readByte() & 0xFF) << 8) + ((readByte() & 0xFF));
        } else
        {
            return (readByte() & 0xFF) + ((readByte() & 0xFF) << 8) + ((readByte() & 0xFF) << 16) + (readByte() << 24);
        }
    }

    /**
     * Returns float at current position.
     *
     * @return Float to be read.
     */
    public float readFloat()
    {
        return Float.intBitsToFloat(readInteger());
    }

    /**
     * Returns long at current position.
     *
     * @return Long to be read.
     */
    public long readLong()
    {
        if (Endianess == Packet.Endian.Big)
        {
            return ((long) (readInteger()) << 32) + (readInteger() & 0xFFFFFFFFL);
        } else
        {
            return (readInteger() & 0xFFFFFFFFL) + ((long) (readInteger()) << 32);
        }
    }

    /**
     * Returns float at current position.
     *
     * @return Double to be read.
     */
    public double readDouble()
    {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Returns string at current position.
     *
     * @return String to be read.
     */
    public String readString()
    {
        String temp = "";
        int Length = readInteger();

        for (int i = 0; i < Length; i++)
        {
            temp += readCharacter();
        }

        return temp;

        /*String s = "";
        
         while(true)
         {
         char c = readCharacter();
            
         if(c != '\0')
         s += c;
         else
         break;
         }
        
         return s;*/
    }

    /**
     * Reads a serialized object from the packet. The data is formatted in
     * Blaze.Serialization 1.0.
     *
     * @return A de-serialized object.
     */
    public Object readObject()
    {
        String Language = readString();

        switch (Language)
        {
            case "java":
                String ClassName = readString();
                int FieldLength = readInteger();

                try
                {
                    Class newClass = Class.forName(ClassName);
                    Object object = newClass.newInstance();

                    for (int i = 0; i < FieldLength; i++)
                    {
                        int Modifier = readInteger();
                        String Type = readString();
                        String Name = readString();

                        Field field = newClass.getDeclaredField(Name);

                        if (Modifier != Field.PUBLIC)
                        {
                            field.setAccessible(true);
                        }

                        switch (Type)
                        {
                            case "byte":
                                field.set(object, readByte());
                                break;
                            case "boolean":
                                field.set(object, readBoolean());
                                break;
                            case "short":
                                field.set(object, readShort());
                                break;
                            case "int":
                                field.set(object, readInteger());
                                break;
                            case "long":
                                field.set(object, readLong());
                                break;
                            case "float":
                                field.set(object, readFloat());
                                break;
                            case "double":
                                field.set(object, readDouble());
                                break;

                            case "String":
                                String string = readString();

                                if (!string.equals("^NIL"))
                                {
                                    field.set(object, string);
                                }
                                break;

                            /*case "Object":
                             boolean isObjectNull = readBoolean();
                                
                             if(isObjectNull)
                             field.set(object, null);
                             else
                             field.set(object, readObject());
                             break;*/

                            /*default:
                             boolean isObjectChildNull = readBoolean();
                             System.out.println(isObjectChildNull);
                             if(isObjectChildNull)
                             field.set(object, null);
                             else
                             field.set(object, readObject());    
                             break;*/
                        }

                        if (Modifier != Field.PUBLIC)
                        {
                            field.setAccessible(false);
                        }
                    }

                    return object;
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException e)
                {
                    System.out.println(e);
                }
                break;

            case "csharp":
                //TODO: C#
                break;

            case "cplusplus":
                //TODO: C++  
                break;

            default:
                System.out.println("Unknown Language: " + Language);
                System.out.println("Note that if this is anything other than a language name, the byte order is most likely messed up.");
                break;
        }

        return null;
    }

    /**
     *
     * @return
     */
    public byte[] readBytes()
    {
        int Length = readInteger();
        byte[] byteArray = new byte[Length];

        for (int i = 0; i < Length; i++)
        {
            byteArray[i] = readByte();
        }

        return byteArray;
    }

    /**
     *
     * @return
     */
    public boolean[] readBooleans()
    {
        int Length = readInteger();
        boolean[] booleanArray = new boolean[Length];

        for (int i = 0; i < Length; i++)
        {
            booleanArray[i] = readBoolean();
        }

        return booleanArray;
    }

    /**
     *
     * @return
     */
    public short[] readShorts()
    {
        int Length = readInteger();
        short[] shortArray = new short[Length];

        for (int i = 0; i < Length; i++)
        {
            shortArray[i] = readShort();
        }

        return shortArray;
    }

    /**
     *
     * @return
     */
    public char[] readCharacters()
    {
        int Length = readInteger();
        char[] characterArray = new char[Length];

        for (int i = 0; i < Length; i++)
        {
            characterArray[i] = readCharacter();
        }

        return characterArray;
    }

    /**
     *
     * @return
     */
    public int[] readIntegers()
    {
        int Length = readInteger();
        int[] integerArray = new int[Length];

        for (int i = 0; i < Length; i++)
        {
            integerArray[i] = readInteger();
        }

        return integerArray;
    }

    /**
     *
     * @return
     */
    public long[] readLongs()
    {
        int Length = readInteger();
        long[] longArray = new long[Length];

        for (int i = 0; i < Length; i++)
        {
            longArray[i] = readLong();
        }

        return longArray;
    }

    /**
     *
     * @return
     */
    public float[] readFloats()
    {
        int Length = readInteger();
        float[] floatArray = new float[Length];

        for (int i = 0; i < Length; i++)
        {
            floatArray[i] = readFloat();
        }

        return floatArray;
    }

    /**
     *
     * @return
     */
    public double[] readDoubles()
    {
        int Length = readInteger();
        double[] doubleArray = new double[Length];

        for (int i = 0; i < Length; i++)
        {
            doubleArray[i] = readDouble();
        }

        return doubleArray;
    }

    /**
     *
     * @return
     */
    public String[] readStrings()
    {
        int Length = readInteger();
        String[] stringArray = new String[Length];

        for (int i = 0; i < Length; i++)
        {
            stringArray[i] = readString();
        }

        return stringArray;
    }

    /**
     * Checks if the packet is valid and non-modified.
     *
     * @return Returns true if the packet is valid and non-modified.
     */
    public boolean isValid()
    {
        if (Data.length > 8)
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
            } else if (getChecksum(InnerData) != Checksum)
            {
                return false;
            } else
            {
                return true;
            }
        } else
        {
            return true;
        }
    }

    /**
     *
     */
    private void updateChecksum()
    {
        if (Data.length > 8)
        {
            byte[] InnerData = Arrays.copyOfRange(Data, 8, getLength());
            //System.out.println("updateChecksum() Length: " + getLength());
            int Checksum = getChecksum(InnerData);
            //System.out.println("updateChecksum() Checksum: " + Checksum);

            if (Endianess == Packet.Endian.Big)
            {
                Data[0] = (byte) (InnerData.length >> 24);
                Data[1] = (byte) (InnerData.length >> 16);
                Data[2] = (byte) (InnerData.length >> 8);
                Data[3] = (byte) (InnerData.length);
                Data[4] = (byte) (Checksum >> 24);
                Data[5] = (byte) (Checksum >> 16);
                Data[6] = (byte) (Checksum >> 8);
                Data[7] = (byte) (Checksum);
            } else
            {
                Data[0] = (byte) (InnerData.length);
                Data[1] = (byte) (InnerData.length >> 8);
                Data[2] = (byte) (InnerData.length >> 16);
                Data[3] = (byte) (InnerData.length >> 24);

                Data[4] = (byte) (Checksum);
                Data[5] = (byte) (Checksum >> 8);
                Data[6] = (byte) (Checksum >> 16);
                Data[7] = (byte) (Checksum >> 24);
            }
        }
    }

    /**
     *
     * @param byteArray
     * @return
     */
    private int getChecksum(byte[] byteArray)
    {
        final CRC32 checksum = new CRC32();
        checksum.update(byteArray, 0, byteArray.length);
        return (int) checksum.getValue();
    }
}