package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.recipe.TinderRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.Objects;

@JeiPlugin
public class JEICompat implements IModPlugin
{
    @Nonnull
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(Firewood.MOD_ID, Firewood.MOD_ID);
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new TinderCategory(guiHelper)
        );
    }
    
    @Override
    public void registerRecipes(final IRecipeRegistration registration)
    {
        Minecraft minecraft = Objects.requireNonNull(Minecraft.getInstance());
        ClientLevel level = Objects.requireNonNull(minecraft.level);
        RecipeManager recipeManager = level.getRecipeManager();
        var recipes = recipeManager.getAllRecipesFor(TinderRecipe.Type.INSTANCE).stream().toList();
        registration.addRecipes(TinderCategory.TYPE, recipes);
    }
    
    @Override
    public void registerItemSubtypes(ISubtypeRegistration r)
    {
        r.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, Register.TINDER_ITEM.get(), (ItemStack itemStack, UidContext context) -> {
            if(!itemStack.hasTag())
            {
                return IIngredientSubtypeInterpreter.NONE;
            }
            return FireEffectHelpers.getJEIType(itemStack);
        });
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK,
                new ItemStack(Register.SPIRITUAL_CAMPFIRE_BLOCK.get()), TinderCategory.TYPE);
    }
}