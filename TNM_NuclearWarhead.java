package net.minecraft.src;

import java.util.Random;

import TNM_AudioManager.TNM_SoundHelper;
import TNM_RegularNukeExplosion.TNM_ExplosionHandler;


public class TNM_NuclearWarhead extends BlockContainer{
    private int Top;
    private int Side;
    private int Bottom;
    private Random random = new Random();

    public TNM_NuclearWarhead(int id, Material material, int index, int index2, int index3){
        super(id, material);
        this.Side = index;
        this.Top = index2;
        this.Bottom = index3;
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

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        int iscasingbelow = world.getBlockId(x, y - 1, z);
        int iscasingabove = world.getBlockId(x, y + 1, z);
        if (te instanceof TNM_TileEntityWarhead) {
            TNM_TileEntityWarhead warhead = (TNM_TileEntityWarhead) te;
            if (warhead.isFull()) {
                world.setBlockMetadata(x, y, z, 4);
            } else if (iscasingabove == mod_NukeModMain.NukeCasing.blockID || iscasingbelow == mod_NukeModMain.NukeCasing.blockID){
                world.setBlockMetadata(x, y, z, 3);
            } else {
                world.setBlockMetadata(x, y, z, 0);
            }
            world.markBlockAsNeedsUpdate(x, y, z);
        }
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
		TNM_TileEntityWarhead var5 = (TNM_TileEntityWarhead)world.getBlockTileEntity(x, y, z);
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

    public void ignite(World world, int x, int y, int z) {
        TNM_TileEntityWarhead warhead = (TNM_TileEntityWarhead)world.getBlockTileEntity(x, y, z);
        int iscasingbelow = world.getBlockId(x, y - 1, z);
        int iscasingabove = world.getBlockId(x, y + 1, z);
        int casingid = mod_NukeModMain.NukeCasing.blockID;
    
        if (warhead != null && warhead.isFull()) {
            TNM_ExplosionHandler entitynuke = new TNM_ExplosionHandler(world, x+0.5, y+0.5, z+0.5);
    
            world.setBlockWithNotify(x, y, z, 0);
            if (iscasingabove == casingid) world.setBlockWithNotify(x, y+1, z, 0);
            if (iscasingbelow == casingid) world.setBlockWithNotify(x, y-1, z, 0);
    
            world.entityJoinedWorld(entitynuke);
        }
    }
    

    public void onNeighborBlockChange(World world, int x, int y, int z, int meta) {
        boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z);
        boolean powered2 = world.isBlockGettingPowered(x, y, z);
        int iscasingbelow = world.getBlockId(x, y - 1, z);
        int iscasingabove = world.getBlockId(x, y + 1, z);
        int casingid = mod_NukeModMain.NukeCasing.blockID;
        int metadata = world.getBlockMetadata(x, y, z);
        TNM_ExplosionHandler entitynuke = new TNM_ExplosionHandler(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F));
        TNM_TileEntityWarhead warhead = (TNM_TileEntityWarhead)world.getBlockTileEntity(x, y, z);
        if (powered && warhead.isFull() && metadata == 4 || powered2 && warhead.isFull() && metadata == 4){
            this.ignite(world, x, y, z);
        }
	}

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        ItemStack held = player.getCurrentEquippedItem();
        int iscasingbelow = world.getBlockId(x, y - 1, z);
        int iscasingabove = world.getBlockId(x, y + 1, z);
        int casingid = mod_NukeModMain.NukeCasing.blockID;
        if (held != null && held.itemID == mod_NukeModMain.Screwdriver.shiftedIndex && iscasingabove == casingid ||
            held != null && held.itemID == mod_NukeModMain.Screwdriver.shiftedIndex && iscasingbelow == casingid ) { 

            if(!world.multiplayerWorld) {
                TNM_TileEntityWarhead warheadte = (TNM_TileEntityWarhead)world.getBlockTileEntity(x, y, z);
                TNM_SoundHelper.playEntitySound(player, "wrench.ogg", 1.0F);
                ModLoader.OpenGUI(player, new TNM_GuiWarhead(player.inventory, warheadte));
                return true;
            } else {
                // Multiplayer: request GUI open, server will sync TileEntity
                ModLoader.OpenGUI(player, new TNM_GuiWarhead(player.inventory,
                (TNM_TileEntityWarhead) world.getBlockTileEntity(x, y, z)));
                TNM_SoundHelper.playEntitySound(player, "wrench.ogg", 1.0F);
                return true;
            }
        } else {
            return false;
        }
	}

    public int idDropped(int id, Random rand){
        return this.blockID;
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TNM_TileEntityWarhead();
    }

}
