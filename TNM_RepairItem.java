package net.minecraft.src;

public class TNM_RepairItem extends Item{
    public boolean isfilter;

    public TNM_RepairItem(int id, boolean IsFilter, int Uses){
        super(id);
        this.isfilter = IsFilter;
        this.setMaxDamage(Uses);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    
        // Loop through armor slots, but skip helmet (index 3)
        if (!isfilter){
            for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                if (i == 3) continue; // skip helmet
        
                ItemStack armorPiece = player.inventory.armorInventory[i];
                if (armorPiece != null && armorPiece.getItem() instanceof TNM_HazmatArmor) {
                    int currentDamage = armorPiece.getItemDamage();
                    if (currentDamage > 0) {
                        armorPiece.setItemDamage(Math.max(0, currentDamage - 100)); // repair 10 durability
                        stack.damageItem(1, player); // consume repair item
                        break; // stop after repairing one piece
                    }
                }
            }
        } else {
            ItemStack armorPiece = player.inventory.armorInventory[3];
            if (armorPiece != null && armorPiece.getItem() instanceof TNM_HazmatArmor) {
                int currentDamage = armorPiece.getItemDamage();
                if (currentDamage > 0) {
                    armorPiece.setItemDamage(Math.max(0, currentDamage - 500)); // repair 10 durability
                    --stack.stackSize;
                }
            }
        }
        return stack; // nothing repaired
    }
    
}
