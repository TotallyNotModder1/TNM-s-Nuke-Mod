package net.minecraft.src;

public class TNM_SlotCentrifuge extends Slot {
    private final TNM_TileEntityCentrifuge Centrifuge;
    private final int slotIndex;

    public TNM_SlotCentrifuge(TNM_TileEntityCentrifuge centrifuge, int index, int x, int y) {
        super(centrifuge, index, x, y); // pass the TileEntity as the IInventory
        this.Centrifuge = centrifuge;
        this.slotIndex = index;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return Centrifuge.isItemValidForSlot(slotIndex, stack);
    }
}
