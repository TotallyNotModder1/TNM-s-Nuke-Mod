package TNM_MiniNuke;

import java.util.*;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.Entity;
import net.minecraft.src.Explosion;
import net.minecraft.src.World;

public class TNM_ClassicalNuke {
    private final World world;
    private final Entity exploder;
    private final double x, y, z;
    private final float size;
    private final boolean flaming;
    private final int FlameBlock;
    private final boolean damage;

    public TNM_ClassicalNuke(World world, Entity exploder, double x, double y, double z, float size, boolean flaming, int flameblock, boolean Damage) {
        this.world = world;
        this.exploder = exploder;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.flaming = flaming;
        this.FlameBlock = flameblock;
        this.damage = Damage;
        runExplosion();
    }

    private void runExplosion() {
        Explosion vanilla = new Explosion(world, exploder, x, y, z, size);
        vanilla.isFlaming = flaming;
        vanilla.doExplosionA(); // collect affected blocks/entities

        // Clear blocks without drops/particles
        for (Object o : vanilla.destroyedBlockPositions) {
            ChunkPosition pos = (ChunkPosition) o;
            int id = world.getBlockId(pos.x, pos.y, pos.z);
            if (id > 0) {
                world.setBlockWithNotify(pos.x, pos.y, pos.z, 0);
                Block.blocksList[id].onBlockDestroyedByExplosion(world, pos.x, pos.y, pos.z);
            }
            if (flaming && world.isAirBlock(pos.x, pos.y, pos.z) &&
                Block.opaqueCubeLookup[world.getBlockId(pos.x, pos.y - 1, pos.z)] &&
                world.rand.nextInt(3) == 0) {
                world.setBlockWithNotify(pos.x, pos.y, pos.z, FlameBlock);
            }
        }

        // Damage entities
        if (damage){
            AxisAlignedBB box = AxisAlignedBB.getBoundingBoxFromPool(
                x - size, y - size, z - size,
                x + size, y + size, z + size
            );
            List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(exploder, box);
            for (Entity e : entities) {
                double dist = e.getDistance(x, y, z);
                double dx = e.posX - x;
                double dy = e.posY - y;
                double dz = e.posZ - z;
                double mag = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (mag > 0.0D) {
                    dx /= mag; dy /= mag; dz /= mag;
                    double strength = (1.0D - dist / size);
                    e.attackEntityFrom(exploder, (int)(strength * size * 8.0D));
                    e.motionX += dx * strength;
                    e.motionY += dy * strength;
                    e.motionZ += dz * strength;
                }
            }
        }
    }
}
