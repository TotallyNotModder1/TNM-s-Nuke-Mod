package net.minecraft.src;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import TNM_RegularNukeExplosion.TNM_EntityCustomFX;

public class TNM_RenderCustomParticle extends Render {

    public TNM_RenderCustomParticle() {}

    {}

    public void RenderIt(TNM_EntityCustomFX entity, double x, double y, double z, float yaw, float partialTicks){
        int age = entity.particleAge;

        Tessellator tess = Tessellator.instance;

        RenderHelper.disableStandardItemLighting();

        // Progress of animation (0 → 1 over lifetime)
        float progress = ((float)age + partialTicks) / (float)entity.particleMaxAge;

        // Fade factor: alpha goes from 1 → 0 immediately
        float fade = progress;
        if (fade > 1.0F) fade = 1.0F;

        Random rand = new Random(432L);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + entity.height / 2.0F, (float)z);

        //beamcount
        int beamCount = entity.beamcount;

        for (int i = 0; i < beamCount; i++) {
            // Apply a bunch of random rotations to scatter beams in a sphere
            GL11.glRotatef(rand.nextFloat() * 360.0F, 1, 0, 0);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0, 1, 0);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0, 0, 1);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 1, 0, 0);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0, 1, 0);
            GL11.glRotatef(rand.nextFloat() * 360.0F + progress * 10.0F, 0, 0, 1);

            tess.startDrawing(GL11.GL_TRIANGLE_FAN);

            // Beam dimensions
            float beamHeight = rand.nextFloat() * entity.beamheight + 5.0F + fade * 10.0F - progress;
            float beamRadius = rand.nextFloat() * entity.beamwidth + 1.0F + fade * 2.0F - progress;

            // Center vertex (white, fading alpha)
            tess.setColorRGBA_I(0xFFFFFF, (int)(255.0F * (1.0F - fade)));
            tess.addVertex(0.0D, 0.0D, 0.0D);

            // Outer vertices (yellow, transparent)
            tess.setColorRGBA_I(0xFCC705, 0);
            tess.addVertex(-0.866D * beamRadius, beamHeight, -0.5D * beamRadius);
            tess.addVertex( 0.866D * beamRadius, beamHeight, -0.5D * beamRadius);
            tess.addVertex(0.0D, beamHeight, 1.0D * beamRadius);
            tess.addVertex(-0.866D * beamRadius, beamHeight, -0.5D * beamRadius);

            tess.draw();
        }

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.RenderIt((TNM_EntityCustomFX)entity, x, y, z, yaw, partialTicks);
    }
}
