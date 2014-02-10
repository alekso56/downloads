package noki.moreturtles.turtletool;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import noki.moreturtles.MoreTurtlesConf;
import noki.moreturtles.turtlebase.TurtleBase;

public class ToolShears extends TurtleBase
  implements ITurtleUpgrade
{
  public ItemStack upgradeItem = new ItemStack(Item.shears);

  public int getUpgradeID()
  {
    return MoreTurtlesConf.shearsTurtleTID;
  }

  public String getAdjective()
  {
    return "Shearing";
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
    setTurtleInfo(turtle);
    int newX = this.posX + net.minecraft.util.Facing.offsetsXForSide[dir];
    int newY = this.posY + net.minecraft.util.Facing.offsetsYForSide[dir];
    int newZ = this.posZ + net.minecraft.util.Facing.offsetsZForSide[dir];
    int blockID = this.world.getBlockId(newX, newY, newZ);
    int metadata = this.world.getBlockMetadata(newX, newY, newZ);
    Block block = Block.blocksList[blockID];
    ItemStack item = this.upgradeItem;

    if (this.world.isAirBlock(newX, newY, newZ)) {
      return false;
    }

    if (block.getBlockHardness(this.world, newX, newY, newZ) <= -1.0F) {
      return false;
    }

    if (!canTurtleHarvest(turtle, item, block, metadata)) {
      return false;
    }

    if (block.blockMaterial.isToolNotRequired()) {
      return false;
    }

    ArrayList items = block.getBlockDropped(this.world, newX, newY, newZ, metadata, 0);
    this.world.setBlockToAir(newX, newY, newZ);
    this.world.playAuxSFX(2001, newX, newY, newZ, blockID + metadata * 4096);
    for (ItemStack each : items) {
      store(turtle, dir, each);
    }

    return true;
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

    for (Iterator i$ = list.iterator(); i$.hasNext(); ) { Object each = i$.next();
      if ((each != null) && ((each instanceof IShearable))) {
        ItemStack shears = new ItemStack(Item.shears);
        Entity entity = (Entity)each;
        if (((IShearable)entity).isShearable(shears, entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ)) {
          ArrayList ret = ((IShearable)entity).onSheared(shears, entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ, 0);

          if (ret != null) {
            for (ItemStack item : ret) {
              store(turtle, dir, item);
            }
          }
          return true;
        }
      }
    }

    return false;
  }
}