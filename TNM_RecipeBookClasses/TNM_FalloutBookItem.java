package TNM_RecipeBookClasses;

import TNM_Weather.TNM_FalloutWeather;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class TNM_FalloutBookItem extends Item { 

    public TNM_FalloutBookItem(int id){
        super(id);
        this.setMaxStackSize(1);
    }

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side) {
        TNM_FalloutWeather falloutWeather = new TNM_FalloutWeather(world, x, y, z);
        world.entityJoinedWorld(falloutWeather);
		return false;
	}
}
