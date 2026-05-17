package net.minecraft.src;

import java.util.Random;


public class TNM_HeatPipe extends BlockContainer{

    public TNM_HeatPipe(int id, Material material, int index){
        super(id, material);
        this.blockIndexInTexture = index;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        // Pipe thickness: 10px out of 16px
        double half = 8.0D / 32.0D; // 0.3125
    
        // central cube
        double minX = x + 0.5D - half;
        double minY = y + 0.5D - half;
        double minZ = z + 0.5D - half;
        double maxX = x + 0.5D + half;
        double maxY = y + 0.5D + half;
        double maxZ = z + 0.5D + half;
    
        // extend outward if connected in each direction
        if (isPipeConnectable(world.getBlockId(x - 1, y, z))) {
            minX = x;
        }
        if (isPipeConnectable(world.getBlockId(x + 1, y, z))) {
            maxX = x + 1.0D;
        }
        if (isPipeConnectable(world.getBlockId(x, y - 1, z))) {
            minY = y;
        }
        if (isPipeConnectable(world.getBlockId(x, y + 1, z))) {
            maxY = y + 1.0D;
        }
        if (isPipeConnectable(world.getBlockId(x, y, z - 1))) {
            minZ = z;
        }
        if (isPipeConnectable(world.getBlockId(x, y, z + 1))) {
            maxZ = z + 1.0D;
        }
    
        return AxisAlignedBB.getBoundingBoxFromPool(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    private boolean isPipeConnectable(int id) {
        return id == this.blockID
            || id == mod_NukeModMain.CentrifugeBlock.blockID
            || id == mod_NukeModMain.MiniReactor.blockID
            || id == mod_NukeModMain.AssemblyTable.blockID;
    }
    
    
    
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return this.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    public int getRenderType() { 
        return mod_NukeModMain.RenderHeatPipeid; 
    }

    public int idDropped(int id, Random rand){
        return mod_NukeModMain.HeatPipeItem.shiftedIndex;
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TNM_TileEntityHeatPipe();
    }

}
