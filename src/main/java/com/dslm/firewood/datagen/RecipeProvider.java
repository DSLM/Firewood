package com.dslm.firewood.datagen;

import com.dslm.firewood.Register;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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
        var logs = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("minecraft", "logs_that_burn"));
        var sticks = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "rods/wooden"));
        var ember = Register.DYING_EMBER_ITEM.get();
        SimpleCookingRecipeBuilder
            
                .campfireCooking(Ingredient.of(sticks), Register.TINDER_ITEM.get(), 0, 100 * 20)
                .unlockedBy("has_stick", has(sticks))
                .save(consumer, "tinder_item");
    
        SimpleCookingRecipeBuilder
                .blasting(Ingredient.of(Items.DEAD_BUSH), ember, 0, 100 * 20)
                .unlockedBy("has_bush", has(Items.DEAD_BUSH))
                .save(consumer, "dying_ember_item");
    
        ShapedRecipeBuilder.shaped(Register.SPIRITUAL_CAMPFIRE_ITEM.get())
                .pattern(" b ")
                .pattern("bcb")
                .pattern("aaa")
                .define('a', logs)
                .define('b', sticks)
                .define('c', ember)
                .unlockedBy("has_ember", has(ember))
                .save(consumer, "spiritual_campfire_item");
    }
}
