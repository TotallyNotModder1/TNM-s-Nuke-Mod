package net.minecraft.src;

import java.util.List;
import java.util.Random;

import TNM_AudioManager.TNM_SoundHelper;

public class TNM_Trinitite extends Block {
	public static boolean fallInstantly = false;
	private boolean irradiated;
	private boolean CanFall;
	private boolean isOre;
	private int oredrop;

	protected TNM_Trinitite(int var1, int index1, boolean isirradiated, boolean canfall, boolean isore, int OreDrop) {
		super(var1, Material.ground);
		this.blockIndexInTexture = index1;
		this.irradiated = isirradiated;
		this.CanFall = canfall;
		this.isOre = isore;
		this.oredrop = OreDrop;
	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if (CanFall){
			this.tryToFall(var1, var2, var3, var4);
		}
	}

	private void tryToFall(World var1, int var2, int var3, int var4) {
		if(canFallBelow(var1, var2, var3 - 1, var4) && var3 >= 0) {
			byte var8 = 32;
			if(!fallInstantly && var1.checkChunksExist(var2 - var8, var3 - var8, var4 - var8, var2 + var8, var3 + var8, var4 + var8)) {
				EntityFallingSand var9 = new EntityFallingSand(var1, (double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), this.blockID);
				var1.entityJoinedWorld(var9);
			} else {
				var1.setBlockWithNotify(var2, var3, var4, 0);

				while(canFallBelow(var1, var2, var3 - 1, var4) && var3 > 0) {
					--var3;
				}

				if(var3 > 0) {
					var1.setBlockWithNotify(var2, var3, var4, this.blockID);
				}
			}
		}
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
	
		if (irradiated) {
			// Particle effect
			double px = (double)x + rand.nextFloat();
			double py = (double)y + rand.nextFloat();
			double pz = (double)z + rand.nextFloat();
	
			float vy = 0.0F + rand.nextFloat() * 0.01F;
	
			mod_NukeModMain.spawnTNMParticle(world, "Particulate",
				px, y + 1.2, pz,
				0.0D, vy, 0.0D,
				0,
				0.35F, 0.35F, 0.35F,
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
					x + 1, y + 1.5, z + 1
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
							helmet.damageItem(1, player);
							TNM_SoundHelper.playEntitySound(player, "geiger2.wav", 0.6F);
						} else {
							helmet.setItemDamage(max - 1); // clamp durability at 1
							player.attackEntityFrom(null, 1); // radiation leaks through
							TNM_SoundHelper.playEntitySound(player, "geiger3.wav", 0.6F);
						}
					} else {
						// No helmet or not full hazmat → radiation hits directly
						player.attackEntityFrom(null, 1);
						TNM_SoundHelper.playEntitySound(player, "geiger3.wav", 0.6F);
					}
					
				} else if (e instanceof EntityCreature){
					e.attackEntityFrom(null, 1);
				}
			}
		}
	
		// Keep ticking
		world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
	}
	
	

	public int idDropped(int var1, Random var2) {
		if (!isOre){
			if (this.blockID == 122){
				return Item.coal.shiftedIndex;
			}
			else {
				return this.blockID;
			}
		} else {
			return this.oredrop;
		}
	}

	public int tickRate() {
		if (irradiated) {
			return 40;
		} else if (this.blockID == 122){
			return 30;
		} else {
			return 1;
		}
	}

	public static boolean canFallBelow(World var0, int var1, int var2, int var3) {
		int var4 = var0.getBlockId(var1, var2, var3);
		if(var4 == 0) {
			return true;
		} else if(var4 == Block.fire.blockID) {
			return true;
		} else {
			Material var5 = Block.blocksList[var4].blockMaterial;
			return var5 == Material.water ? true : var5 == Material.lava;
		}
	}

}
