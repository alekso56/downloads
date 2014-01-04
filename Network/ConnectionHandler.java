package de.take_weiland.CameraCraft.Common.Network;

import ce;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import de.take_weiland.CameraCraft.Common.ConfigurationManager;
import de.take_weiland.CameraCraft.Common.Recipes.CameraCraftRecipes;
import dw;
import eg;
import it;
import net.minecraft.server.MinecraftServer;

public class ConnectionHandler
  implements IConnectionHandler
{
  public void playerLoggedIn(Player player, eg netHandler, ce manager)
  {
    ByteArrayDataOutput data = PacketHelper.buildPacket(NetAction.CONFIRM_CAMERACRAFT);
    data.writeBoolean(ConfigurationManager.allowCraftTeleporter);
    PacketDispatcher.sendPacketToPlayer(PacketHelper.finishPacket(data), player);
  }

  public String connectionReceived(it netHandler, ce manager)
  {
    return null;
  }

  public void connectionOpened(eg netClientHandler, String server, int port, ce manager)
  {
    CameraCraftRecipes.disableRecipesAndItems();
  }

  public void connectionOpened(eg netClientHandler, MinecraftServer server, ce manager)
  {
    CameraCraftRecipes.disableRecipesAndItems();
  }

  public void connectionClosed(ce manager)
  {
  }

  public void clientLoggedIn(eg clientHandler, ce manager, dw login)
  {
  }
}