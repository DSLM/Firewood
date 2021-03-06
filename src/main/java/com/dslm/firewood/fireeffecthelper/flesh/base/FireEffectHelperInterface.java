package com.dslm.firewood.fireeffecthelper.flesh.base;

import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

import static com.dslm.firewood.util.StaticValue.TYPE;

public interface FireEffectHelperInterface
{
    default boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return first.getString(TYPE).equals(second.getString(TYPE));
    }
    
    int getColor(FireEffectNBTDataInterface data);
    
    ArrayList<Component> getToolTips(FireEffectNBTDataInterface data, boolean extended);
    
    CompoundTag saveToNBT(FireEffectNBTDataInterface data);
    
    FireEffectNBTDataInterface readFromNBT(CompoundTag tags);
    
    String getJEIString(FireEffectNBTDataInterface data);
    
    
    default void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
    }
    
    CompoundTag getDefaultNBT();
    
    FireEffectNBTDataInterface getDefaultData();
    
    String getId();
}
