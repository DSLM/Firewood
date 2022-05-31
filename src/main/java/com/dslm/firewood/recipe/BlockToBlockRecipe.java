package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;


public class BlockToBlockRecipe extends TransmuteBlockRecipeBase
{
    protected BlockState targetBlock = Blocks.AIR.defaultBlockState();
    
    public BlockToBlockRecipe(TransmuteBlockRecipeBase recipe, BlockState targetBlock)
    {
        super(recipe);
        this.targetBlock = targetBlock;
    }
    
    public BlockToBlockRecipe(TransmuteBlockRecipeBase recipe)
    {
        super(recipe);
    }
    
    public BlockState getTargetBlock()
    {
        return targetBlock;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Register.BLOCK_TO_BLOCK_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return Register.BLOCK_TO_BLOCK_RECIPE_TYPE.get();
    }
}