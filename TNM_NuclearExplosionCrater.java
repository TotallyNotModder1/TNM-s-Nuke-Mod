package net.minecraft.src;

import java.util.List;

public class TNM_NuclearExplosionCrater {
    private int setDepth;
    private int setRadius;

    public TNM_NuclearExplosionCrater(World world, int x, int y, int z, int Depth, int Radius){
        this.setDepth = Depth;
        this.setRadius = Radius;
        this.createCrater(world, x, y, z);
    }

    public void createCrater(World world, int x, int y, int z) {
        int radius = setRadius; //35; // half of 70
        int maxDepth = setDepth; //20; // shallower than before
        double fuzziness = 0.3;

        world.playSoundEffect(x, y, z, "random.explode", 4.0F, 0.5F);

        // --- Bowl-shaped crater ---
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int distSq = dx*dx + dz*dz;
                if (distSq <= radius*radius) {
                    double dist = Math.sqrt(distSq);
                    int depth = (int)Math.round(maxDepth * (1.0 - dist / radius));
                    if (depth < 0) depth = 0;

                    double edgeFactor = dist / radius; // 0 at center, 1 at edge
                    if (edgeFactor < 0.8 || world.rand.nextDouble() > fuzziness) {
                        for (int dy = 0; dy < depth; dy++) {
                            int id = world.getBlockId(x + dx, y - dy, z + dz);
                            if (id != Block.bedrock.blockID) {
                                world.setBlockWithNotify(x + dx, y - dy, z + dz, 0);
                            }
                        }
                    }
                }
            }
        }

        world.markBlocksDirty(
            x - radius, y - maxDepth, z - radius,
            x + radius, y, z + radius
        );
    }
}