package net.minecraft.src;

import java.util.List;
import java.util.Random;

import TNM_AudioManager.TNM_SoundHelper;

public class TNM_NuclearWaste extends Block {
	protected TNM_NuclearWaste(int id, int index) {
		super(id, index, Material.snow);
		this.blockIndexInTexture = index;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F / 16.0F, 1.0F);
		this.setTickOnLoad(true);
		this.setStepSound(soundPowderFootstep);
	}


	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4) & 7;
		return var5 >= 3 ? AxisAlignedBB.getBoundingBoxFromPool((double)var2 + this.minX, (double)var3 + this.minY, (double)var4 + this.minZ, (double)var2 + this.maxX, (double)((float)var3 + 0.5F), (double)var4 + this.maxZ) : null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4) & 7;
		float var6 = (float)(2 * (1 + var5)) / 16.0F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var6, 1.0F);
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockId(var2, var3 - 1, var4);
		return var5 != 0 && Block.blocksList[var5].isOpaqueCube() ? var1.getBlockMaterial(var2, var3 - 1, var4).getIsSolid() : false;
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		this.func_314_h(var1, var2, var3, var4);
	}

	private boolean func_314_h(World var1, int var2, int var3, int var4) {
		if(!this.canPlaceBlockAt(var1, var2, var3, var4)) {
			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
			var1.setBlockWithNotify(var2, var3, var4, 0);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
	
		// Particle effect
		double px = (double)x + rand.nextFloat();
		double py = (double)y + rand.nextFloat();
		double pz = (double)z + rand.nextFloat();

		float vy = 0.0F + rand.nextFloat() * 0.01F;

		mod_NukeModMain.spawnTNMParticle(world, "Particulate",
			px, py + 0.1, pz,
			0.0D, vy, 0.0D,
			0,
			0.62F, 0.72F, 0.62F,
			1F,
			2F,
			false,
			5,
			false,
			false,
			0F,
			0F,
			0F,
			1F,
			true
		);

		// Radiation effect
		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(
			null,
			AxisAlignedBB.getBoundingBox(
				x, y, z,
				x + 1, y + 1.2, z + 1
			)
		);

		for (Entity e : entities) {
			if (e instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)e;
				ItemStack[] armor = player.inventory.armorInventory;

				boolean fullHazmat =
				armor[3] != null && armor[3].getItem() == mod_NukeModMain.HazmatHelmet &&
				armor[2] != null && armor[2].getItem() == mod_NukeModMain.HazmatChest &&
				armor[1] != null && armor[1].getItem() == mod_NukeModMain.HazmatLegs &&
				armor[0] != null && armor[0].getItem() == mod_NukeModMain.HazmatBoots;
	

				ItemStack helmet = player.inventory.armorInventory[3]; // helmet slot

				if (fullHazmat && helmet != null && helmet.getItem() == mod_NukeModMain.HazmatHelmet) {
					int max = helmet.getMaxDamage();
				
					if (helmet.getItemDamage() < max - 1) {
						helmet.damageItem(5, player);
					} else {
						helmet.setItemDamage(max - 1); // clamp durability at 1
						player.attackEntityFrom(null, 5); // radiation leaks through
					}
				} else {
					// No helmet or not full hazmat → radiation hits directly
					player.attackEntityFrom(null, 5);
				}
				
			} else if (e instanceof EntityCreature){
				e.attackEntityFrom(null, 5);
			}
		}

		for (Entity e : entities) {
			if (e instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)e;

				// Only play if moving
				if (Math.abs(p.motionX) > 0.01 || Math.abs(p.motionZ) > 0.01) {
					// Use player.ticksExisted as a simple cadence
					if (p.ticksExisted % 4 == 0) { // every 6 ticks ≈ 3 steps per second
						TNM_SoundHelper.playEntitySound(p,
							rand.nextBoolean() ? "mud1.wav" : "mud2.wav",
							0.4F
						);
					}
				}
			}
		}

		// Keep ticking
		world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
	}

	public int idDropped(int var1, Random var2) {
		return mod_NukeModMain.FlaskNuclearWaste.shiftedIndex;
	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		return var5 == 1 ? true : super.shouldSideBeRendered(var1, var2, var3, var4, var5);
	}
}
