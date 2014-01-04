package de.take_weiland.CameraCraft.Common.Network;

public abstract interface IFileReceiveCallback
{
  public abstract void fileReceived(String paramString, byte[] paramArrayOfByte);
}