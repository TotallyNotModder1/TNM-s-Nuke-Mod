package net.minecraft.src;

import java.util.Random;

import TNM_MiniNuke.TNM_WheatNukePrimed;


public class TNM_WheatNuke extends Block{
    private int top;
    private int side;
    private int bottom;

    public TNM_WheatNuke(int id, Material material, int index1, int index2, int index3){
        super(id, material);
        this.side = index1;
		this.top = index2;
        this.bottom = index3;
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

    public  void onBlockAdded(World world, int x, int y, int z){
        world.markBlockAsNeedsUpdate(x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int meta) {
        boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z);
        TNM_WheatNukePrimed entitynuke = new TNM_WheatNukePrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F));
        if (powered){
            world.setBlock(x, y, z, 0);
            world.playSoundEffect(x, y, z, "random.fuse", 0.5F, 1.0F);
            world.entityJoinedWorld(entitynuke);
        }
	}

    public int idDropped(int id, Random rand){
        return this.blockID;
    }

}
