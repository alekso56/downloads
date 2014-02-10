package noki.moreturtles.turtlebase;

import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import net.minecraftforge.common.ForgeHooks;

public abstract class TurtleBase
{
  public World world;
  public Vec3 position;
  public int posX;
  public int posY;
  public int posZ;
  public EntityPlayer playerTurtle;
  public String playerTurtleName = "playerTurtle";

  public void setTurtleInfo(ITurtleAccess turtle)
  {
    this.world = turtle.getWorld();
    this.position = turtle.getPosition();
    this.posX = ((int)this.position.xCoord);
    this.posY = ((int)this.position.yCoord);
    this.posZ = ((int)this.position.zCoord);

    if ((this.playerTurtle == null) || (!(this.playerTurtle instanceof FakePlayer))) {
      this.playerTurtle = new FakePlayer(this.world, this.playerTurtleName);
    }
    this.playerTurtle.setPosition(this.posX, this.posY, this.posZ);
  }

  public ITurtleUpgrade getOtherUpgrade(ITurtleAccess turtle, TurtleSide side)
  {
    ITurtleUpgrade opposite = null;
    TurtleSide oppositeSide;
    TurtleSide oppositeSide;
    if (side == TurtleSide.Left) {
      oppositeSide = TurtleSide.Right;
    }
    else {
      oppositeSide = TurtleSide.Left;
    }
    opposite = turtle.getUpgrade(oppositeSide);

    return opposite;
  }

  public String getOtherUpgradeName(ITurtleAccess turtle, TurtleSide side)
  {
    String opposite = null;
    TurtleSide oppositeSide;
    TurtleSide oppositeSide;
    if (side == TurtleSide.Left) {
      oppositeSide = TurtleSide.Right;
    }
    else {
      oppositeSide = TurtleSide.Left;
    }
    ITurtleUpgrade oppositeUpgrade = turtle.getUpgrade(oppositeSide);
    if (oppositeUpgrade != null) {
      opposite = oppositeUpgrade.getAdjective();
    }

    return opposite;
  }

  public ItemStack getOtherUpgradeItem(ITurtleAccess turtle, TurtleSide side)
  {
    ItemStack oppositeItem = null;
    TurtleSide oppositeSide;
    TurtleSide oppositeSide;
    if (side == TurtleSide.Left) {
      oppositeSide = TurtleSide.Right;
    }
    else {
      oppositeSide = TurtleSide.Left;
    }
    ITurtleUpgrade oppositeUpgrade = turtle.getUpgrade(oppositeSide);
    if (oppositeUpgrade != null) {
      oppositeItem = oppositeUpgrade.getCraftingItem();
    }

    return oppositeItem;
  }

  public boolean store(ITurtleAccess turtle, int dir, ItemStack item)
  {
    if (!turtle.storeItemStack(item)) {
      int[] oppositeSide = { 1, 0, 3, 2, 5, 4 };
      if (!turtle.dropItemStack(item, oppositeSide[turtle.getFacingDir()])) {
        turtle.dropItemStack(item, turtle.getFacingDir());
      }
    }
    return true;
  }

  public boolean consume(ITurtleAccess turtle, int selectedSlot)
  {
    ItemStack currentItem = turtle.getSlotContents(selectedSlot);
    int currentSize = currentItem.stackSize;

    if (currentSize == 1) {
      turtle.setSlotContents(selectedSlot, null);
    }
    else {
      ItemStack consumedItem = new ItemStack(currentItem.itemID, currentSize - 1, currentItem.getItemDamage());
      turtle.setSlotContents(selectedSlot, consumedItem);
    }

    return true;
  }

  public boolean canTurtleHarvest(ITurtleAccess turtle, ItemStack item, Block block, int metadata)
  {
    setTurtleInfo(turtle);
    this.playerTurtle.inventory.clearInventory(-1, -1);
    this.playerTurtle.inventory.setInventorySlotContents(0, item);
    this.playerTurtle.inventory.currentItem = 0;

    return ForgeHooks.canHarvestBlock(block, this.playerTurtle, metadata);
  }
}