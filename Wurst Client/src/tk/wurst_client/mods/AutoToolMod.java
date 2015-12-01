/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.BLOCKS,
	description = "Automatically uses the best tool in your hotbar to\n"
		+ "mine blocks. Tip: This works with Nuker.",
	name = "AutoTool")
public class AutoToolMod extends Mod implements LeftClickListener,
	UpdateListener
{
	private boolean isActive = false;
	private int oldSlot;
	
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.add(LeftClickListener.class, this);
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed
			&& isActive)
		{
			isActive = false;
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
		}else if(isActive
			&& Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null
			&& Minecraft.getMinecraft().theWorld
				.getBlockState(
					Minecraft.getMinecraft().objectMouseOver.getBlockPos())
				.getBlock().getMaterial() != Material.air)
			setSlot(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(LeftClickListener.class, this);
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
		isActive = false;
		Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
	}
	
	@Override
	public void onLeftClick()
	{
		if(Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null)
			return;
		if(Minecraft.getMinecraft().theWorld
			.getBlockState(
				Minecraft.getMinecraft().objectMouseOver.getBlockPos())
			.getBlock().getMaterial() != Material.air)
		{
			isActive = true;
			oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
			setSlot(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
		}
	}
	
	public static void setSlot(BlockPos blockPos)
	{
		float bestSpeed = .1F;
                int bestSlot = 0;
                Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
                InventoryPlayer iPlayer = mc.thePlayer.inventory;
                if(Block.getIdFromBlock(block) == 7)return;
 
                for(ItemStack item:iPlayer.mainInventory){
                        if(item != null && item.getStrVsBlock(block) > bestSpeed){
                                bestSpeed = item.getStrVsBlock(block);
                                bestSlot = iPlayer.getInventorySlotContainItem(item.getItem());
                        }
                }
                if(bestSlot > 9){
                        try{
                                mc.playerController.windowClick(0, bestSlot, 0, 2, mc.thePlayer);
                        }catch(Exception e){e.printStackTrace();}
                        bestSlot = 0;
                }
                iPlayer.currentItem = bestSlot;
	}
}
