package TNM_RegularNukeExplosion;

import TNM_RegularNukeExplosion.TNM_BaseCloudHandler;
import TNM_RegularNukeExplosion.TNM_BurnWaveHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandlerOShell;
import TNM_RegularNukeExplosion.TNM_ShockwaveHandler;
import TNM_RegularNukeExplosion.TNM_StemHandler;
import TNM_RegularNukeExplosion.TNM_WilsonCloudHandler;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_ExplosionHandler extends Entity{
    private int tickCounter = 0;

    public TNM_ExplosionHandler(World world, double x, double y, double z) {
        super(world);
        this.setPosition(x, y, z);
    }


    @Override
    public void onUpdate() {
        double py = worldObj.getHeightValue((int)this.posX, (int)this.posZ);;
        TNM_BaseCloudHandler basecloud1 = new TNM_BaseCloudHandler(worldObj, this.posX, this.posY, this.posZ, true, 65);
        TNM_BaseCloudHandler basecloud2 = new TNM_BaseCloudHandler(worldObj, this.posX, this.posY, this.posZ, false, 65);
        TNM_ShockwaveHandler Shockwave = new TNM_ShockwaveHandler(worldObj, this.posX, this.posY, this.posZ, 150, false);
        TNM_MHeadHandler MushroomHead = new TNM_MHeadHandler(worldObj, this.posX, this.posY, this.posZ, 1);
        TNM_MHeadHandlerOShell MushroomHeadShell = new TNM_MHeadHandlerOShell(worldObj, this.posX, this.posY, this.posZ, 1, true);
        TNM_StemHandler Stem = new TNM_StemHandler(worldObj, this.posX, py, this.posZ, 1, false);
        TNM_BurnWaveHandler Burnwave = new TNM_BurnWaveHandler(worldObj, this.posX, this.posY, this.posZ, 62);
        TNM_WilsonCloudHandler Wilson = new TNM_WilsonCloudHandler(worldObj, this.posX, this.posY, this.posZ, 55, false, false);
        TNM_WilsonCloudHandler Wilson2 = new TNM_WilsonCloudHandler(worldObj, this.posX, this.posY + 70, this.posZ, 65, false, true);
        TNM_EntityCustomFX effect = new TNM_EntityCustomFX(worldObj, posX, posY, posZ,
        0, 0.2D, 0, 30, 0, 100, 100, 1000);

        TNM_EntityCustomFX effect2 = new TNM_EntityCustomFX(worldObj, posX, posY + 1, posZ,
        0, 0.2D, 0, 400, 0, 200, 5, 100);

        ++tickCounter;
    
        if (tickCounter > 1) {

            if (tickCounter == 2) {
                mod_NukeModMain.triggerNukeBlastStep1(worldObj, (int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ));
                worldObj.entityJoinedWorld(effect);
                worldObj.entityJoinedWorld(effect2);
            }
            if (tickCounter == 20){
                worldObj.entityJoinedWorld(basecloud1);
                mod_NukeModMain.triggerNukeBlastStep2(worldObj, (int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ), 60, 0.5);
                worldObj.entityJoinedWorld(Burnwave);
            }
            if (tickCounter == 30){
                worldObj.entityJoinedWorld(MushroomHead);
                mod_NukeModMain.triggerNukeBlastStep2(worldObj, (int)Math.floor(this.posX), (int)Math.floor(this.posY) + 2, (int)Math.floor(this.posZ), 80, 0.5);
            }
            if (tickCounter == 31){
                worldObj.entityJoinedWorld(basecloud2);
                mod_NukeModMain.triggerNukeBlastStep2(worldObj, (int)Math.floor(this.posX), (int)Math.floor(this.posY) + 4, (int)Math.floor(this.posZ), 85, 0.5);
                mod_NukeModMain.triggerNukeCrater(worldObj, (int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ), 20, 35, 0.3);
                worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 2000.0F, 0.05F + rand.nextFloat() * 0.2F);
                worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2000.0F, 0.05F + rand.nextFloat() * 0.2F);
            }
            if (tickCounter == 40){
                worldObj.entityJoinedWorld(Shockwave);
                worldObj.entityJoinedWorld(MushroomHeadShell);      
            }
            if (tickCounter == 50){
                worldObj.entityJoinedWorld(Wilson);    
                worldObj.entityJoinedWorld(Stem);   
            }
            if (tickCounter == 200){
                worldObj.entityJoinedWorld(Wilson2);
            }
            // Kill the controller when done
            if (tickCounter > 600) {
                this.setEntityDead();
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
