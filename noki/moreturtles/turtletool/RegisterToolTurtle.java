package noki.moreturtles.turtletool;

import dan200.turtle.api.TurtleAPI;
import noki.moreturtles.MoreTurtlesConf;

public class RegisterToolTurtle
{
  public static void register()
  {
    if (MoreTurtlesConf.dependencyBamboo == true) {
      TurtleAPI.registerUpgrade(new ToolTake());
    }
    if (MoreTurtlesConf.dependencyTofu == true) {
      TurtleAPI.registerUpgrade(new ToolTofu());
    }

    TurtleAPI.registerUpgrade(new ToolLiquid());
    TurtleAPI.registerUpgrade(new ToolShears());
    TurtleAPI.registerUpgrade(new ToolFeeding());
  }
}