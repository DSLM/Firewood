package com.dslm.firewood.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ColorConfig
{
    public static ForgeConfigSpec.BooleanValue HIGH_CONTRAST_MODE;
    
    public static void registerClientConfig(ForgeConfigSpec.Builder CLIENT_BUILDER)
    {
        HIGH_CONTRAST_MODE = CLIENT_BUILDER
                .comment("High Contrast Mode (if true, tooltip wouldn't be dyed)")
                .translation("config.firewood.client.high_contrast_mode")
                .define("highContrastMode", false);
    }
}
