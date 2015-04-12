package tk.wurst_client.mods;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
description = "Shows projectiles' path of motion",
name = "Trajectories")
public class TrajectoriesMod extends Mod implements RenderListener
{
	
	public double pZ = -9000.0D;
	public double pY = -9000.0D;
	public double pX = -9000.0D;
	
	@Override
	public void onEnable()
	{
		EventManager.render.addListener(this);
	}
	
	@Override
	public void onRender()
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack is = player.inventory.getCurrentItem();
		if (is != null)
		{
			String id = is.getItem().getUnlocalizedName().toLowerCase();
			boolean drawPrediction = true;
			float var4 = 0.05F;
			float var5 = 3.2F;
			float var6 = 0.0F;
			if ((id.contains("box")) && (!id.contains("bowl")))
			{
				var4 = 0.05F;
				var5 = 3.2F;
			}
			else if (id.contains("enderpearl"))
			{
				var4 = 0.03F;
				var5 = 1.5F;
			}
			else if (id.contains("snowball"))
			{
				var4 = 0.03F;
				var5 = 1.5F;
			}
			else if (id.contains("fishingrod"))
			{
				var5 = 1.5F;
			}
			else if ((id.contains("egg")) && (!id.contains("monsterstoneegg")))
			{
				var4 = 0.03F;
				var5 = 1.5F;
			}
			else if ((is.getItemDamage() & 0x4000) > 0)
			{
				var4 = 0.05F;
				var5 = 0.5F;
				var6 = -20.0F;
			}
			else
			{
				drawPrediction = false;
			}
			
			if (drawPrediction)
			{
				float playerX = (float)player.posX;
				float playerY = (float)player.posY;
				float playerZ = (float)player.posZ;
				float playerYaw = player.rotationYaw;
				float playerPitch = player.rotationPitch;
				float var15 = playerYaw / 180.0F * 3.141593F;
				float var16 = MathHelper.sin(var15);
				float var17 = MathHelper.cos(var15);
				float var18 = playerPitch / 180.0F * 3.141593F;
				float var19 = MathHelper.cos(var18);
				float var20 = (playerPitch + var6) / 180.0F * 3.141593F;
				float var21 = MathHelper.sin(var20);
				float origX = playerX - var17 * 0.16F;
				float origY = playerY - 0.1F;
				float origZ = playerZ - var16 * 0.16F;
				float nextSegX = -var16 * var19 * 0.4F;
				float nextSegY = -var21 * 0.4F;
				float nextSegZ = var17 * var19 * 0.4F;
				float var28 = MathHelper.sqrt_double(nextSegX * nextSegX + nextSegY * nextSegY + nextSegZ * nextSegZ);
				nextSegX /= var28;
				nextSegY /= var28;
				nextSegZ /= var28;
				nextSegX *= var5;
				nextSegY *= var5;
				nextSegZ *= var5;
				float newX = origX;
				float newY = origY;
		        float newZ = origZ;
		        GL11.glPushMatrix();
		        RenderUtils.beginSmoothLine();
		        GL11.glColor4f(0.25F, 0.65F, 0.95F, 0.7F);
		        GL11.glBegin(1);
		        int segmentCount = 0;
		        boolean continueDraw = true;
		        while (continueDraw)
		        {
		        	segmentCount++;
		            newX += nextSegX;
		            newY += nextSegY;
		            newZ += nextSegZ;
		            Vec3 vecOrig = new Vec3(origX, origY, origZ);
		            Vec3 vecNew = new Vec3(newX, newY, newZ);
		            MovingObjectPosition movingObj = Minecraft.getMinecraft().theWorld.rayTraceBlocks(vecOrig, vecNew, false, true, false);
		            if (movingObj != null)
		            {
		              origX = (float)movingObj.hitVec.xCoord;
		              origY = (float)movingObj.hitVec.yCoord;
		              origZ = (float)movingObj.hitVec.zCoord;
		              continueDraw = false;
		            }
		            else if (segmentCount > 200)
		            {
		              continueDraw = false;
		            }
		            else
		            {
		              drawLine3D(origX - playerX, origY - playerY, origZ - playerZ, newX - playerX, newY - playerY, newZ - playerZ);

				      AxisAlignedBB bounding = AxisAlignedBB.fromBounds(origX - 0.125F, origY, origZ - 0.125F, origX + 0.125F, origY + 0.25F, origZ + 0.125F);
		              var4 = 0.0F;
		              for (int var36 = 0; var36 < 5; var36++)
		              {
		                float var37 = (float)(bounding.minY + (bounding.maxY - bounding.minY) * var36 / 5.0D);
		                float var38 = (float)(bounding.minY + (bounding.maxY - bounding.minY) * (var36 + 1) / 5.0D);
		                AxisAlignedBB var39 = AxisAlignedBB.fromBounds(bounding.minX, var37, bounding.minZ, bounding.maxX, var38, bounding.maxZ);
		                if (Minecraft.getMinecraft().theWorld.isAABBInMaterial(var39, Material.water)) {
		                  var4 += 0.2F;
		                }
		              }
		              float var40 = var4 * 2.0F - 1.0F;
		              nextSegY += 0.04F * var40;
		              float var37 = 0.92F;
		              if (var4 > 0.0F)
		              {
		                var37 *= 0.9F;
		                nextSegY *= 0.8F;
		              }
		              nextSegX *= var37;
		              nextSegY *= var37;
		              nextSegZ *= var37;
		              
		              origX = newX;
		              origY = newY;
		              origZ = newZ;
		            }
		            if (!continueDraw)
		            {
		              this.pX = origX;
		              this.pY = origY;
		              this.pZ = origZ;
		              GL11.glVertex3d(this.pX - playerX - 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ - 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX - 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ + 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX + 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ + 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX + 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ - 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX + 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ + 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX - 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ + 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX - 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ - 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX + 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ - 0.1000000014901161D);
		              GL11.glVertex3d(this.pX - playerX - 0.1000000014901161D, this.pY - playerY, this.pZ - playerZ - 0.1000000014901161D);
		            }
		        }
		        GL11.glEnd();
		        RenderUtils.endSmoothLine();
		        GL11.glPopMatrix();
			}
		}
	}
	
	public void drawLine3D(float var1, float var2, float var3, float var4, float var5, float var6)
	  {
	    GL11.glVertex3d(var1, var2, var3);
	    GL11.glVertex3d(var4, var5, var6);
	  }
	
	@Override
	public void onDisable()
	{
		EventManager.render.removeListener(this);
	}
}
