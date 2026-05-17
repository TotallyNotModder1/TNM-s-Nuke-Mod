package net.minecraft.src;

import forge.IArmorTextureProvider;

public class TNM_HazmatArmor extends ItemArmor implements IArmorTextureProvider {

    public TNM_HazmatArmor(int id, int level, int renderIndex, int slot) {
        super(id, 0, renderIndex, slot);
    }
    
    @Override
    public String getArmorTextureFile() {
        // armorType 2 is for Leggings (Layer 2)
        if (this.armorType == 2) {
            return "/NukeTex/armor/hazmatsuit_2.png";
        }
        // All other pieces use Layer 1
        return "/NukeTex/armor/hazmatsuit_1.png";
    }

}
