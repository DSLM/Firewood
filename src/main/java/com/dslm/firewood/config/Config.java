package com.dslm.firewood.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config
{
    // TODO: 2022/5/10 药水效果削弱乘数，参考滞留
    // TODO: 2022/5/10 维度映射
    // TODO: 2022/5/10 方块基底黑白名单？
    
    public static void register()
    {
        registerServerConfigs();
        registerCommonConfigs();
        registerClientConfigs();
    }
    
    private static void registerClientConfigs()
    {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        //.registerClientConfig(CLIENT_BUILDER);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }
    
    private static void registerCommonConfigs()
    {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        //.registerCommonConfig(COMMON_BUILDER);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }
    
    private static void registerServerConfigs()
    {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        //MobEffectConfig.registerServerConfig(SERVER_BUILDER);
        SpiritualFireBlockEffectConfig.registerServerConfig(SERVER_BUILDER);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }
}