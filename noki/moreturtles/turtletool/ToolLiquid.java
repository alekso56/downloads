package noki.moreturtles.turtletool;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noki.moreturtles.MoreTurtlesConf;
import noki.moreturtles.turtlebase.TurtleBase;

public class ToolLiquid extends TurtleBase
  implements ITurtleUpgrade
{
  public ItemStack upgradeItem = new ItemStack(Item.bucketEmpty);

  public int getUpgradeID()
  {
    return MoreTurtlesConf.liquidTurtleTID;
  }

  public String getAdjective()
  {
    return "Liquid";
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

    if (currentItem == null) {
      return false;
    }

    if ((blockID == Block.waterStill.blockID) || (blockID == Block.waterMoving.blockID))
    {
      if (currentItem.itemID == Item.bucketEmpty.itemID)
      {
        world.setBlockToAir(newX, newY, newZ);

        consume(turtle, currentSlot);

        ItemStack bucketWater = new ItemStack(Item.bucketWater);
        store(turtle, dir, bucketWater);

        return true;
      }

      if (currentItem.itemID == Item.glassBottle.itemID)
      {
        consume(turtle, currentSlot);

        ItemStack bottleWater = new ItemStack(Item.potion, 1, 0);
        store(turtle, dir, bottleWater);
        return true;
      }

    }
    else if (blockID == Block.cauldron.blockID)
    {
      if ((currentItem.itemID == Item.bucketEmpty.itemID) && (metadata == 3))
      {
        world.setBlockMetadataWithNotify(newX, newY, newZ, 0, 3);

        consume(turtle, currentSlot);

        ItemStack bucketWater = new ItemStack(Item.bucketWater);
        store(turtle, dir, bucketWater);
        return true;
      }

      if ((currentItem.itemID == Item.glassBottle.itemID) && (metadata > 0))
      {
        world.setBlockMetadataWithNotify(newX, newY, newZ, metadata - 1, 3);

        consume(turtle, currentSlot);

        ItemStack bottleWater = new ItemStack(Item.potion, 1, 0);
        store(turtle, dir, bottleWater);
        return true;
      }

    }
    else if ((blockID == Block.lavaStill.blockID) || (blockID == Block.lavaMoving.blockID))
    {
      if (currentItem.itemID == Item.bucketEmpty.itemID)
      {
        world.setBlockToAir(newX, newY, newZ);

        consume(turtle, currentSlot);

        ItemStack bucketLava = new ItemStack(Item.bucketLava);
        store(turtle, dir, bucketLava);
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

    if (world.isAirBlock(newX, newY, newZ))
    {
      if (currentItem.itemID == Item.bucketWater.itemID)
      {
        consume(turtle, currentSlot);
        ItemStack bucket = new ItemStack(Item.bucketEmpty);
        store(turtle, dir, bucket);

        world.setBlock(newX, newY, newZ, Block.waterMoving.blockID);
        return true;
      }

      if (currentItem.itemID == Item.bucketLava.itemID)
      {
        consume(turtle, currentSlot);
        ItemStack bucket = new ItemStack(Item.bucketEmpty);
        store(turtle, dir, bucket);

        world.setBlock(newX, newY, newZ, Block.lavaMoving.blockID);
        return true;
      }

    }
    else if ((blockID == Block.waterStill.blockID) || (blockID == Block.waterMoving.blockID))
    {
      if (currentItem.itemID == Item.bucketWater.itemID)
      {
        consume(turtle, currentSlot);
        ItemStack bucket = new ItemStack(Item.bucketEmpty);
        store(turtle, dir, bucket);
        return true;
      }

      if ((currentItem.itemID == Item.potion.itemID) && (currentItem.getItemDamage() == 0))
      {
        consume(turtle, currentSlot);
        ItemStack bottle = new ItemStack(Item.glassBottle);
        store(turtle, dir, bottle);
        return true;
      }

    }
    else if (blockID == Block.cauldron.blockID)
    {
      if (currentItem.itemID == Item.bucketWater.itemID)
      {
        consume(turtle, currentSlot);
        ItemStack bucket = new ItemStack(Item.bucketEmpty);
        store(turtle, dir, bucket);

        world.setBlockMetadataWithNotify(newX, newY, newZ, 3, 3);
        return true;
      }

      if ((currentItem.itemID == Item.potion.itemID) && (currentItem.getItemDamage() == 0)) {
        if (metadata < 3)
        {
          consume(turtle, currentSlot);
          ItemStack bottle = new ItemStack(Item.glassBottle);
          store(turtle, dir, bottle);

          world.setBlockMetadataWithNotify(newX, newY, newZ, metadata + 1, 3);
          return true;
        }
        if (metadata >= 3)
        {
          consume(turtle, currentSlot);
          ItemStack bottle = new ItemStack(Item.glassBottle);
          store(turtle, dir, bottle);

          return true;
        }
      }

    }
    else if (((blockID == Block.lavaStill.blockID) || (blockID == Block.lavaMoving.blockID)) && 
      (currentItem.itemID == Item.bucketLava.itemID)) {
      consume(turtle, currentSlot);
      ItemStack bucket = new ItemStack(Item.bucketEmpty);
      store(turtle, dir, bucket);
      return true;
    }

    return false;
  }
}