package noki.moreturtles.turtletool;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noki.moreturtles.MoreTurtlesConf;
import noki.moreturtles.turtlebase.TurtleBase;

public class ToolTofu extends TurtleBase
  implements ITurtleUpgrade
{
  public ItemStack upgradeItem = new ItemStack(MoreTurtlesConf.tofuStickIID, 1, 0);

  public int getUpgradeID()
  {
    return MoreTurtlesConf.tofuTurtleTID;
  }

  public String getAdjective()
  {
    return "Tofu";
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
    World world = turtle.getWorld();
    Vec3 position = turtle.getPosition();

    if (position == null) {
      return false;
    }

    int newX = (int)position.xCoord + net.minecraft.util.Facing.offsetsXForSide[dir];
    int newY = (int)position.yCoord + net.minecraft.util.Facing.offsetsYForSide[dir];
    int newZ = (int)position.zCoord + net.minecraft.util.Facing.offsetsZForSide[dir];

    int blockID = world.getBlockId(newX, newY, newZ);
    int metadata = world.getBlockMetadata(newX, newY, newZ);
    int currentSlot = turtle.getSelectedSlot();
    ItemStack currentItem = turtle.getSlotContents(currentSlot);

    if (Arrays.asList(MoreTurtlesConf.tofuSet).contains(Integer.valueOf(blockID))) {
      int droppedID = Block.blocksList[blockID].idDropped(metadata, world.rand, 0);
      int quantity = Block.blocksList[blockID].quantityDropped(world.rand);
      ItemStack dropped = new ItemStack(droppedID, quantity, 0);

      world.playAuxSFX(2001, newX, newY, newZ, blockID + metadata * 4096);
      world.setBlockToAir(newX, newY, newZ);
      store(turtle, dir, dropped);

      return true;
    }

    if ((currentItem != null) && (currentItem.itemID == Item.bucketEmpty.itemID)) {
      if ((blockID == MoreTurtlesConf.soymilkStillBID) || (blockID == MoreTurtlesConf.soymilkMovingBID)) {
        consume(turtle, currentSlot);
        ItemStack bucketSoymilk = new ItemStack(MoreTurtlesConf.bucketSoymilkIID, 1, 0);
        store(turtle, dir, bucketSoymilk);
        world.setBlockToAir(newX, newY, newZ);
        return true;
      }
      if ((blockID == MoreTurtlesConf.soymilkHellStillBID) || (blockID == MoreTurtlesConf.soymilkHellMovingBID)) {
        consume(turtle, currentSlot);
        ItemStack bucketSoymilkHell = new ItemStack(MoreTurtlesConf.bucketSoymilkHellIID, 1, 0);
        store(turtle, dir, bucketSoymilkHell);
        world.setBlockToAir(newX, newY, newZ);
        return true;
      }
    }
    return false;
  }

  private boolean attack(ITurtleAccess turtle, int dir)
  {
    World world = turtle.getWorld();
    Vec3 position = turtle.getPosition();

    if (position == null) {
      return false;
    }

    int newX = (int)position.xCoord + net.minecraft.util.Facing.offsetsXForSide[dir];
    int newY = (int)position.yCoord + net.minecraft.util.Facing.offsetsYForSide[dir];
    int newZ = (int)position.zCoord + net.minecraft.util.Facing.offsetsZForSide[dir];

    int blockID = world.getBlockId(newX, newY, newZ);
    int metadata = world.getBlockMetadata(newX, newY, newZ);
    int currentSlot = turtle.getSelectedSlot();
    ItemStack currentItem = turtle.getSlotContents(currentSlot);

    if (currentItem == null) {
      return false;
    }

    if (world.isAirBlock(newX, newY, newZ)) {
      if (currentItem.itemID == MoreTurtlesConf.bucketSoymilkIID) {
        consume(turtle, currentSlot);
        ItemStack bucket = new ItemStack(Item.bucketEmpty);
        store(turtle, dir, bucket);
        world.setBlock(newX, newY, newZ, MoreTurtlesConf.soymilkMovingBID, 0, 3);
        return true;
      }
      if (currentItem.itemID == MoreTurtlesConf.bucketSoymilkHellIID) {
        consume(turtle, currentSlot);
        ItemStack bucket = new ItemStack(Item.bucketEmpty);
        store(turtle, dir, bucket);
        world.setBlock(newX, newY, newZ, MoreTurtlesConf.soymilkHellMovingBID, 0, 3);
        return true;
      }

    }
    else if (currentItem.itemID == MoreTurtlesConf.nigariIID) {
      if ((blockID == MoreTurtlesConf.soymilkStillBID) || (blockID == MoreTurtlesConf.soymilkMovingBID)) {
        if (turtle.consumeFuel(MoreTurtlesConf.consumedFuelLevelTofu)) {
          consume(turtle, currentSlot);
          ItemStack bottle = new ItemStack(Item.glassBottle);
          store(turtle, dir, bottle);
          world.setBlock(newX, newY, newZ, MoreTurtlesConf.tofuKinuBID, 0, 3);
          return true;
        }

        return false;
      }

      if ((blockID == MoreTurtlesConf.soymilkHellStillBID) || (blockID == MoreTurtlesConf.soymilkHellMovingBID)) {
        if (turtle.consumeFuel(MoreTurtlesConf.consumedFuelLevelTofu)) {
          consume(turtle, currentSlot);
          ItemStack bottle = new ItemStack(Item.glassBottle);
          store(turtle, dir, bottle);
          world.setBlock(newX, newY, newZ, MoreTurtlesConf.tofuHellBID, 0, 3);
          return true;
        }

        return false;
      }

    }

    return false;
  }
}