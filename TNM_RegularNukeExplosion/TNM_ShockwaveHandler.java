package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_ShockwaveHandler extends Entity{
    private int setradius;
    private boolean ismini;

    public TNM_ShockwaveHandler(World world, double x, double y, double z, int setrad, boolean IsMini) {
        super(world);
        this.setPosition(x, y, z);
        this.setradius = setrad;
        this.ismini = IsMini;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    
        worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10.0F, 0.5F + rand.nextFloat() * 0.2F);
        int maxRadius = setradius;
    
        // Only expand every 3 ticks
        if (this.ticksExisted % 4 != 0) {
            return;
        }

        // Radius grows faster: +2 blocks per step
        int currentRadius = (this.ticksExisted / 4) * 2;
        if (currentRadius > maxRadius) {
            this.setEntityDead();
            return;
        }
    
        int samples = 3;
        int samples2 = 16;
    
        for (int i = 0; i < samples; i++) {
            double theta = this.rand.nextDouble() * 2.0 * Math.PI;
            int bx = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
            int bz = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
            int by = worldObj.getHeightValue(bx, bz);
    
            mod_NukeModMain.ShockWaveTypes(worldObj,"Foliage", bx, by, bz);
        }
    
        for (int i = 0; i < samples2; i++) {
            double theta = this.rand.nextDouble() * 2.0 * Math.PI;
            int bx = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
            int bz = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
            int by = worldObj.getHeightValue(bx, bz);
    
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                bx + 0.5 + worldObj.rand.nextInt(1), by + 1, bz + 0.5 + worldObj.rand.nextInt(5),
                0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                0.5F, 0.5F, 0.5F,
                1,
                5,
                false,
                40 + worldObj.rand.nextInt(5),
                true,
                false,
                0.3F, 0.3F, 0.3F,
                35F,
                true
            );
        }

        if(ismini){
            for (int i = 0; i < samples2; i++) {
                double theta = this.rand.nextDouble() * 2.0 * Math.PI;
                int bx = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
                int bz = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
                int by = worldObj.getHeightValue(bx, bz);
        
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                    bx + 0.5 + worldObj.rand.nextInt(1), this.posY + 15, bz + 0.5 + worldObj.rand.nextInt(5),
                    0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                    1F, 1F, 1F,
                    1,
                    20F,
                    false,
                    30,
                    true,
                    false,
                    0.3F, 0.3F, 0.3F,
                    35F,
                    true
                );
            }
        } else {
            for (int i = 0; i < 32; i++) {
                double theta = this.rand.nextDouble() * 2.0 * Math.PI;
                int bx = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
                int bz = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
                int by = worldObj.getHeightValue(bx, bz);
        
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                    bx + 0.5 + worldObj.rand.nextInt(1), this.posY + 40, bz + 0.5 + worldObj.rand.nextInt(5),
                    0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                    1F, 1F, 1F,
                    1,
                    70F,
                    false,
                    30,
                    false,
                    false,
                    0.3F, 0.3F, 0.3F,
                    35F,
                    true
                );
            }
        }
    }
    
    
    @Override
    protected void entityInit() {}
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {

    }
}
