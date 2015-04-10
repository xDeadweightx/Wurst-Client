package tk.wurst_client.mods;

import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.RENDER,
description = "Shows projectiles' path of motion",
name = "Trajectories")
public class TrajectoriesMod extends Mod implements UpdateListener
{

}
