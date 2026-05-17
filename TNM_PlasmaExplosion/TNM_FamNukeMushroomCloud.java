package TNM_PlasmaExplosion;

import java.util.Random;

import TNM_RegularNukeExplosion.TNM_StemHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_FamNukeMushroomCloud extends Entity{

    public TNM_FamNukeMushroomCloud(World world, double x, double y, double z) {
        super(world);
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {

        super.onUpdate();
        this.motionY = 0.4D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float Color = 1F;
        float Color2 = 0F;
        int samplesB = 12;
        int radius = 50; // base radius
        Random rand = worldObj.rand;
        int samples = 24; // number of particles per tick
        // Flatten factor: starts at 1.0 (perfect sphere), decreases over time
        // Example: after 200 ticks, vertical scale is ~0.2
        double flattenFactor = Math.max(0.2, 1.0 - (ticksExisted / 200.0));
        
        if (ticksExisted > 600) {
            for (int i = 0; i < 100; i++) {
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
                    0.1F, 0.1F, 0.1F,
                    1F,
                    200F,
                    true,
                    2000 + rand.nextInt(7),
                    false,
                    false,
                    0F, 0F, 0F,
                    0F,
                    false
                );
            }
            setEntityDead();
            return;
        }

        if (ticksExisted > 300){
            this.motionY = 0;
            Color = 0.1F;
            Color2 = 0.1F;
            samplesB = 6;
        }

    
        if (ticksExisted < 400){
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
                    80,
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
                200F,
                true,
                10,
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
