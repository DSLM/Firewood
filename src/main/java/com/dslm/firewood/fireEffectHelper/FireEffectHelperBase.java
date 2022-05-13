package com.dslm.firewood.fireEffectHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class FireEffectHelperBase
{
    public final HashMap<String, String> defaultData;
    
    public FireEffectHelperBase(HashMap<String, String> defaultData)
    {
        this.defaultData = defaultData;
    }
    
    abstract public int getColor(HashMap<String, String> data);
    
    abstract public void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity);
    
    abstract public float getDamage();
    
    abstract public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended);
    
    public CompoundTag getDefaultNBT()
    {
        return saveToNBT(defaultData);
    }
    
    abstract public boolean isSameNBT(CompoundTag first, CompoundTag second);
    
    abstract public CompoundTag saveToNBT(HashMap<String, String> data);
    
    abstract public HashMap<String, String> readFromNBT(CompoundTag tags);
}
