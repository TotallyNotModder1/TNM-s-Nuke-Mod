package net.minecraft.src;

import TNM_MiniNuke.TNM_MininukeEntity;

public class TNM_Bazooka extends Item {
	public TNM_Bazooka(int id) {
		super(id);
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.inventory.consumeInventoryItem(mod_NukeModMain.BlockFamNuke.blockID)) {
            world.playSoundAtEntity(player, "random.explode", 1.0F,
                1.3F);
    
            if (!world.multiplayerWorld) {
                // Create primed TNT at player’s position
                TNM_FamNukePrimed famnuke = new TNM_FamNukePrimed(world,
                    player.posX, player.posY, player.posZ);
    
                // Give it forward velocity like a rocket
                float velocity = 5F; // tweak for speed
                famnuke.motionX = -Math.sin(player.rotationYaw / 180.0F * (float)Math.PI)
                              * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                famnuke.motionZ =  Math.cos(player.rotationYaw / 180.0F * (float)Math.PI)
                              * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                famnuke.motionY = -Math.sin(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
    
                world.entityJoinedWorld(famnuke);
            }
        } else if (player.inventory.consumeInventoryItem(mod_NukeModMain.MiniNuke.shiftedIndex)) {
            world.playSoundAtEntity(player, "random.explode", 1.0F,
            1.3F);

            if (!world.multiplayerWorld) {
                // Create primed TNT at player’s position
                TNM_MininukeEntity mininuke = new TNM_MininukeEntity(world,
                    player.posX, player.posY, player.posZ);

                // Give it forward velocity like a rocket
                float velocity = 5F; // tweak for speed
                mininuke.motionX = -Math.sin(player.rotationYaw / 180.0F * (float)Math.PI)
                            * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                mininuke.motionZ =  Math.cos(player.rotationYaw / 180.0F * (float)Math.PI)
                            * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                mininuke.motionY = -Math.sin(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;

                world.entityJoinedWorld(mininuke);
            } 
        } else if (player.inventory.consumeInventoryItem(mod_NukeModMain.BlockNuke.blockID)) {
            world.playSoundAtEntity(player, "random.explode", 1.0F,
            1.3F);

            if (!world.multiplayerWorld) {
                // Create primed TNT at player’s position
                TNM_NukePrimed truenuke = new TNM_NukePrimed(world,
                    player.posX, player.posY, player.posZ);

                // Give it forward velocity like a rocket
                float velocity = 5F; // tweak for speed
                truenuke.motionX = -Math.sin(player.rotationYaw / 180.0F * (float)Math.PI)
                            * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                truenuke.motionZ =  Math.cos(player.rotationYaw / 180.0F * (float)Math.PI)
                            * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                truenuke.motionY = -Math.sin(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;

                world.entityJoinedWorld(truenuke);
            } 
        }
        else if (player.inventory.consumeInventoryItem(Block.tnt.blockID)) {
            world.playSoundAtEntity(player, "random.explode", 1.0F,
                1.3F / (itemRand.nextFloat() * 0.4F + 0.8F));
    
            if (!world.multiplayerWorld) {
                // Create primed TNT at player’s position
                EntityTNTPrimed tnt = new EntityTNTPrimed(world,
                    player.posX, player.posY, player.posZ);
    
                // Give it forward velocity like a rocket
                float velocity = 5F; // tweak for speed
                tnt.motionX = -Math.sin(player.rotationYaw / 180.0F * (float)Math.PI)
                              * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                tnt.motionZ =  Math.cos(player.rotationYaw / 180.0F * (float)Math.PI)
                              * Math.cos(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
                tnt.motionY = -Math.sin(player.rotationPitch / 180.0F * (float)Math.PI) * velocity;
    
                world.entityJoinedWorld(tnt);
            }
        }

		return stack;
	}

    private boolean playerHasMiniNukes(EntityPlayer player) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack slot = player.inventory.mainInventory[i];
            if (slot != null && slot.itemID == mod_NukeModMain.MiniNuke.shiftedIndex) {
                return true;
            }
        }
        return false;
    }

    private boolean playerHasFamNukes(EntityPlayer player) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack slot = player.inventory.mainInventory[i];
            if (slot != null && slot.itemID == mod_NukeModMain.BlockFamNuke.blockID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld){
        if (entity instanceof EntityPlayer) {
            boolean hasNukes = playerHasFamNukes((EntityPlayer) entity);

            if (!hasNukes){
                this.iconIndex = mod_NukeModMain.Bazookatex;
            }
            else if (hasNukes) {
                this.iconIndex = mod_NukeModMain.BazookatexFamNuke;
            }
        }
    }
}
