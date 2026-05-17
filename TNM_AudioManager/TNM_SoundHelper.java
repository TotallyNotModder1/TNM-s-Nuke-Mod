package TNM_AudioManager;

import net.minecraft.client.Minecraft; // Direct client import for Beta 1.7.3
import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.SoundManager;
import net.minecraft.src.World;
import paulscode.sound.SoundSystem;
import net.minecraft.src.ModLoader;
import net.minecraft.src.SoundManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

public class TNM_SoundHelper {
    private static SoundSystem soundSystem;

    private static SoundSystem getSoundSystem() {
        if (soundSystem != null) return soundSystem;

        try {
            Minecraft mc = ModLoader.getMinecraftInstance();
            if (mc == null || mc.sndManager == null) return null;

            SoundManager sndManager = mc.sndManager;

            for (Field f : SoundManager.class.getDeclaredFields()) {
                if (f.getType() == SoundSystem.class) {
                    f.setAccessible(true);
                    Object obj = f.get(sndManager);
                    if (obj instanceof SoundSystem) {
                        soundSystem = (SoundSystem) obj;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[TNM] Failed to reflect SoundSystem!");
            e.printStackTrace();
        }
        return soundSystem;
    }

    public static void playEntitySound(Entity entity, String filename, float baseVolume) {
        SoundSystem sys = getSoundSystem();
        if (sys == null) return;

        URL url = TNM_SoundHelper.class.getClassLoader().getResource("NukeTex/sounds/" + filename);
        if (url == null) {
            System.out.println("Missing sound: " + filename);
            return;
        }

        // Unique source name so sounds don’t overwrite each other
        String sourceName = "TNM_" + filename + "_" + entity.entityId + "_" + System.nanoTime();

        // Create the source
        sys.newSource(
            false, sourceName, url, filename, false,
            (float) entity.posX, (float) entity.posY, (float) entity.posZ,
            2, 16.0F
        );

        // Apply game’s master volume directly
        float globalVol = ModLoader.getMinecraftInstance().gameSettings.soundVolume;
        sys.setVolume(sourceName, baseVolume * globalVol);

        sys.play(sourceName);
    }

    public static void playBlockSound(World world, int x, int y, int z, String filename, float baseVolume) {
        SoundSystem sys = getSoundSystem();
        if (sys == null) return;
    
        URL url = TNM_SoundHelper.class.getClassLoader().getResource("NukeTex/sounds/" + filename);
        if (url == null) {
            System.out.println("Missing sound: " + filename);
            return;
        }
    
        // Unique source name so multiple block sounds don’t overwrite each other
        String sourceName = "TNM_" + filename + "_" + x + "_" + y + "_" + z + "_" + System.nanoTime();
    
        // Create the source at block coordinates
        sys.newSource(
            false, sourceName, url, filename, false,
            (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, // center of block
            2, 16.0F
        );
    
        // Apply global volume scaling
        float globalVol = ModLoader.getMinecraftInstance().gameSettings.soundVolume;
        sys.setVolume(sourceName, baseVolume * globalVol);
    
        sys.play(sourceName);
    }
}
