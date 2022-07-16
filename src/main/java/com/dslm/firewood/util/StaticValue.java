package com.dslm.firewood.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

import static com.dslm.firewood.config.ColorConfig.HIGH_CONTRAST_MODE;
import static com.dslm.firewood.datagen.LanguageUtil.TINDER_TOOLTIP;

public class StaticValue
{
    public static final String MOD_ID = "firewood";
    
    public static final String MAJOR = "majorEffects";
    public static final String MINOR = "minorEffects";
    public static final int MAJOR_EFFECT_COLOR = 0x48c774;
    public static final int MINOR_EFFECT_COLOR = 0x48c774;
    public static final int TOTAL_COLOR = 0x48c774;
    public static final int VANILLA_FONT_COLOR = 0x404040;
    public static final int BLACK_FONT_COLOR = 0;
    
    public static final String TYPE = "type";
    public static final String SUB_TYPE = "subType";
    public static final String ING_BLOCK = "ing_block";
    public static final String TARGET_BLOCK = "target_block";
    public static final String COLOR = "color";
    public static final String DAMAGE = "damage";
    public static final String MIN_HEALTH = "min_health";
    public static final String PROCESS = "process";
    public static final String IN_CACHE = "in_cache";
    public static final String CACHE = "cache";
    public static final String CHANCE = "chance";
    public static final String RANGE = "range";
    public static final String TARGET_LIMIT = "target_limit";
    public static final String COOLDOWN = "cooldown";
    
    public static final String ACTIVE_LANTERN = "activeLantern";
    
    public static final TagKey<Item> ITEM_TINDER_TAG = TagKey.create(Registry.ITEM_REGISTRY,
            new ResourceLocation("firewood", "tinder"));
    
    public static final TagKey<Block> BLOCK_TINDER_TAG = TagKey.create(Registry.BLOCK_REGISTRY,
            new ResourceLocation("firewood", "tinder"));
    
    public static final ResourceLocation ICONS = new ResourceLocation(MOD_ID, "textures/gui/icons.png");
    
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    public static int colorInt(String s)
    {
        var s1 = s.toLowerCase().replace("0x", "");
        return HexFormat.fromHexDigits(s1);
    }
    
    public static final String TOP_MOD = "theoneprobe";
    public static final String CURIOS_MOD = "curios";
    public static final String PATCHOULI_MOD = "patchouli";
    public static final String SHIMMER = "shimmer";
    
    public static boolean checkMod(String modId)
    {
        return ModList.get().isLoaded(modId);
    }
    
    public static int reverseColor(int color)
    {
        Color temp1 = Color.intToColor(color);
        return Color.colorToInt(0, 255 - temp1.Red(), 255 - temp1.Green(), 255 - temp1.Blue());
    }
    
    public static TranslatableComponent colorfulText(TranslatableComponent text, int color)
    {
        return HIGH_CONTRAST_MODE != null && HIGH_CONTRAST_MODE.get() ? text : (TranslatableComponent) text.withStyle(style -> style.withColor(TextColor.fromRgb(color)));
    }
    
    public static void getPotionLines(List<MobEffectInstance> mobEffectInstances, List<Component> lines, float durationFactor)
    {
        
        List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
        
        for(MobEffectInstance effectInstance : mobEffectInstances)
        {
            MutableComponent mutablecomponent = new TranslatableComponent(effectInstance.getDescriptionId());
            MobEffect mobeffect = effectInstance.getEffect();
            Map<Attribute, AttributeModifier> map = mobeffect.getAttributeModifiers();
            if(!map.isEmpty())
            {
                for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet())
                {
                    AttributeModifier attributemodifier = entry.getValue();
                    AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), mobeffect.getAttributeModifierValue(effectInstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                    list1.add(new Pair<>(entry.getKey(), attributemodifier1));
                }
            }
            
            if(effectInstance.getAmplifier() > 0)
            {
                mutablecomponent = new TranslatableComponent("potion.withAmplifier", mutablecomponent, new TranslatableComponent("potion.potency." + effectInstance.getAmplifier()));
            }
            
            if(effectInstance.getDuration() > 20)
            {
                mutablecomponent = new TranslatableComponent("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(effectInstance, durationFactor));
            }
            
            lines.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
        }
        
        if(!list1.isEmpty())
        {
            MutableComponent longLine = (new TranslatableComponent("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE);
            
            for(int i = 0; i < list1.size(); i++)
            {
                Pair<Attribute, AttributeModifier> pair = list1.get(i);
                AttributeModifier attributemodifier2 = pair.getSecond();
                double d0 = attributemodifier2.getAmount();
                double d1;
                if(attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL)
                {
                    d1 = attributemodifier2.getAmount();
                }
                else
                {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }
                
                if(d0 > 0.0D)
                {
                    if(i > 0)
                    {
                        longLine.append(new TextComponent("|"));
                    }
                    longLine.append((new TranslatableComponent("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
                }
                else if(d0 < 0.0D)
                {
                    d1 *= -1.0D;
                    if(i > 0)
                    {
                        longLine.append(new TextComponent("|"));
                    }
                    longLine.append((new TranslatableComponent("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
                }
            }
            lines.add(longLine);
        }
    }
    
    public static List<Component> getDescToolTips(String type, String subType, int color)
    {
        List<Component> lines = new ArrayList<>();
        var lang = Language.getInstance();
        var key = "%s.%s.%s.desc.".formatted(TINDER_TOOLTIP + "major_effect", type, subType);
        int nowKey = 1;
        while(lang.has(key + nowKey))
        {
            lines.add(colorfulText(new TranslatableComponent(key + nowKey), color));
            nowKey++;
        }
        return lines;
    }
}
