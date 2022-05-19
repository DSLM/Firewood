package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.tooltip.MiddleComponent;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.POTION_BASE_DAMAGE;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class PotionFireEffectHelper extends FireEffectHelperBase
{
    public PotionFireEffectHelper()
    {
        super(new HashMap<>()
        {{
            put("potion", "minecraft:water");
        }});
    }
    
    @Override
    public int getColor(HashMap<String, String> data)
    {
        return getPotionColor(data.get("potion"));
    }
    
    @Override
    public void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        Potion potion = getPotion(data.get("potion"));
        List<MobEffectInstance> effects = potion.getEffects();
        // TODO: 2022/5/10 实现效果打折？
        for(MobEffectInstance effect : effects)
        {
            entity.addEffect(new MobEffectInstance(effect));
        }
    }
    
    @Override
    public float getDamage()
    {
        return POTION_BASE_DAMAGE.get().floatValue();
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = new TranslatableComponent(
                getPotion(data.get("potion")).getName(
                        Util.makeDescriptionId("item", Items.POTION.getRegistryName()) + ".effect."));
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.major_effect." + data.get("type"),
                        name),
                getPotionColor(data.get("potion")));
        mainLine.setDamage(getDamage());
        lines.add(mainLine);
        if(extended)
        {
            // TODO: 2022/5/13 实现药水削弱
            float pDurationFactor = 1f;
    
            List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
    
            for(MobEffectInstance effectInstance : getPotion(data.get("potion")).getEffects())
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
                    mutablecomponent = new TranslatableComponent("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(effectInstance, pDurationFactor));
                }
    
                lines.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
            }
    
            if(!list1.isEmpty())
            {
                MutableComponent longLine = (new TranslatableComponent("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE);
    
                for(Pair<Attribute, AttributeModifier> pair : list1)
                {
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
                        longLine.append(new TextComponent("|"));
                        longLine.append((new TranslatableComponent("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
                    }
                    else if(d0 < 0.0D)
                    {
                        d1 *= -1.0D;
                        longLine.append(new TextComponent("|"));
                        longLine.append((new TranslatableComponent("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslatableComponent(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
                    }
                }
                lines.add(longLine);
            }
        }
        return lines;
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return first.getString("type").equals(second.getString("type")) &&
                first.getString("potion").equals(second.getString("potion"));
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString("type", "potion");
        tags.putString("potion", data.get("potion"));
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", "potion");
        data.put("potion", tags.getString("potion"));
        return data;
    }
    
    public static int getPotionColor(String potion)
    {
        return PotionUtils.getColor(getPotion(potion));
    }
    
    public static Potion getPotion(String potion)
    {
        return ForgeRegistries.POTIONS.getValue(new ResourceLocation(potion));
    }
}
