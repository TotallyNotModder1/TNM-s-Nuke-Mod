package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_BurnWaveHandler extends Entity{
    private int setradius;

    public TNM_BurnWaveHandler(World world, double x, double y, double z, int setrad) {
        super(world);
        this.setPosition(x, y, z);
        this.setradius = setrad;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    
        int maxRadius = setradius;
        if (this.ticksExisted % 3 != 0) {
            return;
        }

        // Radius grows faster: +2 blocks per step
        int currentRadius = (this.ticksExisted / 3) * 2;
        if (currentRadius > maxRadius) {
            this.setEntityDead();
            return;
        }
    
        // Cover the circumference at this radius
        int samples = 16;
        for (int i = 0; i < samples; i++) {
            double theta = this.rand.nextDouble() * 2.0 * Math.PI;
            int bx = (int)Math.floor(this.posX + currentRadius * Math.cos(theta));
            int bz = (int)Math.floor(this.posZ + currentRadius * Math.sin(theta));
            int by = worldObj.getHeightValue(bx, bz);
    
            // Apply burn only to the surface block
            mod_NukeModMain.ShockWaveTypes(worldObj, "Burn", bx, by, bz);
        }

        if (ticksExisted > 600) {
            this.setEntityDead();
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
