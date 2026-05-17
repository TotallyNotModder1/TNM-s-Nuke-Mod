package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class TNM_GuiWarhead extends GuiContainer {
    private TNM_TileEntityWarhead Warhead;

    public TNM_GuiWarhead(InventoryPlayer playerInv, TNM_TileEntityWarhead warhead) {
        super(new TNM_ContainerNuke(playerInv, warhead));
        this.xSize = 176; // standard width
        this.ySize = 166; // standard height
        this.Warhead = warhead;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Inventory", 8, ySize - 96 + 2, 0xffffff);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int tex = mc.renderEngine.getTexture("/NukeTex/GuiNuke.png");
        mc.renderEngine.bindTexture(tex);
    
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
    
        // Draw the base GUI background
        GL11.glColor4f(1F, 1F, 1F, 1F);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
        // Overlay when warhead is full
        if (Warhead.isFull()) {
            // Overlay at GUI coords (109, 51), size 57x19
            // Source texture region at (176, 0) on a 256x256 sheet
            drawTexturedModalRect(x + 109, y + 51, 176, 0, 57, 19);
        }
    }    
}
