package net.minecraft.src;

import java.util.Random;


public class TNM_NukeBlock extends Block{

    public TNM_NukeBlock(int id, Material material, int index){
        super(id, material);
        this.blockIndexInTexture = index;
    }

    public  void onBlockAdded(World world, int x, int y, int z){
        world.markBlockAsNeedsUpdate(x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int meta) {
        boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z);
        TNM_NukePrimed entitynuke = new TNM_NukePrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F));
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
