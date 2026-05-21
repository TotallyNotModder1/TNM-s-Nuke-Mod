package TNM_RecipeBookClasses;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class TNM_RecipeBookItem extends Item { 

    public TNM_RecipeBookItem(int id){
        super(id);
        this.setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.multiplayerWorld) {
            ModLoader.OpenGUI(player, new TNM_GuiRecipes());
        }
        return stack;
    }
}
