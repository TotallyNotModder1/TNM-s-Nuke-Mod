package net.minecraft.src;

import java.util.Random;

import TNM_BakerExplosion.TNM_BakerExplosion;

public class TNM_PieBomb extends Block {
	private int Side;
	private int Top;
	private int Bottom;


	protected TNM_PieBomb(int id, int index1, int index2, int index3) {
		super(id, index1, Material.cakeMaterial);
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F,
			1F, 0.75F, 1F);
		this.Side = index1;
		this.Top = index2;
		this.Bottom = index3;
	}

	public int getRenderType() {
		return 13;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		float var6 = 1.0F / 16.0F;
		float var7 = (float)(1 + var5 * 2) / 16.0F;
		float var8 = 0.75F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)var2 + var7), (double)var3, (double)((float)var4 + var6), (double)((float)(var2 + 1) - var6), (double)((float)var3 + var8 - var6), (double)((float)(var4 + 1) - var6));
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		float var6 = 1.0F / 16.0F;
		float var7 = (float)(1 + var5 * 2) / 16.0F;
		float var8 = 0.75F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)var2 + var7), (double)var3, (double)((float)var4 + var6), (double)((float)(var2 + 1) - var6), (double)((float)var3 + var8), (double)((float)(var4 + 1) - var6));
	}

	public int getBlockTextureFromSide(int side) {
		if (side == 0) return Bottom;
		else if (side == 1) return Top;
		else return Side;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}
	

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		Explode(var1, var2, var3, var4);
		return true;
	}

	public void Explode(World world, int x, int y, int z){
		TNM_BakerExplosion entity = new TNM_BakerExplosion(world, x, y, z);
		world.entityJoinedWorld(entity);
		world.setBlock(x, y, z, 0);
	}


	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return !super.canPlaceBlockAt(var1, var2, var3, var4) ? false : this.canBlockStay(var1, var2, var3, var4);
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(!this.canBlockStay(var1, var2, var3, var4)) {
			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

	}

	public boolean canBlockStay(World var1, int var2, int var3, int var4) {
		return var1.getBlockMaterial(var2, var3 - 1, var4).isSolid();
	}

	public int idDropped(int var1, Random var2) {
		return mod_NukeModMain.Pie.shiftedIndex;
	}
}
