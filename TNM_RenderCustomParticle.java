package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import TNM_RegularNukeExplosion.TNM_EntityCustomFX;

public class TNM_RenderCustomParticle extends Render {

    public TNM_RenderCustomParticle() {
    }

    public void RenderIt(TNM_EntityCustomFX entity, double x, double y, double z, float yaw, float partialTicks){
    
        RenderEngine engine = ModLoader.getMinecraftInstance().renderEngine;
        String texturePath = entity.texturePath;
        engine.bindTexture(engine.getTexture(texturePath));
    
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    
        // Billboard to camera
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    
        float size = 0.1F * entity.particleScale;
    
        // Enable alpha blending and disable depth test
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 1.0F, 0.0F);
    
        tess.addVertexWithUV(-size, -size, 0.0D, 0.0D, 1.0D);
        tess.addVertexWithUV( size, -size, 0.0D, 1.0D, 1.0D);
        tess.addVertexWithUV( size,  size, 0.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(-size,  size, 0.0D, 0.0D, 0.0D);
        tess.draw();
    
        // Restore GL state
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.RenderIt((TNM_EntityCustomFX)entity, x, y, z, yaw, partialTicks);
    }
}
