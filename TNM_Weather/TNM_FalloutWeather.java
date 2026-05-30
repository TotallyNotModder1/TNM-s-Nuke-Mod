package TNM_Weather;

import java.util.List;

import TNM_AudioManager.TNM_SoundHelper;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TNM_Trinitite;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

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
        int radius = 256;
    
        // activate only after set ticks
        if (ticktrack >= 300) {
            // Every 30 ticks (1.5 seconds), try to place fallout
            if (ticktrack % 10 == 0) {
                for (int i = 0; i < 100; i++) {

                    int dx = (int)(posX + (rand.nextInt(radius * 2 + 1) - radius));
                    int dz = (int)(posZ + (rand.nextInt(radius * 2 + 1) - radius));

                    // Reject samples outside the circle
                    int distSq = (dx - (int)posX) * (dx - (int)posX) + (dz - (int)posZ) * (dz - (int)posZ);
                    if (distSq > radius * radius) continue;

                    int y = worldObj.getHeightValue(dx, dz);
                    int blockbelow = worldObj.getBlockId(dx, y - 1, dz);
                    int blockg = worldObj.getBlockId(dx, y, dz);
                    Block belowBlock = Block.blocksList[blockbelow];

                    //place on IGrass
                    if (blockg == mod_NukeModMain.IGrass.blockID){
                        worldObj.setBlockWithNotify(dx, y + 1, dz, mod_NukeModMain.Fallout.blockID);
                    }

                    if (blockg == Block.leaves.blockID){
                        worldObj.setBlockWithNotify(dx, y, dz, 0);
                    }

                    // Check if exposed to sky and surface is solid
                    if (worldObj.canBlockSeeTheSky(dx, y, dz) &&
                        worldObj.isBlockSolidOnSide(dx, y - 1, dz, 1)) {

                        // Only place fallout if the block below is NOT Trinitite
                        if (!(belowBlock instanceof TNM_Trinitite)) {
                            worldObj.setBlockWithNotify(dx, y, dz, mod_NukeModMain.Fallout.blockID);
                        }
                    }
                }
            }
        }

        // fallout ambient effects
        if (!worldObj.multiplayerWorld) { // client-side only
            List<EntityPlayer> players = worldObj.playerEntities;
            for (EntityPlayer player : players) {
                if (isPlayerInFallout(worldObj, player)) {
                    if (ticktrack % 5 == 0) {
                        for (int i = 0; i < 15; i++) {
                            // Random offsets in a 10x10 shell around player
                            double px = player.posX + (rand.nextDouble() - 0.5) * 10.0;
                            double py = player.posY + (rand.nextDouble() - 0.5) * 10.0;
                            double pz = player.posZ + (rand.nextDouble() - 0.5) * 10.0;

                            // Spawn fallout particle at offset
                            mod_NukeModMain.spawnTNMParticle(worldObj, "Fallout",
                                px, py, pz,
                                0.0D, 0.0D, 0.0D,
                                0.005F,
                                0.72F, 0.72F, 0.72F,
                                1F,
                                1F + rand.nextInt(2),
                                false,
                                40,
                                false,
                                false,
                                0F,
                                0F,
                                0F,
                                1F,
                                true
                            );
                        }
                    }

                    //radiation and geiger
					ItemStack[] armor = player.inventory.armorInventory;
					ItemStack[] inventory = player.inventory.mainInventory;

                    int radroll = rand.nextInt(50);

                    boolean hasGeiger = false;
					for (ItemStack stack : inventory) {
						if (stack != null && stack.getItem() == mod_NukeModMain.geigercounter) {
							hasGeiger = true;
							break;
						}
					}

                    if (hasGeiger && radroll == 1) TNM_SoundHelper.playEntitySound(player, "geiger3.wav", 0.6F);

                    /* ambient sounds work in progress...

                    if (ticktrack % 10 == 0){
                        int ambientroll = rand.nextInt(100);

                        if (ambientroll == 1){
                            TNM_SoundHelper.playEntitySound(player, "wind_hit1.wav", 0.6F);
                        }
                        if (ambientroll == 2){
                            TNM_SoundHelper.playEntitySound(player, "wind_snippet3.wav", 0.6F);
                        }
                        if (ambientroll == 3){
                            TNM_SoundHelper.playEntitySound(player, "wind_snippet4.wav", 0.6F);
                        }
                    }

                    */
                }
            }
        }


        if (ticktrack >= 12000) this.setEntityDead();
    }

    public static boolean isPlayerInFallout(World world, EntityPlayer player) {
        List<TNM_FalloutWeather> list = world.getEntitiesWithinAABB(
            TNM_FalloutWeather.class,
            player.boundingBox.expand(256, 256, 256) // radius
        );
        
        for (TNM_FalloutWeather e : list) {
            if (player.getDistanceToEntity(e) < 256) return true;
        }
        return false;
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