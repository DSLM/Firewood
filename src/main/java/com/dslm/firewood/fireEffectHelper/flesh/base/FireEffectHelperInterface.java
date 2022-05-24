package com.dslm.firewood.fireEffectHelper.flesh.base;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperBase.TYPE;

public interface FireEffectHelperInterface
{
    default boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return first.getString(TYPE).equals(second.getString(TYPE));
    }
    
    int getColor(FireEffectNBTData data);
    
    ArrayList<Component> getToolTips(FireEffectNBTData data, boolean extended);
    
    CompoundTag saveToNBT(FireEffectNBTData data);
    
    FireEffectNBTData readFromNBT(CompoundTag tags);
    
    String getJEIString(FireEffectNBTData data);
    
    
    default void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
    }
    
    CompoundTag getDefaultNBT();
    
    FireEffectNBTData getDefaultData();
    
    String getId();
    
    static List<FireEffectHelperInterface> getInstanceList()
    {
        return null;
    }
}
