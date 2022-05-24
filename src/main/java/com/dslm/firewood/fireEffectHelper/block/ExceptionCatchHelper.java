package com.dslm.firewood.fireEffectHelper.block;

import com.dslm.firewood.fireEffectHelper.block.baseClass.FireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.block.baseClass.MajorFireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.block.baseClass.MinorFireEffectHelperInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;

public class ExceptionCatchHelper extends FireEffectHelperBase implements MajorFireEffectHelperInterface, MinorFireEffectHelperInterface
{
    public ExceptionCatchHelper()
    {
        super(new HashMap<>(), "EXCEPTION");
    }
    
    @Override
    public int getColor(HashMap<String, String> data)
    {
        return 0;
    }
    
    @Override
    public HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        return data;
    }
    
    @Override
    public HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos)
    {
        return data;
    }
    
    @Override
    public boolean canBePlacedOn(HashMap<String, String> data, Level level, BlockPos pos)
    {
        return true;
    }
    
    @Override
    public float getDamage(HashMap<String, String> data)
    {
        return 0;
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(new TranslatableComponent("tooltip.firewood.tinder_item.exception_effect"));
        if(extended)
        {
            lines.add(new TranslatableComponent(data.toString()));
        }
        return lines;
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return false;
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        for(String i : data.keySet())
        {
            tags.putString(i, data.get(i));
        }
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        for(String i : tags.getAllKeys())
        {
            data.put(i, tags.getString(i));
        }
        return data;
    }
}
