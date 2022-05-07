package com.dslm.firewood.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class SpiritualFireBlockEffectConfig
{
    public static ForgeConfigSpec.DoubleValue POTION_BASE_DAMAGE;
    
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
                .defineInRange("damage", 1.0, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
        
        SERVER_BUILDER.pop();
        
        SERVER_BUILDER.pop();
    }
}
