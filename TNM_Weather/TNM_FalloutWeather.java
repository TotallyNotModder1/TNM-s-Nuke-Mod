package TNM_Weather;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

//WORK IN PROGRESS

public class TNM_FalloutWeather extends Entity{
    private int ticktrack;

    public TNM_FalloutWeather(World world) {
        super(world);
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.noClip = true;
    }

    public TNM_FalloutWeather(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y, z);
        this.ticktrack = 0;
    }
    
    protected void entityInit() {}

    @Override
    public void moveEntity(double x, double y, double z) {
        // Prevent any movement
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false; // optional, if you don’t want it selectable
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        ++ticktrack;
    
        // activate only after explosionhandler is dead
        if (ticktrack >= 1) {
            // Every 30 ticks (1.5 seconds), try to place fallout
            if (ticktrack % 10 == 0) {
                for (int i = 0; i < 50; i++) { // try 10 random spots per cycle
                    int dx = (int)(posX + (rand.nextInt(601) - 300)); // -200..+200
                    int dz = (int)(posZ + (rand.nextInt(601) - 300));
                    int y = worldObj.getHeightValue(dx, dz); // highest block at column
        
                    // Check if exposed to sky
                    if (worldObj.canBlockSeeTheSky(dx, y, dz)) {
                        int blockId = worldObj.getBlockId(dx, y, dz);
        
                        // Only place fallout on solid blocks
                        if (blockId != 0 && Block.blocksList[blockId].isBlockSolidOnSide(worldObj, dx, y, dz, 1)) {
                            int aboveId = worldObj.getBlockId(dx, y + 1, dz);
                            if (aboveId == 0) {
                                worldObj.setBlockWithNotify(dx, y + 1, dz, mod_NukeModMain.Fallout.blockID);
                            }
                        }
                    }
                }
            }
        }

        if (ticktrack >= 12000) this.setEntityDead();
    }
    
    @Override
    public boolean isEntityAlive() {
        return !this.isDead;
    }

    
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("ticktrack", this.ticktrack);
    }
    
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.ticktrack = nbt.getInteger("ticktrack");
    }
    
    
}
