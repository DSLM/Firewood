package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.base.MajorFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.StaticValue;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.POTION_BASE_DAMAGE;
import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.colorfulText;

public class PotionFireEffectHelper extends MajorFireEffectHelperBase
{
    private static final ArrayList<FireEffectHelperInterface> instanceList = new ArrayList<>();
    
    public static final String POTION_TAG_ID = "potion";
    
    public PotionFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            put(POTION_TAG_ID, "minecraft:water");
        }}, id);
        instanceList.add(this);
    }
    
    @Override
    public int getColor(FireEffectNBTData data)
    {
        return getPotionColor(data.get(POTION_TAG_ID));
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, TinderSourceType tinderSourceType, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        Potion potion = getPotion(data.get(POTION_TAG_ID));
        List<MobEffectInstance> effects = potion.getEffects();
        // TODO: 2022/5/10 实现效果打折？
        for(MobEffectInstance effect : effects)
        {
            entity.addEffect(new MobEffectInstance(effect));
        }
        return data;
    }
    
    @Override
    public float getDamage(FireEffectNBTData data)
    {
        return POTION_BASE_DAMAGE.get().floatValue();
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTData data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = new TranslatableComponent(
                getPotion(data.get(POTION_TAG_ID)).getName(
                        Util.makeDescriptionId("item", Items.POTION.getRegistryName()) + ".effect."));
        if(extended)
        {
            MiddleComponent mainLine = (MiddleComponent) colorfulText(
                    new MiddleComponent("tooltip.firewood.tinder_item.multi_tooltip_format",
                            new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.potion"), name),
                    getPotionColor(data.get(POTION_TAG_ID)));
            mainLine.setDamage(getDamage(data));
            mainLine.setMinHealth(getMinHealth(data));
            lines.add(mainLine);
        
            // TODO: 2022/5/13 实现药水削弱
            float pDurationFactor = 1f;
        
            List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
        
            for(MobEffectInstance effectInstance : getPotion(data.get(POTION_TAG_ID)).getEffects())
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
        else
        {
            lines.add(colorfulText(name, getPotionColor(data.get(POTION_TAG_ID))));
        }
        return lines;
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return super.isSameNBT(first, second) &&
                first.getString(POTION_TAG_ID).equals(second.getString(POTION_TAG_ID));
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTData data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(StaticValue.TYPE, ID);
        tags.putString(POTION_TAG_ID, data.get(POTION_TAG_ID));
        return tags;
    }
    
    @Override
    public FireEffectNBTData readFromNBT(CompoundTag tags)
    {
        FireEffectNBTData data = new FireEffectNBTData();
        data.put(StaticValue.TYPE, ID);
        data.put(POTION_TAG_ID, tags.getString(POTION_TAG_ID));
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
    
    @Override
    public String getJEIString(FireEffectNBTData data)
    {
        return data.getType() + "-" + data.get(POTION_TAG_ID);
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        for(Potion potion : ForgeRegistries.POTIONS)
        {
            if(potion == Potions.EMPTY) continue;
    
            String potionId = potion.getRegistryName().toString();
            ItemStack stack = FireEffectHelpers.addMajorEffect(item.copy(), ID, new FireEffectNBTData()
            {{
                put(POTION_TAG_ID, potionId);
            }});
            
            if(!stack.isEmpty())
                items.add(stack);
        }
    }
    
    public static List<FireEffectHelperInterface> getInstanceList()
    {
        return instanceList;
    }
}
