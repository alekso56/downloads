package noki.moreturtles.turtletool;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noki.moreturtles.MoreTurtlesConf;
import noki.moreturtles.turtlebase.TurtleBase;

public class ToolTake extends TurtleBase
  implements ITurtleUpgrade
{
  public ItemStack upgradeItem = new ItemStack(MoreTurtlesConf.upgradeItemsIID + 256, 1, 0);

  public int getUpgradeID()
  {
    return MoreTurtlesConf.takeTurtleTID;
  }

  public String getAdjective()
  {
    return "Take";
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
    int bambooBID = MoreTurtlesConf.bambooBID;
    int bambooIID = MoreTurtlesConf.bambooIID;
    int takenokoIID = MoreTurtlesConf.takenokoIID;

    if ((blockID == bambooBID) && (metadata == 15)) {
      ItemStack takenoko = new ItemStack(takenokoIID, 1, 0);
      store(turtle, dir, takenoko);

      world.playAuxSFX(2001, newX, newY, newZ, bambooBID + metadata * 4096);
      world.setBlockToAir(newX, newY, newZ);
      return true;
    }

    if (world.isAirBlock(newX, newY, newZ)) {
      int belowBlockID = world.getBlockId(newX, newY - 1, newZ);
      if ((belowBlockID == 2) || (belowBlockID == 3)) {
        Block block = Block.tilledField;
        world.playSoundEffect(newX + 0.5F, newY - 1.0F + 0.5F, newZ + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

        world.setBlock(newX, newY - 1, newZ, 60, 0, 3);
        return true;
      }

    }
    else if ((blockID == bambooBID) && (metadata != 15)) {
      ItemStack bamboo = new ItemStack(bambooIID, 1, 0);
      store(turtle, dir, bamboo);

      world.playAuxSFX(2001, newX, newY, newZ, bambooBID + metadata * 4096);
      world.setBlockToAir(newX, newY, newZ);
      return true;
    }

    return false;
  }

  private boolean attack(ITurtleAccess turtle, int dir) {
    World world = turtle.getWorld();
    Vec3 position = turtle.getPosition();

    if (position == null) {
      return false;
    }

    int newX = (int)position.xCoord + net.minecraft.util.Facing.offsetsXForSide[dir];
    int newY = (int)position.yCoord + net.minecraft.util.Facing.offsetsYForSide[dir];
    int newZ = (int)position.zCoord + net.minecraft.util.Facing.offsetsZForSide[dir];

    int blockID = world.getBlockId(newX, newY, newZ);
    int belowBlockID = world.getBlockId(newX, newY - 1, newZ);
    int bambooBID = MoreTurtlesConf.bambooBID;
    int takenokoIID = MoreTurtlesConf.takenokoIID;

    if (blockID == bambooBID) {
      int metadata = world.getBlockMetadata(newX, newY, newZ);

      if ((metadata == 15) && (world.getBlockLightValue(newX, newY, newZ) > 7)) {
        boolean boneFlag = checkBonemeal(turtle);
        if (boneFlag == true) {
          world.setBlock(newX, newY, newZ, bambooBID, 0, 3);
          world.playAuxSFX(2005, newX, newY, newZ, 0);
          return true;
        }

        return false;
      }

      int i = 1;
      int newMetadata = 1;
      boolean airFlag = false;
      boolean childFlag = false;
      while (i <= 10) {
        if (i == 10) {
          childFlag = true;
          break;
        }
        int aboveBlockID = world.getBlockId(newX, newY + i, newZ);
        if (aboveBlockID == bambooBID) {
          newMetadata++;
          i++;
        } else {
          if ((aboveBlockID != 0) && (aboveBlockID != 18)) break;
          airFlag = true;
          break;
        }

      }

      if (airFlag == true) {
        boolean boneFlag = checkBonemeal(turtle);
        if (boneFlag == true) {
          tryRaisingBamboo(newX, newY, newZ, i, newMetadata, world);
          return true;
        }

        return false;
      }

      if (childFlag == true) {
        boolean boneFlag = checkBonemeal(turtle);
        if (boneFlag == true) {
          tryChildSpawn(newX, newY, newZ, world);
          return true;
        }

        return false;
      }

    }
    else if ((blockID == 0) && ((belowBlockID == 2) || (belowBlockID == 3))) {
      int currentSlot = turtle.getSelectedSlot();
      ItemStack currentItem = turtle.getSlotContents(currentSlot);
      if (currentItem == null) {
        return false;
      }
      if (currentItem.itemID == takenokoIID) {
        consume(turtle, currentSlot);
        world.setBlock(newX, newY, newZ, bambooBID, 15, 3);
        return true;
      }
    }

    return false;
  }

  private boolean tryRaisingBamboo(int newX, int newY, int newZ, int height, int newMetadata, World world)
  {
    world.setBlock(newX, newY + height, newZ, MoreTurtlesConf.bambooBID, newMetadata, 3);
    world.playAuxSFX(2005, newX, newY, newZ, 0);

    return true;
  }

  private boolean tryChildSpawn(int newX, int newY, int newZ, World world)
  {
    for (int i = 1; i <= 3; i++) {
      for (int j = 1; j <= 3; j++) {
        int id = world.getBlockId(newX - 2 + j, newY, newZ - 2 + i);
        if (id == 0) {
          Random random = new Random();
          int belowID = world.getBlockId(newX - 2 + j, newY - 1, newZ - 2 + i);
          if ((belowID == 60) && 
            (random.nextInt(2) == 0)) {
            world.setBlock(newX - 2 + j, newY, newZ - 2 + i, MoreTurtlesConf.bambooBID, 15, 3);
            world.setBlock(newX - 2 + j, newY - 1, newZ - 2 + i, 3, 0, 3);
          }

          if ((belowID == 2) || (belowID == 3)) {
            if (random.nextInt(world.isRaining() ? 5 : 10) == 0) {
              world.setBlock(newX - 2 + j, newY, newZ - 2 + i, MoreTurtlesConf.bambooBID, 15, 3);
            }
          }
        }
      }
    }
    world.playAuxSFX(2005, newX, newY, newZ, 0);
    return true;
  }

  private boolean checkBonemeal(ITurtleAccess turtle)
  {
    int currentSlot = turtle.getSelectedSlot();
    ItemStack currentItem = turtle.getSlotContents(currentSlot);
    if (currentItem == null) {
      return false;
    }
    if ((currentItem.itemID == 351) && (currentItem.getItemDamage() == 15)) {
      consume(turtle, currentSlot);
      return true;
    }
    return false;
  }

  public Icon getIcon(ITurtleAccess turtle, TurtleSide side)
  {
    return this.upgradeItem.getIconIndex();
  }
}