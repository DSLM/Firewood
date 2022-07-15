package com.dslm.firewood.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FakeEntityTransmuteContainer implements Container
{
    LivingEntity livingEntity;
    Level level;
    
    public FakeEntityTransmuteContainer(LivingEntity livingEntity, Level level)
    {
        this.livingEntity = livingEntity;
        this.level = level;
    }
    
    public LivingEntity getLivingEntity()
    {
        return livingEntity;
    }
    
    public void setLivingEntity(LivingEntity livingEntity)
    {
        this.livingEntity = livingEntity;
    }
    
    public Level getLevel()
    {
        return level;
    }
    
    public void setLevel(Level level)
    {
        this.level = level;
    }
    
    @Override
    public int getContainerSize()
    {
        return 0;
    }
    
    @Override
    public boolean isEmpty()
    {
        return false;
    }
    
    @Override
    public ItemStack getItem(int pIndex)
    {
        return null;
    }
    
    @Override
    public ItemStack removeItem(int pIndex, int pCount)
    {
        return null;
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int pIndex)
    {
        return null;
    }
    
    @Override
    public void setItem(int pIndex, ItemStack pStack)
    {
    
    }
    
    @Override
    public void setChanged()
    {
    
    }
    
    @Override
    public boolean stillValid(Player pPlayer)
    {
        return false;
    }
    
    @Override
    public void clearContent()
    {
    
    }
}
