package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import java.text.DecimalFormat;

public class TNM_GuiNuclearGenerator extends GuiContainer {
    private TNM_TileEntityRGenerator Generator;

    public TNM_GuiNuclearGenerator(InventoryPlayer playerInv, TNM_TileEntityRGenerator generator) {
        super(new TNM_ContainerReactor(playerInv, generator));
        this.xSize = 189; 
        this.ySize = 179; 
        this.Generator = generator;
    }

    private String formatEnergy(int energy) {
        DecimalFormat df = new DecimalFormat("#.##"); // up to 2 decimal places
    
        if (energy >= 1_000_000) {
            double millions = energy / 1_000_000.0;
            return df.format(millions) + "M";
        } else if (energy >= 1_000) {
            double thousands = energy / 1_000.0;
            return df.format(thousands) + "K";
        } else {
            return String.valueOf(energy);
        }
    }
    
    
    @Override
    protected void drawGuiContainerForegroundLayer() {
        String burnText = formatEnergy(Generator.EnergyOutputPerTick);
    
        fontRenderer.drawString("TeF/t:", 7, ySize - 165 + 2, 0x111f09);
        fontRenderer.drawString(burnText, 14, ySize - 158 + 2, 0x111f09);
        if (Generator.IsSeized){
            fontRenderer.drawString("Clog!", 7, ySize - 125 + 2, 0xff0000);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int tex = mc.renderEngine.getTexture("/NukeTex/RTGGui.png");
        mc.renderEngine.bindTexture(tex);
    
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
    
        GL11.glColor4f(1F, 1F, 1F, 1F);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        
        // --- Progress bar (no logic) ---
        int barX = x + 26;

        // Just render the full bar slice
        if (Generator.Enrichmentprogress > 0) {
            // scale progress to 80 ticks
            int progress = (Generator.Enrichmentprogress * 22) / 1000;
            if (progress > 22) progress = 22; // clamp
    
            drawTexturedModalRect(x + 7, y + 85, 0, 179, progress, 6);
        }
    }
}
