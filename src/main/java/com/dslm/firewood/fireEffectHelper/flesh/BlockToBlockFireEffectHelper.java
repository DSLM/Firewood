package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.TransmuteFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.recipe.FakeTransmuteContainer;
import com.dslm.firewood.recipe.TransmuteBlockRecipeBase;
import com.dslm.firewood.recipe.TransmuteBlockToBlockRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
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
        for(TransmuteBlockRecipeBase recipe : level.getRecipeManager().getAllRecipesFor(TransmuteBlockToBlockRecipe.Type.INSTANCE))
        {
            if(recipe instanceof TransmuteBlockToBlockRecipe blockRecipe && recipe.getRecipeSubType().equals(data.get(SUB_TAG_ID)))
            {
                FakeTransmuteContainer container = new FakeTransmuteContainer(blockState, blockPos, level);
                if(blockRecipe.matches(container, level))
                {
                    blockRecipe.assemble(container);
                    break;
                }
            }
        }
    }
}
