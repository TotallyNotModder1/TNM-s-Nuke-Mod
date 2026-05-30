package TNM_RecipeBookClasses;

import org.lwjgl.opengl.GL11;
import TNM_Weather.TNM_RadiationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;

public class TNM_GuiDosimeter extends GuiContainer {
    private final TNM_RadiationManager manager;
    private final EntityPlayer player;

    public TNM_GuiDosimeter(EntityPlayer player, TNM_RadiationManager manager) {
        super(new Container() {
            @Override
            public boolean isUsableByPlayer(EntityPlayer var1) {
               return true;
            }
        });
        this.xSize = 102;
        this.ySize = 104;
        this.player = player;
        this.manager = manager;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        // Bind your dosimeter texture
        int tex = mc.renderEngine.getTexture("/NukeTex/guidosimeter.png");
        mc.renderEngine.bindTexture(tex);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        GL11.glColor4f(1F, 1F, 1F, 1F);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        // Radiation readout
        double dosage = manager.getRadiationLevel(player);
        String text = Double.toString(dosage);

        // Center text
        int textX = x + (xSize / 2) - (fontRenderer.getStringWidth(text) / 2);
        int textY = y + (ySize / 2);

        // Color code: green < 10, yellow < 50, red otherwise
        int color = 0x00FF00;
        if (dosage >= 10 && dosage < 50) color = 0xFFFF00;
        else if (dosage >= 100) color = 0xFF0000;

        fontRenderer.drawString(text, textX, textY, color);
    }
}
