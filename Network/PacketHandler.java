package de.take_weiland.CameraCraft.Common.Network;

import amq;
import bn;
import ce;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.take_weiland.CameraCraft.Common.Blocks.CameraCraftBlock;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.CameraCraftCommonProxy;
import de.take_weiland.CameraCraft.Common.Gui.ContainerViewPhotos;
import de.take_weiland.CameraCraft.Common.Gui.GenericContainer;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.Inventory.InventoryCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemCamera;
import de.take_weiland.CameraCraft.Common.Items.ItemPhoto;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import de.take_weiland.CameraCraft.Common.Recipes.CameraCraftRecipes;
import de.take_weiland.CameraCraft.Common.TileEntities.TileEntityCamera;
import di;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;
import qx;
import rq;
import ur;
import yc;

public class PacketHandler
  implements IPacketHandler
{
  private static HashMap partialImages = new HashMap();

  public void onPacketData(ce manager, di packet, Player player)
  {
    ByteArrayDataInput data = PacketHelper.openPacket(packet);
    NetAction action = PacketHelper.readAction(data);

    final qx playerEntity = (qx)player;
    final ur currentItem;
    int x;
    int y;
    int z;
    String photoID;
    int windowId;
    int photoIndex;
    switch (4.$SwitchMap$de$take_weiland$CameraCraft$Common$Network$NetAction[action.ordinal()])
    {
    case 1:
      currentItem = playerEntity.bS();
      ItemCamera.tryTakePhoto(currentItem, playerEntity);
      break;
    case 2:
      CameraCraft.proxy.executePhotoPlayer(data.readUTF());
      break;
    case 3:
      x = data.readInt();
      y = data.readInt();
      z = data.readInt();
      String photoId = data.readUTF();
      CameraCraft.proxy.executePhotoCameraEntity(x, y, z, photoId);
      break;
    case 4:
      currentItem = playerEntity.bS();

      if (ItemCamera.canTakePhoto(currentItem)) {
        String photoID = data.readUTF();
        final String photoName = data.readUTF();
        PacketHelper.registerFileReceiveCallback(photoID, new IFileReceiveCallback()
        {
          public void fileReceived(String fileID, byte[] bytes) {
            try {
              PacketHelper.readFile(ItemPhoto.getPhotoSaveFile(fileID), bytes);
              ItemCamera.photoTakenWithCamera(currentItem, playerEntity, fileID, photoName);
            } catch (Exception e) {
              playerEntity.a(bn.a().b("cameracraft.imageprocess.fail"));
              e.printStackTrace();
            }
          }
        });
      }
      else {
        playerEntity.a(bn.a().b("cameracraft.takephoto.fail"));
      }

      break;
    case 5:
      x = data.readInt();
      y = data.readInt();
      z = data.readInt();
      final String photoId1 = data.readUTF();
      if (playerEntity.p.a(x, y, z) == CameraCraftBlock.cameraPlaced.cm) {
        final TileEntityCamera tileEntity = (TileEntityCamera)playerEntity.p.q(x, y, z);
        if (ItemCamera.canTakePhoto(tileEntity.getCameraInventory().getCameraStack()))
          PacketHelper.registerFileReceiveCallback(photoId1, new IFileReceiveCallback()
          {
            public void fileReceived(String fileID, byte[] bytes)
            {
              try {
                PacketHelper.readFile(ItemPhoto.getPhotoSaveFile(fileID), bytes);
                ItemCamera.photoTakenWithCamera(tileEntity, photoId1);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          });
      }
      break;
    case 6:
      photoID = data.readUTF();

      if (!ItemPhoto.doesPhotoExist(photoID)) {
        ((qx)player).a(bn.a().b("cameracraft.photomiss"));
      } else {
        ByteArrayDataOutput output = PacketHelper.buildPacket(NetAction.IMAGEDATA);
        try {
          PacketHelper.sendPacketsToPlayer(PacketHelper.writeFile(photoID, ItemPhoto.getPhotoSaveFile(photoID)), player);

          output.writeUTF(photoID);
          PacketDispatcher.sendPacketToPlayer(PacketHelper.finishPacket(output), player);
        } catch (Exception e) {
          playerEntity.a(bn.a().b("cameracraft.photomiss"));
        }

      }

      break;
    case 7:
      photoID = data.readUTF();
      PacketHelper.registerFileReceiveCallback(photoID, new IFileReceiveCallback()
      {
        public void fileReceived(String fileID, byte[] bytes) {
          CameraCraft.proxy.imageDataRecieved(fileID, PacketHelper.readFileToImage(bytes));
        }
      });
      break;
    case 8:
      windowId = data.readInt();
      if ((playerEntity.bL.d == windowId) && ((playerEntity.bL instanceof GenericContainer))) {
        GenericContainer container = (GenericContainer)playerEntity.bL;
        if ((container.getUpperInventory() instanceof IPhotoSource)) {
          IPhotoSource source = (IPhotoSource)container.getUpperInventory();
          if (source.canViewPhotos(playerEntity))
            playerEntity.openGui(CameraCraft.instance, source.getScreenTypeViewPhotos().toGuiId(), playerEntity.p, source.getX(), source.getY(), source.getZ());
        }
      }
      break;
    case 9:
      windowId = data.readInt();
      photoIndex = data.readByte();
      String newName = data.readUTF();
      if ((playerEntity.bL.d == windowId) && ((playerEntity.bL instanceof ContainerViewPhotos))) {
        ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.bL;
        container.getSource().nameChanged(photoIndex, newName);
      }break;
    case 10:
      windowId = data.readInt();
      int numPhotos = data.readByte();
      PhotoSizeAmountInfo[] amountInfo = new PhotoSizeAmountInfo[numPhotos];
      for (int i = 0; i < numPhotos; i++) {
        amountInfo[i] = PhotoSizeAmountInfo.createFrom(data);
      }

      if (((playerEntity.bL instanceof ContainerViewPhotos)) && (playerEntity.bL.d == windowId)) {
        ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.bL;
        if (container.getSource().canPrint()) {
          container.getSource().addToPrintQueue(Arrays.asList(amountInfo));
          playerEntity.i();
        }
      }
      break;
    case 11:
      windowId = data.readInt();
      photoIndex = data.readByte();
      if ((playerEntity.bL.d == windowId) && ((playerEntity.bL instanceof ContainerViewPhotos))) {
        ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.bL;
        PhotoInformation info = container.getSource().getPhotoInformation(photoIndex);
        if (info != null) {
          info.teleport(playerEntity);
          playerEntity.i();
        }
      }
      break;
    case 12:
      String fileID = data.readUTF();
      int totalLength = data.readInt();
      int lengthThisPacket = data.readInt();
      int startThisPacket = data.readInt();
      byte[] bytes = new byte[lengthThisPacket];
      data.readFully(bytes);
      PacketHelper.partialFileReceived(fileID, totalLength, startThisPacket, bytes);
      break;
    case 13:
      windowId = data.readInt();
      photoIndex = data.readByte();
      if (((playerEntity.bL instanceof ContainerViewPhotos)) && (playerEntity.bL.d == windowId)) {
        ContainerViewPhotos container = (ContainerViewPhotos)playerEntity.bL;
        if (container.getSource().canDelete()) {
          container.getSource().deletePhoto(photoIndex);
          if (container.getSource().numPhotos() == 0)
            playerEntity.i();
        }
      }
      break;
    case 14:
      CameraCraftRecipes.enableRecipesAndItems();
      boolean enableEnabler = data.readBoolean();
      CameraCraftRecipes.teleportationEnabler.forceDisable = (!enableEnabler);
      break;
    case 15:
      FMLCommonHandler.instance().getFMLLogger().warning("[CameraCraft] Received unknown NetAction with channel CameraCraft!");
    }
  }
}