package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class TNM_GuiCentrifuge extends GuiContainer {
    private TNM_TileEntityCentrifuge Centrifuge;

    public TNM_GuiCentrifuge(InventoryPlayer playerInv, TNM_TileEntityCentrifuge centrifuge) {
        super(new TNM_ContainerCentrifuge(playerInv, centrifuge));
        this.xSize = 176; // standard width
        this.ySize = 166; // standard height
        this.Centrifuge = centrifuge;
    }

    
    @Override
    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Inventory", 8, ySize - 96 + 2, 0x111f09);
        String burnText = Integer.toString(Centrifuge.burntime);
        fontRenderer.drawString("TeF:", 100, ySize - 96 + 2, 0x111f09);
        fontRenderer.drawString(burnText, 120, ySize - 96 + 2, 0x111f09);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int tex = mc.renderEngine.getTexture("/NukeTex/GuiCentrifuge.png");
        mc.renderEngine.bindTexture(tex);
    
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
    
        // Draw the base GUI background
        GL11.glColor4f(1F, 1F, 1F, 1F);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
        // --- Energy bar ---
        if (Centrifuge.burntime > 0) {
            // Scale burntime to bar height (18 pixels max)
            int barHeight = (int)((Centrifuge.burntime / (float)Centrifuge.maxburntime) * 18);
    
            // Draw from bottom up: texture at (200,14), size 6x18
            // We offset Y so the bar shrinks upward
            drawTexturedModalRect(
                x + 63,                  // GUI X position for the bar
                y + 52 + (18 - barHeight), // GUI Y position adjusted for shrinking
                200,                     // texture X
                14 + (18 - barHeight),   // texture Y offset
                6,                       // width
                barHeight                // height
            );
        }
        if (Centrifuge.centrifugeCookTime > 0) {
            // scale progress to 80 ticks
            int progress = (Centrifuge.centrifugeCookTime * 22) / 150;
            if (progress > 22) progress = 22; // clamp
    
            // draw bar at GUI coords (80,35), using texture slice at (177,14)
            drawTexturedModalRect(x + 80, y + 35, 177, 14, progress, 16);
        }
    }
}
