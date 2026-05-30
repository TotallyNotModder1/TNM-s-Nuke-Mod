package TNM_RecipeBookClasses;

import TNM_AudioManager.TNM_SoundHelper;
import TNM_Weather.TNM_FalloutWeather;
import TNM_Weather.TNM_RadiationManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_Dosimeter extends Item { 
    private final TNM_RadiationManager manager;

    public TNM_Dosimeter(int id, TNM_RadiationManager manager){
        super(id);
        this.setMaxStackSize(1);
        this.manager = manager;
    }



    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        // open dosimeter
        if (!world.multiplayerWorld) {
            ModLoader.OpenGUI(player, new TNM_GuiDosimeter(player, manager));
        }
        TNM_SoundHelper.playEntitySound(player, "zoom.wav", 1.0F);

        return stack; // item was successfully used
    }
}
