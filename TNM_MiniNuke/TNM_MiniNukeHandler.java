package TNM_MiniNuke;

import TNM_RegularNukeExplosion.TNM_BaseCloudHandler;
import TNM_RegularNukeExplosion.TNM_BurnWaveHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandlerOShell;
import TNM_RegularNukeExplosion.TNM_ShockwaveHandler;
import TNM_RegularNukeExplosion.TNM_StemHandler;
import TNM_RegularNukeExplosion.TNM_WilsonCloudHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TNM_NuclearExplosionBlast;
import net.minecraft.src.TNM_NuclearExplosionDamage;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_MiniNukeHandler extends Entity{
    private int tickCounter = 0;

    public TNM_MiniNukeHandler(World world, double x, double y, double z) {
        super(world);
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        TNM_ShockwaveHandler Shockwave = new TNM_ShockwaveHandler(worldObj, this.posX, this.posY, this.posZ, 35, true);
        TNM_WilsonCloudHandler Wilson = new TNM_WilsonCloudHandler(worldObj, this.posX, this.posY, this.posZ, 35, true, false);
        TNM_BurnWaveHandler Burnwave = new TNM_BurnWaveHandler(worldObj, this.posX, this.posY, this.posZ, 15);
        TNM_MiniNukeMushroomCloud Mushroomcloud = new TNM_MiniNukeMushroomCloud(worldObj, this.posX, this.posY, this.posZ);
        if (ticksExisted == 1){
                mod_NukeModMain.spawnTNMParticle(worldObj, "Flash", 
                posX, posY, posZ,   
                0.0D, 0.0D, 0.0D, 0.00F,
                1F,     //R
                1F,   //G
                1F,   //B
                1F,        
                500,        
                true,     
                10,       
                true,     
                false,      
                0.1F, 0.0F, 0.0F, //Target RGB
                0F,
                false
            );
            mod_NukeModMain.triggerNukeCrater(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 5, 10);
            mod_NukeModMain.triggerNukeBlastStep2(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 30);
        }  
        if (ticksExisted == 10){
            worldObj.entityJoinedWorld(Burnwave);
            worldObj.entityJoinedWorld(Shockwave);
        }
        if (ticksExisted == 15){
            TNM_NuclearExplosionDamage.NuclearDamage(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 35, 15, 35);
            worldObj.entityJoinedWorld(Mushroomcloud);
        }
        if (ticksExisted == 25){
            worldObj.entityJoinedWorld(Wilson);
        }
        if (ticksExisted > 1 && ticksExisted < 20){
            worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 50.0F, 0.05F + rand.nextFloat() * 0.2F);
        }
        if (ticksExisted >= 40){
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
