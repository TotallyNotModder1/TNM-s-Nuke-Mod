package TNM_RecipeBookClasses;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryBasic;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.TNM_AssemblyRecipes;


public class TNM_ContainerRecipeBook extends Container {
    private InventoryBasic displayInventory = new InventoryBasic("Display", 17);

    public TNM_ContainerRecipeBook() {
        // Recipe grid (0–14)
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new TNM_SlotDisplayOnly(displayInventory, col + row * 3,
                        16 + col * 18, 57 + row * 18));
            }
        }
        // Output slot (15)
        this.addSlot(new TNM_SlotDisplayOnly(displayInventory, 15, 123, 93));

        // Blueprint slot
        this.addSlot(new TNM_SlotDisplayOnly(displayInventory, 16, 71 , 129));
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    public void setRecipe(TNM_AssemblyRecipes.RecipeEntry recipe) {
        // clear
        for (int i = 0; i < displayInventory.getSizeInventory(); i++) {
            displayInventory.setInventorySlotContents(i, null);
        }


        // fill grid
        int slotIndex = 0;
        for (int row = 0; row < recipe.shape.length; row++) {
            char[] line = recipe.shape[row].toCharArray();
            for (int col = 0; col < line.length; col++) {
                ItemStack stack = recipe.mapping.get(line[col]);
                if (stack != null) {
                    // get slot object and adjust its display position
                    Slot slot = (Slot)this.slots.get(slotIndex);
                    slot.xDisplayPosition = 16 + col * 18;
                    slot.yDisplayPosition = 57 + row * 18; //+ yOffset;
                    displayInventory.setInventorySlotContents(slotIndex, stack);
                }
                slotIndex++;
            }
        }


        // output
        displayInventory.setInventorySlotContents(15, recipe.output);

        //blueprint
        displayInventory.setInventorySlotContents(16, recipe.blueprint);
    }
}
