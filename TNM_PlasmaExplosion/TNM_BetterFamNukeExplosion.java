package TNM_PlasmaExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

import java.util.List;

import TNM_RegularNukeExplosion.TNM_StemHandler;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;

public class TNM_BetterFamNukeExplosion extends Entity {
    private int setradius;
    private int currentRadius = 0;
    private int currentfillradius = 0;
    

    public TNM_BetterFamNukeExplosion(World world, double x, double y, double z, int setrad) {
        super(world);
        this.setPosition(x, y, z);
        this.setradius = setrad;
        this.NuclearDamage(world, (int)x, (int)y, (int)z);
    }

    
    public void NuclearDamage(World world, int x, int y, int z){
        int damageRadius = setradius + 20;

        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(
            null,
            AxisAlignedBB.getBoundingBox(
                x - damageRadius, y - damageRadius, z - damageRadius,
                x + damageRadius, y + damageRadius, z + damageRadius
            )
        );

        for (Entity e : entities) {
            if (e instanceof EntityLiving) {
                double dist = e.getDistance(x, y, z);
                if (dist <= damageRadius) {
                    ((EntityLiving)e).attackEntityFrom(null, 100);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        TNM_FamNukeMushroomCloud MushroomHead = new TNM_FamNukeMushroomCloud(worldObj, this.posX, this.posY - 50, this.posZ);
        TNM_StemHandler Stem = new TNM_StemHandler(worldObj, this.posX, this.posY - 50, this.posZ, 1, true);
        // Expand every 2 ticks
        if (ticksExisted % 1 != 0) return;

        currentRadius++;
        if (currentRadius > setradius) {
            worldObj.entityJoinedWorld(Stem);
            setEntityDead();
            return;
        }

        // --- Shell 1: delete blocks except bedrock ---
        int r = currentRadius;
        int R = currentRadius + 1;
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    int distSq = dx*dx + dy*dy + dz*dz;
                    if (distSq <= r*r && distSq > (r-1)*(r-1)) {
                        int bx = (int)posX + dx;
                        int by = (int)posY + dy;
                        int bz = (int)posZ + dz;

                        int id = worldObj.getBlockId(bx, by, bz);

                        if (id != 0 && id != Block.bedrock.blockID) {
                            worldObj.setBlock(bx, by, bz, 0);
                        }
                        if (id != 0 && id != Block.bedrock.blockID) {
                            worldObj.setBlock((int)posX, (int)posY, (int)posZ, 0);
                        }
                    }
                }
            }
        }

        for (int dx = -R; dx <= R; dx++) {
            for (int dy = -R; dy <= R; dy++) {
                for (int dz = -R; dz <= R; dz++) {
                    int distSq = dx*dx + dy*dy + dz*dz;
                    if (distSq <= R*R && distSq > (R-1)*(R-1)) {
                        int bx = (int)posX + dx;
                        int by = (int)posY + dy;
                        int bz = (int)posZ + dz;

                        int id = worldObj.getBlockId(bx, by, bz);
                        int roll = worldObj.rand.nextInt(4);

                        if (id != 0 && id != Block.bedrock.blockID) {
                            if (roll == 1){
                                worldObj.setBlock(bx, by, bz, 0);
                            }
                        }
                    }
                }
            }
        }

        if (this.ticksExisted == 1){
            worldObj.entityJoinedWorld(MushroomHead);
        }
        if (this.ticksExisted > 1 && this.ticksExisted < 20){
            worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 200.0F, 0.05F + rand.nextFloat() * 0.2F);
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
