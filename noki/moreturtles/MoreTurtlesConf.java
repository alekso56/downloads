package noki.moreturtles;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import java.io.File;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class MoreTurtlesConf
{
  public static Side side;
  public static int liquidTurtleTID;
  public static int shearsTurtleTID;
  public static int takeTurtleTID;
  public static int tofuTurtleTID;
  public static int silkTouchTurtlePID;
  public static int fortuneTurtlePID;
  public static int feedingTurtleTID;
  public static int upgradeItemsIID;
  public static int discColoredIID;
  public static CreativeTabs ccTab;
  public static String confNameCC;
  public static String confNameBamboo;
  public static String confNameTofu;
  public static boolean dependencyCC = false;
  public static boolean dependencyBamboo = false;
  public static boolean dependencyTofu = false;
  public static boolean consumeFuel;
  public static int consumedFuelLevel;
  public static boolean consumeFuelTofu;
  public static int consumedFuelLevelTofu;
  public static int shiftIndexBlockBamboo;
  public static int shiftIndexItemBamboo;
  public static int bambooBID;
  public static int bambooIID;
  public static int takenokoIID;
  public static int shiftIndexBlockTofu;
  public static int shiftIndexItemTofu;
  public static int saltFurnaceActiveBID;
  public static int saltFurnaceIdleBID;
  public static int tofuKinuBID;
  public static int tofuMomenBID;
  public static int tofuIshiBID;
  public static int tofuMetalBID;
  public static int tofuGrilledBID;
  public static int tofuDriedBID;
  public static int tofuFriedPouchBID;
  public static int tofuFriedBID;
  public static int tofuEggBID;
  public static int tofuAnninBID;
  public static int tofuSesameBID;
  public static int tofuZundaBID;
  public static int tofuStrawberryBID;
  public static int tofuHellBID;
  public static int tofuGlowBID;
  public static int tofuDiamondBID;
  public static Integer[] tofuSet;
  public static int soymilkStillBID;
  public static int soymilkMovingBID;
  public static int soymilkHellStillBID;
  public static int soymilkHellMovingBID;
  public static int bucketSoymilkIID;
  public static int bucketSoymilkHellIID;
  public static int nigariIID;
  public static int tofuStickIID;

  public static void setConf(FMLPreInitializationEvent event)
  {
    side = event.getSide();

    setMoreTurtlesConf(event);
    setCCConf(event);
    setTakeConf(event);
    setTofuConf(event);
  }

  private static void setMoreTurtlesConf(FMLPreInitializationEvent event)
  {
    Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
    cfg.load();

    Property prop = cfg.get("upgrade", "liquidTurtleTID", 1001);
    liquidTurtleTID = prop.getInt();
    prop = cfg.get("upgrade", "shearsTurtleTID", 1002);
    shearsTurtleTID = prop.getInt();
    prop = cfg.get("upgrade", "feedingTurtleTID", 1003);
    feedingTurtleTID = prop.getInt();

    prop = cfg.get("upgrade", "silkTouchTurtlePID", 1101);
    silkTouchTurtlePID = prop.getInt();
    prop = cfg.get("upgrade", "fortuneTurtlePID", 1102);
    fortuneTurtlePID = prop.getInt();

    prop = cfg.get("upgrade", "takeTurtleTID", 1201);
    takeTurtleTID = prop.getInt();
    prop = cfg.get("upgrade", "tofuTurtleTID", 1202);
    tofuTurtleTID = prop.getInt();

    prop = cfg.getItem("upgradeItemsIID", 10000);
    upgradeItemsIID = prop.getInt();

    prop = cfg.get("dependency", "confNameCC", "ComputerCraft");
    confNameCC = prop.getString();
    prop = cfg.get("dependency", "confNameBamboo", "mod_BambooIDConfig");
    confNameBamboo = prop.getString();
    prop = cfg.get("dependency", "confNameTofu", "TofuCraft");
    confNameTofu = prop.getString();

    prop = cfg.get("fuel", "consumeFuel", true);
    consumeFuel = prop.getBoolean(true);
    prop = cfg.get("fuel", "consumedFuelLevel", 1);
    consumedFuelLevel = prop.getInt();
    if (!consumeFuel) {
      consumedFuelLevel = 0;
    }
    prop = cfg.get("fuel", "consumeFuelTofu", true);
    consumeFuelTofu = prop.getBoolean(true);
    prop = cfg.get("fuel", "consumedFuelLevelTofu", 1);
    consumedFuelLevelTofu = prop.getInt();
    if (!consumeFuelTofu) {
      consumedFuelLevelTofu = 0;
    }

    cfg.save();
  }

  private static void setCCConf(FMLPreInitializationEvent event)
  {
    File configurationDir = event.getModConfigurationDirectory();
    File suggestedFile = new File(configurationDir, confNameCC + ".cfg");
    if ((suggestedFile.exists()) && (Loader.isModLoaded("ComputerCraft")))
    {
      dependencyCC = true;

      Configuration cfg = new Configuration(suggestedFile);
      cfg.load();

      Property prop = cfg.getItem("diskExpandedItemID", 4001);
      discColoredIID = prop.getInt() + 256;

      cfg.save();

      if (side == Side.CLIENT) {
        CreativeTabs[] tabs = CreativeTabs.creativeTabArray;
        for (CreativeTabs tab : tabs)
          if (tab.getTabLabel() == "ComputerCraft") {
            ccTab = tab;
            break;
          }
      }
    }
  }

  private static void setTakeConf(FMLPreInitializationEvent event)
  {
    File configurationDir = event.getModConfigurationDirectory();
    File suggestedFile = new File(configurationDir, confNameBamboo + ".cfg");
    if ((suggestedFile.exists()) && (Loader.isModLoaded("BambooMod")))
    {
      dependencyBamboo = true;

      Configuration cfg = new Configuration(suggestedFile);
      cfg.load();

      Property prop = cfg.get("A", "blockShiftIndex", 0, "BlockID bulk movement");
      shiftIndexBlockBamboo = prop.getInt();
      prop = cfg.get("A", "itemShiftIndex", 0, "ItemID bulk movement");
      shiftIndexItemBamboo = prop.getInt();

      prop = cfg.getBlock("bamboo", 3239);
      bambooBID = prop.getInt() + shiftIndexBlockBamboo;

      prop = cfg.getItem("bamboo", 23557);
      bambooIID = prop.getInt() + shiftIndexItemBamboo + 256;
      prop = cfg.getItem("takenoko", 23548);
      takenokoIID = prop.getInt() + shiftIndexItemBamboo + 256;

      cfg.save();
    }
  }

  private static void setTofuConf(FMLPreInitializationEvent event)
  {
    File configurationDir = event.getModConfigurationDirectory();
    File suggestedFile = new File(configurationDir, confNameTofu + ".cfg");
    if ((suggestedFile.exists()) && (Loader.isModLoaded("TofuCraft")))
    {
      dependencyTofu = true;

      Configuration cfg = new Configuration(suggestedFile);
      cfg.load();

      Property prop = cfg.get("general", "blockShiftIndex", 0);
      shiftIndexBlockTofu = prop.getInt();
      prop = cfg.get("general", "itemShiftIndex", 0);
      shiftIndexItemTofu = prop.getInt();

      prop = cfg.getBlock("saltFurnaceActive", 1292);
      saltFurnaceActiveBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("saltFurnaceIdle", 1291);
      saltFurnaceIdleBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuKinu", 1281);
      tofuKinuBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuMomen", 1282);
      tofuMomenBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuIshi", 1283);
      tofuIshiBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuMetal", 1284);
      tofuMetalBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuGrilled", 1285);
      tofuGrilledBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuDried", 1286);
      tofuDriedBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuFriedPouch", 1287);
      tofuFriedPouchBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuFried", 1288);
      tofuFriedBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuEgg", 1289);
      tofuEggBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuAnnin", 1304);
      tofuAnninBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuSesame", 1305);
      tofuSesameBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuZunda", 1306);
      tofuZundaBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuStrawberry", 1308);
      tofuStrawberryBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuHell", 1334);
      tofuHellBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuGlow", 1335);
      tofuGlowBID = prop.getInt() + shiftIndexBlockTofu;
      prop = cfg.getBlock("tofuDiamond", 1336);
      tofuDiamondBID = prop.getInt() + shiftIndexBlockTofu;
      Integer[] set = { Integer.valueOf(tofuKinuBID), Integer.valueOf(tofuMomenBID), Integer.valueOf(tofuIshiBID), Integer.valueOf(tofuMetalBID), Integer.valueOf(tofuGrilledBID), Integer.valueOf(tofuDriedBID), Integer.valueOf(tofuFriedPouchBID), Integer.valueOf(tofuFriedBID), Integer.valueOf(tofuEggBID), Integer.valueOf(tofuAnninBID), Integer.valueOf(tofuSesameBID), Integer.valueOf(tofuZundaBID), Integer.valueOf(tofuStrawberryBID), Integer.valueOf(tofuHellBID), Integer.valueOf(tofuGlowBID), Integer.valueOf(tofuDiamondBID) };

      tofuSet = set;

      prop = cfg.getBlock("soymilk", 1295);
      soymilkStillBID = prop.getInt() + shiftIndexBlockTofu;
      soymilkMovingBID = soymilkStillBID + 1;
      prop = cfg.getBlock("soymilkHell", 1337);
      soymilkHellStillBID = prop.getInt() + shiftIndexBlockTofu;
      soymilkHellMovingBID = soymilkHellStillBID + 1;

      prop = cfg.getItem("bucketSoymilk", 13686);
      bucketSoymilkIID = prop.getInt() + shiftIndexItemTofu + 256;
      prop = cfg.getItem("bucketSoymilkHell", 13759);
      bucketSoymilkHellIID = prop.getInt() + shiftIndexItemTofu + 256;
      prop = cfg.getItem("nigari", 13671);
      nigariIID = prop.getInt() + shiftIndexItemTofu + 256;
      prop = cfg.getItem("tofuStick", 13698);
      tofuStickIID = prop.getInt() + shiftIndexItemTofu + 256;

      cfg.save();
    }
  }
}