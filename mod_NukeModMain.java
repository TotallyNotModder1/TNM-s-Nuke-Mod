package net.minecraft.src;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import TNM_AudioManager.TNM_SoundHelper;
import forge.MinecraftForge;
import forge.ForgeHooksClient;
import forge.Property;
import TNM_BakerExplosion.TNM_BakerExplosion;
import TNM_BakerExplosion.TNM_BakerMushroomCloud;
import TNM_MiniNuke.TNM_ClassicalNuke;
import TNM_MiniNuke.TNM_MiniNukeHandler;
import TNM_MiniNuke.TNM_MiniNukeMushroomCloud;
import TNM_MiniNuke.TNM_MininukeEntity;
import TNM_MiniNuke.TNM_WheatNukePrimed;
import TNM_PlasmaExplosion.TNM_PlasmaExplosion;
import TNM_RecipeBookClasses.TNM_Dosimeter;
import TNM_RecipeBookClasses.TNM_RecipeBookItem;
import TNM_RegularNukeExplosion.TNM_BaseCloudHandler;
import TNM_RegularNukeExplosion.TNM_BurnWaveHandler;
import TNM_RegularNukeExplosion.TNM_EntityCustomFX;
import TNM_RegularNukeExplosion.TNM_ExplosionHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandler;
import TNM_RegularNukeExplosion.TNM_MHeadHandlerOShell;
import TNM_RegularNukeExplosion.TNM_ShockwaveHandler;
import TNM_RegularNukeExplosion.TNM_StemHandler;
import TNM_Weather.TNM_FalloutWeather;
import TNM_Weather.TNM_RadiationManager;
import forge.Configuration;
import net.minecraft.client.Minecraft;

public class mod_NukeModMain extends BaseMod {
    public static Configuration config;
    public boolean hasRadiationLoaded;
    public TNM_RadiationManager radiationManager;
    public World lastWorld = null;

    static {
        File configFile = new File(Minecraft.getMinecraftDir(), "/config/NukeMod.cfg");
        config = new Configuration(configFile);
        config.load();

    
        config.save();
    }

    //block textures
    public static int NukeTexSide = ModLoader.addOverride("/terrain.png", "NukeTex/TNukeSide.png");
    public static int NukeTexTip = ModLoader.addOverride("/terrain.png", "NukeTex/TNukeTip.png");
    public static int FamNuke0 = ModLoader.addOverride("/terrain.png", "NukeTex/FamNukeBottom.png");
    public static int FamNuke1 = ModLoader.addOverride("/terrain.png", "NukeTex/FamNukeTop.png");
    public static int FamNukeSide = ModLoader.addOverride("/terrain.png", "NukeTex/FamNukeSide.png");
    public static int AssemblyTex = ModLoader.addOverride("/terrain.png", "NukeTex/AssemblyUnit.png");
    public static int ShatteredOb = ModLoader.addOverride("/terrain.png", "NukeTex/ShatteredObsidian.png");
    public static int NukedLogTop = ModLoader.addOverride("/terrain.png", "NukeTex/NukedLogT.png");
    public static int NukedLogSide = ModLoader.addOverride("/terrain.png", "NukeTex/NukedLogS.png");
    public static int TriniteTex = ModLoader.addOverride("/terrain.png", "NukeTex/trinitite.png");
    public static int IGrassSide = ModLoader.addOverride("/terrain.png", "NukeTex/IrradiatedGrass.png");
    public static int IGrassTop = ModLoader.addOverride("/terrain.png", "NukeTex/IrradiatedGrassTop.png");
    public static int IGrassBottom = ModLoader.addOverride("/terrain.png", "NukeTex/IrradiatedGrassBottom.png");
    public static int NukedPlanksTex = ModLoader.addOverride("/terrain.png", "NukeTex/NukedPlanks.png");
    public static int NukeCasingTop = ModLoader.addOverride("/terrain.png", "NukeTex/NukeTip.png");
    public static int NukeCasingBottom = ModLoader.addOverride("/terrain.png", "NukeTex/NukeTipB.png");
    public static int NukeCasingSide = ModLoader.addOverride("/terrain.png", "NukeTex/NukeSide.png");
    public static int CentrifugeSide = ModLoader.addOverride("/terrain.png", "NukeTex/centrifugebottom.png");
    public static int CentrifugeTop = ModLoader.addOverride("/terrain.png", "NukeTex/centrifugetopbott.png");
    public static int miniReactorTex = ModLoader.addOverride("/terrain.png", "NukeTex/Reactortex.png");
    public static int pipetex = ModLoader.addOverride("/terrain.png", "NukeTex/HeatPipeTex.png");
    public static int piebombside = ModLoader.addOverride("/terrain.png", "NukeTex/piebombside.png");
    public static int piebombtop = ModLoader.addOverride("/terrain.png", "NukeTex/piebombtop.png");
    public static int piebombbottom = ModLoader.addOverride("/terrain.png", "NukeTex/piebombbottom.png");
    public static int AssemblySideTex = ModLoader.addOverride("/terrain.png", "NukeTex/AssemblyUnitSide.png");
    public static int antennatip = ModLoader.addOverride("/terrain.png", "NukeTex/Antennatop.png");
    public static int antennaside = ModLoader.addOverride("/terrain.png", "NukeTex/Antenna.png");
    public static int AssemblerTexSide1 = ModLoader.addOverride("/terrain.png", "NukeTex/assemblersidenopower.png");
    public static int AssemblerTexSide2 = ModLoader.addOverride("/terrain.png", "NukeTex/assemblerside1.png");
    public static int AssemblerTexSide3 = ModLoader.addOverride("/terrain.png", "NukeTex/assemblerside2.png");
    public static int AssemblerTexTop = ModLoader.addOverride("/terrain.png", "NukeTex/assemblertop.png");
    public static int pulverizedstonetex = ModLoader.addOverride("/terrain.png", "NukeTex/pulverizedstone.png");
    public static int scorchedstonetex = ModLoader.addOverride("/terrain.png", "NukeTex/scorchedstone.png");
    public static int nukediamondstex = ModLoader.addOverride("/terrain.png", "NukeTex/nukediamonds.png");
    public static int nukedirttex = ModLoader.addOverride("/terrain.png", "NukeTex/drieddirt.png");
    public static int slagtex = ModLoader.addOverride("/terrain.png", "NukeTex/slag.png");
    public static int wastetex = ModLoader.addOverride("/terrain.png", "NukeTex/nuclearwaste.png");
    public static int wheattop = ModLoader.addOverride("/terrain.png", "NukeTex/wheatbombtop.png");
    public static int wheatside = ModLoader.addOverride("/terrain.png", "NukeTex/wheatbombside.png");
    public static int wheatbottom = ModLoader.addOverride("/terrain.png", "NukeTex/wheatbombbottom.png");


