package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_WilsonCloudHandler extends Entity{
    private int setradius;
    private boolean ismini;
    private boolean Ring;

    public TNM_WilsonCloudHandler(World world, double x, double y, double z, int setrad, boolean IsMini, boolean isRing) {
        super(world);
        this.setPosition(x, y, z);
        this.setradius = setrad;
        this.ismini = IsMini;
        this.Ring = isRing;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

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

        int samples2 = 24;

        if (Ring){
            for (int i = 0; i < samples2; i++) {
                double theta = this.rand.nextDouble() * 2.0 * Math.PI;
                int bx = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
                int bz = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
                int by = worldObj.getHeightValue(bx, bz);
        
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                    bx + 0.5 + worldObj.rand.nextInt(1), this.posY, bz + 0.5 + worldObj.rand.nextInt(5),
                    0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                    1F, 1F, 1F,
                    1,
                    50F,
                    false,
                    15,
                    false,
                    false,
                    0.3F, 0.3F, 0.3F,
                    35F,
                    true
                );
            }
        } else {
            if(ismini){
                for (int i = 0; i < samples2; i++) {
                    double theta = this.rand.nextDouble() * 2.0 * Math.PI;
                    int bx = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
                    int bz = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
                    int by = worldObj.getHeightValue(bx, bz);
            
                    mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                        bx + 0.5 + worldObj.rand.nextInt(1), this.posY + 25, bz + 0.5 + worldObj.rand.nextInt(5),
                        0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                        1F, 1F, 1F,
                        1,
                        20F,
                        false,
                        15,
                        false,
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
                        bx + 0.5 + worldObj.rand.nextInt(1), this.posY + 50, bz + 0.5 + worldObj.rand.nextInt(5),
                        0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                        1F, 1F, 1F,
                        1,
                        70F,
                        false,
                        15,
                        false,
                        false,
                        0.3F, 0.3F, 0.3F,
                        35F,
                        true
                    );
                }
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
