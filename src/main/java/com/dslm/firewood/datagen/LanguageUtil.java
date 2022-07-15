package com.dslm.firewood.datagen;

import com.dslm.firewood.Register;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
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
    
    public static HashMap<EntityType, String> EntityType_en = new HashMap<>();
    public static HashMap<EntityType, String> EntityType_zh = new HashMap<>();
    
    
    public static final String JEI_SUB_INFO = "tooltip." + StaticValue.MOD_ID + ".jei_sub_info.";
    public static final String TINDER_TOOLTIP = "tooltip." + StaticValue.MOD_ID + "." + Register.TINDER_ITEM.get() + ".";
    
    public static void buildLanguage(DataGenerator generator)
    {
        //group
        add("itemGroup." + StaticValue.MOD_ID, "Firewood", "薪火");
        
        //block
        {
            add(Register.SPIRITUAL_FIRE_BLOCK.get(), "Spiritual Fire", "灵性之火");
            add(Register.SPIRITUAL_CAMPFIRE_BLOCK.get(), "Spiritual Campfire", "灵性营火");
            add(Register.LANTERN_BLOCK.get(), "Lantern", "提灯");
        }
    
        //item
        {
            add(Register.TINDER_ITEM.get(), "Tinder", "火种");
            add(Register.DYING_EMBER_ITEM.get(), "Dying Ember", "余烬");
            add(Register.DEBUG_ITEM.get(), "DEBUG Tool", "测试工具");
            add(Register.REMNANT_SOUL_ITEM.get(), "Remnant Soul", "残存之魂");
        }
    
        //mob effect
        {
            add(Register.FIRED_SPIRIT.get(), "Fired Spirit", "燃烧之魂");
            add(Register.FIRED_FLESH.get(), "Fired Flesh", "燃烧之躯");
        }
    
        //death
        {
            add("death.attack.spiritualFire",
                    "%s's spirit is burned to ashes.", "%s的灵魂被烧成了灰烬。");
            add("death.attack.spiritualFire.player",
                    "%1$s's spirit is burned to ashes by %2$s's conspiracy.", "%2$s的阴谋将%1$s的灵魂烧成了灰烬。");
            add("death.attack.fleshyFire",
                    "%s's flesh is burned to ashes.", "%s的躯体被烧成了灰烬。");
            add("death.attack.fleshyFire.player",
                    "%1$s's flesh is burned to ashes by %2$s's conspiracy.", "%2$s的阴谋将%1$s的躯体烧成了灰烬。");
        }
    
        //config
        {
            add("config.firewood.client.high_contrast_mode",
                    "High Contrast Mode (if true, tooltip wouldn't be dyed)", "高对比度模式（开启后提示信息不会变色）");
    
            add("config.firewood.fired_spirit", "Settings for the Fired Spirit", "燃烧之魂设置");
            add("config.firewood.fired_spirit.interval", "Fired Spirit damage interval (tick)", "燃烧之魂伤害间隔（刻）");
            add("config.firewood.fired_flesh", "Settings for the Fired Flesh", "燃烧之躯设置");
            add("config.firewood.fired_flesh.interval", "Fired Flesh damage interval (tick)", "燃烧之躯伤害间隔（刻）");
    
            add("config.firewood.spiritual_fire_block_effects",
                    "Settings for the Spiritual Fire Block Effects", "灵性之火效果设置");
    
            add("config.firewood.spiritual_fire_block_effects.major",
                    "Settings for the Major Effects", "主要效果设置");
            add("config.firewood.spiritual_fire_block_effects.major.potion",
                    "Settings for the Potion Effects", "药水效果设置");
            add("config.firewood.spiritual_fire_block_effects.major.potion.base_damage",
                    "the base damage for trigger a potion effect", "触发一个药水效果时造成的基础伤害");
            add("config.firewood.spiritual_fire_block_effects.major.set_block_name",
                    "Settings for Set Block By Name Effects", "方块注册名转化效果设置");
            add("config.firewood.spiritual_fire_block_effects.major.set_block_name.blacklist",
                    "the blacklist for trigger a set block by name effect", "触发一个方块注册名转化效果时忽略的方块列表");
    
            add("config.firewood.spiritual_fire_block_effects.minor",
                    "Settings for the Minor Effects", "次要效果设置");
    
            add("config.firewood.spiritual_fire_block_effects.minor.ground",
                    "Settings for Ground Effects", "基底效果设置");
            add("config.firewood.spiritual_fire_block_effects.minor.ground.default_block",
                    "the default block ID for a ground effect", "默认基底方块的ID");
            add("config.firewood.spiritual_fire_block_effects.minor.ground.blacklist",
                    "the blacklist for ground effect", "禁止的基底方块ID列表");
    
            add("config.firewood.spiritual_fire_block_effects.others", "Settings for Others", "其他设置");
            add("config.firewood.spiritual_fire_block_effects.others.fired_flesh_interval",
                    "Fired Flesh default effect time for each major effect (tick); it is also a cooldown time of triggering spiritual fire effects",
                    "每个主要效果造成的燃烧之躯效果默认时长（刻）；这个时间也代表着灵性之火效果触发的冷却时间");
        }
    
        //tooltip
        {
            add(String.format("tooltip.%s.%s.%s", StaticValue.MOD_ID, Register.DYING_EMBER_ITEM.get(), "1"),
                    "A relic of the past...... ", "过去残存之事物……");
            add(String.format("tooltip.%s.%s.%s", StaticValue.MOD_ID, Register.DYING_EMBER_ITEM.get(), "2"),
                    "Coordinate: %1$s (%2$s, %3$s, %4$s)", "坐标：%1$s（%2$s，%3$s，%4$s）");
    
    
            add(TINDER_TOOLTIP + "cooldown",
                    " x%1$sS ", " x%1$s秒 ");
            add(TINDER_TOOLTIP + "total",
                    "Total: ", "总计：");
            add(TINDER_TOOLTIP + "total.full",
                    "Total:", "总计：");
            add(TINDER_TOOLTIP + "total.damage",
                    "Damage: %s", "伤害：%s");
            add(TINDER_TOOLTIP + "total.min_health",
                    "Min Health: %s", "最低生命值需求：%s");
            add(TINDER_TOOLTIP + "total.cooldown",
                    "Cooldown: %sS", "冷却时间：%s秒");
    
            add(TINDER_TOOLTIP + "major_effect",
                    "§lMajor Effect: ", "§l主要效果：");
            add(TINDER_TOOLTIP + "major_effect.potion",
                    "Potion", "药水");
            add(TINDER_TOOLTIP + "major_effect.potion.multi_tooltip_format",
                    "%2$s-%1$s", "%2$s-%1$s");
            add(TINDER_TOOLTIP + "major_effect.potion.potion",
                    "Potion", "药水");
            add(TINDER_TOOLTIP + "major_effect.potion.splash_potion",
                    "Splash Potion", "喷溅药水");
            add(TINDER_TOOLTIP + "major_effect.potion.lingering_potion",
                    "Lingering Potion", "滞留药水");
    
            add(TINDER_TOOLTIP + "major_effect.teleport",
                    "Teleport", "传送");
            add(TINDER_TOOLTIP + "major_effect.teleport.extend.1",
                    "Teleport to: %1$s (%2$s, %3$s, %4$s)", "传送到：%1$s（%2$s，%3$s，%4$s）");
            add(TINDER_TOOLTIP + "major_effect.teleport.teleport",
                    "Teleport", "传送");
    
            add(TINDER_TOOLTIP + "major_effect.smelter",
                    "Smelter", "熔炉");
            add(TINDER_TOOLTIP + "major_effect.smelter.smelter",
                    "Smelter", "熔炉");
            add(TINDER_TOOLTIP + "major_effect.smelter.nether_smelter",
                    "Smelter", "下界熔炉");
            add(TINDER_TOOLTIP + "major_effect.smelter.black_smelter",
                    "Smelter", "黑石熔炉");
    
            add(TINDER_TOOLTIP + "major_effect.entity_to_item",
                    "Mob -> Item", "生物->物品");
            add(TINDER_TOOLTIP + "major_effect.entity_to_item.tinker",
                    "Tinkers' Construct", "匠魂");
    
            add(TINDER_TOOLTIP + "major_effect.block_to_block",
                    "Block -> Block", "方块->方块");
            add(TINDER_TOOLTIP + "major_effect.block_to_block.rock_to_mud",
                    "Transmute Rock to Mud", "化石为泥");
            add(TINDER_TOOLTIP + "major_effect.block_to_block.mud_to_rock",
                    "Transmute Mud to Rock", "化泥为石");
    
            add(TINDER_TOOLTIP + "major_effect.set_block_name",
                    "Set Block by Name", "方块注册名转化");
            add(TINDER_TOOLTIP + "major_effect.set_block_name.rainbow",
                    "Rainbow", "彩虹");
            add(TINDER_TOOLTIP + "major_effect.set_block_name.graft",
                    "Graft", "嫁接");
    
            add(TINDER_TOOLTIP + "minor_effect",
                    "§lMinor Effect: ", "§l次要效果：");
            add(TINDER_TOOLTIP + "minor_effect.ground",
                    "Ground: %s", "基底：%s");
            add(TINDER_TOOLTIP + "minor_effect.order",
                    "Block Check Order: %s", "方块检查顺序：%s");
    
            add(TINDER_TOOLTIP + "exception_effect",
                    "Unexpected Data", "错误数据");
    
            add(TINDER_TOOLTIP + "multi_tooltip_format",
                    "%2$s (%1$s)", "%2$s（%1$s）");
    
            add("tooltip." + StaticValue.MOD_ID + "." + Register.LANTERN_ITEM.get() + ".active",
                    "Active", "已开启");
            add("tooltip." + StaticValue.MOD_ID + "." + Register.LANTERN_ITEM.get() + ".inactive",
                    "Inactive", "已关闭");
        }
        
        //JEI sub info tooltip
        {
            add(JEI_SUB_INFO + "title",
                    "Fire Effect Sub Type Info", "火焰效果子种类信息");
            add(JEI_SUB_INFO + "type",
                    "Type: %s", "类型：%s");
            add(JEI_SUB_INFO + "sub_type",
                    "Sub Type: %s", "子类型：%s");
            add(JEI_SUB_INFO + "color",
                    "Color: %s", "颜色：%s");
            add(JEI_SUB_INFO + "damage",
                    "Damage: %s", "伤害：%s");
            add(JEI_SUB_INFO + "min_health",
                    "Min Health Required: %s", "最低生命值：%s");
            add(JEI_SUB_INFO + "cooldown",
                    "Cooldown: %1$sS (%2$s ticks)", "冷却：%1$s秒（%2$s刻）");
            add(JEI_SUB_INFO + "process",
                    "Requested Firewood: %s", "薪火需求：%s");
            add(JEI_SUB_INFO + "chance",
                    "Success Rate: %s%%", "成功率：%s%%");
            add(JEI_SUB_INFO + "range",
                    "Effect Radius: (X: %s, Y: %s, Z: %s)", "效果半径：（X：%s，Y：%s，Z：%s）");
            add(JEI_SUB_INFO + "range_single",
                    "Effect Range: self", "效果范围：自身");
            add(JEI_SUB_INFO + "target_limit",
                    "Target Limit Per Tick: %s", "每刻目标数量上限：%s");
            
            //potion
            add(JEI_SUB_INFO + "color_mixed",
                    "Color Mixed: Potion %1$s%%, Fire %2$s%%", "颜色混合：药水%1$s%%，火焰%2$s%%");
            add(JEI_SUB_INFO + "effect_multi",
                    "Effect Multiplier: %s%%", "效果乘数：%s%%");
            add(JEI_SUB_INFO + "to_enemy.true",
                    "Effective on the Attacked Enemy: True", "攻击敌人后生效：是");
            add(JEI_SUB_INFO + "to_enemy.false",
                    "Effective on the Attacked Enemy: False", "攻击敌人后生效：否");
            
            //teleport
            add(JEI_SUB_INFO + "dim_from_blacklist.true",
                    "Teleport Origin List Type: Blacklist", "传送始发维度名单类型：黑名单");
            add(JEI_SUB_INFO + "dim_from_blacklist.false",
                    "Teleport Origin List Type: Whitelist", "传送始发维度名单类型：白名单");
            add(JEI_SUB_INFO + "dim_from_list",
                    "Teleport Origin List: ", "传送始发维度名单：");
            add(JEI_SUB_INFO + "dim_to_blacklist.true",
                    "Teleport Destination List Type: Blacklist", "传送目标维度名单类型：黑名单");
            add(JEI_SUB_INFO + "dim_to_blacklist.false",
                    "Teleport Destination List Type: Whitelist", "传送目标维度名单类型：白名单");
            add(JEI_SUB_INFO + "dim_to_list",
                    "Teleport Destination List: ", "传送目标维度名单：");
        }
        
        //recipe tooltip
        {
            add("tooltip." + StaticValue.MOD_ID + ".recipe.process",
                    "Requested Firewood: %s", "薪火需求：%s");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.now_process",
                    "Current Firewood: %1$s / %2$s", "当前薪火：%1$s / %2$s");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.chance",
                    "Burning Firewood Success Rate: %s%%", "添薪成功率：%s%%");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.damage",
                    "Burning Firewood Damage: %s", "添薪伤害：%s");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.min_health",
                    "Burning Firewood Min Health Required: %s", "添薪最低生命值：%s");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.cooldown",
                    "Burning Firewood Cooldown: %1$sS (%2$s ticks)", "添薪冷却：%1$s秒（%2$s刻）");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.range",
                    "Effect Radius: (X: %s, Y: %s, Z: %s)", "效果半径：（X：%s，Y：%s，Z：%s）");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.range_single",
                    "Effect Range: self", "效果范围：自身");
            
            add("tooltip." + StaticValue.MOD_ID + ".recipe.block_state",
                    "Block State:", "方块状态：");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.block_state_line",
                    "%1$s = %2$s", "%1$s = %2$s");
    
            add("tooltip." + StaticValue.MOD_ID + ".recipe.title.fire_effect_sub",
                    "Fire Effect: %2$s (%1$s)", "火焰效果：%2$s（%1$s）");
            add("tooltip." + StaticValue.MOD_ID + ".recipe.title.fire_effect",
                    "Fire Effect: %s", "火焰效果：%s");
        }
    
        //patchouli
        {
            final String patchouli = "patchouli." + StaticValue.MOD_ID + ".book.";
            add(patchouli + "name",
                    "A Relic of the Past (WIP)", "过去残存之事物（WIP）");
            add(patchouli + "landing_text",
                    "This is a mod related to fire, so that all practical and effective effects must be paid by irresistible fire damage.",
                    "这是一个与火焰有关的mod，一切切实有效的效果都需要以受到不可阻挡的火焰伤害为代价。");
        
        
            add(patchouli + "categories.fire_effects.name",
                    "Fire Effects", "火焰效果");
            add(patchouli + "categories.fire_effects.description",
                    "Introduce All Fire Effects.", "介绍了所有的火焰效果。");
            {
                add(patchouli + "categories.fire_effects.flesh.name",
                        "Fleshy Fire Effects", "肉体火焰效果");
                add(patchouli + "categories.fire_effects.flesh.description",
                        "Introduce All Fleshy Fire Effects.", "介绍了所有的肉体火焰效果。");
            
            
                add(patchouli + "categories.fire_effects.flesh.major.name",
                        "Major Fire Effects", "主要火焰效果");
                add(patchouli + "categories.fire_effects.flesh.major.description",
                        "Introduce All Major Fire Effects.", "介绍了所有的主要火焰效果。");
            
            
                add(patchouli + "entries.fire_effects.flesh.major.potion.name",
                        "Fire Effect: Potion", "火焰效果：药水");
                add(patchouli + "entries.fire_effects.flesh.major.potion.pages.0.text",
                        "Give the entity potion effects as same as tooltips shown.", "给予实体对应的药水效果。");
            
                add(patchouli + "entries.fire_effects.flesh.major.teleport.name",
                        "Fire Effect: Teleport", "火焰效果：传送");
                add(patchouli + "entries.fire_effects.flesh.major.teleport.pages.0.text",
                        "Teleport the entity to the position as same as tooltips shown.", "将实体传送至显示的位置。");
            
            
                add(patchouli + "categories.fire_effects.flesh.minor.name",
                        "Minor Fire Effects", "次要火焰效果");
                add(patchouli + "categories.fire_effects.flesh.minor.description",
                        "Introduce All Minor Fire Effects.", "介绍了所有的次要火焰效果。");
    
    
                add(patchouli + "entries.fire_effects.flesh.minor.ground.name",
                        "Fire Effect: Ground", "火焰效果：基底");
                add(patchouli + "entries.fire_effects.flesh.minor.ground.pages.0.text",
                        "Define the block that Spiritual Fire can be placed. Useless for Lanterns.", "决定了灵性之火可以放在什么方块上面，对提灯无效。");
            }
        }
        
        add(Register.REMNANT_SOUL_ENTITY.get(), "Remnant Soul", "残存之魂");
        
        //start generation
        generator.addProvider(new LanguageProvider(generator, "en_us"));
        generator.addProvider(new LanguageZhProvider(generator, "zh_cn"));
    }
    
    private static void add(EntityType key, String en, String zh)
    {
        EntityType_en.put(key, en);
        EntityType_zh.put(key, zh);
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
