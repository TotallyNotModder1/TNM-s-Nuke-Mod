package net.minecraft.src;

import java.util.List;

public class TNM_NuclearExplosionBlastStep2 {

    public TNM_NuclearExplosionBlastStep2(World world, int x, int y, int z, int blastRadius){
        this.createCrater(world, x, y, z, blastRadius);
    }

    public void createCrater(World world, int x, int y, int z, int blastradius) {
        double fuzziness = 0.5;
        // --- Blast dome above ---
        int blastRadius = blastradius;
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
}