    //Item Textures
    public static int Bazookatex = ModLoader.addOverride("/gui/items.png", "NukeTex/familiarweapon.png");
    public static int BazookatexNuke = ModLoader.addOverride("/gui/items.png", "NukeTex/familiarweaponMiniNuke.png");
    public static int BazookatexFamNuke = ModLoader.addOverride("/gui/items.png", "NukeTex/familiarweaponNuke.png");
    public static int Target = ModLoader.addOverride("/gui/items.png", "NukeTex/STarget.png");
    public static int Bullet = ModLoader.addOverride("/gui/items.png", "NukeTex/SBullet.png");
    public static int Propellant = ModLoader.addOverride("/gui/items.png", "NukeTex/Propellant.png");
    public static int ScrewdriverTex = ModLoader.addOverride("/gui/items.png", "NukeTex/Screwdriver.png");
    public static int FlaskE = ModLoader.addOverride("/gui/items.png", "NukeTex/FlaskEmpty.png");
    public static int FlaskW = ModLoader.addOverride("/gui/items.png", "NukeTex/FlaskWater.png");
    public static int FlaskL = ModLoader.addOverride("/gui/items.png", "NukeTex/FlaskLava.png");
    public static int FlaskN = ModLoader.addOverride("/gui/items.png", "NukeTex/FlaskNukage.png");
    public static int ERedstoneTex = ModLoader.addOverride("/gui/items.png", "NukeTex/EnrichedRedstone.png");
    public static int RedcakeTex = ModLoader.addOverride("/gui/items.png", "NukeTex/RedCake.png");
    public static int BerylliumDustTex = ModLoader.addOverride("/gui/items.png", "NukeTex/magnesiumpowder.png");
    public static int BerylliumIngotTex = ModLoader.addOverride("/gui/items.png", "NukeTex/magnesiumingot.png");
    public static int Mininuketex = ModLoader.addOverride("/gui/items.png", "NukeTex/mininuke.png");
    public static int uraniumtex = ModLoader.addOverride("/gui/items.png", "NukeTex/uraniumingot.png");
    public static int plutoniumtex = ModLoader.addOverride("/gui/items.png", "NukeTex/Plutonium238.png");
    public static int rodtex = ModLoader.addOverride("/gui/items.png", "NukeTex/HeavyMatterRod.png");
    public static int FlaskD = ModLoader.addOverride("/gui/items.png", "NukeTex/FlaskDeuterium.png");
    public static int Sideshowtex = ModLoader.addOverride("/gui/items.png", "NukeTex/nuclearsideshowtemplate.png");
    public static int piebombtex = ModLoader.addOverride("/gui/items.png", "NukeTex/piebombitem.png");
    public static int berylliumplate = ModLoader.addOverride("/gui/items.png", "NukeTex/MagnesiumPlate.png");
    public static int nukebarrel = ModLoader.addOverride("/gui/items.png", "NukeTex/NukeBarrel.png");
    public static int detonatortex = ModLoader.addOverride("/gui/items.png", "NukeTex/remotedetonator.png");
    public static int neutroninitiator = ModLoader.addOverride("/gui/items.png", "NukeTex/neutronberylliuminitiator.png");
    public static int hazmathelm = ModLoader.addOverride("/gui/items.png", "NukeTex/hazmathelm.png");
    public static int hazmatpants = ModLoader.addOverride("/gui/items.png", "NukeTex/hazmatpants.png");
    public static int hazmatchest = ModLoader.addOverride("/gui/items.png", "NukeTex/hazmatchest.png");
    public static int hazmatboots = ModLoader.addOverride("/gui/items.png", "NukeTex/hazmatboots.png");
    public static int latextex = ModLoader.addOverride("/gui/items.png", "NukeTex/latex.png");
    public static int rubbertex = ModLoader.addOverride("/gui/items.png", "NukeTex/rawrubber.png");
    public static int repairkittex = ModLoader.addOverride("/gui/items.png", "NukeTex/hrepairkit.png");
    public static int filtertex = ModLoader.addOverride("/gui/items.png", "NukeTex/filter.png");
    public static int heatpipeitemtex = ModLoader.addOverride("/gui/items.png", "NukeTex/heatpipe.png");
    public static int recipebooktex = ModLoader.addOverride("/gui/items.png", "NukeTex/recipebook.png");
    public static int geigertex = ModLoader.addOverride("/gui/items.png", "NukeTex/geigercounter.png");
    public static int dosimetertex = ModLoader.addOverride("/gui/items.png", "NukeTex/dosimetertex.png");

    //---------------------------------------------------------------------------------block declarations---------------------------------------------------------------------------------//
    public static Block BlockNuke = new TNM_NukeBlock(getBlockIdFor("NukeTest", 115)
    , Material.leaves, NukeTexSide, NukeTexTip).setBlockName("NukeTest");

    public static Block BlockFamNuke = new TNM_FamNuke(getBlockIdFor("NuclearExplosive",116)
    , Material.leaves, FamNukeSide, FamNuke1, FamNuke0).setStepSound(Block.soundGrassFootstep).setBlockName("Nuclear Explosive");

    public static Block Warhead1 = new TNM_NuclearWarhead(getBlockIdFor("AssemblyModule",117)
    , Material.leaves, AssemblySideTex, AssemblyTex, AssemblyTex).setHardness(0.4F).setBlockName("Assembly Module");

    public static Block ShattOb = new TNM_Trinitite(getBlockIdFor("ShatteredObsidian",118)
    , ShatteredOb, false, true,false, 0, Material.ground).setStepSound(Block.soundGlassFootstep).setHardness(0.5F).setBlockName("Shattered Obsidian");

    public static Block Trinitite = new TNM_Trinitite(getBlockIdFor("Trinitite",119)
    , TriniteTex, true, true, false, 0, Material.ground).setHardness(0.2F).setBlockName("Trinitite");

    public static Block NukedLog = new TNM_BurntLog(getBlockIdFor("BurntLog",120)
    , NukedLogSide, NukedLogTop).setBlockName("Burnt Log");

