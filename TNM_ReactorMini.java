package net.minecraft.src;

import java.util.Random;


public class TNM_ReactorMini extends BlockContainer{
    private int Top;
    private int Side;
    private int Bottom;
    private Random random = new Random();

    public TNM_ReactorMini(int id, Material material, int index, int index2){
        super(id, material);
        this.Side = index;
        this.Top = index2;
        this.Bottom = index2;
        this.setTickOnLoad(true);
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
        super.onBlockAdded(world, x, y, z);
        world.markBlockAsNeedsUpdate(x, y, z);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
		TNM_TileEntityRGenerator var5 = (TNM_TileEntityRGenerator)world.getBlockTileEntity(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 0){
            for(int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
                ItemStack var7 = var5.getStackInSlot(var6);
                if(var7 != null) {
                    float var8 = this.random.nextFloat() * 0.8F + 0.1F;
                    float var9 = this.random.nextFloat() * 0.8F + 0.1F;
                    float world0 = this.random.nextFloat() * 0.8F + 0.1F;
    
                    while(var7.stackSize > 0) {
                        int world1 = this.random.nextInt(21) + 10;
                        if(world1 > var7.stackSize) {
                            world1 = var7.stackSize;
                        }
    
                        var7.stackSize -= world1;
                        EntityItem world2 = new EntityItem(world, (double)((float)x + var8), (double)((float)y + var9), (double)((float)z + world0), new ItemStack(var7.itemID, world1, var7.getItemDamage()));
                        float world3 = 0.05F;
                        world2.motionX = (double)((float)this.random.nextGaussian() * world3);
                        world2.motionY = (double)((float)this.random.nextGaussian() * world3 + 0.2F);
                        world2.motionZ = (double)((float)this.random.nextGaussian() * world3);
                        world.entityJoinedWorld(world2);
                    }
                }
            }
        }

		super.onBlockRemoval(world, x, y, z);
	}

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        ItemStack held = player.getCurrentEquippedItem();
        if (held != null && held.itemID == mod_NukeModMain.HeatPipeItem.shiftedIndex) return false;

        if(!world.multiplayerWorld) {
            TNM_TileEntityRGenerator reactorminite = (TNM_TileEntityRGenerator)world.getBlockTileEntity(x, y, z);
            ModLoader.OpenGUI(player, new TNM_GuiNuclearGenerator(player.inventory, reactorminite));
            return true;
        } else {
            // Multiplayer: request GUI open, server will sync TileEntity
            ModLoader.OpenGUI(player, new TNM_GuiNuclearGenerator(player.inventory,
            (TNM_TileEntityRGenerator) world.getBlockTileEntity(x, y, z)));
            return true;
        }
	}

    public int idDropped(int id, Random rand){
        return this.blockID;
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TNM_TileEntityRGenerator();
    }

}
