package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.TransmuteFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.subType.DyeBlockSubType;
import com.dslm.firewood.subType.FireEffectSubTypeBase;
import com.dslm.firewood.subType.FireEffectSubTypeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

import static com.dslm.firewood.util.StaticValue.COLORS;
import static com.dslm.firewood.util.StaticValue.DEFAULT_COLOR_ORDER;

public class DyeBlockFireEffectHelper extends TransmuteFireEffectHelperBase
{// TODO: 2022/6/4 整个通用的关键字替换；食谱中自定义映射关键字——吗？；床大问题，玻璃板小问题，考虑 plan B 
    
    public DyeBlockFireEffectHelper(String id)
    {
        super(id);
    }
    
    @Override
    public void transmuteBlock(FireEffectNBTData data, BlockState blockState, Level level, BlockPos blockPos)
    {
        FireEffectSubTypeBase subType = FireEffectSubTypeManager.getEffectsMap().get("firewood:dye_block").get(data.getSubType());
        ArrayList<String> nowOrder = subType instanceof DyeBlockSubType dyeBlockSubType ? dyeBlockSubType.getColorOrder() : DEFAULT_COLOR_ORDER;
        
        for(String color : COLORS)
        {
            String oldString = blockState.getBlock().getRegistryName().toString();
            if(!nowOrder.contains(color) || !oldString.contains(color))
            {
                continue;
            }
            String newString = oldString.replace(color, getNext(nowOrder, color));
            ResourceLocation newID = new ResourceLocation(newString);
            
            if(!oldString.equals(newString) && ForgeRegistries.BLOCKS.containsKey(newID))
            {
                Block newBlock = ForgeRegistries.BLOCKS.getValue(newID);
                
                CompoundTag oldTag = NbtUtils.writeBlockState(blockState);
                CompoundTag newTag = new CompoundTag();
                newTag.putString("Name", newString);
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
                break;
            }
        }
    }
    
    public String getNext(ArrayList<String> nowOrder, String color)
    {
        return nowOrder.get((nowOrder.indexOf(color) + 1) % nowOrder.size());
    }
}
