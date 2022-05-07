//package com.dslm.firewood.config;
//
//import net.minecraftforge.common.ForgeConfigSpec;
//
//public class MobEffectConfig
//{
//    // TODO: 2022/5/10 暂时放弃debuff方案
//    public static ForgeConfigSpec.DoubleValue FIRED_SPIRIT_DAMAGE;
//    public static ForgeConfigSpec.IntValue FIRED_SPIRIT_INTERVAL;
//    public static ForgeConfigSpec.DoubleValue FIRED_FLESH_DAMAGE;
//    public static ForgeConfigSpec.IntValue FIRED_FLESH_INTERVAL;
//
//    public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER)
//    {
//        SERVER_BUILDER
//                .comment("Settings for the Fired Spirit")
//                //.translation("config.firewood.fired_spirit")
//                .push("firedSpirit");
//        FIRED_SPIRIT_DAMAGE = SERVER_BUILDER
//                .comment("Fired Spirit damage in each level")
//                .translation("config.firewood.fired_spirit.damage")
//                .defineInRange("firedSpiritDamage", 1.0, 0, Integer.MAX_VALUE);
//        FIRED_SPIRIT_INTERVAL = SERVER_BUILDER
//                .comment("Fired Spirit damage interval (tick)")
//                .translation("config.firewood.fired_spirit.interval")
//                .defineInRange("firedSpiritInterval", 20, 10, Integer.MAX_VALUE);
//        SERVER_BUILDER.pop();
//
//        SERVER_BUILDER
//                .comment("Settings for the Fired Flesh")
//                //.translation("config.firewood.fired_flesh")
//                .push("firedFlesh");
//        FIRED_FLESH_DAMAGE = SERVER_BUILDER
//                .comment("Fired Flesh damage in each level")
//                .translation("config.firewood.fired_flesh.damage")
//                .defineInRange("firedFleshDamage", 1.0, 0, Integer.MAX_VALUE);
//        FIRED_FLESH_INTERVAL = SERVER_BUILDER
//                .comment("Fired Flesh damage interval (tick)")
//                .translation("config.firewood.fired_flesh.interval")
//                .defineInRange("firedFleshInterval", 20, 10, Integer.MAX_VALUE);
//        SERVER_BUILDER.pop();
//    }
//}
