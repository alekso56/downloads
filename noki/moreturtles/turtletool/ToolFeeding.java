package noki.moreturtles.turtletool;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import noki.moreturtles.MoreTurtlesConf;
import noki.moreturtles.turtlebase.TurtleBase;

public class ToolFeeding extends TurtleBase
  implements ITurtleUpgrade
{
  public ItemStack upgradeItem = new ItemStack(MoreTurtlesConf.upgradeItemsIID + 256, 1, 3);

  public int getUpgradeID()
  {
    return MoreTurtlesConf.feedingTurtleTID;
  }

  public String getAdjective()
  {
    return "Feeding";
  }

  public TurtleUpgradeType getType()
  {
    return TurtleUpgradeType.Tool;
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
    return null;
  }

  public Icon getIcon(ITurtleAccess turtle, TurtleSide side)
  {
    return this.upgradeItem.getIconIndex();
  }

  public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction)
  {
    switch (1.$SwitchMap$dan200$turtle$api$TurtleVerb[verb.ordinal()]) {
    case 1:
      return dig(turtle, direction);
    case 2:
      return attack(turtle, direction);
    }
    return false;
  }

  private boolean dig(ITurtleAccess turtle, int dir)
  {
    return false;
  }

  private boolean attack(ITurtleAccess turtle, int dir)
  {
    setTurtleInfo(turtle);

    int newX = this.posX + net.minecraft.util.Facing.offsetsXForSide[dir];
    int newY = this.posY + net.minecraft.util.Facing.offsetsYForSide[dir];
    int newZ = this.posZ + net.minecraft.util.Facing.offsetsZForSide[dir];

    AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(newX - 0.5D, newY - 0.5D, newZ - 0.5D, newX + 0.5D, newY + 0.5D, newZ + 0.5D);

    List list = this.world.getEntitiesWithinAABBExcludingEntity(this.playerTurtle, aabb);
    if (list == null) {
      return false;
    }

    int currentSlot = turtle.getSelectedSlot();
    ItemStack currentItem = turtle.getSlotContents(currentSlot);
    if (currentItem == null) {
      return false;
    }

    for (Iterator i$ = list.iterator(); i$.hasNext(); ) { Object each = i$.next();
      if ((each != null) && ((each instanceof EntityAnimal))) {
        EntityAnimal target = (EntityAnimal)each;
        if ((target.isBreedingItem(currentItem)) && (target.getGrowingAge() == 0) && (target.inLove <= 0)) {
          consume(turtle, currentSlot);
          target.func_110196_bT();
          return true;
        }
      }
    }

    return false;
  }
}