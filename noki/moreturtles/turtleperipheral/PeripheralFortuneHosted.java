package noki.moreturtles.turtleperipheral;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.ILuaContext;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noki.moreturtles.MoreTurtlesConf;
import noki.moreturtles.turtlebase.TurtleBase;

public class PeripheralFortuneHosted extends TurtleBase
  implements IHostedPeripheral
{
  private ITurtleAccess turtleHosting;
  private TurtleSide sideHosting;
  private IComputerAccess computerHosting;

  PeripheralFortuneHosted(ITurtleAccess turtle, TurtleSide side)
  {
    this.turtleHosting = turtle;
    this.sideHosting = side;
  }

  public String getType()
  {
    return "Fortune";
  }

  public String[] getMethodNames()
  {
    return new String[] { "digFortune", "digFortuneUp", "digFortuneDown" };
  }

  public boolean canAttachToSide(int side)
  {
    return true;
  }

  public void attach(IComputerAccess computer)
  {
    this.computerHosting = computer;
  }

  public void detach(IComputerAccess computer)
  {
  }

  public void update()
  {
  }

  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
  }

  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
  }

  public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
    throws Exception
  {
    int dir;
    switch (method) {
    case 0:
      dir = this.turtleHosting.getFacingDir();
      break;
    case 1:
      dir = 1;
      break;
    case 2:
      dir = 0;
      break;
    default:
      dir = this.turtleHosting.getFacingDir();
    }

    boolean flag = true;
    String string = null;

    if (arguments.length > 0) {
      flag = false;
      throw new Exception("This method accepts no arguments.");
    }

    setTurtleInfo(this.turtleHosting);
    if (!checkTool(this.turtleHosting)) {
      flag = false;
      string = "This turtle couldn't dig.";

      return new Object[] { Boolean.valueOf(flag), string };
    }

    int newX = this.posX + net.minecraft.util.Facing.offsetsXForSide[dir];
    int newY = this.posY + net.minecraft.util.Facing.offsetsYForSide[dir];
    int newZ = this.posZ + net.minecraft.util.Facing.offsetsZForSide[dir];
    int blockID = this.world.getBlockId(newX, newY, newZ);
    int metadata = this.world.getBlockMetadata(newX, newY, newZ);
    Block block = Block.blocksList[blockID];
    ItemStack item = getOtherUpgradeItem(this.turtleHosting, this.sideHosting);

    if (this.world.isAirBlock(newX, newY, newZ)) {
      flag = false;
      string = "nothing to dig.";

      return new Object[] { Boolean.valueOf(flag), string };
    }

    if (block.getBlockHardness(this.world, newX, newY, newZ) <= -1.0F) {
      flag = false;
      string = "can't dig this block.";

      return new Object[] { Boolean.valueOf(flag), string };
    }

    if (!this.turtleHosting.consumeFuel(MoreTurtlesConf.consumedFuelLevel)) {
      flag = false;
      string = "not enough fuel level.";

      return new Object[] { Boolean.valueOf(flag), string };
    }

    if (canTurtleHarvest(this.turtleHosting, item, block, metadata)) {
      ArrayList items = block.getBlockDropped(this.world, newX, newY, newZ, metadata, 3);
      this.world.setBlockToAir(newX, newY, newZ);
      this.world.playAuxSFX(2001, newX, newY, newZ, blockID + metadata * 4096);
      for (ItemStack each : items) {
        store(this.turtleHosting, dir, each);
      }

      return new Object[] { Boolean.valueOf(flag) };
    }

    this.world.setBlockToAir(newX, newY, newZ);
    this.world.playAuxSFX(2001, newX, newY, newZ, blockID + metadata * 4096);

    return new Object[] { Boolean.valueOf(flag) };
  }

  public boolean checkTool(ITurtleAccess turtle)
  {
    String[] allowed = { "Digging", "Mining", "Felling", "Farming" };
    String currentUpgrade = getOtherUpgradeName(this.turtleHosting, this.sideHosting);

    return Arrays.asList(allowed).contains(currentUpgrade);
  }
}