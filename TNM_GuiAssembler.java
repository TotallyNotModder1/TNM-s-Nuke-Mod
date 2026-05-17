package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class TNM_GuiAssembler extends GuiContainer {
    private TNM_TileEntityAssembler Assembler;

    public TNM_GuiAssembler(InventoryPlayer playerInv, TNM_TileEntityAssembler assembler) {
        super(new TNM_ContainerAssembler(playerInv, assembler));
        this.xSize = 177; // standard width
        this.ySize = 222; // standard height
        this.Assembler = assembler;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        // Draw your custom text at GUI-relative coordinates (82, 114)
        String burnText = Integer.toString(Assembler.uses);
        this.fontRenderer.drawString("Uses: " + burnText, 82, 114, 0x111f09);
    }

    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int tex = mc.renderEngine.getTexture("/NukeTex/Assembler.png");
        mc.renderEngine.bindTexture(tex);
    
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
    
        // Draw the base GUI background
        GL11.glColor4f(1F, 1F, 1F, 1F);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
        // Overlay when Assembler is full
        if (Assembler.isFull()) {
            // Overlay at GUI coords (109, 51), size 57x19
            // Source texture region at (176, 0) on a 256x256 sheet
            drawTexturedModalRect(x + 109, y + 51, 176, 0, 57, 19);
        }
    }    
}
