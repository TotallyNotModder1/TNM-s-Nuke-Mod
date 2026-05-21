package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class TNM_LeafKiller {

    public TNM_LeafKiller(World world, int x, int y, int z){
        this.createCrater(world, x, y, z);
        this.ShockwaveDamage(world, x, y, z);
    }

    public void createCrater(World world, int x, int y, int z) {
        int radius = 30;
        Random rand = world.rand;
        double fuzziness = 0.5;
    
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int distSq = dx*dx + dy*dy + dz*dz;
                    if (distSq <= radius*radius) {
                        int id = world.getBlockId(x + dx, y + dy, z + dz);
                        double dist = Math.sqrt(distSq);
                        double edgeFactor = dist / radius; // 0 at center, 1 at edge
                        if (edgeFactor < 0.8 || world.rand.nextDouble() > fuzziness) {
                            if (id != Block.bedrock.blockID) {
                                Block block = Block.blocksList[id];
                                if (id == Block.leaves.blockID || id == Block.snow.blockID || id == Block.reed.blockID || id == Block.cactus.blockID || block instanceof BlockStairs || id == Block.stairSingle.blockID || id == Block.redstoneWire.blockID || id == Block.torchWood.blockID || block instanceof BlockRedstoneTorch || block instanceof BlockRedstoneRepeater || id == Block.fence.blockID || id == Block.doorWood.blockID || id == Block.trapdoor.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                }
                                if (id == Block.ice.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.waterStill.blockID);
                                }
                                if (id == Block.grass.blockID){
                                    int roll = world.rand.nextInt(150);
                                    if (roll == 1) {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.Trinitite.blockID);
                                    }
                                    else if (roll > 10 && roll < 20){
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.IGrass.blockID);
                                        if (world.getBlockId(x + dx, y + dy + 1, z + dz) == 0){
                                            world.setBlockWithNotify(x + dx, y + 1 + dy, z + dz, Block.fire.blockID);
                                        }
                                    } else {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.IGrass.blockID);
                                    }
                                }
                                if (id == Block.dirt.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.NukeDirt.blockID);
                                }
                                if (id == Block.stairDouble.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.cobblestone.blockID);
                                }
                                if (id == Block.glass.blockID || id == Block.glowStone.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                    int roll = world.rand.nextInt(10);
                                    if (roll == 1) {
                                        world.playSoundEffect(x + dx, y + dy, z + dz, "random.glass", 3.0F, 1F + rand.nextFloat() * 0.2F);
                                    }
                                }
                                if (id == Block.cobblestone.blockID) {
                                    int roll = world.rand.nextInt(70);
                                    if (roll == 1) {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.stone.blockID);
                                    } else if (roll == 4){
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                    } else {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.cobblestone.blockID);
                                    }
                                }
                                if (id == Block.sand.blockID) {
                                    int roll = world.rand.nextInt(100);
                                    if (roll == 1) {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.Trinitite.blockID);
                                    }
                                }
                                if (id == Block.cobblestoneMossy.blockID) {
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.cobblestone.blockID);
                                }
                                if (id == Block.blockSnow.blockID){
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                }
                                if (id == Block.planks.blockID || id == Block.bookShelf.blockID || id == Block.workbench.blockID){
    
                                    int roll = world.rand.nextInt(80);
    
                                    if (roll < 10){
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                
                                        // Spawn a falling burnt log entity one block higher
                                        EntityFallingSand falling = new EntityFallingSand(
                                            world,
                                            (double)(x + dx) + 0.5D,
                                            (double)(y + dy + 1) + 0.5D,
                                            (double)(z + dz) + 0.5D,
                                            mod_NukeModMain.NukedPlanks.blockID
                                        );
                                    
                                        // Compute direction away from blast center
                                        double dirX = (x + dx) - x;
                                        double dirY = (y + dy) - y;
                                        double dirZ = (z + dz) - z;
                                        double dist2 = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                                        if (dist2 > 0.001D) {
                                            dirX /= dist2;
                                            dirY /= dist2;
                                            dirZ /= dist2;
                                        }
                                    
                                        // Give upward + outward velocity
                                        double strength = 0.3D + world.rand.nextInt(2); // tweak multiplier
                                        falling.motionX = dirX * strength;
                                        falling.motionY = 0.4D + (dirY * 0.2D); // always some upward push
                                        falling.motionZ = dirZ * strength;
                                    
                                        world.entityJoinedWorld(falling);
                                    } else if (roll > 10 && roll < 20){
                                        if (world.getBlockId(x + dx, y + dy + 1, z + dz) == 0) {
                                            world.setBlockWithNotify(x + dx, y + dy + 1, z + dz, Block.fire.blockID);
                                        }
                                    }
                                }
                                if (id == Block.wood.blockID) {
                                    world.setBlockWithNotify(x + dx, y + dy, z + dz, mod_NukeModMain.NukedLog.blockID);
                                }                            
                                if ( block instanceof BlockFlower || block instanceof BlockDeadBush || id == Block.pumpkin.blockID || block instanceof BlockCloth || block instanceof BlockBed || block instanceof BlockFlower){
                                    if (world.rand.nextBoolean()) {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, Block.fire.blockID);
                                    } else {
                                        world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } 
    }

    public void ShockwaveDamage(World world, int x, int y, int z) {
        int damageRadius = 20;
    
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
                    ((EntityLiving)e).attackEntityFrom(null, 4);
    
                    // Normalize direction
                    dx /= dist;
                    dy /= dist;
                    dz /= dist;
    
                    // Knockback strength (tweak multiplier)
                    double strength = 1.5D * (4.0D - (dist / damageRadius));
    
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

            if (e instanceof EntityBoat){
                double dx = e.posX - x;
                double dy = e.posY - y;
                double dz = e.posZ - z;
    
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist <= damageRadius && dist > 0.01) {
                    ((EntityBoat)e).setEntityDead();
                }
            }

            if (e instanceof EntityMinecart){
                double dx = e.posX - x;
                double dy = e.posY - y;
                double dz = e.posZ - z;
    
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist <= damageRadius && dist > 0.01) {
                    ((EntityMinecart)e).setEntityDead();
                }
            }
        }
    }
}