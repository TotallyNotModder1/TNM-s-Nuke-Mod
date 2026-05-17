package net.minecraft.src;

import java.util.Random;


public class TNM_FamNuke extends Block{
    private int Top;
    private int Side;
    private int Bottom;

    public TNM_FamNuke(int id, Material material, int index, int index2, int index3){
        super(id, material);
        this.Side = index;
        this.Top = index2;
        this.Bottom = index3;
    }

    public int getBlockTextureFromSide(int side) {
		if (side == 0){
            return this.Bottom;
        } else if (side == 1){
            return this.Top;
        } else {
            return this.Side;
        }
	}

    public  void onBlockAdded(World world, int x, int y, int z){
        world.markBlockAsNeedsUpdate(x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int meta) {
        boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z);
        TNM_FamNukePrimed entitynuke = new TNM_FamNukePrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F));
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
