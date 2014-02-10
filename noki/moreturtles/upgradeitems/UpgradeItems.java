package noki.moreturtles.upgradeitems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import noki.moreturtles.MoreTurtlesConf;

public class UpgradeItems extends Item
{

  @SideOnly(Side.CLIENT)
  public static Icon[] icons;
  private static final String[] ICON = { "takeHoe", "silkTouchProgram", "fortuneProgram", "feedingBucket" };

  public UpgradeItems(int id)
  {
    super(id);

    if (MoreTurtlesConf.ccTab != null) {
      setCreativeTab(MoreTurtlesConf.ccTab);
    }
    else {
      setCreativeTab(CreativeTabs.tabMisc);
    }

    setHasSubtypes(true);
  }

  public String getUnlocalizedName(ItemStack itemstack)
  {
    return RegisterUpgradeItems.upgradeItems_unlocalizedName[itemstack.getItemDamage()];
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister icon)
  {
    icons = new Icon[ICON.length];

    icons[0] = icon.registerIcon("MoreTurtles".toLowerCase() + ":" + ICON[0]);
    icons[1] = icon.registerIcon("MoreTurtles".toLowerCase() + ":" + ICON[1]);
    icons[2] = icon.registerIcon("MoreTurtles".toLowerCase() + ":" + ICON[2]);
    icons[3] = icon.registerIcon("MoreTurtles".toLowerCase() + ":" + ICON[3]);
  }

  @SideOnly(Side.CLIENT)
  public Icon getIconFromDamage(int damage)
  {
    return icons[damage];
  }

  public void getSubItems(int id, CreativeTabs tab, List list)
  {
    for (int i = 0; i < icons.length; i++) {
      ItemStack itemstack = new ItemStack(id, 1, i);
      list.add(itemstack);
    }
  }
}