package net.minecraft.src;

import java.util.Random;


public class TNM_Centrifuge extends BlockContainer{
    private int Top;
    private int Side;
    private int Bottom;
    private Random random = new Random();

    public TNM_Centrifuge(int id, Material material, int index, int index2){
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

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        ItemStack held = player.getCurrentEquippedItem();
        if (held != null && held.itemID == mod_NukeModMain.HeatPipeItem.shiftedIndex) return false;

        if(!world.multiplayerWorld) {
            TNM_TileEntityCentrifuge centrifugete = (TNM_TileEntityCentrifuge)world.getBlockTileEntity(x, y, z);
            ModLoader.OpenGUI(player, new TNM_GuiCentrifuge(player.inventory, centrifugete));
            return true;
        } else {
            // Multiplayer: request GUI open, server will sync TileEntity
            ModLoader.OpenGUI(player, new TNM_GuiCentrifuge(player.inventory,
            (TNM_TileEntityCentrifuge) world.getBlockTileEntity(x, y, z)));
            return true;
        }
	}

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        boolean powered = world.isBlockGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y, z);
        if (powered) {
            // spawn smoke particles above the block
            for (int i = 0; i < 3; i++) {
                double px = x + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
                double py = y + 1.0D;
                double pz = z + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
                world.spawnParticle("smoke", px, py, pz, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        TNM_TileEntityCentrifuge te = (TNM_TileEntityCentrifuge) world.getBlockTileEntity(x, y, z);
        if (te != null) {
            for (int i = 0; i < te.getSizeInventory(); i++) {
                ItemStack stack = te.getStackInSlot(i);
                if (stack != null) {
                    // drop each item into the world
                    EntityItem drop = new EntityItem(world,
                        x + world.rand.nextFloat(),
                        y + world.rand.nextFloat(),
                        z + world.rand.nextFloat(),
                        stack);
                    world.entityJoinedWorld(drop);
                }
            }
        }
        super.onBlockRemoval(world, x, y, z);
    }


    public int idDropped(int id, Random rand){
        return this.blockID;
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TNM_TileEntityCentrifuge();
    }

}
