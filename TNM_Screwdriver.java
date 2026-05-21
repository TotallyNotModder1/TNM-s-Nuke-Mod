package net.minecraft.src;

public class TNM_Screwdriver extends Item { 

    public TNM_Screwdriver(int id){
        super(id);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side){
        int blockID = world.getBlockId(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        if (blockID != mod_NukeModMain.Warhead1.blockID && blockMeta != 3){
            return false;
        }

        world.playSoundAtEntity(player, "nukemod.wrench", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
        return true;
    }
}
