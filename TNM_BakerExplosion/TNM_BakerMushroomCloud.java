package TNM_BakerExplosion;

import java.util.Random;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_BakerMushroomCloud extends Entity{

    public TNM_BakerMushroomCloud(World world, double x, double y, double z) {
        super(world);
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    
        if (ticksExisted > 35) {
            setEntityDead();
            return;
        }
    
        Random rand = worldObj.rand;
        int radius = 15;   // cylinder radius
        int height = 15;   // cylinder height
        int samples = 4;  // particles per tick
    
        // --- Cylinder shell particles ---
        for (int i = 0; i < samples; i++) {
            double theta = rand.nextDouble() * 2.0 * Math.PI; // angle around circle
            double h     = rand.nextDouble() * height;        // random height along cylinder
    
            double dx = radius * Math.cos(theta);
            double dz = radius * Math.sin(theta);
            double dy = h;
    
            double px = posX + dx;
            double py = posY + dy;
            double pz = posZ + dz;
    
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                0.0D, 0.7D, 0.0D, 0.01F,   // upward velocity
                0.3F, 0.3F, 0.3F,
                1F,
                100F+ rand.nextInt(50),
                true,
                900 + rand.nextInt(5),
                false,
                false,
                0F, 0F, 0F,
                0F,
                false
            );
        }
    
        // --- Center plume particles ---
        for (int i = 0; i < 2; i++) {
            double px = posX + (rand.nextDouble() * 0.2 - 0.1); // tiny X offset
            double py = posY;
            double pz = posZ + (rand.nextDouble() * 0.2 - 0.1); // tiny Z offset
        
            // Upward velocity between 0.0 and 0.2
            double vy = rand.nextDouble() * 0.65D;
        
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                0.0D, vy, 0.0D, 0.01F,   // gravity
                0.95F + rand.nextInt(1), 0.95F+ rand.nextInt(1), 0.95F + rand.nextInt(1),        // RGB
                1F,
                60F + rand.nextInt(20),  // scale
                true,
                900 + rand.nextInt(5),   // lifespan
                false,
                false,
                0F, 0F, 0F,
                0F,
                false
            );
        }
        
    }
    


    @Override
    protected void entityInit() {}
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
}
