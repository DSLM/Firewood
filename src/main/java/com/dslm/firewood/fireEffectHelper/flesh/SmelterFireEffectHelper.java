package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.TransformFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.TransformFireEffectType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class SmelterFireEffectHelper extends TransformFireEffectHelperBase
{
    
    public SmelterFireEffectHelper(String id)
    {
        super(id);
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        TransformFireEffectType effectData = getSubRealEffect(data);
        int nowProccess = Integer.parseInt(data.get(PROCESS)) + 1;
        if(nowProccess < effectData.getProcess())
        {
            data.put(PROCESS, String.valueOf(nowProccess));
            return data;
        }
        data.put(PROCESS, "0");
        Random random = level.random;
        getBlockPosIterable(pos, effectData.getRange()).forEach(blockPos -> {
            nextBlock:
            if(random.nextDouble() * 100 < effectData.getChance())
            {
                BlockState blockState = level.getBlockState(blockPos);
                if(ForgeRegistries.ITEMS.containsKey(blockState.getBlock().getRegistryName()))
                {
                    ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(blockState.getBlock().getRegistryName()));
                    for(SmeltingRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING))
                    {
                        ItemStack result = recipe.getResultItem();
                        if(recipe.getIngredients().get(0).test(itemStack))
                        {
                            if(result.getItem() instanceof BlockItem)
                            {
                                level.setBlock(blockPos, ((BlockItem) result.getItem()).getBlock().defaultBlockState(), Block.UPDATE_ALL);
                            }
                            else
                            {
                                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                                dropItemAt(level, blockPos, result);
                            }
                            break nextBlock;
                        }
                    }
                }
            }
        });
        return data;
    }
}
