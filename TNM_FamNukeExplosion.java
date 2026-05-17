package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class TNM_FamNukeExplosion {

    public TNM_FamNukeExplosion(World world, int x, int y, int z){
        this.createCrater(world, x, y, z);
        this.NuclearDamage(world, x, y, z);
    }

    public void createCrater(World world, int x, int y, int z) {
        int radius = 80; // ~100 block diameter
        double fuzziness = 0.01; // 0.0 = clean sphere, higher = fuzzier edges
        Random rand = new Random();
    
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int distSq = dx*dx + dy*dy + dz*dz;
                    if (distSq <= radius*radius) {
                        // Add fuzziness: skip some edge blocks randomly
                        double dist = Math.sqrt(distSq);
                        double edgeFactor = dist / radius; // 0 at center, 1 at edge
                        if (edgeFactor < 0.8 || world.rand.nextDouble() > fuzziness) {
                            int id = world.getBlockId(x + dx, y + dy, z + dz);
                            if (id != Block.bedrock.blockID) {
                                world.setBlock(x + dx, y + dy, z + dz, 0);
                            }
                        }
                    }
                }
            }
        }
        world.playSoundEffect(x, y, z, "ambient.weather.thunder", 10000.0F, 0.05F + rand.nextFloat() * 0.2F);
        world.playSoundEffect(x, y, z, "random.explode", 10000.0F, 0.05F + rand.nextFloat() * 0.2F);
    
    }

    public void NuclearDamage(World world, int x, int y, int z){
        int damageRadius = 70;

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
}