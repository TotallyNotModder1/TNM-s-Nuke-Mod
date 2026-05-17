package TNM_MiniNuke;

import TNM_MiniNuke.TNM_MiniNukeHandler;
import TNM_RegularNukeExplosion.TNM_BaseCloudHandler;
import TNM_RegularNukeExplosion.TNM_BurnWaveHandler;
import TNM_RegularNukeExplosion.TNM_ExplosionHandler;
import TNM_RegularNukeExplosion.TNM_ShockwaveHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TNM_NuclearExplosionDamage;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_WheatNukePrimed extends Entity {
	public int wheatfuse;
    public int explosiontimer = 0;

	public TNM_WheatNukePrimed(World var1) {
		super(var1);
		this.wheatfuse = 0;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
	}

	public TNM_WheatNukePrimed(World var1, double var2, double var4, double var6) {
		this(var1);
		this.setPosition(var2, var4, var6);
		float var8 = (float)(Math.random() * (double)((float)Math.PI) * 2.0D);
		this.motionX = (double)(-MathHelper.sin(var8 * (float)Math.PI / 180.0F) * 0.02F);
		this.motionY = (double)0.2F;
		this.motionZ = (double)(-MathHelper.cos(var8 * (float)Math.PI / 180.0F) * 0.02F);
		this.wheatfuse = 80;
		this.prevPosX = var2;
		this.prevPosY = var4;
		this.prevPosZ = var6;
	}

	public void ImpactDustCloud(World world, double x, double y, double z, int radius, int density){
		for (int i = 0; i < density; i++) {
			// Random horizontal spread
			double theta = world.rand.nextDouble() * 2.0 * Math.PI;
			double r = world.rand.nextDouble() * radius; // radial distance in XZ plane

			// Horizontal offsets
			double dx = r * Math.cos(theta);
			double dz = r * Math.sin(theta);

			// Vertical offset: bias upward
			double dy = world.rand.nextDouble() * radius * 0.3; // taller plume

			// Velocity: mostly upward, slight outward
			double vx = dx * 0.03;
			double vz = dz * 0.03;
			double vy = 0.2 + world.rand.nextDouble() * 0.3; // strong upward push

			// Gravity pulls down over time
			double gravity = -0.04;

			mod_NukeModMain.spawnTNMParticle(worldObj, "Smokepuff", 
				x, y, z,   
				vx, dy + gravity, vz, 0.01F,
				1F,     //R
				0.7F,   //G
				0.3F,   //B
				1,
				40F,
				true,
				200 + world.rand.nextInt(5),
				false,
				false,
				0,
				0,
				0,
				0,
				true
			);
		} 
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
		this.ImpactDustCloud(worldObj, this.posX, this.posY, this.posZ, 6, 90);
		mod_NukeModMain.newexplosionNuke(worldObj, (Entity)null, posX, posY, posZ, 64F, true, mod_NukeModMain.NuclearWaste.blockID, false);
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

		TNM_BurnWaveHandler Burnwave = new TNM_BurnWaveHandler(worldObj, this.posX, this.posY, this.posZ, 31);
		TNM_ShockwaveHandler Shockwave = new TNM_ShockwaveHandler(worldObj, this.posX, this.posY, this.posZ, 70, true);
		

		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		if(this.wheatfuse-- <= 0) {
			if(!this.worldObj.multiplayerWorld) {
				this.explode();
				TNM_NuclearExplosionDamage.NuclearDamage(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 45, 25, 100);
				this.worldObj.entityJoinedWorld(Burnwave);
				this.worldObj.entityJoinedWorld(Shockwave);
				worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2000.0F, 0.05F + rand.nextFloat() * 0.2F);
				this.setEntityDead();
			} else {
				this.setEntityDead();
			}
		} else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}

	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setByte("Fuse", (byte)this.wheatfuse);
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		this.wheatfuse = var1.getByte("Fuse");
	}

	public float getShadowSize() {
		return 0.0F;
	}
}
