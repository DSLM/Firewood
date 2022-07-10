package com.dslm.firewood.fireeffecthelper.flesh;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.recipe.BlockToBlockRecipe;
import com.dslm.firewood.recipe.FakeTransmuteContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockToBlockFireEffectHelper extends SubMajorFireEffectHelperBase
{
    
    public BlockToBlockFireEffectHelper(String id)
    {
        super(id, TargetType.BLOCK);
    }
    
    @Override
    public void transmuteBlock(FireEffectNBTDataInterface data, BlockState blockState, Level level, BlockPos blockPos)
    {
        for(BlockToBlockRecipe blockRecipe : level.getRecipeManager().getAllRecipesFor(Register.BLOCK_TO_BLOCK_RECIPE_TYPE.get()))
        {
            if(blockRecipe.getRecipeSubType().equals(data.getSubType()))
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
