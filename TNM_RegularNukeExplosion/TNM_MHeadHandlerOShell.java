package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_MHeadHandlerOShell extends Entity{
    private float sizemultiplier;
    private boolean isMega;
    private int cooldown = 0;

    public TNM_MHeadHandlerOShell(World world, double x, double y, double z, float size_multiplier, boolean ismega) {
        super(world);
        this.setPosition(x, y, z);
        this.sizemultiplier = size_multiplier;
        this.isMega = ismega;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.motionY = 0.2D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        ++ticksExisted;

        //--------------Shell--------------//
        int samplesC = (int) (3 * Math.sqrt(sizemultiplier)); // number of particles per tick
        double RC = 18.0 * Math.sqrt(sizemultiplier);   // major radius
        double rC = 13.0 * Math.sqrt(sizemultiplier);   // minor radius
    
        
        double spinC = this.ticksExisted * 30 * Math.sqrt(sizemultiplier); // rotation speed
        
        float color1 = 0.28F;
        float color2 = 0.35F;
        float skirtscale = (float) (100F + worldObj.rand.nextInt(5)  * Math.sqrt(sizemultiplier)); // 45F ± 5 
        float latescale = (float) ((float)70F * Math.sqrt(sizemultiplier));

        String particlename = "Smokepuff";


        if (ticksExisted >= 1000 * Math.sqrt(sizemultiplier)){
            samplesC = (int)(15 * Math.sqrt(sizemultiplier));
            RC = 21.5 * Math.sqrt(sizemultiplier);
            rC = 16.5 * Math.sqrt(sizemultiplier);
            skirtscale = (float) (200F + worldObj.rand.nextInt(5)  * Math.sqrt(sizemultiplier)); 
            latescale = (float) (80F + worldObj.rand.nextInt(5)  * Math.sqrt(sizemultiplier));
            color1 = 0.35F;
            color2 = 0.38F;
            spinC = this.ticksExisted * 35 * Math.sqrt(sizemultiplier);
            particlename = "Smokepuff";
        }

        if (ticksExisted >= 1500 * Math.sqrt(sizemultiplier)){
            samplesC = (int)(7 * Math.sqrt(sizemultiplier));
            RC = 28 * Math.sqrt(sizemultiplier);
            rC = 23 * Math.sqrt(sizemultiplier);
            color1 = 0.95F;
            color2 = 0.95F;
            latescale = (float) (150F + worldObj.rand.nextInt(5)  * Math.sqrt(sizemultiplier));
            spinC = this.ticksExisted * 40;
            particlename = "Smoke";
        }
    
        if (ticksExisted < 380 * Math.sqrt(sizemultiplier)){
            for (int i = 0; i < samplesC; i++) {
                double theta = (2.0 * Math.PI * i) / samplesC + spinC;
                double phi = (this.rand.nextDouble() * 2.0 * Math.PI);
            
                double px = this.posX + (RC + rC * Math.cos(phi)) * Math.cos(theta);
                double py = this.posY + rC * Math.sin(phi);
                double pz = this.posZ + (RC + rC * Math.cos(phi)) * Math.sin(theta);
            
                // Tangent direction around the tube (derivative wrt phi)
                double vx = +Math.sin(phi) * Math.cos(theta);
                double vy = -Math.cos(phi);   // <-- flipped sign
                double vz = +Math.sin(phi) * Math.sin(theta);
            
                // Normalize and scale velocity
                double mag = Math.sqrt(vx*vx + vy*vy + vz*vz);
                vx /= mag;
                vy /= mag;
                vz /= mag;
            
                double speed = 0.2D; // tweak for smoothness
                vx *= speed;
                vy *= speed;
                vz *= speed;
            
                mod_NukeModMain.spawnTNMParticle(worldObj, particlename,
                    px, py, pz,
                    vx, vy, vz, 0.0F,
                    color2,
                    color1,
                    color1,
                    1,
                    (float) (40F + worldObj.rand.nextInt(4) * Math.sqrt(sizemultiplier)),
                    false,
                    70 + worldObj.rand.nextInt(4),
                    false,
                    false,
                    0.3F,
                    0.3F,
                    0.3F,
                    10F,
                    true
                );
            }
        } else if (ticksExisted > 480 * Math.sqrt(sizemultiplier)){
            for (int i = 0; i < samplesC; i++) {
                double theta = (2.0 * Math.PI * i) / samplesC + spinC;
                double phi = (this.rand.nextDouble() * 2.0 * Math.PI);
            
                double px = this.posX + (RC + rC * Math.cos(phi)) * Math.cos(theta);
                double py = this.posY + rC * Math.sin(phi);
                double pz = this.posZ + (RC + rC * Math.cos(phi)) * Math.sin(theta);
            
                // Tangent direction around the tube (derivative wrt phi)
                double vx = +Math.sin(phi) * Math.cos(theta);
                double vy = -Math.cos(phi);   // <-- flipped sign
                double vz = +Math.sin(phi) * Math.sin(theta);
            
                // Normalize and scale velocity
                double mag = Math.sqrt(vx*vx + vy*vy + vz*vz);
                vx /= mag;
                vy /= mag;
                vz /= mag;
            
                double speed = 0.2D; // tweak for smoothness
                vx *= speed;
                vy *= speed;
                vz *= speed;

                float scale = latescale;
            
                mod_NukeModMain.spawnTNMParticle(worldObj, particlename,
                    px, py, pz,
                    vx, vy, vz, 0.0F,
                    color2,
                    color1,
                    color1,
                    1,
                    scale,
                    false,
                    90 + worldObj.rand.nextInt(4),
                    false,
                    false,
                    0.3F,
                    0.3F,
                    0.3F,
                    10F,
                    true
                );
            }
            if (ticksExisted >= 330  * Math.sqrt(sizemultiplier) && isMega) {
                for (int i = 0; i < 1; i++) {
                    double px = this.posX + (worldObj.rand.nextDouble() * 3.0 - 1.0); // random offset -1..+1
                    double py = this.posY; // spawn at entity height
                    double pz = this.posZ + (worldObj.rand.nextDouble() * 3.0 - 1.0); // random offset -1..+1
                    double apx = this.posX + (worldObj.rand.nextDouble() * 6.0 - 2.0); // random offset -1..+1
                    double apz = this.posZ + (worldObj.rand.nextDouble() * 6.0 - 2.0);
            
            
                    mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                        px, py, pz,
                        0.0D, 0.0D, 0.0D, 0.0F,   // gravity
                        0.95F, 0.95F, 0.95F, // RGB
                        1,                  // brightness
                        skirtscale,              // initial scale
                        false,              // fullbright
                        900,                 // lifespan
                        false,              // scale aging
                        false,              // color aging
                        0.3F, 0.3F, 0.3F,   // target RGB
                        90F,
                        false               // target scale (same as initial)
                    );
                }
            }
        }

        if (ticksExisted >= 2498 * Math.sqrt(sizemultiplier)){
            for (int i = 0; i < 156; i++) {
                double theta = rand.nextDouble() * 2.0 * Math.PI; // azimuth
                double phi   = rand.nextDouble() * Math.PI;       // polar angle
        
                double dx = 60 * Math.sin(phi) * Math.cos(theta) * Math.sqrt(sizemultiplier);
                double dy = 15 * Math.cos(phi) * Math.sqrt(sizemultiplier); // vertical compressed
                double dz = 60 * Math.sin(phi) * Math.sin(theta) * Math.sqrt(sizemultiplier);
        
                double px = posX + dx;
                double py = posY + dy;
                double pz = posZ + dz;
        
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                    px, py, pz,
                    0.0D, 0.0D, 0.0D, 0.0F,
                    0.95F, 0.95F, 0.95F,
                    1F,
                    (float)(160F + rand.nextInt(20) * Math.sqrt(sizemultiplier)),
                    false,
                    830 + rand.nextInt(7),
                    false,
                    false,
                    0F, 0F, 0F,
                    1F,
                    false
                );
            }
        }

        if (ticksExisted >= 2500 * Math.sqrt(sizemultiplier)){
            this.setEntityDead();
        }
    }
    
    @Override
    protected void entityInit() {}
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {

    }
}
