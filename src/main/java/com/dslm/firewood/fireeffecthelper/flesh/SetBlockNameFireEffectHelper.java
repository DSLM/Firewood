package com.dslm.firewood.fireeffecthelper.flesh;

import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.FireEffectSubTypeManager;
import com.dslm.firewood.subtype.SetBlockNameSubType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.SET_BLOCK_NAME_BLACKLIST;

public class SetBlockNameFireEffectHelper extends SubMajorFireEffectHelperBase
{
    public SetBlockNameFireEffectHelper(String id)
    {
        super(id, TargetType.BLOCK);
    }
    
    @Override
    public void transmuteBlock(FireEffectNBTDataInterface data, BlockState blockState, Level level, BlockPos blockPos)
    {
        if(SET_BLOCK_NAME_BLACKLIST.get().contains(blockState.getBlock().getRegistryName().toString()))
        {
            return;
        }
        CompoundTag compoundTag = new CompoundTag();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockState.getBlock() instanceof EntityBlock && blockEntity != null)
        {
            compoundTag = blockEntity.saveWithoutMetadata();
        }
    
        FireEffectSubTypeBase subType = FireEffectSubTypeManager.getEffectsMap().get("firewood:set_block_name").get(data.getSubType());
        ArrayList<ArrayList<String>> nowOrder = subType instanceof SetBlockNameSubType setBlockNameSubType ? setBlockNameSubType.getOrder() : new ArrayList<>();
        ArrayList<Integer> checkOrder = subType instanceof SetBlockNameSubType setBlockNameSubType ? setBlockNameSubType.getCheckOrder() : new ArrayList<>();
    
        if(nowOrder.size() == 0) return;
    
        String oldString = blockState.getBlock().getRegistryName().toString();
    
        allCheck:
        {
            for(int i : checkOrder)
            {
                if(nowOrder.get(0).size() < i)
                {
                    continue;
                }
                if(((SetBlockNameSubType) subType).getBlockMap().get(i).containsKey(oldString))
                {
                    setBlockByOrder(blockState, level, blockPos, ((SetBlockNameSubType) subType).getBlockMap().get(i).get(oldString), compoundTag);
                    break allCheck;
                }
            }
            for(int i = 0; i < nowOrder.get(0).size(); i++)
            {
                if(checkOrder.contains(i))
                {
                    continue;
                }
                if(((SetBlockNameSubType) subType).getBlockMap().get(i).containsKey(oldString))
                {
                    setBlockByOrder(blockState, level, blockPos, ((SetBlockNameSubType) subType).getBlockMap().get(i).get(oldString), compoundTag);
                    break allCheck;
                }
            }
        }
    }
    
    private boolean setBlockByOrder(BlockState blockState, Level level, BlockPos blockPos, Block newBlock, CompoundTag compoundTag)
    {
        
        CompoundTag oldTag = NbtUtils.writeBlockState(blockState).getCompound("Properties");
        CompoundTag newTag = new CompoundTag();
        newTag.putString("Name", newBlock.getRegistryName().toString());
        CompoundTag propertiesTag = new CompoundTag();
        for(var property : newBlock.defaultBlockState().getValues().entrySet())
        {
            if(oldTag.contains(property.getKey().getName()))
            {
                propertiesTag.put(property.getKey().getName(), oldTag.get(property.getKey().getName()));
            }
        }
        newTag.put("Properties", propertiesTag);
        
        
        level.setBlock(blockPos, NbtUtils.readBlockState(newTag), Block.UPDATE_ALL);
        
        
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(newBlock instanceof EntityBlock && blockEntity != null && compoundTag != null)
        {
            blockEntity.load(compoundTag);
        }
        
        return true;
    }
    
}
