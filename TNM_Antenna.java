package net.minecraft.src;

import java.util.Random;

public class TNM_Antenna extends Block {
	private int Side;
	private int Top;
	private int Bottom;


	protected TNM_Antenna(int id, int index1, int index2) {
		super(id, index1, Material.leaves);
		this.setTickOnLoad(true);
        this.setHardness(0.1F);
        // 10 px wide (0.625F), 8 px tall (0.5F)
        this.setBlockBounds(
            0.1875F,  // X min (3/16 offset)
            0.0F,     // Y min
            0.1875F,  // Z min (3/16 offset)
            0.8125F,  // X max (13/16)
            0.5F,     // Y max (8 px tall)
            0.8125F   // Z max (13/16)
        );
		this.Side = index1;
		this.Top = index2;
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool(
            (double)x + 0.1875D,
            (double)y + 0.0D,
            (double)z + 0.1875D,
            (double)x + 0.8125D,
            (double)y + 0.5D,
            (double)z + 0.8125D
        );
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool(
            (double)x + 0.1875D,
            (double)y + 0.0D,
            (double)z + 0.1875D,
            (double)x + 0.8125D,
            (double)y + 0.5D,
            (double)z + 0.8125D
        );
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
	

    public void trigger(World world, int x, int y, int z) {
        int[][] offsets = { {0, -1, 0}, {0, -2, 0} };
    
        for (int[] o : offsets) {
            int wx = x + o[0];
            int wy = y + o[1];
            int wz = z + o[2];
            int id = world.getBlockId(wx, wy, wz);
            int meta = world.getBlockMetadata(wx, wy, wz);
    
            if (id == mod_NukeModMain.Warhead1.blockID) {
                Block warhead = Block.blocksList[id];
                if (warhead instanceof TNM_NuclearWarhead && meta == 4) {
                    ((TNM_NuclearWarhead)warhead).ignite(world, wx, wy, wz);
                    world.setBlock(x, y, z, 0); // remove antenna after use
                }
            }
        }
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

	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.getBlockMaterial(x, y - 1, z).isSolid();
	}

	public int idDropped(int var1, Random var2) {
		return this.blockID;
	}
}
