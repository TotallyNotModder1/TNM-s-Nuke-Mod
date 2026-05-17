package net.minecraft.src;


public class TNM_RenderHeatPipe {
    public static boolean render(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block, int meta) {
        // pipe thickness: 10px out of 16px
        float thickness = 8F / 16F; // 0.625F
        float offset = (1.0F - thickness) / 2.0F; // 0.1875F

        // central cube
        block.setBlockBounds(offset, offset, offset,
        offset + thickness, offset + thickness, offset + thickness);
        renderer.renderStandardBlock(block, x, y, z);

        // east
        if (isPipeConnectable(world.getBlockId(x + 1, y, z))) {
            block.setBlockBounds(offset + thickness, offset, offset,
        1.0F, offset + thickness, offset + thickness);
            renderer.renderStandardBlock(block, x, y, z);
        }
        // west
        if (isPipeConnectable(world.getBlockId(x - 1, y, z))) {
            block.setBlockBounds(0.0F, offset, offset,
        offset, offset + thickness, offset + thickness);
            renderer.renderStandardBlock(block, x, y, z);
        }
        // up
        if (isPipeConnectable(world.getBlockId(x, y + 1, z))) {
            block.setBlockBounds(offset, offset + thickness, offset,
        offset + thickness, 1.0F, offset + thickness);
            renderer.renderStandardBlock(block, x, y, z);
        }
        // down
        if (isPipeConnectable(world.getBlockId(x, y - 1, z))) {
            block.setBlockBounds(offset, 0.0F, offset,
        offset + thickness, offset, offset + thickness);
            renderer.renderStandardBlock(block, x, y, z);
        }
        // south
        if (isPipeConnectable(world.getBlockId(x, y, z + 1))) {
            block.setBlockBounds(offset, offset, offset + thickness,
        offset + thickness, offset + thickness, 1.0F);
            renderer.renderStandardBlock(block, x, y, z);
        }
        // north
        if (isPipeConnectable(world.getBlockId(x, y, z - 1))) {
            block.setBlockBounds(offset, offset, 0.0F,
        offset + thickness, offset + thickness, offset);
            renderer.renderStandardBlock(block, x, y, z);
        }

        // reset bounds
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    private static boolean isPipeConnectable(int id) {
        return id == mod_NukeModMain.heatPipe.blockID
            || id == mod_NukeModMain.CentrifugeBlock.blockID
            || id == mod_NukeModMain.MiniReactor.blockID
            || id == mod_NukeModMain.AssemblyTable.blockID;
    }
}


