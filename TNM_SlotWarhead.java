package net.minecraft.src;

public class TNM_SlotWarhead extends Slot {
    private final TNM_TileEntityWarhead warhead;
    private final int slotIndex;

    public TNM_SlotWarhead(TNM_TileEntityWarhead warhead, int index, int x, int y) {
        super(warhead, index, x, y); // pass the TileEntity as the IInventory
        this.warhead = warhead;
        this.slotIndex = index;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return warhead.isItemValidForSlot(slotIndex, stack);
    }
}
