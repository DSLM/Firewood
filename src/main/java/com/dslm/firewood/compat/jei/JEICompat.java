package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.recipe.TinderRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.dslm.firewood.fireEffectHelper.flesh.base.TransformFireEffectHelperBase.SUB_TAG_ID;

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
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration)
    {
    
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
    
        registration.addRecipeCatalyst(new ItemStack(Register.SPIRITUAL_CAMPFIRE_BLOCK.get()), TinderCategory.TYPE);
    
        Minecraft minecraft = Objects.requireNonNull(Minecraft.getInstance());
        ClientLevel level = Objects.requireNonNull(minecraft.level);
        RecipeManager recipeManager = level.getRecipeManager();
    
        Set<Pair<Item, String>> set = new HashSet<>();
    
        for(var recipes : recipeManager.getAllRecipesFor(TinderRecipe.Type.INSTANCE))
        {
            for(var effectData : recipes.getAddEffects().getMajorEffects())
            {
                if(effectData.getType().equals("smelter"))
                {
                    for(var item : Arrays.stream(recipes.getTinder().getItems()).map(ItemStack::getItem).toList())
                    {
                        if(set.contains(Pair.of(item, effectData.get(SUB_TAG_ID))))
                        {
                            continue;
                        }
                        set.add(Pair.of(item, effectData.get(SUB_TAG_ID)));
                        registration.addRecipeCatalyst(FireEffectHelpers.addMajorEffects(new ItemStack(item), Arrays.asList(effectData)), RecipeTypes.SMELTING);
                    }
                }
            }
        }
    }
}