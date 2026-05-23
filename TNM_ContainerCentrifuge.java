package net.minecraft.src;

public class TNM_ContainerCentrifuge extends Container {
	private TNM_TileEntityCentrifuge Centrifuge;

    public TNM_ContainerCentrifuge(InventoryPlayer playerInv, TNM_TileEntityCentrifuge centrifuge) {
        this.Centrifuge = centrifuge;

        // centrifuge slots
        this.addSlot(new TNM_SlotCentrifuge(centrifuge, 0, 42, 53)); //output canister
        this.addSlot(new TNM_SlotCentrifuge(centrifuge, 1, 73, 53)); //input fuel
        this.addSlot(new TNM_SlotCentrifuge(centrifuge, 2, 48, 17)); //input block or fluid
        this.addSlot(new TNM_SlotCentrifuge(centrifuge, 3, 66, 17)); //input canister for fluid bonus
        this.addSlot(new TNM_SlotCentrifuge(centrifuge, 4, 105, 35)); //output results 1
        this.addSlot(new TNM_SlotCentrifuge(centrifuge, 5, 123, 35)); //output results 2
        this.addSlot(new TNM_SlotCentrifuge(centrifuge, 6, 141, 35)); //output results 3

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

    public ItemStack getStackInSlot(int index) {
        ItemStack result = null;
        Slot slot = (Slot) this.slots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            result = stack.copy();

            // Centrifuge slots (0–6) → move to player inventory (7–43)
            if (index < 7) {
                this.func_28125_a(stack, 7, 43, true);
            }
            // Player inventory slots (7–43)
            else {
                // Main inventory (7–34) → move to hotbar (34–43)
                if (index >= 7 && index < 34) {
                    this.func_28125_a(stack, 34, 43, false);
                }
                // Hotbar (34–43) → move to main inventory (7–34)
                else if (index >= 34 && index < 43) {
                    this.func_28125_a(stack, 7, 34, false);
                }
            }

            if (stack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.stackSize == result.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(stack);
        }

        return result;
    }
    

    public boolean isUsableByPlayer(EntityPlayer var1) {
        return this.Centrifuge.canInteractWith(var1);
    }
}
