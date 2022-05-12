package com.dslm.firewood.fireEffectHelper;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.POTION_BASE_DAMAGE;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class PotionFireEffectHelper extends FireEffectHelperBase
{
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
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extend)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = new TranslatableComponent(
                getPotion(data.get("potion")).getName(
                        Util.makeDescriptionId("item", Items.POTION.getRegistryName()) + ".effect."));
        lines.add(colorfulText(
                new TranslatableComponent("tooltip.firewood.tinder_item.major_effect." + data.get("type"),
                        name),
                getPotionColor(data.get("potion"))));
        return lines;
    }
    
    @Override
    public CompoundTag getDefaultNBT()
    {
        return saveToNBT(new HashMap<>()
        {{
            put("potion", "minecraft:water");
        }});
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
