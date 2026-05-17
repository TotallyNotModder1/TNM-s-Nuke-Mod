package net.minecraft.src;

public class TNM_SlotAssembler extends Slot {
    private final TNM_TileEntityAssembler tile;
    private final EntityPlayer player;

    public TNM_SlotAssembler(EntityPlayer player, TNM_TileEntityAssembler tile, int index, int x, int y) {
        super(tile, index, x, y);
        this.tile = tile;
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Output slot should never accept manual input
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack stack) {
        // Consume the inputs when the player takes the crafted item
        tile.consumeInputs();
        super.onPickupFromSlot(stack);
    }
}
