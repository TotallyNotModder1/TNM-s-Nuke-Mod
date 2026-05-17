package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_WilsonCloudLarge extends Entity{
    private int setradius;
    private boolean ismini;
    private int startRadius;


    public TNM_WilsonCloudLarge(World world, double x, double y, double z, int setrad, boolean IsMini, int startRad) {
        super(world);
        this.setPosition(x, y, z);
        this.setradius = setrad;
        this.ismini = IsMini;
        this.startRadius = startRad;
    }
    

    @Override
    public void onUpdate() {
        super.onUpdate();

        int maxRadius = setradius;

        // Radius grows faster: +2 blocks per step, starting at startRadius
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

        int samples2 = 16;

        if(ismini){
            for (int i = 0; i < samples2; i++) {
                
                // theta: azimuth around XZ plane
                double theta = rand.nextDouble() * 2.0 * Math.PI;
                // phi: polar angle, restricted to upper hemisphere (0 to π/2)
                double phi = rand.nextDouble() * (Math.PI / 2);
            
                // spherical to Cartesian
                double dx = currentRadius * Math.sin(phi) * Math.cos(theta);
                double dz = currentRadius * Math.sin(phi) * Math.sin(theta);
                double dy = currentRadius * Math.cos(phi); // always positive
            
                double px = this.posX + dx;
                double py = this.posY + dy;
                double pz = this.posZ + dz;
    
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                px , py, pz,
                0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                1F, 1F, 1F,
                1,
                20F,
                false,
                15,
                true,
                false,
                0.3F, 0.3F, 0.3F,
                35F,
                true
                );
            }
        } else {
            for (int i = 0; i < 32; i++) {
                
                // theta: azimuth around XZ plane
                double theta = rand.nextDouble() * 2.0 * Math.PI;
                // phi: polar angle, restricted to upper hemisphere (0 to π/2)
                double phi = rand.nextDouble() * (Math.PI / 2);
            
                // spherical to Cartesian
                double dx = currentRadius * Math.sin(phi) * Math.cos(theta);
                double dz = currentRadius * Math.sin(phi) * Math.sin(theta);
                double dy = currentRadius * Math.cos(phi); // always positive
            
                double px = this.posX + dx;
                double py = this.posY + dy;
                double pz = this.posZ + dz;
    
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke", 
                px , py, pz,
                0.0D + worldObj.rand.nextInt(1), 0.07D, 0.0D + worldObj.rand.nextInt(1), 0.005F,
                1F, 1F, 1F,
                1,
                100F,
                false,
                15,
                true,
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
