package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_StemHandler extends Entity{
    private float sizemultiplier;
    private boolean mega;
    private int cooldown = 0;

    public TNM_StemHandler(World world, double x, double y, double z, float size_multiplier, boolean Mega) {
        super(world);
        this.setPosition(x, y - 2, z);
        this.sizemultiplier = size_multiplier;

        this.mega = Mega;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        ++ticksExisted;
        
        if (!mega){
            for (int i = 0; i < 25; i++) {
                double px = this.posX + (rand.nextDouble() * 0.6 - 0.3);
                double py = this.posY;
                double pz = this.posZ + (rand.nextDouble() * 0.6 - 0.3);

                double vel = rand.nextDouble() * 0.2D * Math.sqrt(sizemultiplier);
                float scale = (float)(70F + rand.nextInt(10) * Math.sqrt(sizemultiplier));
            
                mod_NukeModMain.spawnStemparticle(
                    worldObj, "Smoke", px, py, pz, 
                    0.0D, vel, 0.0D,      // 0.2D velocity
                    scale, 2000 + rand.nextInt(7), 930,       // Scale, Lifespan, Freeze Tick
                    false,                 // dragOn = false
                    0.3F, 0.3F, 0.3F
                );
            }

            for (int i = 0; i < 25; i++) {
                double px = this.posX + (rand.nextDouble() * 0.6 - 0.3);
                double py = this.posY;
                double pz = this.posZ + (rand.nextDouble() * 0.6 - 0.3);

                double vel = rand.nextDouble() * 0.15D * Math.sqrt(sizemultiplier);
                float scale = (float)(70F + rand.nextInt(10) * Math.sqrt(sizemultiplier));
            
                mod_NukeModMain.spawnStemparticle(
                    worldObj, "Smoke", px, py, pz, 
                    0.0D, vel, 0.0D,      // 0.2D velocity
                    scale, 2000 + rand.nextInt(7), 930,       // Scale, Lifespan, Freeze Tick
                    false,                 // dragOn = false
                    0.3F, 0.3F, 0.3F
                );
            }
        } else {
            for (int i = 0; i < 25; i++) {
                double px = this.posX + (rand.nextDouble() * 0.6 - 0.3);
                double py = this.posY;
                double pz = this.posZ + (rand.nextDouble() * 0.6 - 0.3);
    
                double vel = rand.nextDouble() * 0.4D;
                float scale = (float)(70F + rand.nextInt(10) * Math.sqrt(sizemultiplier));
            
                mod_NukeModMain.spawnStemparticle(
                    worldObj, "Smoke", px, py, pz, 
                    0.0D, vel, 0.0D,      // 0.2D velocity
                    scale, 2000 + rand.nextInt(7), 600,       // Scale, Lifespan, Freeze Tick
                    false,                 // dragOn = false
                    0.3F, 0.3F, 0.3F
                );
            }
    
            for (int i = 0; i < 25; i++) {
                double px = this.posX + (rand.nextDouble() * 0.6 - 0.3);
                double py = this.posY;
                double pz = this.posZ + (rand.nextDouble() * 0.6 - 0.3);
    
                double vel = rand.nextDouble() * 0.25D;
                float scale = (float)(70F + rand.nextInt(10) * Math.sqrt(sizemultiplier));
            
                mod_NukeModMain.spawnStemparticle(
                    worldObj, "Smoke", px, py, pz, 
                    0.0D, vel, 0.0D,      // 0.2D velocity
                    scale, 2000 + rand.nextInt(7), 600,       // Scale, Lifespan, Freeze Tick
                    false,                 // dragOn = false
                    0.1F, 0.1F, 0.1F
                );
            }
        }

        if (ticksExisted > 2){
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