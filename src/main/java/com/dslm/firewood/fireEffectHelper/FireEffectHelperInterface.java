package com.dslm.firewood.fireEffectHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface FireEffectHelperInterface
{
    default boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return first.getString("type").equals(second.getString("type"));
    }
    
    int getColor(HashMap<String, String> data);
    
    void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity);
    
    float getDamage();
    
    ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended);
    
    CompoundTag saveToNBT(HashMap<String, String> data);
    
    HashMap<String, String> readFromNBT(CompoundTag tags);
    
    String getJEIString(HashMap<String, String> data);
    
    
    default void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
    }
    
    CompoundTag getDefaultNBT();
    
    HashMap<String, String> getDefaultData();
    
    String getId();
    
    static List<FireEffectHelperInterface> getInstanceList()
    {
        return null;
    }
}
