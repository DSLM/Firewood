package com.dslm.firewood.datagen;

import com.dslm.firewood.Register;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider
{
    
    public RecipeProvider(DataGenerator generatorIn)
    {
        super(generatorIn);
    }
    
    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
    {
        SimpleCookingRecipeBuilder
                .campfireCooking(Ingredient.of(Items.DEAD_BUSH), Register.TINDER_ITEM.get(), 0, 100 * 20)
                .unlockedBy("has_bush", has(Items.DEAD_BUSH))
                .save(consumer, "tinder_item");
        SimpleCookingRecipeBuilder
                .blasting(Ingredient.of(Items.DEAD_BUSH), Register.DYING_EMBER_ITEM.get(), 0, 100 * 20)
                .unlockedBy("has_bush", has(Items.DEAD_BUSH))
                .save(consumer, "dying_ember_item");
    
    }
}
