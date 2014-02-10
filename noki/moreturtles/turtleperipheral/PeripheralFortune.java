package noki.moreturtles.turtleperipheral;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import noki.moreturtles.MoreTurtlesConf;

public class PeripheralFortune
  implements ITurtleUpgrade
{
  public ItemStack upgradeItem = new ItemStack(MoreTurtlesConf.upgradeItemsIID + 256, 1, 2);

  public int getUpgradeID()
  {
    return MoreTurtlesConf.fortuneTurtlePID;
  }

  public String getAdjective()
  {
    return "Fortune";
  }

  public TurtleUpgradeType getType()
  {
    return TurtleUpgradeType.Peripheral;
  }

  public ItemStack getCraftingItem()
  {
    return this.upgradeItem;
  }

  public boolean isSecret()
  {
    return false;
  }

  public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side)
  {
    return new PeripheralFortuneHosted(turtle, side);
  }

  public Icon getIcon(ITurtleAccess turtle, TurtleSide side)
  {
    return Block.blockRedstone.getBlockTextureFromSide(0);
  }

  public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction)
  {
    return false;
  }
}