    public static Block IGrass = new TNM_IrradiatedGrass(getBlockIdFor("DeadGrass",121)
    , IGrassSide, IGrassTop, IGrassBottom).setBlockName("Irradiated Grass");

    public static Block NukedPlanks = new TNM_Trinitite(getBlockIdFor("BurntPlanks",122)
    , NukedPlanksTex, false, true, false, 0, Material.wood).setHardness(0.1F).setBlockName("BurntPlanks");
    
    public static Block NukeCasing = new TNM_NukeCasing(getBlockIdFor("GunTypeWarheadHousing",123)
    , Material.leaves, NukeCasingSide, NukeCasingTop).setHardness(0.4F).setBlockName("Gun-Type Warhead Housing");

    public static Block CentrifugeBlock = new TNM_Centrifuge(getBlockIdFor("Centrifuge", 124)
    , Material.leaves, CentrifugeSide, CentrifugeTop).setHardness(0.4F).setBlockName("Centrifuge");

    public static Block MiniReactor = new TNM_ReactorMini(getBlockIdFor("MiniReactor",125)
    , Material.leaves, miniReactorTex, AssemblyTex).setHardness(0.4F).setBlockName("Mini Reactor");

    public static Block heatPipe = new TNM_HeatPipe(getBlockIdFor("HeatPipe",126)
    , Material.leaves, pipetex).setHardness(0.1F).setStepSound(Block.soundMetalFootstep).setBlockName("Heat Pipe");

    public static Block AssemblyTable = new TNM_Assembler(getBlockIdFor("AssemblyTable", 127)
    , AssemblerTexSide1, AssemblerTexSide2, AssemblerTexSide3, AssemblerTexTop, AssemblyTex)
    .setHardness(0.4F).setStepSound(Block.soundMetalFootstep).setBlockName("Assembly Table");

    public static Block PieBomb = new TNM_PieBomb(getBlockIdFor("Pie",128)
    , piebombside, piebombtop, piebombbottom).setHardness(0.4F).setStepSound(Block.soundClothFootstep).setBlockName("Pie");

    public static Block Antenna = new TNM_Antenna(getBlockIdFor("DetonationAntenna", 129)
    , antennaside, antennatip).setStepSound(Block.soundMetalFootstep).setBlockName("Detonation Antenna");

    public static Block PulverizedStone = new TNM_Trinitite(getBlockIdFor("PulverizedStone", 130), pulverizedstonetex, true, false, false, 0, Material.ground)
    .setHardness(1F).setBlockName("Pulverized Stone");
    
    public static Block ScorchedStone = new TNM_Trinitite(getBlockIdFor("ScorchedStone", 131), scorchedstonetex, false, false, false, 0, Material.rock)
    .setHardness(1F).setBlockName("Scorched Stone");

    public static Block NukeDiamonds = new TNM_Trinitite(getBlockIdFor("NukeDiamonds", 132), nukediamondstex, false, false, true, Item.diamond.shiftedIndex, Material.rock)
    .setHardness(1F).setBlockName("Nuke Diamonds");

    public static Block NukeDirt = new TNM_Trinitite(getBlockIdFor("DriedDirt", 133), nukedirttex, false, false, false, 0, Material.ground)
    .setHardness(0.1F).setStepSound(Block.soundGravelFootstep).setBlockName("Dried Dirt");

    public static Block SlagBlock = new TNM_Trinitite(getBlockIdFor("Slag", 134), slagtex, false, false, false, 0, Material.iron)
    .setHardness(1F).setStepSound(Block.soundGravelFootstep).setBlockName("Slag");

    public static Block NuclearWaste = new TNM_NuclearWaste(getBlockIdFor("NuclearWaste", 135), wastetex, false).setHardness(0.1F).setBlockName("Nuclear Waste");

    public static Block WheatNuke = new TNM_WheatNuke(getBlockIdFor("WTDOUNuclearBomb", 136)
    , Material.leaves, wheatside, wheattop, wheatbottom).setBlockName("WTDOTU Nuclear Bomb");

    public static Block Fallout = new TNM_NuclearWaste(getBlockIdFor("Fallout", 137), 0, true).setStepSound(Block.soundSandFootstep)
    .setHardness(0.1F).setBlockName("Fallout");

    //---------------------------------------------------------------------------------Item declarations---------------------------------------------------------------------------------//
    public static Item Bazooka = new TNM_Bazooka(getItemIdFor("FamiliarWeapon",109)
    ).setIconIndex(Bazookatex).setItemName("Familiar Weapon");

    public static Item NukeTarget = new Item(getItemIdFor("NuclearTarget",110)
    ).setIconIndex(Target).setMaxStackSize(1).setItemName("Nuclear Target");
    
    public static Item NukeBullet = new Item(getItemIdFor("EnrichedBullet",111)
    ).setIconIndex(Bullet).setMaxStackSize(1).setItemName("Enriched Bullet");
    
    public static Item NPropellant = new Item(getItemIdFor("Propellant",112)
    ).setIconIndex(Propellant).setMaxStackSize(1).setItemName("Propellant");

    public static Item Screwdriver = new TNM_Screwdriver(getItemIdFor("Screwdriver",114)
    ).setIconIndex(ScrewdriverTex).setMaxStackSize(1).setItemName("Screwdriver");

    public static Item EmptyFlask = new TNM_Flask(getItemIdFor("EmptyFlask",115)
    , false).setIconIndex(FlaskE).setItemName("Empty Flask");

    public static Item FlaskWater = new TNM_Flask(getItemIdFor("LavaFlask",116)
    , true).setIconIndex(FlaskW).setItemName("Water Flask");

    public static Item FlaskLava = new TNM_Flask(getItemIdFor("WaterFlask",117)
    , true).setIconIndex(FlaskL).setItemName("Lava Flask");

    public static Item FlaskNuclearWaste = new TNM_Fuel(getItemIdFor("ReclaimedNuclearMateria",118)
    ,100).setIconIndex(FlaskN).setItemName("Reclaimed Nuclear Material");

    public static Item FlaskDeuterium = new Item(getItemIdFor("DeuteriumFlask",119)
    ).setIconIndex(FlaskD).setItemName("Deuterium Flask");

    public static Item EnrichedRedstone = new Item(getItemIdFor("EnrichedRedstonePellet",120)
    ).setIconIndex(ERedstoneTex).setItemName("Enriched Redstone Pellet");

    public static Item RedCake = new Item(getItemIdFor("RedCake",121)
    ).setIconIndex(RedcakeTex).setItemName("Red Cake");

