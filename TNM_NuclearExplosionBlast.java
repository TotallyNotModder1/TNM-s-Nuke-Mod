package net.minecraft.src;

import java.util.List;

public class TNM_NuclearExplosionBlast {

    public TNM_NuclearExplosionBlast(World world, int x, int y, int z){
        this.createCrater(world, x, y, z);
        this.NuclearDamage(world, x, y, z);
    }

    public void createCrater(World world, int x, int y, int z) {
        double fuzziness = 0.5;
        // --- Blast dome above ---
        int blastRadius = 20;
        for (int dx = -blastRadius; dx <= blastRadius; dx++) {
            for (int dz = -blastRadius; dz <= blastRadius; dz++) {
                for (int dy = 0; dy <= blastRadius; dy++) {
                    int distSq = dx*dx + dy*dy + dz*dz;
                    if (distSq <= blastRadius*blastRadius) {
                        double dist = Math.sqrt(distSq);
                        double edgeFactor = dist / blastRadius; // 0 at center, 1 at edge
                        if (edgeFactor < 0.8 || world.rand.nextDouble() > fuzziness) {
                            int id = world.getBlockId(x + dx, y + dy, z + dz);
                            if (id != Block.bedrock.blockID) {
                                world.setBlockWithNotify(x + dx, y + dy, z + dz, 0);
                            }
                        }
                    }
                }
            }
        }

        world.markBlocksDirty(
            x - blastRadius, y - blastRadius, z - blastRadius,
            x + blastRadius, y + blastRadius, z + blastRadius
        );
    }

    public void NuclearDamage(World world, int x, int y, int z){
        int damageRadius = 100;
        int innerdamageRadius = 40;
        int fireRadius = 200;

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
                    ((EntityLiving)e).attackEntityFrom(null, 10);
                }
                if (dist <= innerdamageRadius) {
                    ((EntityLiving)e).attackEntityFrom(null, 100);
                }
                if (dist <= fireRadius){
                    ((EntityLiving)e).fire = 400;
                }
            }
        }
    }
}