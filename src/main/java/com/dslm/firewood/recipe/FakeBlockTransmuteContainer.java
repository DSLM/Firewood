package com.dslm.firewood.recipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FakeBlockTransmuteContainer implements Container
{
    BlockState state;
    BlockPos pos;
    Level level;
    
    public FakeBlockTransmuteContainer(BlockState state, BlockPos pos, Level level)
    {
        this.state = state;
        this.pos = pos;
        this.level = level;
    }
    
    public BlockState getState()
    {
        return state;
    }
    
    public void setState(BlockState state)
    {
        this.state = state;
    }
    
    public BlockPos getPos()
    {
        return pos;
    }
    
    public void setPos(BlockPos pos)
    {
        this.pos = pos;
    }
    
    public Level getLevel()
    {
        return level;
    }
    
    public void setLevel(Level level)
    {
        this.level = level;
    }
    
    public Boolean isSame(FakeTransmuteBlockState otherState)
    {
        return otherState.compareBlockState(state);
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
