package net.minecraft.src;

import TNM_MiniNuke.TNM_MiniNukeHandler;
import TNM_RegularNukeExplosion.TNM_EntityCustomFX;
import TNM_RegularNukeExplosion.TNM_ExplosionHandler;

public class TNM_NukePrimed extends Entity {
	public int nukefuse;
    public int explosiontimer = 0;

	public TNM_NukePrimed(World var1) {
		super(var1);
		this.nukefuse = 0;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
	}

	public TNM_NukePrimed(World var1, double var2, double var4, double var6) {
		this(var1);
		this.setPosition(var2, var4, var6);
		float var8 = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.motionX = (double)(-MathHelper.sin(var8 * (float)Math.PI / 180.0F) * 0.02F);
		this.motionY = (double)0.2F;
		this.motionZ = (double)(-MathHelper.cos(var8 * (float)Math.PI / 180.0F) * 0.02F);
		this.nukefuse = 80;
		this.prevPosX = var2;
		this.prevPosY = var4;
		this.prevPosZ = var6;
	}

	protected void entityInit() {
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	private void explode() {
		mod_NukeModMain.triggerNukeBlastStep2(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 60, 0);
		TNM_NuclearExplosionDamage.NuclearDamage(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 100, 25, 200);
		mod_NukeModMain.triggerNukeCrater(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 20, 35, 0);
		TNM_EntityCustomFX effect = new TNM_EntityCustomFX(worldObj, posX, posY, posZ,
        0, 0.2D, 0, 30, 0, 100, 100, 1000);
		worldObj.entityJoinedWorld(effect);
		worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 2000.0F, 0.05F + rand.nextFloat() * 0.2F);
		worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2000.0F, 0.05F + rand.nextFloat() * 0.2F);
        if (this.worldObj == null) return;
	}


	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		if(this.nukefuse-- <= 0) {
			if(!this.worldObj.multiplayerWorld) {
				this.explode();
				this.setEntityDead();
			} else {
				this.setEntityDead();
			}
		} else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}

	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setByte("Fuse", (byte)this.nukefuse);
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.nukefuse = var1.getByte("Fuse");
	}

	public float getShadowSize() {
		return 0.0F;
	}
}
