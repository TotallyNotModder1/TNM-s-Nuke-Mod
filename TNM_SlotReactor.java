package net.minecraft.src;

public class TNM_SlotReactor extends Slot {
    private final TNM_TileEntityRGenerator Reactor;
    private final int slotIndex;

    public TNM_SlotReactor(TNM_TileEntityRGenerator Reactor, int index, int x, int y) {
        super(Reactor, index, x, y); // pass the TileEntity as the IInventory
        this.Reactor = Reactor;
        this.slotIndex = index;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return Reactor.isItemValidForSlot(slotIndex, stack);
    }
}
