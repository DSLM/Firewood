package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class FireEffectHelperBase
{
    public FireEffectHelperBase()
    {
    
    }
    
    abstract public int getColor(HashMap<String, String> data);
    
    abstract public void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity);
    
    abstract public float getDamage();
    
    abstract public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extend);
}
