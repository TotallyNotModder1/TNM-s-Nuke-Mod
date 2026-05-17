package net.minecraft.src;

public class TNM_SmokeSystem extends EntityFX {
    private float particlebrightness;
    private boolean fullbright;
    private boolean Enableaging;
    private boolean EnableColorAging;
    private float initialRed;
    private float initialGreen;
    private float initialBlue;
    private float TargetRed;
    private float TargetGreen;
    private float TargetBlue;
    private float TargetScale;
    private boolean DragOn;

    public TNM_SmokeSystem(World world, double renderdistancew, int index, double x, double y, double z, double vx, double vy, double vz, float grav, float R, float G, float B, float brightness, float scale, boolean isFullBright, int age, boolean enablescaleaging, boolean enablecoloraging, float TRed, float TGreen, float TBlue, float targetscale, boolean enabledrag) {
        super(world, x, y, z, vx, vy, vz);
        this.renderDistanceWeight = renderdistancew;
        this.particleScale = scale;
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;
        this.particleRed = R;  
        this.particleGreen = G;
        this.particleBlue = B;
        this.initialRed = R;  
        this.initialGreen = G;
        this.initialBlue = B;
        this.particleGravity = grav;
        this.particleMaxAge = age;
        this.particleTextureIndex = index;
        this.particlebrightness = brightness;
        this.fullbright = isFullBright;
        this.Enableaging = enablescaleaging;
        this.EnableColorAging = enablecoloraging;
        this.TargetRed = TRed;
        this.TargetGreen = TGreen;
        this.TargetBlue = TBlue;
        this.TargetScale = targetscale;
        this.DragOn = enabledrag;
    }


    @Override
    public void onUpdate() {
        super.onUpdate(); // <-- important, updates position and age

        if (!DragOn){
            this.posX += this.motionX;
            this.posY += this.motionY; // keep rising
            this.posZ += this.motionZ;
        }
    
        if (Enableaging) {
            float lifeProgress = (float)this.particleAge / (float)this.particleMaxAge;
            
            this.particleScale   = particleScale   + (TargetScale   - particleScale)   * lifeProgress;
        }
        if (EnableColorAging) {
            float lifeProgress = (float)this.particleAge / (float)this.particleMaxAge;

            // Example: fade from initial color to black
            this.particleRed   = initialRed   + (TargetRed   - initialRed)   * lifeProgress;
            this.particleGreen = initialGreen + (TargetGreen - initialGreen) * lifeProgress;
            this.particleBlue  = initialBlue  + (TargetBlue  - initialBlue)  * lifeProgress;
        }
    }

    @Override
    public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ) {
        super.renderParticle(tessellator, partialTick, rotX, rotZ, rotYZ, rotXY, rotXZ);
    }

    @Override
    public float getEntityBrightness(float partialTick) {
        if (fullbright == false){
            int x = MathHelper.floor_double(this.posX);
            int y = MathHelper.floor_double(this.posY);
            int z = MathHelper.floor_double(this.posZ);
            return this.worldObj.getLightBrightness(x, y, z);
        } else {
            return this.particlebrightness;
        }
    }

    @Override
    public int getFXLayer() {
        return 2; 
    }

}
