package noki.moreturtles.upgradeitems;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noki.moreturtles.MoreTurtlesConf;

public class RegisterUpgradeItems
{
  public static final String rootName = "More Turtles Upgrade Items";
  public static final String[] upgradeItems_unlocalizedName = { "takeHoe", "silkTouchProgram", "fortuneProgram", "geedingBucket" };

  public static final String[] upgradeItems_name = { "Take Hoe", "Silk Touch Program", "Fortune Program", "Feeding Bucket" };

  public static final String[] upgradeItems_jp_name = { "竹クワ", "シルクタッチプログラム", "幸運プログラム", "えさやりバケツ" };
  public static Item upgradeItems;

  public static void registerPre()
  {
    upgradeItems = new UpgradeItems(MoreTurtlesConf.upgradeItemsIID);
    GameRegistry.registerItem(upgradeItems, "More Turtles Upgrade Items");
  }

  public static void register()
  {
    for (int i = 0; i < upgradeItems_name.length; i++) {
      LanguageRegistry.addName(new ItemStack(upgradeItems, 1, i), upgradeItems_name[i]);
    }
    for (int i = 0; i < upgradeItems_jp_name.length; i++) {
      LanguageRegistry.instance().addNameForObject(new ItemStack(upgradeItems, 1, i), "ja_JP", upgradeItems_jp_name[i]);
    }

    ItemStack stick = new ItemStack(Item.stick);
    ItemStack diamond = new ItemStack(Item.diamond);
    int diskID = MoreTurtlesConf.discColoredIID;
    int itemID = MoreTurtlesConf.upgradeItemsIID + 256;

    if (MoreTurtlesConf.dependencyBamboo == true) {
      ItemStack bamboo = new ItemStack(MoreTurtlesConf.bambooIID, 1, 0);
      GameRegistry.addRecipe(new ItemStack(itemID, 1, 0), new Object[] { "xx ", " y ", " y ", Character.valueOf('x'), diamond, Character.valueOf('y'), bamboo });
    }

    ItemStack grass = new ItemStack(Block.grass);
    ItemStack ice = new ItemStack(Block.ice);
    ItemStack book = new ItemStack(Item.book);
    GameRegistry.addRecipe(new ItemStack(itemID, 1, 1), new Object[] { "xyx", "yzy", "xyx", Character.valueOf('x'), grass, Character.valueOf('y'), ice, Character.valueOf('z'), new ItemStack(diskID, 1, 0) });

    ItemStack emerald = new ItemStack(Item.emerald);
    GameRegistry.addRecipe(new ItemStack(itemID, 1, 2), new Object[] { "xyx", "yzy", "xyx", Character.valueOf('x'), emerald, Character.valueOf('y'), diamond, Character.valueOf('z'), new ItemStack(diskID, 1, 0) });

    ItemStack iron = new ItemStack(Item.ingotIron);
    ItemStack wheat = new ItemStack(Item.wheat);
    GameRegistry.addRecipe(new ItemStack(itemID, 1, 3), new Object[] { "xyx", "xyx", "xxx", Character.valueOf('x'), iron, Character.valueOf('y'), wheat });
  }
}