package net.minecraft.src;

public class TNM_StemParticle extends EntityFX {
    private boolean dragOn;
    private int freezeTick;

    public TNM_StemParticle(World world, int index, double x, double y, double z, double motX, double motY, double motZ, float scale, int maxAge, int freezeAt, boolean useDrag, float R, float G, float B) {
        super(world, x, y, z, motX, motY, motZ);
        this.motionX = motX;
        this.motionY = motY;
        this.motionZ = motZ;
        this.particleRed = R;
        this.particleBlue = B;
        this.particleGreen = G;
        this.particleScale = scale;
        this.particleMaxAge = maxAge;
        this.freezeTick = freezeAt;
        this.dragOn = useDrag;
        this.particleTextureIndex = index;
        
        // Beta 1.7.3 color defaults (Greyish smoke)
        this.particleRed = 0.3F;
        this.particleGreen = 0.3F;
        this.particleBlue = 0.3F;
    }

    @Override
    public void onUpdate() {
        // Record previous positions for smooth rendering (interpolation)
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }

        // --- THE FREEZE LOGIC ---
        // If the particle has existed longer than the freezeTick, stop all motion.
        if (this.particleAge >= this.freezeTick) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
        } else {
            // Only apply gravity and drag while the particle is "active"
            this.motionY += this.particleGravity;

            if (this.dragOn) {
                this.motionX *= 0.98D;
                this.motionY *= 0.98D;
                this.motionZ *= 0.98D;
            }
        }

        // Apply movement manually to bypass moveEntity (better performance, no collision)
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
    }

    @Override
    public int getFXLayer() {
        return 2; // Required for standard smoke textures
    }
}
