package TNM_RegularNukeExplosion;


import net.minecraft.src.Entity;
import net.minecraft.src.World;


public class TNM_EntityCustomFX extends Entity {
    public int particleAge = 0;
    public int particleMaxAge;
    public float particleScale;
    public float particleGravity;
    public final String texturePath;

    public TNM_EntityCustomFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale, int maxAge, float gravity, String texturePath) {
        super(world);
        this.setSize(0.2F, 0.2F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.renderDistanceWeight = 200.0D;

        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;

        this.particleScale = scale;
        this.particleMaxAge = maxAge;
        this.particleGravity = gravity;
        this.texturePath = texturePath;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (++this.particleAge >= this.particleMaxAge) {
            this.setEntityDead();
        }

        // gravity
        this.motionY -= 0.04D * (double)this.particleGravity;

        // move
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        // ground friction
        if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
        }
    }   

    @Override
    public float getEntityBrightness(float partialTick) {
        return 1.0F;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbt) {
    }

    @Override
    protected void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbt) {
    }
}