    public static Item BerylliumDust = new Item(getItemIdFor("BerylDust",122)).setIconIndex(BerylliumDustTex).setItemName("Beryl Dust");
    
    public static Item BerylliumIngot = new Item(getItemIdFor("BerylliumIngot",123)
    ).setIconIndex(BerylliumIngotTex).setItemName("Beryllium Ingot");

    public static Item MiniNuke = new Item(getItemIdFor("MiniNuke",124)
    ).setIconIndex(Mininuketex).setItemName("Mini Nuke");

    public static Item Uranium = new Item(getItemIdFor("UraniumIngot",125)
    ).setIconIndex(uraniumtex).setItemName("Exotic Uranium Ingot");

    public static Item Plutonium = new Item(getItemIdFor("Plutonium238Pellet",126)
    ).setIconIndex(plutoniumtex).setItemName("Plutonium 238 Pellet");

    public static Item HeavyRod = new Item(getItemIdFor("UraniumRod",127)
    ).setIconIndex(rodtex).setItemName("Exotic Uranium Rod");

    public static Item SideShowBP = new Item(getItemIdFor("NuclearSideshowBlueprint",128)
    ).setIconIndex(Sideshowtex).setItemName("Nuclear Sideshow Blueprint");

    public static Item Pie = (new ItemReed(getItemIdFor("Pie",129)
    , PieBomb)).setMaxStackSize(1).setIconIndex(piebombtex).setItemName("Pie");

    public static Item Berylliumplate = new Item(getItemIdFor("BerylliumReflector",130)
    ).setIconIndex(berylliumplate).setItemName("Beryllium Reflector");

    public static Item Nukebarrel = new Item(getItemIdFor("GunTypeBarrel",131)
    ).setIconIndex(nukebarrel).setItemName("Gun-Type Barrel");

    public static Item detonator = new TNM_Detonator(getItemIdFor("detonator",132)
    ).setIconIndex(detonatortex).setItemName("detonator");

    public static Item latex = new Item(getItemIdFor("Rubber",137)
    ).setIconIndex(latextex).setItemName("Rubber");

    public static Item rawrubber = new Item(getItemIdFor("RawRubber",138)
    ).setIconIndex(rubbertex).setItemName("Raw Rubber");

    public static Item HeatPipeItem = (new ItemReed(getItemIdFor("HeatPipeItem",141)
    , heatPipe)).setIconIndex(heatpipeitemtex).setItemName("Heat Pipe");

    public static Item RecipeBook = new TNM_RecipeBookItem(getItemIdFor("RecipeBook",142)).setIconIndex(recipebooktex).setItemName("Recipe Book");

    public static Item geigercounter = new Item(getItemIdFor("GeigerCounter",150)).setMaxStackSize(1).setIconIndex(geigertex).setItemName("Geiger Counter");

    //nonstatic items
    public Item DosimeterItem;


    //---------------------------------------------------------------------------------armor declarations---------------------------------------------------------------------------------//

    public static final Item HazmatHelmet = new TNM_HazmatArmor(getItemIdFor("HazmatSuitHelmet",133)
    , 0, ModLoader.AddArmor("hazmatsuit"), 0).setMaxDamage(600).setIconIndex(hazmathelm)
    .setItemName("Hazmat Suit Helmet");

    public static final Item HazmatChest  = new TNM_HazmatArmor(getItemIdFor("HazmatSuitJacket",134)
    , 0, ModLoader.AddArmor("hazmatsuit"), 1).setMaxDamage(1000).setIconIndex(hazmatchest)
    .setItemName("Hazmat Suit Jacket");

    public static final Item HazmatLegs   = new TNM_HazmatArmor(getItemIdFor("HazmatSuitPants",135)
    , 0, ModLoader.AddArmor("hazmatsuit"), 2).setMaxDamage(1000).setIconIndex(hazmatpants)
    .setItemName("Hazmat Suit Pants");

    public static final Item HazmatBoots  = new TNM_HazmatArmor(getItemIdFor("HazmatSuitBoots",136)
    , 0, ModLoader.AddArmor("hazmatsuit"), 3).setMaxDamage(1000).setIconIndex(hazmatboots)
    .setItemName("Hazmat Suit Boots");

    //---------------------------------------------------------------------------------repair Item declarations---------------------------------------------------------------------------------//

    public static Item RepairKit = new TNM_RepairItem(getItemIdFor("HazmatRepairKit",139)
    , false, 100).setMaxStackSize(1).setIconIndex(repairkittex).setItemName("Hazmat Repair Kit");

    public static Item Filter = new TNM_RepairItem(getItemIdFor("MaskFilter",140)
    , true, 1).setMaxStackSize(4).setIconIndex(filtertex).setItemName("Mask Filter");

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

    //particle textures
    public static int Flash;
    public static int Smoke;
    public static int Particulate;
    public static int Smokepuff;

    //block renderers
    public static int RenderHeatPipeid;



