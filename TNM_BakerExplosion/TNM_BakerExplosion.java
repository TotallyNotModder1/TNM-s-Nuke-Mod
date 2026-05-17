package TNM_BakerExplosion;

import TNM_RegularNukeExplosion.TNM_BurnWaveHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandlerOShell;
import TNM_RegularNukeExplosion.TNM_ShockwaveHandler;
import TNM_RegularNukeExplosion.TNM_StemHandler;
import TNM_RegularNukeExplosion.TNM_WilsonCloudHandler;
import TNM_RegularNukeExplosion.TNM_WilsonCloudLarge;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TNM_NuclearExplosionDamage;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_BakerExplosion extends Entity{

    public TNM_BakerExplosion(World world, double x, double y, double z) {
        super(world);
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        TNM_ShockwaveHandler Shockwave = new TNM_ShockwaveHandler(worldObj, this.posX, this.posY, this.posZ, 60, false);
        TNM_WilsonCloudHandler Wilson = new TNM_WilsonCloudHandler(worldObj, this.posX, this.posY, this.posZ, 45, false, false);
        TNM_BurnWaveHandler Burnwave = new TNM_BurnWaveHandler(worldObj, this.posX, this.posY, this.posZ, 60);
        TNM_StemHandler stem = new TNM_StemHandler(worldObj, this.posX, this.posY, this.posZ, 0.8F, false);
        TNM_MHeadHandler shell1 = new TNM_MHeadHandler(worldObj, this.posX, this.posY, this.posZ, 0.8F);
        TNM_MHeadHandlerOShell shell2 = new TNM_MHeadHandlerOShell(worldObj, this.posX, this.posY, this.posZ, 0.8F, false);
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
            mod_NukeModMain.triggerNukeBlastStep2(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 50);
            TNM_NuclearExplosionDamage.NuclearDamage(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 35, 35, 0);
        }  
        if (ticksExisted == 10){
            mod_NukeModMain.triggerNukeCrater(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 20, 25);
            worldObj.entityJoinedWorld(Burnwave);
            worldObj.entityJoinedWorld(Shockwave);
            worldObj.entityJoinedWorld(Wilson);
        }
        if (ticksExisted == 15){
            TNM_NuclearExplosionDamage.NuclearDamage(worldObj, (int)this.posX, (int)this.posY, (int)this.posZ, 0, 35, 0);
            worldObj.entityJoinedWorld(shell1);
            worldObj.entityJoinedWorld(shell2);
            worldObj.entityJoinedWorld(stem);
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
