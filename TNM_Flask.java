package net.minecraft.src;

public class TNM_Flask extends Item {
    private boolean isFull;

    public TNM_Flask(int id, boolean full) {
        super(id);
        this.setMaxStackSize(64); // like a bucket
        this.isFull = full;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        // Ray trace to find what the player is looking at
        float var4 = 1.0F;
		float var5 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * var4;
		float var6 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * var4;
		double var7 = player.prevPosX + (player.posX - player.prevPosX) * (double)var4;
		double var9 = player.prevPosY + (player.posY - player.prevPosY) * (double)var4 + 1.62D - (double)player.yOffset;
		double var11 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)var4;
		Vec3D var13 = Vec3D.createVector(var7, var9, var11);
		float var14 = MathHelper.cos(-var6 * 0.01745329F - 3.141593F);
		float var15 = MathHelper.sin(-var6 * 0.01745329F - 3.141593F);
		float var16 = -MathHelper.cos(-var5 * 0.01745329F);
		float var17 = MathHelper.sin(-var5 * 0.01745329F);
		float var18 = var15 * var16;
		float var20 = var14 * var16;
		double var21 = 5.0D;
		Vec3D var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
		MovingObjectPosition mop = world.rayTraceBlocks_do(var13, var23, !this.isFull);

        if (mop == null) {
            return stack;
        }

        if (mop.typeOfHit == EnumMovingObjectType.TILE) {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;

            // Check if block is a liquid source
            int blockId = world.getBlockId(x, y, z);
            Block block = Block.blocksList[blockId];


            if (blockId == Block.waterStill.blockID) {
                // Add filled water flask to inventory
                stack.stackSize--;
                player.inventory.addItemStackToInventory(new ItemStack(mod_NukeModMain.FlaskWater));
                return stack;
            }

            if (blockId == Block.lavaStill.blockID) {
                // Add filled lava flask to inventory
                stack.stackSize--;
                player.inventory.addItemStackToInventory(new ItemStack(mod_NukeModMain.FlaskLava));
                return stack;
            }

            if (blockId == mod_NukeModMain.NuclearWaste.blockID){
                stack.stackSize--;
                world.setBlockWithNotify(x, y, z, 0);
                player.inventory.addItemStackToInventory(new ItemStack(mod_NukeModMain.FlaskNuclearWaste));
                return stack;
            }

            if (!(block instanceof BlockFluid) && this.isFull){
                stack.stackSize--;
                player.inventory.addItemStackToInventory(new ItemStack(mod_NukeModMain.EmptyFlask));
                return stack;
            }
        }

        // No special case: return unchanged
        return stack;
    }
}
