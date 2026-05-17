package TNM_PlasmaExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;

public class TNM_PlasmaExplosion extends Entity {
    private int setradius;
    private int currentRadius = 0;

    public TNM_PlasmaExplosion(World world, double x, double y, double z, int setrad) {
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

        // Expand every 2 ticks
        if (ticksExisted % 1 != 0) return;

        currentRadius++;
        if (currentRadius > setradius) {
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
                        int roll = worldObj.rand.nextInt(6);

                        if (id != 0 && id != Block.bedrock.blockID) {
                            if (roll == 1){
                                worldObj.setBlock(bx, by, bz, 0);
                            }
                        }
                    }
                }
            }
        }

        Random rand = worldObj.rand;
        int samples = 4; // number of particles per shell
        int samplesB = 10;
    
        for (int i = 0; i < samples; i++) {
            double theta = rand.nextDouble() * 2.0 * Math.PI; // azimuth
            double phi   = rand.nextDouble() * Math.PI;       // polar angle
    
            double dx = currentRadius * Math.sin(phi) * Math.cos(theta);
            double dy = currentRadius * Math.cos(phi);
            double dz = currentRadius * Math.sin(phi) * Math.sin(theta);
    
            double px = posX + dx;
            double py = posY + dy;
            double pz = posZ + dz;
    
            // Spawn your custom particle at this surface point
            mod_NukeModMain.spawnTNMParticle(worldObj, "Flash",
                px, py, pz,
                0.0D, 0.0D, 0.0D, 0.0F,
                0.5F, 0.5F, 1F,
                1F,
                70F + worldObj.rand.nextInt(10),
                true,
                100,
                true,
                false,
                0F, 0F, 0F,
                0F,
                false
            );
            worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 50.0F, 0.05F + rand.nextFloat() * 0.2F);
        }
        for (int i = 0; i < samplesB; i++) {
            double theta = rand.nextDouble() * 2.0 * Math.PI; // azimuth
            double phi   = rand.nextDouble() * Math.PI;       // polar angle
    
            double dx = currentRadius * Math.sin(phi) * Math.cos(theta);
            double dy = currentRadius * Math.cos(phi);
            double dz = currentRadius * Math.sin(phi) * Math.sin(theta);
    
            double px = posX + dx;
            double py = posY + dy;
            double pz = posZ + dz;
    
            // Spawn your custom particle at this surface point
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                0.0D, 0.0D, 0.0D, 0.0F,
                0.7F, 0.4F, 1F,
                0.7F,
                40F + worldObj.rand.nextInt(10),
                true,
                400,
                true,
                false,
                0F, 0F, 0F,
                0F,
                false
            );
        }
    }

    @Override
    protected void entityInit() {}
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {

    }
}
