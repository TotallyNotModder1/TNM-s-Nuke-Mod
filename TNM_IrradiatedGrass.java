package net.minecraft.src;

import java.util.Random;

public class TNM_IrradiatedGrass extends Block {
	private int side;
	private int top;
	private int bottom;

	protected TNM_IrradiatedGrass(int var1, int index1, int index2, int index3) {
		super(var1, Material.grassMaterial);
		this.stepSound = Block.soundGrassFootstep;
		this.side = index1;
		this.top = index2;
		this.bottom = index3;
	}
	

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, int side) {
		// Still solid for placement, but not valid for mob spawning
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return true; // stays solid
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	public int idDropped(int var1, Random var2) {
		return Block.dirt.blockID;
	}

    public int getBlockTextureFromSide(int side) {
		if (side == 0){
            return this.bottom;
        } else if (side == 1){
            return this.top;
        } else {
            return this.side;
        }
	}

}
