package net.minecraft.src;

public class TNM_Fuel extends Item {

    public TNM_Fuel(int id, int maxdamage) {
        super(id);
        this.setMaxStackSize(1); // like a bucket
        this.setMaxDamage(maxdamage);
    }

}