    public mod_NukeModMain(){
        this.radiationManager = new TNM_RadiationManager();

        //Harvest Level
        MinecraftForge.setBlockHarvestLevel(NukeDiamonds, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ScorchedStone, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(PulverizedStone, "pickaxe", 1);
        
        //particle texture assignment
        Flash = ModLoader.addOverride("/gui/items.png", "/NukeTex/FlashParticle.png");
        Smoke = ModLoader.addOverride("/gui/items.png", "/NukeTex/BigSmoke.png");
        Smokepuff = ModLoader.addOverride("/gui/items.png", "/NukeTex/Smokepuff.png");
        Particulate = ModLoader.addOverride("/gui/items.png", "/NukeTex/particle.png");

        //custom block renderers
        RenderHeatPipeid = ModLoader.getUniqueBlockModelID(this, true);

        //nonstatic items
        DosimeterItem = new TNM_Dosimeter(getItemIdFor("Dosimeter",151), this.radiationManager).setIconIndex(dosimetertex).setItemName("Dosimeter");

        //block registry
        ModLoader.RegisterBlock(BlockNuke);
        ModLoader.RegisterBlock(Warhead1);
        ModLoader.RegisterBlock(BlockFamNuke);
        ModLoader.RegisterBlock(NukedLog);
        ModLoader.RegisterBlock(Trinitite);
        ModLoader.RegisterBlock(IGrass);
        ModLoader.RegisterBlock(ShattOb);
        ModLoader.RegisterBlock(NukedPlanks);
        ModLoader.RegisterBlock(NukeCasing);
        ModLoader.RegisterBlock(CentrifugeBlock);
        ModLoader.RegisterBlock(MiniReactor);
        ModLoader.RegisterBlock(heatPipe);
        ModLoader.RegisterBlock(AssemblyTable);
        ModLoader.RegisterBlock(PieBomb);
        ModLoader.RegisterBlock(Antenna);
        ModLoader.RegisterBlock(NukeDiamonds);
        ModLoader.RegisterBlock(ScorchedStone);
        ModLoader.RegisterBlock(PulverizedStone);
        ModLoader.RegisterBlock(NukeDirt);
        ModLoader.RegisterBlock(SlagBlock);
        ModLoader.RegisterBlock(NuclearWaste);
        ModLoader.RegisterBlock(WheatNuke);
        ModLoader.RegisterBlock(Fallout);


        //Name registry
        ModLoader.AddName(BlockNuke, "NukeTest");
        ModLoader.AddName(Warhead1, "Assembly Module");
        ModLoader.AddName(BlockFamNuke, "Nuclear Explosive");
        ModLoader.AddName(NukedLog, "Burnt Log");
        ModLoader.AddName(NukedPlanks, "Burnt Planks");
        ModLoader.AddName(Trinitite, "Trinitite");
        ModLoader.AddName(ShattOb, "Shattered Obsidian");
        ModLoader.AddName(IGrass, "Irradiated Grass");
        ModLoader.AddName(NukeCasing, "Gun-Type Warhead Housing");
        ModLoader.AddName(CentrifugeBlock, "Centrifuge");
        ModLoader.AddName(MiniReactor, "Mini Reactor");
        ModLoader.AddName(heatPipe, "Heat Pipe");
        ModLoader.AddName(AssemblyTable, "Assembly Table");
        ModLoader.AddName(PieBomb, "Pie");
        ModLoader.AddName(Antenna, "Detonation Antenna");
        ModLoader.AddName(ScorchedStone, "Scorched Stone");
        ModLoader.AddName(PulverizedStone, "Pulverized Stone");
        ModLoader.AddName(NukeDiamonds, "Nuke Diamonds");
        ModLoader.AddName(NukeDirt, "Dried Dirt");
        ModLoader.AddName(SlagBlock, "Slag");
        ModLoader.AddName(NuclearWaste, "Nuclear Waste");
        ModLoader.AddName(WheatNuke, "WTDOTU Nuclear Bomb");
        ModLoader.AddName(Fallout, "Fallout");

        //Item Name registry
        ModLoader.AddName(Bazooka, "Familiar Weapon");
        ModLoader.AddName(NukeBullet, "Enriched Bullet");
        ModLoader.AddName(NukeTarget, "Nuclear Target");
        ModLoader.AddName(NPropellant, "Propellant");
        ModLoader.AddName(Screwdriver, "Screwdriver");
        ModLoader.AddName(EmptyFlask, "Empty Flask");
        ModLoader.AddName(FlaskLava, "Lava Flask");
        ModLoader.AddName(FlaskWater, "Water Flask");
        ModLoader.AddName(EnrichedRedstone, "Enriched Redstone Pellet");
        ModLoader.AddName(FlaskNuclearWaste, "Reclaimed Nuclear Material");
        ModLoader.AddName(RedCake, "Red Cake");
        ModLoader.AddName(BerylliumDust, "Beryl Dust");
        ModLoader.AddName(BerylliumIngot, "Beryllium Ingot");
        ModLoader.AddName(Uranium, "Exotic Uranium Ingot");
        ModLoader.AddName(Plutonium, "Plutonium 238");
        ModLoader.AddName(MiniNuke, "Mini Nuke");
        ModLoader.AddName(HeavyRod, "Exotic Uranium Rod");
        ModLoader.AddName(FlaskDeuterium, "Deuterium Flask");
        ModLoader.AddName(Pie, "Pie");
        ModLoader.AddName(SideShowBP, "Nuclear Sideshow Blueprint");
        ModLoader.AddName(Berylliumplate, "Beryllium Reflector");
        ModLoader.AddName(Nukebarrel, "Gun-Tye Barrel");
        ModLoader.AddName(rawrubber, "Raw Rubber");
        ModLoader.AddName(latex, "Rubber");
        ModLoader.AddName(RepairKit, "Hazmat Repair Kit");
        ModLoader.AddName(Filter, "Mask Filter");
        ModLoader.AddName(detonator, "detonator");
        ModLoader.AddName(HeatPipeItem, "Heat Pipe");
        ModLoader.AddName(RecipeBook, "Nuclear Recipe Book");
        ModLoader.AddName(geigercounter, "Geiger Counter");
        ModLoader.AddName(DosimeterItem, "Dosimeter");
        

        //Armor Name Registry
        ModLoader.AddName(HazmatHelmet, "Hazmat Suit Helmet");
        ModLoader.AddName(HazmatChest, "Hazmat Suit Jacket");
        ModLoader.AddName(HazmatLegs, "Hazmat Suit Pants");
        ModLoader.AddName(HazmatBoots, "Hazmat Suit Boots");

        //Entity registry
        ModLoader.RegisterEntityID(TNM_NukePrimed.class, "NukePrimed", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_FamNukePrimed.class, "FamNukePrimed", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_ExplosionHandler.class, "NukeExplosionHandler", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_BaseCloudHandler.class, "BaseCloudHandler", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_ShockwaveHandler.class, "ShockwaveHandler", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_StemHandler.class, "StemHandler", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_MHeadHandler.class, "MushroomHeadHandler", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_MHeadHandlerOShell.class, "MushroomShellHandler", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_BurnWaveHandler.class, "Burnwave", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_PlasmaExplosion.class, "PlasmaExplosion", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_MiniNukeHandler.class, "MiniNuke", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_MiniNukeMushroomCloud.class, "MiniNukeMushroomCloud", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_MininukeEntity.class, "Mininuke", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_BakerExplosion.class, "Baker", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_BakerMushroomCloud.class, "BakerMushroom", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_WheatNukePrimed.class, "WheatNukePrimed", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(TNM_FalloutWeather.class, "FalloutWeather", ModLoader.getUniqueEntityId()); //WORK IN PROGRESS
        ModLoader.RegisterEntityID(TNM_EntityCustomFX.class, "EntityCustomFX", ModLoader.getUniqueEntityId());

        //TileEntity registry
        ModLoader.RegisterTileEntity(TNM_TileEntityWarhead.class, "TileEntityWarhead");
        ModLoader.RegisterTileEntity(TNM_TileEntityCentrifuge.class, "TileEntityCentrifuge");
        ModLoader.RegisterTileEntity(TNM_TileEntityRGenerator.class, "TileEntityRGenerator");
        ModLoader.RegisterTileEntity(TNM_TileEntityHeatPipe.class, "TileEntityHeatPipe");
        ModLoader.RegisterTileEntity(TNM_TileEntityAssembler.class, "TileEntityAssembler");


        //centrifuge recipes
        TNM_CentrifugeRecipes.centrifuge().addCentrifuge(
            mod_NukeModMain.RedCake.shiftedIndex,
            new ItemStack(mod_NukeModMain.EnrichedRedstone), // guaranteed
        
            // Slot 5 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(mod_NukeModMain.Trinitite), 0),
            },
        
            // Slot 6 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(mod_NukeModMain.BerylliumDust), 45),
            }
        
        );
        TNM_CentrifugeRecipes.centrifuge().addCentrifuge(
            Block.gravel.blockID,
            new ItemStack(Block.dirt), // guaranteed
        
            // Slot 5 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(mod_NukeModMain.Trinitite), 0),
            },
        
            // Slot 6 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(Item.flint), 1),
            }
        
        );
        TNM_CentrifugeRecipes.centrifuge().addCentrifuge(
            mod_NukeModMain.Trinitite.blockID,
            new ItemStack(Block.dirt), // guaranteed
        
            // Slot 5 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(mod_NukeModMain.FlaskNuclearWaste), 6),
            },
        
            // Slot 6 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(mod_NukeModMain.BerylliumDust), 1),
            }
        
        );
        TNM_CentrifugeRecipes.centrifuge().addCentrifuge(
            Item.slimeBall.shiftedIndex,
            new ItemStack(rawrubber), // guaranteed
        
            // Slot 5 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(mod_NukeModMain.FlaskNuclearWaste), 0)
            },
        
            // Slot 6 pool
            new TNM_CentrifugeRecipes.WeightedOutput[]{
                new TNM_CentrifugeRecipes.WeightedOutput(new ItemStack(mod_NukeModMain.BerylliumDust), 0),
            }
        
        );
        //smelting
        ModLoader.AddSmelting(mod_NukeModMain.BerylliumDust.shiftedIndex, new ItemStack(mod_NukeModMain.BerylliumIngot));
        ModLoader.AddSmelting(mod_NukeModMain.rawrubber.shiftedIndex, new ItemStack(mod_NukeModMain.latex));

        //shapeless crafting
        ModLoader.AddShapelessRecipe(new ItemStack(mod_NukeModMain.RedCake), new Object[]{Item.redstone, Item.bucketWater, Item.gunpowder});
        ModLoader.AddShapelessRecipe(new ItemStack(RecipeBook, 1), new Object[] {Item.book, Item.redstone, new ItemStack(Item.dyePowder, 1, 6)});

        //assembly
        /*
        // disabled for the time being

        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(BlockFamNuke),
            new ItemStack(SideShowBP), // blueprint item
            true, // requires blueprint
            new String[] {
                "G#G",
                "#G#",
                "G#G",
            },
            '#', HeavyRod,
            'G', Block.tnt
        );

        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(HeatPipeItem),
            null,
            false,
            new String[]{
                "R.R",
                "R.R",
                "R.R",
                "R.R"
            },
            'R', Item.ingotIron
        );
        */
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(CentrifugeBlock),
            null,
            false,
            new String[]{
                "RGR",
                "RIR",
                "RRR"
            },
            'R', Item.ingotIron,
            'I', Item.redstone,
            'G', Block.glass
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(NPropellant),
            null, // blueprint item
            false, // requires blueprint
            new String[] {
                "G.",
                "G.",
            },
            'G', Item.gunpowder
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(NukeBullet),
            null, // blueprint item
            false, // requires blueprint
            new String[] {
                "G.",
                "G.",
            },
            'G', mod_NukeModMain.EnrichedRedstone
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(NukeTarget),
            null,
            false,
            new String[] {
                "E#E",
                "E#E",
                "RER"
            }, 'E', EnrichedRedstone,
            'R', Berylliumplate
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(Berylliumplate),
            null, // blueprint item
            false, // requires blueprint
            new String[] {
                "G#",
                "G#"
            },
            'G', Block.obsidian,
            '#', BerylliumIngot
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(Nukebarrel),
            null,
            false,
            new String[] {
                "I..",
                ".I.",
                "..I"
            }, 'I', Item.ingotIron,
            '.', null
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(Warhead1),
            new ItemStack(Screwdriver),
            true,
            new String[] {
                "I#I",
                "I#I",
                "I#I"
            }, 'I', Item.ingotIron,
            '#', Nukebarrel
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(NukeCasing),
            new ItemStack(Screwdriver),
            true,
            new String[] {
                "IAI",
                ".#.",
                "IAI"
            }, 'I', Item.ingotIron,
            '.', Berylliumplate,
            'A', Warhead1
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(Antenna),
            null,
            false,
            new String[]{
                "###",
                "#R#",
            },
            '#', Item.ingotIron,
            'R', Block.torchRedstoneActive
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(detonator),
            null,
            false,
            new String[]{
                ".B",
                "TI",
                ".I"
            },
            'B', Block.button,
            'T', Block.torchRedstoneActive,
            'I', Item.ingotIron
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(HazmatHelmet),
            null,
            false,
            new String[]{
                "III",
                "I.I",
                "IRI"
            },
            'I', latex,
            'R', Item.ingotIron
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(HazmatChest),
            null,
            false,
            new String[]{
                "I.I",
                "III",
                "III"
            },
            'I', latex
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(HazmatLegs),
            null,
            false,
            new String[]{
                "III",
                "I.I",
                "I.I"
            },
            'I', latex
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(HazmatBoots),
            null,
            false,
            new String[]{
                "I.I",
                "I.I"
            },
            'I', latex
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(RepairKit),
            null,
            false,
            new String[]{
                "RIR",
                "RRR"
            },
            'I', latex,
            'R', Item.ingotIron
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(Filter),
            null,
            false,
            new String[]{
                "RPR",
                "RCR",
                "RPR",
                ".R."
            },
            'P', Item.paper, 'C', Item.coal,
            'R', Item.ingotIron
        );
        TNM_AssemblyRecipes.getInstance().addRecipe(
            new ItemStack(geigercounter),
            null,
            false,
            new String[]{
                "RTR",
                "IDI",
                "III"
            },
            'R', Berylliumplate, 'T', Block.torchRedstoneActive,
            'I', Item.ingotIron, 'D', Item.redstone
        );



        //crafting
        ModLoader.AddRecipe(new ItemStack(Bazooka, 1), new Object[]
        {
            "I#I",
            "I#I",
            "IGI",
            Character.valueOf('I'),Item.ingotIron,
            Character.valueOf('G'),Item.gunpowder
        });
        ModLoader.AddRecipe(new ItemStack(EmptyFlask, 4), new Object[]
        {
            "IGI",
            "I#I",
            "III",
            Character.valueOf('I'),Item.ingotIron,
            Character.valueOf('G'),Block.glass
        });
        ModLoader.AddRecipe(new ItemStack(Screwdriver, 1), new Object[]
        {
            "I#",
            "#L",
            Character.valueOf('I'),Item.ingotIron,
            Character.valueOf('L'),new ItemStack(Item.dyePowder, 1, 4)
        });
        ModLoader.AddRecipe(new ItemStack(AssemblyTable, 1), new Object[]
        {
            "III",
            "IRI",
            "BBB",
            Character.valueOf('I'),Item.ingotIron,
            Character.valueOf('B'),Block.stairSingle,
            Character.valueOf('R'),Item.redstone
        });
        

        ModLoader.SetInGameHook(this, true, false);
        config.save();
    }

    public boolean OnTickInGame(Minecraft mc) {
        EntityPlayer player = mc.thePlayer;

        //fallout tint
        if (mc.currentScreen == null && TNM_FalloutWeather.isPlayerInFallout(mc.theWorld, player)) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    
            // Switch to orthographic projection (screen space)
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0, mc.displayWidth, mc.displayHeight, 0, -1, 1);
    
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
    
            // Faint dark‑green tint
            GL11.glColor4f(0.1F, 0.2F, 0.1F, 0.3F);
    
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.addVertex(0, mc.displayHeight, 0);
            t.addVertex(mc.displayWidth, mc.displayHeight, 0);
            t.addVertex(mc.displayWidth, 0, 0);
            t.addVertex(0, 0, 0);
            t.draw();
    
            // Restore matrices
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
    
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        //radiation logic
        for (Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof EntityLiving) {
                EntityLiving living = (EntityLiving) o;
                double current = this.radiationManager.getRadiationLevel(living);

                // Block under feet
                int bx = (int)Math.floor(living.posX);
                int by = (int)Math.floor(living.posY - 2);
                int bz = (int)Math.floor(living.posZ);

                int blockId = mc.theWorld.getBlockId(bx, by, bz);
                Block block = Block.blocksList[blockId];
                Random rand = mc.theWorld.rand;

                //block radiation
                if (mc.theWorld.getWorldTime() % 10 * rand.nextInt(5) == 0) {
                    if (block instanceof TNM_Trinitite && ((TNM_Trinitite)block).irradiated) {
                        if (living instanceof EntityPlayer) {
                            EntityPlayer p = (EntityPlayer) living;
                    
                            ItemStack[] armor = p.inventory.armorInventory;
                            boolean fullHazmat =
                                armor[3] != null && armor[3].getItem() == mod_NukeModMain.HazmatHelmet &&
                                armor[2] != null && armor[2].getItem() == mod_NukeModMain.HazmatChest &&
                                armor[1] != null && armor[1].getItem() == mod_NukeModMain.HazmatLegs &&
                                armor[0] != null && armor[0].getItem() == mod_NukeModMain.HazmatBoots;
                    
                            boolean hasGeiger = false;
                            for (ItemStack stack : p.inventory.mainInventory) {
                                if (stack != null && stack.getItem() == mod_NukeModMain.geigercounter) {
                                    hasGeiger = true;
                                    break;
                                }
                            }
                    
                            ItemStack helmet = armor[3];
    
                            if (fullHazmat && helmet != null) {
                                int max = helmet.getMaxDamage();
                                if (helmet.getItemDamage() < max - 1) {
                                    helmet.damageItem(1, p);
                                    if (hasGeiger) TNM_SoundHelper.playEntitySound(p, "geiger2.wav", 0.6F);
                                } else {
                                    helmet.setItemDamage(max - 1);
                                    radiationManager.setRadiationLevel(p, current + 0.1);
                                    if (hasGeiger) TNM_SoundHelper.playEntitySound(p, "geiger3.wav", 0.6F);
                                }
                            } else {
                                radiationManager.setRadiationLevel(p, current + 0.1);
                                if (hasGeiger) TNM_SoundHelper.playEntitySound(p, "geiger3.wav", 0.6F);
                            }
                        } else {
                            // Non‑player entities
                            radiationManager.setRadiationLevel(living, current + 0.1);
                        }
                    }
                }

                //damage / future poisoning logic
                if (current > 100){
                    if (mc.theWorld.getWorldTime() % 200 == 0) {
                        //insert unique radiation sickness logic
                    }
                }

                
                //contamination decay
                if (mc.theWorld.getWorldTime() % 500 == 0) {
                    if (current > 0){
                        this.radiationManager.setRadiationLevel(living, current - 0.1);
                    }
                    if (current < 0){
                        this.radiationManager.setRadiationLevel(living, 0.0);
                    }
                }
            }
        }

        if (mc.theWorld != null && mc.thePlayer != null && !hasRadiationLoaded && !(mc.currentScreen instanceof GuiIngameMenu)) {
            this.radiationManager.loadRadiation(mc.theWorld);
            hasRadiationLoaded = true;
            lastWorld = mc.theWorld;
            System.err.println("Radiation loaded for " + mc.theWorld.getWorldInfo().getWorldName());
        }
        
        if (mc.currentScreen instanceof GuiIngameMenu && hasRadiationLoaded) {
            this.radiationManager.saveRadiation(mc.theWorld);
            hasRadiationLoaded = false;
            System.err.println("radiation saved");
        }

        return true;
    }


    //register custom block renderers
    public boolean RenderWorldBlock(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block, int modelId){
        if (modelId == RenderHeatPipeid) {
            return TNM_RenderHeatPipe.render(renderer, world, x, y, z, block, world.getBlockMetadata(x,y,z));
        }
        return false;
    }

    public static void triggerNukeBlastStep1(World world, int x, int y, int z) {
        new TNM_NuclearExplosionBlast(world, x, y, z);
    }

    public static void triggerNukeBlastStep2(World world, int x, int y, int z, int blastradius, double fuzziness) {
        new TNM_NuclearExplosionBlastStep2(world, x, y, z, blastradius, fuzziness);
    }

    public static void ShockWaveTypes(World world, String Type, int x, int y, int z) {
        Minecraft mc = ModLoader.getMinecraftInstance();

        if (Type.equals("Foliage")){
            new TNM_LeafKiller(world, x, y, z);
        }
        if (Type.equals("Burn")){
            new TNM_BurnLoosen(world, x, y, z);
        }
    }
    

    public static void triggerNukeCrater(World world, int x, int y, int z, int depth, int radius, double fuzziness) {
        new TNM_NuclearExplosionCrater(world, x, y, z, depth, radius, fuzziness);
    }

    public static void spawnTNMParticle(World world, String name, double x, double y, double z, double vx, double vy, double vz, float grav, float R, float G, float B, float brightness, float scale, boolean isFullbright, int age, boolean EnableScaleAging, boolean EnableColorAging, float TRed, float TGreen, float TBlue, float TScale, boolean DragisOn) {
        Minecraft mc = ModLoader.getMinecraftInstance();
        EntityFX fx = null;
        
        if (name.equals("Flash")) {
            fx = new TNM_SmokeSystem(world, 200.0D, Flash, x, y, z, vx, vy, vz, grav, R, G, B, brightness, scale, isFullbright, age, EnableScaleAging, EnableColorAging, TRed, TGreen, TBlue, TScale,DragisOn);
        }

        if (name.equals("Smoke")) {
            fx = new TNM_SmokeSystem(world, 200.0D ,Smoke, x, y, z, vx, vy, vz, grav, R, G, B, brightness, scale, isFullbright, age, EnableScaleAging, EnableColorAging, TRed, TGreen, TBlue, TScale,DragisOn);
        }

        if (name.equals("Smokepuff")) {
            fx = new TNM_SmokeSystem(world, 200.0D ,Smokepuff, x, y, z, vx, vy, vz, grav, R, G, B, brightness, scale, isFullbright, age, EnableScaleAging, EnableColorAging, TRed, TGreen, TBlue, TScale,DragisOn);
        }


        if (name.equals("Particulate")) {
            fx = new TNM_SmokeSystem(world, 1.0D, Particulate, x, y, z, vx, vy, vz, grav, R, G, B, brightness, scale, isFullbright, age, EnableScaleAging, EnableColorAging, TRed, TGreen, TBlue, TScale,DragisOn);
        }

        if (name.equals("Fallout")) {
            fx = new TNM_SmokeSystem(world, 0.5D, Particulate, x, y, z, vx, vy, vz, grav, R, G, B, brightness, scale, isFullbright, age, EnableScaleAging, EnableColorAging, TRed, TGreen, TBlue, TScale,DragisOn);
        }


        if (fx != null ) {
            mc.effectRenderer.addEffect(fx);
        }

    }

    public static void spawnStemparticle(World world, String name, double x, double y, double z, double motX, double motY, double motZ, float scale, int maxAge, int freezeAt, boolean useDrag, float R, float G, float B){
        Minecraft mc = ModLoader.getMinecraftInstance();
        EntityFX fx = null;
        if (name.equals("Smoke")){
            fx = new TNM_StemParticle(world, Smoke, x, y, z, motX, motY, motZ, scale, maxAge, freezeAt, useDrag, R, G, B);
        }
        if (name.equals("Smokepuff")){
            fx = new TNM_StemParticle(world, Smokepuff, x, y, z, motX, motY, motZ, scale, maxAge, freezeAt, useDrag, R, G, B);
        }
        mc.effectRenderer.addEffect(fx);
    }
    

    public static void newexplosionNuke(World world, Entity exploder,
        double x, double y, double z, float size, boolean flaming, int flameblock, boolean damage) {
        new TNM_ClassicalNuke(world, exploder, x, y, z, size, flaming, flameblock, damage);
    }
    

    @Override
    public void AddRenderer(Map map) {
        // renderers
        map.put(TNM_NukePrimed.class, new TNM_RenderNuke());
        map.put(TNM_FamNukePrimed.class, new TNM_RenderFamNuke());
        map.put(TNM_ExplosionHandler.class, new TNM_RenderInvisible());
        map.put(TNM_BaseCloudHandler.class, new TNM_RenderInvisible());
        map.put(TNM_ShockwaveHandler.class, new TNM_RenderInvisible());
        map.put(TNM_StemHandler.class, new TNM_RenderInvisible());
        map.put(TNM_MHeadHandler.class, new TNM_RenderInvisible());
        map.put(TNM_MHeadHandlerOShell.class, new TNM_RenderInvisible());
        map.put(TNM_BurnWaveHandler.class, new TNM_RenderInvisible());
        map.put(TNM_PlasmaExplosion.class, new TNM_RenderInvisible());
        map.put(TNM_MiniNukeHandler.class, new TNM_RenderInvisible());
        map.put(TNM_MiniNukeMushroomCloud.class, new TNM_RenderInvisible());
        map.put(TNM_MininukeEntity.class, new RenderSnowball(Mininuketex));
        map.put(TNM_BakerMushroomCloud.class, new TNM_RenderInvisible());
        map.put(TNM_BakerExplosion.class, new TNM_RenderInvisible());
        map.put(TNM_WheatNukePrimed.class, new TNM_RenderWheatNuke());
        map.put(TNM_FalloutWeather.class, new TNM_RenderInvisible());
        map.put(TNM_EntityCustomFX.class, new TNM_RenderCustomParticle());
    }

    public static int getBlockIdFor(String key, int defaultID) {
        if (config == null) return defaultID;
        Property prop = config.getOrCreateIntProperty(key, 1, defaultID); // 1 = block category
        if (prop.value == null || prop.value.isEmpty()) {
            prop.value = Integer.toString(defaultID); // force default into file
        }
        // reassign to ensure Forge sees it as changed
        prop.value = prop.value;
        return Integer.parseInt(prop.value);
    }
    
    public static int getItemIdFor(String key, int defaultID) {
        if (config == null) return defaultID;
        Property prop = config.getOrCreateIntProperty(key, 2, defaultID); // 2 = item category
        if (prop.value == null || prop.value.isEmpty()) {
            prop.value = Integer.toString(defaultID);
        }
        prop.value = prop.value;
        return Integer.parseInt(prop.value);
    }
    
    
    
    public String Version() {
        return "TNM's Nuke Mod: PUBLIC BETA 0.5";
    }
}

/*

*/