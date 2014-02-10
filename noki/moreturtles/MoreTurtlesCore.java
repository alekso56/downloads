package noki.moreturtles;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import noki.moreturtles.proxy.CommonProxy;
import noki.moreturtles.turtleperipheral.RegisterPeripheralTurtle;
import noki.moreturtles.turtletool.RegisterToolTurtle;
import noki.moreturtles.upgradeitems.RegisterUpgradeItems;

@Mod(modid="MoreTurtles", name="More Turtles", version="1.1.2", dependencies="after:CCTurtle,BambooMod,TofuCraft")
@NetworkMod(channels={"MoreTurtles"}, clientSideRequired=true, serverSideRequired=true)
public class MoreTurtlesCore
{

  @Mod.Instance("MoreTurtles")
  public static MoreTurtlesCore instance;

  @SidedProxy(clientSide="noki.moreturtles.proxy.ClientProxy", serverSide="noki.moreturtles.proxy.CommonProxy")
  public static CommonProxy proxy;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    MoreTurtlesConf.setConf(event);

    RegisterUpgradeItems.registerPre();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    proxy.registerRenderers();

    RegisterUpgradeItems.register();
    RegisterToolTurtle.register();
    RegisterPeripheralTurtle.register();
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
  }
}