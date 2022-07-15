package com.dslm.firewood.fireeffecthelper.flesh.major;

import com.dslm.firewood.capprovider.PlayerSpiritualDataProvider;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.PotionSubType;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.Color;
import com.dslm.firewood.util.StaticValue;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.dslm.firewood.util.StaticValue.colorfulText;

public class PotionFireEffectHelper extends SubMajorFireEffectHelperBase
{
    public static final String POTION_TAG_ID = "potion";
    
    public PotionFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            setType(id);
            setSubType("");
            setProcess(0);
            set(POTION_TAG_ID, "minecraft:water");
        }}, id, TargetType.LIVING_ENTITY);
    }
    
    @Override
    public int getColor(FireEffectNBTDataInterface data)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        if(!(effectData instanceof PotionSubType potionSubType))
        {
            return 0;
        }
        Color subColor = Color.intToColor(potionSubType.getColor());
        Color potionColor = Color.intToColor(getPotionColor(data.get(POTION_TAG_ID)));
        double colorMixed = potionSubType.getColorMixed();
        int newColor = Color.colorToInt(0,
                (int) (potionColor.Red() * (1 - colorMixed) + subColor.Red() * colorMixed),
                (int) (potionColor.Green() * (1 - colorMixed) + subColor.Green() * colorMixed),
                (int) (potionColor.Blue() * (1 - colorMixed) + subColor.Blue() * colorMixed));
        return newColor;
    }
    
    
    @Override
    public void transmuteEntity(FireEffectNBTDataInterface data, Level level, LivingEntity livingEntity, LivingEntity source)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        if(!(effectData instanceof PotionSubType potionSubType))
        {
            return;
        }
        Potion potion = getPotion(data.get(POTION_TAG_ID));
        List<MobEffectInstance> effects = potion.getEffects();
        for(MobEffectInstance effect : effects)
        {
            if(effect.getEffect().isInstantenous())
            {
                if(potionSubType.isToEnemy())
                {
                    livingEntity.getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                            playerSpiritualData -> playerSpiritualData.getFleshToEnemyEffects().add(Pair.of(potionSubType.getEffectMulti(), effect)));
                }
                else
                {
                    effect.getEffect().applyInstantenousEffect(source, source, livingEntity, effect.getAmplifier(), potionSubType.getEffectMulti());
                }
            }
            else
            {
                CompoundTag tempTag = new CompoundTag();
                effect.save(tempTag);
                tempTag.putInt("Duration", (int) (tempTag.getInt("Duration") * potionSubType.getEffectMulti()));
                MobEffectInstance newEffect = MobEffectInstance.load(tempTag);
    
                assert newEffect != null;
                if(potionSubType.isToEnemy())
                {
                    livingEntity.getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                            playerSpiritualData -> playerSpiritualData.getFleshToEnemyEffects().add(Pair.of(1.0, effect)));
                }
                else
                {
                    livingEntity.addEffect(newEffect);
                }
            }
        }
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTDataInterface data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.potion.multi_tooltip_format",
                new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%1$s.%2$s".formatted(data.getType(), data.getSubType())),
                new TranslatableComponent(getPotion(data.get(POTION_TAG_ID)).getName(
                        Util.makeDescriptionId("item", Items.POTION.getRegistryName()) + ".effect.")));
        if(extended)
        {
            MiddleComponent mainLine = (MiddleComponent) colorfulText(
                    new MiddleComponent("tooltip.firewood.tinder_item.multi_tooltip_format",
                            new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.potion"), name),
                    getPotionColor(data.get(POTION_TAG_ID)));
            mainLine.setDamage(getDamage(data));
            mainLine.setMinHealth(getMinHealth(data));
            mainLine.setCooldown(getCooldown(data));
            lines.add(mainLine);
    
            lines.addAll(getExtraToolTips(data));
    
            if(getSubRealEffect(data) instanceof PotionSubType potionSubType)
                StaticValue.getPotionLines(getPotion(data.get(POTION_TAG_ID)).getEffects(), lines, (float) potionSubType.getEffectMulti());
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
    public CompoundTag saveToNBT(FireEffectNBTDataInterface data)
    {
        CompoundTag tags = super.saveToNBT(data);
        tags.putString(POTION_TAG_ID, data.get(POTION_TAG_ID));
        return tags;
    }
    
    @Override
    public FireEffectNBTDataInterface readFromNBT(CompoundTag tags)
    {
        FireEffectNBTDataInterface data = super.readFromNBT(tags);
        data.set(POTION_TAG_ID, tags.getString(POTION_TAG_ID));
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
    public String getJEIString(FireEffectNBTDataInterface data)
    {
        return super.getJEIString(data) + "-" + data.get(POTION_TAG_ID);
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        for(Potion potion : ForgeRegistries.POTIONS)
        {
            for(String subType : getSubIdList())
            {
                FireEffectNBTDataInterface defaultData = getDefaultData();
                defaultData.setSubType(subType);
                defaultData.set(POTION_TAG_ID, potion.getRegistryName().toString());
            
                if(getSubRealEffect(defaultData) instanceof PotionSubType potionSubType && potionSubType.includePotion(subType))
                {
                    ItemStack stack = FireEffectHelpers.addMajorEffect(item.copy(), ID, defaultData);
                
                    if(!stack.isEmpty())
                        items.add(stack);
                }
            }
        }
    }
}
