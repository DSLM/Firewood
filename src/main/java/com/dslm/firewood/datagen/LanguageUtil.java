package com.dslm.firewood.datagen;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;

public class LanguageUtil
{
    public static HashMap<String, String> StringKey_en = new HashMap<>();
    public static HashMap<String, String> StringKey_zh = new HashMap<>();
    
    public static HashMap<Item, String> ItemKey_en = new HashMap<>();
    public static HashMap<Item, String> ItemKey_zh = new HashMap<>();
    
    public static HashMap<Block, String> BlockKey_en = new HashMap<>();
    public static HashMap<Block, String> BlockKey_zh = new HashMap<>();
    
    public static HashMap<MobEffect, String> MobEffectKey_en = new HashMap<>();
    public static HashMap<MobEffect, String> MobEffectKey_zh = new HashMap<>();
    
    public static void buildLanguage(DataGenerator generator)
    {
        //group
        add("itemGroup." + Firewood.MOD_ID, "Firewood", "薪火");
    
        //block
        add(Register.SPIRITUAL_FIRE_BLOCK.get(), "Spiritual Fire", "灵性之火");
        add(Register.SPIRITUAL_CAMPFIRE_BLOCK.get(), "Spiritual Campfire", "灵性营火");
    
        //item
        add(Register.TINDER_ITEM.get(), "Tinder", "火种");
        add(Register.DYING_EMBER_ITEM.get(), "Dying Ember", "余烬");
        add(Register.DEBUG_ITEM.get(), "DEBUG Tool", "测试工具");
    
        //mob effect
        add(Register.FIRED_SPIRIT.get(), "Fired Spirit", "燃烧之魂");
        add(Register.FIRED_FLESH.get(), "Fired Flesh", "燃烧之躯");
    
        //death
        add("death.attack.spiritualFire",
                "%s\'s spirit is burned to ashes.", "%s的灵魂被烧成了灰烬。");
        add("death.attack.spiritualFire.player",
                "%1$s\'s spirit is burned to ashes by %2$s\'s conspiracy.", "%2$s的阴谋将%1$s的灵魂烧成了灰烬。");
        add("death.attack.fleshyFire",
                "%s\'s flesh is burned to ashes.", "%s的躯体被烧成了灰烬。");
        add("death.attack.fleshyFire.player",
                "%1$s\'s flesh is burned to ashes by %2$s\'s conspiracy.", "%2$s的阴谋将%1$s的躯体烧成了灰烬。");
    
        //config
        add("config.firewood.fired_spirit", "Settings for the Fired Spirit", "燃烧之魂设置");
//        add("config.firewood.fired_spirit.damage", "Fired Spirit Damage in each level", "每级燃烧之魂伤害");
        add("config.firewood.fired_spirit.interval", "Fired Spirit damage interval (tick)", "燃烧之魂伤害间隔（刻）");
        add("config.firewood.fired_flesh", "Settings for the Fired Flesh", "燃烧之躯设置");
//        add("config.firewood.fired_flesh.damage", "Fired Flesh Damage in each level", "每级燃烧之躯伤害");
        add("config.firewood.fired_flesh.interval", "Fired Flesh damage interval (tick)", "燃烧之躯伤害间隔（刻）");
    
        add("config.firewood.spiritual_fire_block_effects",
                "Settings for the Spiritual Fire Block Effects", "灵性之火效果设置");
    
        add("config.firewood.spiritual_fire_block_effects.major",
                "Settings for the Major Effects", "主要效果设置");
        add("config.firewood.spiritual_fire_block_effects.major.potion",
                "Settings for the Potion Effects", "药水效果设置");
        add("config.firewood.spiritual_fire_block_effects.major.potion.base_damage",
                "the base damage for trigger a potion effect", "触发一个药水效果时造成的基础伤害");
        add("config.firewood.spiritual_fire_block_effects.major.teleport",
                "Settings for the Teleport Effects", "传送效果设置");
        add("config.firewood.spiritual_fire_block_effects.major.teleport.base_damage",
                "the base damage for trigger a teleport effect", "触发一个传送效果时造成的基础伤害");
    
        add("config.firewood.spiritual_fire_block_effects.minor",
                "Settings for the Minor Effects", "次要效果设置");
    
        add("config.firewood.spiritual_fire_block_effects.minor.ground",
                "Settings for Ground Effects", "基底效果设置");
        add("config.firewood.spiritual_fire_block_effects.minor.ground.default_block",
                "the default block ID for a ground effect", "默认基底方块的ID");
    
        add("config.firewood.spiritual_fire_block_effects.others", "Settings for Others", "其他设置");
        add("config.firewood.spiritual_fire_block_effects.others.fired_flesh_interval",
                "Fired Flesh effect time (tick); it is also a cooldown time of triggering spiritual fire effects",
                "燃烧之躯效果时长（刻）；这个时间也代表着灵性之火效果触发的冷却时间");


//        add("config.firewood.spiritual_campfire_block",
//                "Settings for the Spiritual Campfire Block", "灵性营火效果设置");
//        add("config.firewood.spiritual_campfire_block.process_damage",
//                "the base damage for each time offering the campfire process",
//                "增加营火进度时造成的基础伤害");
//        add("config.firewood.spiritual_campfire_block.process_chance",
//                "the success rate of offering the campfire process (%)",
//                "增加营火进度的成功率（%）");
//        add("config.firewood.spiritual_campfire_block.fired_flesh_interval",
//                "Fired Flesh effect time (tick); it is also a cooldown time of offering the campfire process from one entity",
//                "燃烧之躯效果时长（刻）；这个时间也代表着每个实体增加营火进度的冷却时间");
    
        //tooltip
        add(String.format("tooltip.%s.%s.%s", Firewood.MOD_ID, Register.DYING_EMBER_ITEM.get(), "1"),
                "A relic of the past...... ", "过去残存之事物……");
        add(String.format("tooltip.%s.%s.%s", Firewood.MOD_ID, Register.DYING_EMBER_ITEM.get(), "2"),
                "Coordinate: %1$s (%2$s, %3$s, %4$s)", "坐标：%1$s（%2$s，%3$s，%4$s）");
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".major_effect",
                "§lMajor Effect: ", "§l主要效果：");
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".major_effect.potion",
                "Potion: %s", "药水：%s");
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".major_effect.teleport",
                "Teleport", "传送");
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".major_effect.teleport.extend.1",
                "Teleport to: %1$s (%2$s, %3$s, %4$s)", "传送到：%1$s（%2$s，%3$s，%4$s）");
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".major_effect.smelter",
                "Smelter", "熔炉");
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".major_effect.smelter.smelter",
                "Smelter", "熔炉");
    
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".minor_effect",
                "§lMinor Effect: ", "§l次要效果：");
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".minor_effect.ground",
                "Ground: %s", "基底：%s");
    
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".exception_effect",
                "Unexpected Data", "错误数据");
    
        add("tooltip." + Firewood.MOD_ID + "." + Register.TINDER_ITEM.get() + ".multi_tooltip_format",
                "%2$s (%1$s)", "%2$s（%1$s）");
    
    
        //recipe tooltip
        add("tooltip." + Firewood.MOD_ID + ".recipe.progress",
                "Requested Firewood: %s", "薪火需求：%s");
        add("tooltip." + Firewood.MOD_ID + ".recipe.now_progress",
                "Current Firewood: %1$s / %2$s", "当前薪火：%1$s / %2$s");
        add("tooltip." + Firewood.MOD_ID + ".recipe.chance",
                "Burning Firewood Success Rate: %s%%", "添薪成功率：%s%%");
        add("tooltip." + Firewood.MOD_ID + ".recipe.damage",
                "Burning Firewood Damage: %s", "添薪伤害：%s");
        add("tooltip." + Firewood.MOD_ID + ".recipe.cooldown",
                "Burning Firewood Cooldown: %1$sS (%2$s ticks)", "添薪冷却：%1$s秒（%2$s刻）");
        add("tooltip." + Firewood.MOD_ID + ".recipe.minhealth",
                "Burning Firewood Min Health Required: %s", "添薪最低生命值：%s");
    
        //start generation
        generator.addProvider(new LanguageProvider(generator, "en_us"));
        generator.addProvider(new LanguageZhProvider(generator, "zh_cn"));
    }
    
    public static void add(String key, String en, String zh)
    {
        StringKey_en.put(key, en);
        StringKey_zh.put(key, zh);
    }
    
    public static void add(Item key, String en, String zh)
    {
        ItemKey_en.put(key, en);
        ItemKey_zh.put(key, zh);
    }
    
    public static void add(Block key, String en, String zh)
    {
        BlockKey_en.put(key, en);
        BlockKey_zh.put(key, zh);
    }
    
    public static void add(MobEffect key, String en, String zh)
    {
        MobEffectKey_en.put(key, en);
        MobEffectKey_zh.put(key, zh);
    }
}
