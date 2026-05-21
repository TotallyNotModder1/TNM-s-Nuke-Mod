package TNM_RecipeBookClasses;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class TNM_SlotDisplayOnly extends Slot {
    public TNM_SlotDisplayOnly(IInventory inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false; // can't insert
    }

    @Override
    public void onPickupFromSlot(ItemStack stack) {
        // do nothing, prevents taking
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return null; // nothing to remove
    }
}


