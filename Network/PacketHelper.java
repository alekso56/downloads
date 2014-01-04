package de.take_weiland.CameraCraft.Common.Network;

import bq;
import ca;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import di;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import javax.imageio.ImageIO;
import up;
import ur;

public class PacketHelper
{
  private static HashMap partialFiles = new HashMap();
  private static HashMap numBytesReceived = new HashMap();
  private static HashMap callbacks = new HashMap();

  public static ByteArrayDataOutput buildPacket(NetAction action) {
    ByteArrayDataOutput output = ByteStreams.newDataOutput();
    output.writeByte(action.ordinal());
    return output;
  }

  public static di finishPacket(ByteArrayDataOutput data) {
    di packet = new di();
    packet.a = "CameraCraft";
    packet.c = data.toByteArray();
    packet.b = packet.c.length;
    return packet;
  }

  public static di buildAndFinishPacket(NetAction action) {
    return finishPacket(buildPacket(action));
  }

  public static NetAction readAction(ByteArrayDataInput data) {
    int offset = data.readByte();
    if (offset < NetAction.values().length) {
      return NetAction.values()[offset];
    }
    return NetAction.UNKNOWN;
  }

  public static ByteArrayDataInput openPacket(di packet)
  {
    return ByteStreams.newDataInput(packet.c);
  }

  public static void readFile(File file, byte[] bytes) throws Exception {
    FileOutputStream outputstream = null;
    Exception exception = null;
    try {
      outputstream = new FileOutputStream(file);
      for (int i = 0; i < bytes.length; i++)
        outputstream.write(bytes[i]);
    }
    catch (Exception e) {
      exception = e;
    } finally {
      if (outputstream != null)
        outputstream.close();
    }
  }

  public static BufferedImage readFileToImage(byte[] bytes)
  {
    try {
      return ImageIO.read(new ByteArrayInputStream(bytes)); } catch (Exception e) {
    }
    return null;
  }

  public static di[] writeFile(String fileID, File file) throws Exception
  {
    FileInputStream inputstream = null;
    Exception exception = null;
    di[] packets = null;
    try {
      int fileLength = (int)file.length();

      byte[] bytes = new byte[fileLength];
      inputstream = new FileInputStream(file);
      inputstream.read(bytes);

      packets = writeByteArray(fileID, bytes);
    } catch (Exception e) {
      exception = e;
    } finally {
      inputstream.close();
    }
    if (exception != null) {
      throw exception;
    }
    return packets;
  }

  public static di[] writeFileFromImage(String fileID, BufferedImage image) throws Exception {
    ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
    ImageIO.write(image, "PNG", outputstream);
    byte[] bytes = outputstream.toByteArray();
    return writeByteArray(fileID, bytes);
  }

  private static di[] writeByteArray(String fileID, byte[] array) {
    di[] packets = new di[(int)Math.ceil(array.length / 30000.0F)];
    int packetIndex = 0;
    int bytesWritten = 0;

    while (bytesWritten < array.length)
    {
      int numBytesToWriteNext;
      int numBytesToWriteNext;
      if (30000 > array.length - bytesWritten)
        numBytesToWriteNext = array.length - bytesWritten;
      else {
        numBytesToWriteNext = 30000;
      }

      ByteArrayDataOutput data = buildPacket(NetAction.FILEPART);
      data.writeUTF(fileID);
      data.writeInt(array.length);
      data.writeInt(numBytesToWriteNext);
      data.writeInt(bytesWritten);
      data.write(Arrays.copyOfRange(array, bytesWritten, numBytesToWriteNext + bytesWritten));
      packets[packetIndex] = finishPacket(data);
      packetIndex++;
      bytesWritten += numBytesToWriteNext;
    }
    return packets;
  }

  public static void partialFileReceived(String fileID, int totalSize, int partBegin, byte[] bytes) {
    if (!partialFiles.containsKey(fileID)) {
      partialFiles.put(fileID, new byte[totalSize]);
      numBytesReceived.put(fileID, Integer.valueOf(0));
    }
    byte[] previousReceived = (byte[])partialFiles.get(fileID);
    System.arraycopy(bytes, 0, previousReceived, partBegin, bytes.length);
    numBytesReceived.put(fileID, Integer.valueOf(((Integer)numBytesReceived.get(fileID)).intValue() + bytes.length));
    checkForFileComplete(fileID);
  }

  public static void registerFileReceiveCallback(String fileID, IFileReceiveCallback callback) {
    callbacks.put(fileID, callback);
    checkForFileComplete(fileID);
  }

  private static void checkForFileComplete(String fileID) {
    if (!partialFiles.containsKey(fileID)) {
      return;
    }
    byte[] bytes = (byte[])partialFiles.get(fileID);

    if ((bytes.length == ((Integer)numBytesReceived.get(fileID)).intValue()) && 
      (callbacks.containsKey(fileID))) {
      ((IFileReceiveCallback)callbacks.get(fileID)).fileReceived(fileID, bytes);
      callbacks.remove(fileID);
      partialFiles.remove(fileID);
      numBytesReceived.remove(fileID);
    }
  }

  public static void sendPacketsToServer(di[] packets)
  {
    for (di packet : packets)
      PacketDispatcher.sendPacketToServer(packet);
  }

  public static void sendPacketsToPlayer(di[] packets, Player player)
  {
    for (di packet : packets)
      PacketDispatcher.sendPacketToPlayer(packet, player);
  }

  public static void writeItemStack(ur stack, ByteArrayDataOutput data) throws IOException
  {
    if (stack == null) {
      data.writeShort(-1);
    } else {
      data.writeShort(stack.c);
      data.writeByte(stack.a);
      data.writeShort(stack.j());
      bq nbt = null;
      if ((stack.b().n()) || (stack.b().q())) {
        nbt = stack.d;
      }
      writeNBT(nbt, data);
    }
  }

  public static ur readItemStack(ByteArrayDataInput data) throws IOException {
    ur stack = null;
    short id = data.readShort();
    if (id > 0) {
      byte stackSize = data.readByte();
      short damage = data.readShort();
      stack = new ur(id, stackSize, damage);
      stack.d = readNBT(data);
    }
    return stack;
  }

  public static bq readNBT(ByteArrayDataInput data) throws IOException {
    short length = data.readShort();
    if (length < 0) {
      return null;
    }
    byte[] bytes = new byte[length];
    data.readFully(bytes);
    return ca.a(bytes);
  }

  public static void writeNBT(bq nbt, ByteArrayDataOutput data) throws IOException
  {
    if (nbt == null) {
      data.writeShort(-1);
    } else {
      byte[] bytes = ca.a(nbt);
      data.writeShort(bytes.length);
      data.write(bytes);
    }
  }
}