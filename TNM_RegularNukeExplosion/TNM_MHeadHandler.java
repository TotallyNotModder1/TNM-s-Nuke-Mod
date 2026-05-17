package TNM_RegularNukeExplosion;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_NukeModMain;

public class TNM_MHeadHandler extends Entity{
    private double sizemultiplier;

    public TNM_MHeadHandler(World world, double x, double y, double z, double size_multiplier) {
        super(world);
        this.setPosition(x, y, z);
        this.sizemultiplier = size_multiplier;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY = 0.2D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        ++ticksExisted;

        

        //--------------Inner--------------//
        int samples = (int) (4 * Math.sqrt(sizemultiplier)); // number of particles per tick
        double R = 10.0 * Math.sqrt(sizemultiplier);   // major radius
        double r = 5.0 * Math.sqrt(sizemultiplier);   // minor radius
    
        double spin = this.ticksExisted * 1 * Math.sqrt(sizemultiplier); // rotation speed
    
        for (int i = 0; i < samples; i++) {
            double theta = (2.0 * Math.PI * i) / samples + spin;
            double phi = (this.rand.nextDouble() * 2.0 * Math.PI);
        
            double px = this.posX + (R + r * Math.cos(phi)) * Math.cos(theta);
            double py = this.posY + r * Math.sin(phi);
            double pz = this.posZ + (R + r * Math.cos(phi)) * Math.sin(theta);
        
            // Tangent direction around the tube (derivative wrt phi)
            double vx = +Math.sin(phi) * Math.cos(theta);
            double vy = -Math.cos(phi);   // <-- flipped sign
            double vz = +Math.sin(phi) * Math.sin(theta);
        
            // Normalize and scale velocity
            double mag = Math.sqrt(vx*vx + vy*vy + vz*vz);
            vx /= mag;
            vy /= mag;
            vz /= mag;
        
            double speed = 0.1D; // tweak for smoothness
            vx *= speed;
            vy *= speed;
            vz *= speed;
        
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                vx, vy, vz, 0.0F,
                1F,
                0.9F,
                0.9F,
                1,
                (float)(35 * Math.sqrt(sizemultiplier)),
                true,
                60,
                false,
                false,
                0.3F,
                0.3F,
                0.3F,
                50F,
                true
            );
        }
        //--------------Outer--------------//
        int samplesB = (int) (7 * Math.sqrt(sizemultiplier)); // number of particles per tick
        double RB = 15.0 * Math.sqrt(sizemultiplier);   // major radius
        double rB = 10.0 * Math.sqrt(sizemultiplier);   // minor radius
    
        double spinB = this.ticksExisted * 1 * Math.sqrt(sizemultiplier); // rotation speed
    
        for (int i = 0; i < samplesB; i++) {
            double theta = (2.0 * Math.PI * i) / samplesB + spinB;
            double phi = (this.rand.nextDouble() * 2.0 * Math.PI);
        
            double px = this.posX + (RB + rB * Math.cos(phi)) * Math.cos(theta);
            double py = this.posY + rB * Math.sin(phi);
            double pz = this.posZ + (RB + rB * Math.cos(phi)) * Math.sin(theta);
        
            // Tangent direction around the tube (derivative wrt phi)
            double vx = +Math.sin(phi) * Math.cos(theta);
            double vy = -Math.cos(phi);   // <-- flipped sign
            double vz = +Math.sin(phi) * Math.sin(theta);
        
            // Normalize and scale velocity
            double mag = Math.sqrt(vx*vx + vy*vy + vz*vz);
            vx /= mag;
            vy /= mag;
            vz /= mag;
        
            double speed = 0.1D; // tweak for smoothness
            vx *= speed;
            vy *= speed;
            vz *= speed;
        
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                vx, vy, vz, 0.0F,
                0.9F,
                0.5F,
                0.0F,
                1,
                (float) (35 + worldObj.rand.nextInt(10) * Math.sqrt(sizemultiplier)),
                true,
                60,
                false,
                false,
                0.3F,
                0.3F,
                0.3F,
                10F,
                true
            );
        }
        //--------------Shell--------------//
        int samplesC = (int) (4 * Math.sqrt(sizemultiplier)); // number of particles per tick
        double RC = 15.0 * Math.sqrt(sizemultiplier);   // major radius
        double rC = 10.0 * Math.sqrt(sizemultiplier);   // minor radius
    
        double spinC = this.ticksExisted * 2 * sizemultiplier; // rotation speed
    
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
        
            mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                px, py, pz,
                vx, vy, vz, 0.0F,
                0.3F,
                0.1F,
                0.1F,
                1,
                (float) (25+ worldObj.rand.nextInt(4) * Math.sqrt(sizemultiplier)),
                true,
                60,
                false,
                false,
                0.3F,
                0.3F,
                0.3F,
                10F,
                true
            );
        }

        if (ticksExisted >= 30 * Math.sqrt(sizemultiplier)){
            int samplesD = (int) (2 * Math.sqrt(sizemultiplier)); // number of particles per tick
            double RD = 8.0 * Math.sqrt(sizemultiplier);   // major radius
            double rD = 4.0 * Math.sqrt(sizemultiplier);   // minor radius
        
            double spinD = this.ticksExisted * 0.8 * Math.sqrt(sizemultiplier); // rotation speed
        
            for (int i = 0; i < samplesD; i++) {
                double theta = (2.0 * Math.PI * i) / samplesD + spinD;
                double phi = (this.rand.nextDouble() * 2.0 * Math.PI);
            
                double px = this.posX + (RD + rD * Math.cos(phi)) * Math.cos(theta);
                double py = this.posY + rD * Math.sin(phi);
                double pz = this.posZ + (RD + rD * Math.cos(phi)) * Math.sin(theta);
            
                // Tangent direction around the tube (derivative wrt phi)
                double vx = +Math.sin(phi) * Math.cos(theta);
                double vy = -Math.cos(phi);   // <-- flipped sign
                double vz = +Math.sin(phi) * Math.sin(theta);
            
                // Normalize and scale velocity
                double mag = Math.sqrt(vx*vx + vy*vy + vz*vz);
                vx /= mag;
                vy /= mag;
                vz /= mag;
            
                double speed = 0.1D; // tweak for smoothness
                vx *= speed;
                vy *= speed;
                vz *= speed;
            
                mod_NukeModMain.spawnTNMParticle(worldObj, "Smoke",
                    px, py - 30, pz,
                    vx, vy, vz, 0.0F,
                    0.45F,
                    0.35F,
                    0.35F,
                    1,
                    (float) (60+ worldObj.rand.nextInt(10) * Math.sqrt(sizemultiplier)),
                    true,
                    30,
                    false,
                    false,
                    0.3F,
                    0.3F,
                    0.3F,
                    10F,
                    true
                );
            }
        }

        if (ticksExisted >= 680 * Math.sqrt(sizemultiplier)){
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
