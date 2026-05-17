package net.minecraft.src;

import java.util.HashMap;

import TNM_AudioManager.TNM_SoundHelper;

public class TNM_Detonator extends Item {
    // Store antenna coordinates per player
    private static HashMap<String, int[]> boundCoords = new HashMap<String, int[]>();

    public TNM_Detonator(int id) {
        super(id);
        this.maxStackSize = 1;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            activateBound(world, player);
            TNM_SoundHelper.playEntitySound(player, "button14.wav", 1.0F);
            return stack;
        }

        // Ray trace from player’s eyes
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        double px = player.posX;
        double py = player.posY + (double)player.getEyeHeight();
        double pz = player.posZ;
        Vec3D start = Vec3D.createVector(px, py, pz);
        Vec3D look = player.getLookVec();
        Vec3D end = start.addVector(look.xCoord * 5.0D, look.yCoord * 5.0D, look.zCoord * 5.0D); // 5 block reach

        MovingObjectPosition mop = world.rayTraceBlocks(start, end);
        if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE) {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;
            int id = world.getBlockId(x, y, z);

            if (id == mod_NukeModMain.Antenna.blockID) {
                Block block = Block.blocksList[id];
                if (block instanceof TNM_Antenna) {
                    // Normal right‑click → bind coordinates to this player
                    boundCoords.put(player.username, new int[]{x, y, z});
                    TNM_SoundHelper.playEntitySound(player, "blip1.wav", 1.0F);

                }
            }
        }
        return stack;
    }

    // Remote activation using stored coordinates
    public static void activateBound(World world, EntityPlayer player) {
        int[] coords = boundCoords.get(player.username);
        if (coords != null) {
            int x = coords[0], y = coords[1], z = coords[2];
            int id = world.getBlockId(x, y, z);
            if (id == mod_NukeModMain.Antenna.blockID) {
                Block block = Block.blocksList[id];
                if (block instanceof TNM_Antenna) {
                    ((TNM_Antenna)block).trigger(world, x, y, z);
                }
            }
        }
    }
}
