package net.minecraft.src;

import java.util.List;

public class TNM_NuclearExplosionDamage {
    
    public static void NuclearDamage(World world, int x, int y, int z, int Damageradius, int InnerdamageRadius, int FireRadius){
        int damageRadius = Damageradius;//100;
        int innerdamageRadius = InnerdamageRadius;//40;
        int fireRadius = FireRadius;//200;

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