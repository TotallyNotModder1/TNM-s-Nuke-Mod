package net.minecraft.src;

import java.util.Random;

public class TNM_NukeCasing extends Block {
	private int side;
	private int top;

	protected TNM_NukeCasing(int var1, Material mat, int index1, int index2) {
		super(var1, mat);
		this.stepSound = Block.soundMetalFootstep;
		this.side = index1;
		this.top = index2;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}
	
	public boolean isOpaqueCube(){
		return true;
	}

	public int idDropped(int var1, Random var2) {
		return this.blockID;
	}

    public int getBlockTextureFromSide(int side) {
		if (side == 0){
            return this.top;
        } else if (side == 1){
            return this.top;
        } else {
            return this.side;
        }
	}

}
