package net.minecraft.src;

public class TNM_ContainerNuke extends Container {
	private TNM_TileEntityWarhead Warhead;

    public TNM_ContainerNuke(InventoryPlayer playerInv, TNM_TileEntityWarhead warhead) {
        this.Warhead = warhead;

        // Warhead slots
        this.addSlot(new TNM_SlotWarhead(warhead, 0, 79, 16));
        this.addSlot(new TNM_SlotWarhead(warhead, 1, 79, 33));
        this.addSlot(new TNM_SlotWarhead(warhead, 2, 79, 50));

        // Player inventory slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    public ItemStack getStackInSlot(int var1) {
        ItemStack var2 = null;                          // result to return
        Slot var3 = (Slot)this.slots.get(var1);         // the slot you shift‑clicked
        if(var3 != null && var3.getHasStack()) {        // only if slot has an item
            ItemStack var4 = var3.getStack();           // the item in the slot
            var2 = var4.copy();                         // make a copy for return
    
            // Routing logic depending on which slot was clicked:
            if(var1 >= 0 && var1 < 3) {

                this.func_28125_a(var4, 3, 39, true);
            } else if(var1 >= 3 && var1 < 30) {
                // Main inventory (3–29) → move to hotbar (30–38
                this.func_28125_a(var4, 30, 39, false);
            } else if(var1 >= 30 && var1 < 39) {
                // Hotbar (30–38) → move to main inventory (3–29)
                this.func_28125_a(var4, 3, 30, false);
            }
    
            // Update the slot after transfer
            if(var4.stackSize == 0) {
                var3.putStack((ItemStack)null);         // clear slot if empty
            } else {
                var3.onSlotChanged();                   // notify slot changed
            }
    
            // If nothing changed, return null (no transfer happened)
            if(var4.stackSize == var2.stackSize) {
                return null;
            }
    
            // Notify slot that an item was picked up
            var3.onPickupFromSlot(var4);
        }
    
        return var2;                                    // return the moved stack
    }
    
    
    
    


    public boolean isUsableByPlayer(EntityPlayer var1) {
        return this.Warhead.canInteractWith(var1);
    }
}
