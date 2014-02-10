package noki.moreturtles.turtleperipheral;

import dan200.turtle.api.TurtleAPI;

public class RegisterPeripheralTurtle
{
  public static void register()
  {
    TurtleAPI.registerUpgrade(new PeripheralSilkTouch());
    TurtleAPI.registerUpgrade(new PeripheralFortune());
  }
}