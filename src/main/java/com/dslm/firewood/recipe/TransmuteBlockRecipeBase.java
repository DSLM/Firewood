package com.dslm.firewood.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * NEVER make a instance of this class
 */
public class TransmuteBlockRecipeBase implements Recipe<FakeTransmuteContainer>, SubRecipeBase
{
    protected final ResourceLocation id;
    protected final String recipeType;
    protected final String recipeSubType;
    protected final FakeTransmuteBlockState ingBlock;
    
    public TransmuteBlockRecipeBase(ResourceLocation id, String recipeType, String recipeSubType, FakeTransmuteBlockState ingBlock)
    {
        this.id = id;
        this.recipeType = recipeType;
        this.recipeSubType = recipeSubType;
        this.ingBlock = ingBlock;
    }
    
    public TransmuteBlockRecipeBase(TransmuteBlockRecipeBase copy)
    {
        this.id = copy.id;
        this.recipeType = copy.recipeType;
        this.recipeSubType = copy.recipeSubType;
        this.ingBlock = copy.ingBlock;
    }
    
    @Override
    public boolean matches(FakeTransmuteContainer container, Level level)
    {
        return container.isSame(ingBlock);
    }
    
    @Override
    public ItemStack assemble(FakeTransmuteContainer container)
    {
        return null;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }
    
    @Override
    public ItemStack getResultItem()
    {
        return null;
    }
    
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return null;
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return null;
    }
    
    public String getRecipeType()
    {
        return recipeType;
    }
    
    public String getRecipeSubType()
    {
        return recipeSubType;
    }
    
    public FakeTransmuteBlockState getIngBlock()
    {
        return ingBlock;
    }
    
    public List<ItemStack> getJEIInputs()
    {
        List<Block> blocks = ingBlock.getAllPossibleBlocks();
        ArrayList<ItemStack> items = new ArrayList<>();
        for(var block : blocks)
        {
            items.add(new ItemStack(block.asItem()));
        }
        return items;
    }
}