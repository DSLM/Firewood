package com.dslm.firewood.fireeffecthelper.flesh;

import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class SmelterFireEffectHelper extends SubMajorFireEffectHelperBase
{
    
    public SmelterFireEffectHelper(String id)
    {
        super(id, TargetType.BLOCK);
    }
    
    @Override
    public void transmuteBlock(FireEffectNBTDataInterface data, BlockState blockState, Level level, BlockPos blockPos)
    {
        nextBlock:
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
}
