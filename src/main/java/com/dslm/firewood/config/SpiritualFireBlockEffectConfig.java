package com.dslm.firewood.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SpiritualFireBlockEffectConfig
{
    public static ForgeConfigSpec.DoubleValue POTION_BASE_DAMAGE;
    public static ForgeConfigSpec.DoubleValue TELEPORT_BASE_DAMAGE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SET_BLOCK_NAME_BLACKLIST;
    
    public static ForgeConfigSpec.ConfigValue<String> GROUND_DEFAULT_BLOCK;
    
    public static ForgeConfigSpec.IntValue FIRED_FLESH_TIME;
    
    public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER)
    {
        SERVER_BUILDER
                .comment("Settings for the Spiritual Fire Block Effects")
                //.translation("config.firewood.spiritual_fire_block_effects")
                .push("spiritualFire");
        
        //major
        SERVER_BUILDER
                .comment("Settings for Major Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.major")
                .push("spiritualFireMajor");
    
        //potion
        SERVER_BUILDER
                .comment("Settings for Potion Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.major.potion")
                .push("spiritualFireMajorPotion");
        POTION_BASE_DAMAGE = SERVER_BUILDER
                .comment("the base damage for trigger a potion effect")
                .translation("config.firewood.spiritual_fire_block_effects.major.potion.base_damage")
                .defineInRange("damage", 0.1, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    
        //teleport
        SERVER_BUILDER
                .comment("Settings for Teleport Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.major.teleport")
                .push("spiritualFireMajorTeleport");
        TELEPORT_BASE_DAMAGE = SERVER_BUILDER
                .comment("the base damage for trigger a teleport effect")
                .translation("config.firewood.spiritual_fire_block_effects.major.teleport.base_damage")
                .defineInRange("damage", 0.1, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
        
        SERVER_BUILDER.pop();
        
        
        //minor
        SERVER_BUILDER
                .comment("Settings for Minor Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.minor")
                .push("spiritualFireMinor");
        
        //ground
        SERVER_BUILDER
                .comment("Settings for Ground Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.minor.ground")
                .push("spiritualFireMinorGround");
        GROUND_DEFAULT_BLOCK = SERVER_BUILDER
                .comment("the default block ID for a ground effect")
                .translation("config.firewood.spiritual_fire_block_effects.minor.ground.default_block")
                .define("block", "minecraft:bone_block");
        SERVER_BUILDER.pop();
        
        
        SERVER_BUILDER.pop();
        
        //global
        SERVER_BUILDER
                .comment("Settings for Others")
                //.translation("config.firewood.spiritual_fire_block_effects.others")
                .push("others");
        FIRED_FLESH_TIME = SERVER_BUILDER
                .comment("Fired Flesh effect time (tick); it is also a cooldown time of triggering spiritual fire effects")
                .translation("config.firewood.spiritual_fire_block_effects.others.fired_flesh_interval")
                .defineInRange("time", 100, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    
    
        SERVER_BUILDER.pop();
    }
    
    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER)
    {
        COMMON_BUILDER
                .comment("Settings for the Spiritual Fire Block Effects")
                //.translation("config.firewood.spiritual_fire_block_effects")
                .push("spiritualFire");
        
        //major
        COMMON_BUILDER
                .comment("Settings for Major Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.major")
                .push("spiritualFireMajor");
        
        //set block name
        ArrayList<String> defaultBlacklist = new ArrayList<>()
        {{
            for(DyeColor dyeColor : DyeColor.values())
            {
                add("minecraft:%s_bed".formatted(dyeColor.getName()));
            }
        }};
        COMMON_BUILDER
                .comment("Settings for Set Block By Name Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.major.set_block_name")
                .push("spiritualFireMajorSetBlockName");
        SET_BLOCK_NAME_BLACKLIST = COMMON_BUILDER
                .comment("the blacklist for trigger a set block by name effect")
                .translation("config.firewood.spiritual_fire_block_effects.major.set_block_name.blacklist")
                .defineList("blacklist", defaultBlacklist, o -> ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(String.valueOf(o))));
        COMMON_BUILDER.pop();
        
        COMMON_BUILDER.pop();
        
        
        //minor
        COMMON_BUILDER
                .comment("Settings for Minor Effects")
                //.translation("config.firewood.spiritual_fire_block_effects.minor")
                .push("spiritualFireMinor");
        
        COMMON_BUILDER.pop();
        
        
        COMMON_BUILDER.pop();
        
    }
}
