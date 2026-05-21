package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class TNM_BurnLoosen {

    public TNM_BurnLoosen(World world, int x, int y, int z){
        this.createCrater(world, x, y, z);
        this.ShockwaveDamage(world, x, y, z);
        this.pulverize(world, x, y, z);
    }

    public void createCrater(World world, int x, int y, int z) {
        int radius = 10;
        double fuzziness = 0.5;
    
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int distSq = dx*dx + dy*dy + dz*dz;
                    double dist2 = Math.sqrt(distSq);
                    double edgeFactor = dist2 / radius; // 0 at center, 1 at edge
                    if (edgeFactor < 0.8 || world.rand.nextDouble() > fuzziness) {
                        if (distSq <= radius*radius) {
                            int id = world.getBlockId(x + dx, y + dy, z + dz);
                            if (id != Block.bedrock.blockID) {
                                Block block = Block.blocksList[id];
                                if (block instanceof BlockLog || id == mod_NukeModMain.NukedLog.blockID){
                                    world.setBlock(x + dx, y + dy, z + dz, 0);
                                }
                                if (block instanceof BlockGrass || id == mod_NukeModMain.IGrass.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                }
                                if (block instanceof BlockSand || block instanceof BlockDirt || id == mod_NukeModMain.NukeDirt.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.Trinitite.blockID);
                                }
                                if (id == Block.oreCoal.blockID){
                                    if (world.rand.nextBoolean()){
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.NukeDiamonds.blockID);
                                    } else {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.PulverizedStone.blockID);
                                    }
                                }
                                if (block instanceof BlockOre && id != Block.oreCoal.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.SlagBlock.blockID);
                                }
                                if (block instanceof BlockOreStorage) {
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.SlagBlock.blockID);
                                }
                                if (id == Block.stone.blockID || id == Block.cobblestone.blockID || id == Block.sandStone.blockID) {
                                    if (world.rand.nextBoolean()) {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.ScorchedStone.blockID);
                                    } else {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.PulverizedStone.blockID);
                                    }
                                }
                                if (id == Block.obsidian.blockID) {
                                    // Remove the original log
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                
                                    // Spawn a falling burnt log entity one block higher
                                    EntityFallingSand falling = new EntityFallingSand(
                                        world,
                                        (double)(x + dx) + 0.5D,
                                        (double)(y + dy + 1) + 0.5D,
                                        (double)(z + dz) + 0.5D,
                                        mod_NukeModMain.ShattOb.blockID
                                    );
                                
                                    // Compute direction away from blast center
                                    double dirX = (x + dx) - x;
                                    double dirY = (y + dy) - y;
                                    double dirZ = (z + dz) - z;
                                    double dist = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                                    if (dist > 0.001D) {
                                        dirX /= dist;
                                        dirY /= dist;
                                        dirZ /= dist;
                                    }
                                
                                    // Give upward + outward velocity
                                    double strength = 1D + world.rand.nextInt(4); // tweak multiplier
                                    falling.motionX = dirX * strength;
                                    falling.motionY = 0.4D + (dirY * 0.2D); // always some upward push
                                    falling.motionZ = dirZ * strength;
                                
                                    world.entityJoinedWorld(falling);
                                }
                            }
                        }
                    }
                }
            }
        } 
    }

    public void pulverize(World world, int x, int y, int z) {
        int radius = 12;
    
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int distSq = dx*dx + dy*dy + dz*dz;
                    if (distSq <= radius*radius) {
                        int id = world.getBlockId(x + dx, y + dy, z + dz);
                        if (id != Block.bedrock.blockID) {
                            if (id == Block.stone.blockID){
                                if (world.rand.nextInt(100) > 50) {
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.cobblestone.blockID);
                                }
                            }
                        }
                    }
                }
            }
        } 
    }


    public void ShockwaveDamage(World world, int x, int y, int z) {
        int damageRadius = 6;
    
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(
            null,
            AxisAlignedBB.getBoundingBox(
                x - damageRadius, y - damageRadius, z - damageRadius,
                x + damageRadius, y + damageRadius, z + damageRadius
            )
        );
    
        for (Entity e : entities) {
            if (e instanceof EntityLiving) {
                double dx = e.posX - x;
                double dy = e.posY - y;
                double dz = e.posZ - z;
    
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist <= damageRadius && dist > 0.01) {
                    // Damage
                    ((EntityLiving)e).attackEntityFrom(null, 50);
                    ((EntityLiving)e).setOnFireFromLava();

                    // Normalize direction
                    dx /= dist;
                    dy /= dist;
                    dz /= dist;
    
                    // Knockback strength (tweak multiplier)
                    double strength = 1.5D * (1.0D - (dist / damageRadius));
    
                    // Apply velocity directly
                    e.motionX += dx * strength;
                    e.motionY += dy * strength * 0.5; // less vertical push
                    e.motionZ += dz * strength;
                }
            }
            if (e instanceof EntityItem){
                double dx = e.posX - x;
                double dy = e.posY - y;
                double dz = e.posZ - z;
    
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist <= damageRadius && dist > 0.01) {
                    ((EntityItem)e).setEntityDead();
                }
            }
        }
    }
}