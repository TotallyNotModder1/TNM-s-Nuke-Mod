package TNM_Weather;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagDouble;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

//So help me fucking GOD if this fucks up compatibility somehow...

public class TNM_RadiationManager {
    private final Map<String, Double> radiationLevels = new HashMap<String, Double>();


    // Helper to get a reliable unique key string for any Living Entity
    private String getEntityKey(EntityLiving e) {
        if (e instanceof EntityPlayer) {
            return "player_" + ((EntityPlayer) e).username;
        }
        // If you want to track radiation on persistent mobs, use entityId as a temporary fallback,
        // but remember mob IDs completely change on world reload!
        return "mob_" + e.entityId;
    }

    public Double getRadiationLevel(EntityLiving e) {
        Double val = this.radiationLevels.get(this.getEntityKey(e));
        return val == null ? 0.0 : val;
    }

    public void setRadiationLevel(EntityLiving e, Double level) {
        // Clamp to 1 decimal place before storing
        double clamped = Math.round(level * 10.0) / 10.0;
        this.radiationLevels.put(this.getEntityKey(e), clamped);
    }

    public void clear(EntityLiving e) {
        // FIX: Swapped e.entityId to this.getEntityKey(e)
        this.radiationLevels.remove(this.getEntityKey(e));
    }

    // Build the world save directory path manually using worldInfo
    private File getWorldDir(World world) {
        String worldName = world.getWorldInfo().getWorldName();
        File savesDir = new File(Minecraft.getMinecraftDir(), "saves");
        return new File(savesDir, worldName);
    }

    public void saveRadiation(World world) {
        try {
            File dir = this.getWorldDir(world);
            File file = new File(dir, "TNM_Radiation.dat");

            NBTTagCompound root = new NBTTagCompound();
            
            // FIX: This now iterates over Map.Entry<String, Double> correctly
            for (Map.Entry<String, Double> entry : this.radiationLevels.entrySet()) {
                if (entry.getKey().startsWith("player_")) {
                    root.setDouble(entry.getKey(), entry.getValue());
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            CompressedStreamTools.writeGzippedCompoundToOutputStream(root, fos);
            fos.close();
            System.out.println("Radiation data saved to " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRadiation(World world) {
        try {
            File dir = this.getWorldDir(world);
            File file = new File(dir, "TNM_Radiation.dat");
            if (!file.exists()) return;

            FileInputStream fis = new FileInputStream(file);
            NBTTagCompound root = CompressedStreamTools.func_1138_a(fis);
            fis.close();

            this.radiationLevels.clear();

            // FIX: Using your exact public method func_28110_c() to get the tags collection safely
            java.util.Collection tagsCollection = root.func_28110_c();
            
            for (Object obj : tagsCollection) {
                if (obj instanceof NBTBase) {
                    NBTBase tag = (NBTBase) obj;
                    String key = tag.getKey();
                    if (key.startsWith("player_")) {
                        this.radiationLevels.put(key, root.getDouble(key));
                    }
                }
            }
            System.out.println("Radiation data loaded from " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
