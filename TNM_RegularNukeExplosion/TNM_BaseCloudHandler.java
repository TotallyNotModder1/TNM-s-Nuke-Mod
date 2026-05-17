package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_BaseCloudHandler extends Entity{
    private boolean cold;
    private int sizemultiplier;

    public TNM_BaseCloudHandler(World world, double x, double y, double z, boolean iscold, int size) {
        super(world);
        this.setPosition(x, y, z);
        this.cold = iscold;
        this.sizemultiplier = size;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    
        int maxRadius = sizemultiplier; //55;
        int currentRadius = this.ticksExisted;
        if (currentRadius > maxRadius) {
            this.setEntityDead();
            return;
        }
    
        int samples = 12; // keep it small per tick
    
        for (int i = 0; i < samples; i++) {
            double theta = this.rand.nextDouble() * 2.0 * Math.PI;
    
            int x = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
            int z = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
    
            int y = worldObj.getHeightValue(x, z);
    
            // Always spawn above the terrain surface
            if (cold){
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smokepuff", 
                x + 0.5 + worldObj.rand.nextInt(1), y + 1, z + 0.5 + worldObj.rand.nextInt(5),   // origin
                    0.0D, 0.01D, 0.0D, 0.0F,
                    0.3F,
                    0.3F,
                    0.3F,
                    1,
                    10 + worldObj.rand.nextInt(10),
                    false,
                    2000 + worldObj.rand.nextInt(5),
                    true,
                    false,
                    0.3F,
                    0.3F,
                    0.3F,
                    70F - worldObj.rand.nextInt(20),
                    true
                );
            } else {
                // Particle spawn position
                double px = x + 0.5 + worldObj.rand.nextInt(1);
                double py = y + 1;
                double pz = z + 0.5 + worldObj.rand.nextInt(5);

                // Direction vector from particle to entity origin
                double dx = this.posX - px;
                double dy = this.posY - py;
                double dz = this.posZ - pz;

                // Normalize
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist > 0.0001D) {
                    dx /= dist;
                    dy /= dist;
                    dz /= dist;
                }

                // Scale velocity (tweak multiplier for strength)
                double vel = 0.2D + worldObj.rand.nextDouble() * 0.5D; // 0.2–0.3 range

                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                    px, py, pz,
                    dx * vel, 0.0D, dz * vel, 0.0F,   // velocity vector toward origin
                    1F,
                    0.5F,
                    0.0F,
                    1F,
                    10 + worldObj.rand.nextInt(10),
                    true,
                    400 + worldObj.rand.nextInt(5),
                    true,
                    true,
                    0.1F,
                    0.1F,
                    0.1F,
                    80F - worldObj.rand.nextInt(20),
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
