package com.dslm.firewood.fireEffectHelper.block;

import com.dslm.firewood.fireEffectHelper.block.baseClass.RangedFireEffectData;
import com.dslm.firewood.fireEffectHelper.block.baseClass.RangedFireEffectHelperBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Random;

public class SmelterFireEffectHelper extends RangedFireEffectHelperBase
{
    
    public SmelterFireEffectHelper(String id)
    {
        super(id);
    }
    
    @Override
    public HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        RangedFireEffectData effectData = getSubRealEffect(data);
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
                        Item result = recipe.getResultItem().getItem();
                        if(recipe.getIngredients().get(0).test(itemStack) && result instanceof BlockItem)
                        {
                            level.setBlock(blockPos, ((BlockItem) result).getBlock().defaultBlockState(), Block.UPDATE_ALL);
                            break nextBlock;
                        }
                    }
                }
            }
        });
        return data;
    }
}
