package com.dslm.firewood.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpiritualFireBlockEffectConfig
{
    public static ForgeConfigSpec.DoubleValue POTION_BASE_DAMAGE;
    public static ForgeConfigSpec.DoubleValue TELEPORT_BASE_DAMAGE;
    
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
}
