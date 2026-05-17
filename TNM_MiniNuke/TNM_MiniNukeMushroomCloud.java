package TNM_MiniNuke;

import java.util.Random;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_MiniNukeMushroomCloud extends Entity{

    public TNM_MiniNukeMushroomCloud(World world, double x, double y, double z) {
        super(world);
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY = 0.2D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float Color = 1F;
        float Color2 = 0F;
        int samplesB = 12;

        if (ticksExisted > 200) {
            setEntityDead();
            return;
        }

        if (ticksExisted > 100){
            this.motionY = 0;
            Color = 0.3F;
            Color2 = 0.3F;
            samplesB = 6;
        }

        int radius = 10; // base radius
        Random rand = worldObj.rand;
        int samples = 12; // number of particles per tick
        // Flatten factor: starts at 1.0 (perfect sphere), decreases over time
        // Example: after 200 ticks, vertical scale is ~0.2
        double flattenFactor = Math.max(0.2, 1.0 - (ticksExisted / 200.0));
    
        if (ticksExisted < 150){
            for (int i = 0; i < samplesB; i++) {
                double theta = rand.nextDouble() * 2.0 * Math.PI; // azimuth
                double phi   = rand.nextDouble() * Math.PI;       // polar angle
        
                double dx = radius * Math.sin(phi) * Math.cos(theta);
                double dy = radius * Math.cos(phi) * flattenFactor; // vertical compressed
                double dz = radius * Math.sin(phi) * Math.sin(theta);
        
                double px = posX + dx;
                double py = posY + dy;
                double pz = posZ + dz;
        
                mod_NukeModMain.spawnTNMParticle(worldObj, "Flash",
                    px, py, pz,
                    0.0D, 0.0D, 0.0D, 0.0F,
                    1F, 1F, 0.3F,
                    1F,
                    30F,
                    true,
                    10,
                    true,
                    false,
                    0F, 0F, 0F,
                    1F,
                    false
                );
            }
        }
        for (int i = 0; i < samples; i++) {
            double theta = rand.nextDouble() * 2.0 * Math.PI; // azimuth
            double phi   = rand.nextDouble() * Math.PI;       // polar angle
    
            double dx = radius * Math.sin(phi) * Math.cos(theta);
            double dy = radius * Math.cos(phi) * flattenFactor; // vertical compressed
            double dz = radius * Math.sin(phi) * Math.sin(theta);
    
            double px = posX + dx;
            double py = posY + dy;
            double pz = posZ + dz;
    
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                0.0D, 0.0D, 0.0D, 0.0F,
                Color, Color, Color2,
                1F,
                35F,
                true,
                10,
                false,
                false,
                0F, 0F, 0F,
                0F,
                false
            );
        }

        for (int i = 0; i < 1; i++) {
            double px = this.posX + (worldObj.rand.nextDouble() * 3.0 - 1.0); // random offset -1..+1
            double py = this.posY; // spawn at entity height
            double pz = this.posZ + (worldObj.rand.nextDouble() * 3.0 - 1.0); // random offset -1..+1
            double apx = this.posX + (worldObj.rand.nextDouble() * 6.0 - 2.0); // random offset -1..+1
            double apz = this.posZ + (worldObj.rand.nextDouble() * 6.0 - 2.0);
    
            float scale = 10F + worldObj.rand.nextInt(2); // 45F ± 5
    
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                0.0D, 0.0D, 0.0D, 0.0F,   // gravity
                0.3F, 0.3F, 0.3F, // RGB
                1,                  // brightness
                scale,              // initial scale
                false,              // fullbright
                150,                 // lifespan
                false,              // scale aging
                false,              // color aging
                0.3F, 0.3F, 0.3F,   // target RGB
                5F,
                false               // target scale (same as initial)
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
