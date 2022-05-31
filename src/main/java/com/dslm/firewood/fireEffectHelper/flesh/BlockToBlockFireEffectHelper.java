package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.TransmuteFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.recipe.BlockToBlockRecipe;
import com.dslm.firewood.recipe.FakeTransmuteContainer;
import com.dslm.firewood.recipe.TransmuteBlockRecipeBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockToBlockFireEffectHelper extends TransmuteFireEffectHelperBase
{
    
    public BlockToBlockFireEffectHelper(String id)
    {
        super(id);
    }
    
    @Override
    public void transmuteBlock(FireEffectNBTData data, BlockState blockState, Level level, BlockPos blockPos)
    {
        for(TransmuteBlockRecipeBase recipe : level.getRecipeManager().getAllRecipesFor(BlockToBlockRecipe.Type.INSTANCE))
        {
            if(recipe instanceof BlockToBlockRecipe blockRecipe && recipe.getRecipeSubType().equals(data.getSubType()))
            {
                FakeTransmuteContainer container = new FakeTransmuteContainer(blockState, blockPos, level);
                if(blockRecipe.matches(container, level))
                {
                    level.setBlock(blockPos, blockRecipe.getTargetBlock(), Block.UPDATE_ALL);
                    break;
                }
            }
        }
    }
}
