package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.item.TinderTypeItemBase;
import com.dslm.firewood.recipe.TinderRecipe;
import com.dslm.firewood.recipe.type.TinderRecipeType;
import com.dslm.firewood.subType.FireEffectSubTypeBase;
import com.dslm.firewood.subType.FireEffectSubTypeManager;
import com.dslm.firewood.subType.SetBlockNameSubType;
import com.dslm.firewood.util.StaticValue;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@JeiPlugin
public class JEICompat implements IModPlugin
{
    static HashMap<String, HashMap<String, RecipeType>> recipeTypes = new HashMap<>();
    
    
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(StaticValue.MOD_ID, StaticValue.MOD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        recipeTypes = new HashMap<>();
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new TinderCategory(guiHelper)
        );
    
    
        {
            HashMap<String, RecipeType> subMap = new HashMap<>();
            for(Map.Entry<String, FireEffectSubTypeBase> subTypes : FireEffectSubTypeManager.getEffectsMap().get("firewood:block_to_block").entrySet())
            {
                var category = new BlockToBlockCategory(guiHelper, subTypes.getKey());
                subMap.put(subTypes.getKey(), category.getRecipeType());
                registry.addRecipeCategories(category);
            }
            recipeTypes.put("block_to_block", subMap);
        }
    
    
        {
            HashMap<String, RecipeType> subMap = new HashMap<>();
            for(Map.Entry<String, FireEffectSubTypeBase> subTypes : FireEffectSubTypeManager.getEffectsMap().get("firewood:set_block_name").entrySet())
            {
                var category = new SetBlockNameCategory(guiHelper, subTypes.getKey());
                subMap.put(subTypes.getKey(), category.getRecipeType());
                registry.addRecipeCategories(category);
            }
            recipeTypes.put("set_block_name", subMap);
        }
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
        List<TinderRecipe> tinderRecipes = recipeManager.getRecipes().stream()
                .filter(recipe -> recipe.getType() instanceof TinderRecipeType)
                .map(recipe -> (TinderRecipe) recipe)
                .toList();
        registration.addRecipes(TinderCategory.TYPE, tinderRecipes);
    
    
        if(recipeTypes.containsKey("block_to_block"))
        {
            for(Map.Entry<String, FireEffectSubTypeBase> subTypes : FireEffectSubTypeManager.getEffectsMap().get("firewood:block_to_block").entrySet())
            {
                var recipes = recipeManager.getAllRecipesFor(Register.BLOCK_TO_BLOCK_RECIPE_TYPE.get())
                        .stream()
                        .filter(blockRecipe -> blockRecipe.getRecipeSubType().equals(subTypes.getKey()))
                        .toList();
                registration.addRecipes(recipeTypes.get("block_to_block").get(subTypes.getKey()), recipes);
            }
        }
    
    
        if(recipeTypes.containsKey("set_block_name"))
        {
            for(Map.Entry<String, FireEffectSubTypeBase> subTypes : FireEffectSubTypeManager.getEffectsMap().get("firewood:set_block_name").entrySet())
            {
                var recipes = ((SetBlockNameSubType) subTypes.getValue()).getBlockMap();
                registration.addRecipes(recipeTypes.get("set_block_name").get(subTypes.getKey()), recipes);
            }
        }
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
        r.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, Register.LANTERN_ITEM.get(), (ItemStack itemStack, UidContext context) -> {
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
    
        ArrayList<Item> items = new ArrayList<>();
        for(Item item : ForgeRegistries.ITEMS)
        {
            if(item instanceof TinderTypeItemBase)
            {
                items.add(item);
            }
        }
        registerRecipeSubCatalyst(registration, RecipeTypes.SMELTING, items, "smelter", null);
    
    
        {
            for(Map.Entry<String, FireEffectSubTypeBase> subTypes : FireEffectSubTypeManager.getEffectsMap().get("firewood:block_to_block").entrySet())
            {
                registerRecipeSubCatalyst(registration, recipeTypes.get("block_to_block").get(subTypes.getKey()), items, "block_to_block", subTypes.getKey());
            }
        }
    
    
        {
            for(Map.Entry<String, FireEffectSubTypeBase> subTypes : FireEffectSubTypeManager.getEffectsMap().get("firewood:set_block_name").entrySet())
            {
                registerRecipeSubCatalyst(registration, recipeTypes.get("set_block_name").get(subTypes.getKey()), items, "set_block_name", subTypes.getKey());
            }
        }
    
    }
    
    public void registerRecipeSubCatalyst(IRecipeCatalystRegistration registration, RecipeType recipeType, ArrayList<Item> items, String type, String subType)
    {
        if(subType == null)
        {
            NonNullList<ItemStack> itemStacks = NonNullList.create();
            items.forEach(item -> FireEffectHelpers.getMajorHelperByType(type).fillItemCategory(itemStacks, new ItemStack(item)));
            itemStacks.forEach(itemStack -> registration.addRecipeCatalyst(itemStack, recipeType));
        }
        else
        {
            FireEffectNBTDataInterface defaultData = FireEffectHelpers.getMajorHelperByType(type).getDefaultData();
            defaultData.put("subType", subType);
            for(Item item : items)
            {
                registration.addRecipeCatalyst(FireEffectHelpers.addMajorEffects(new ItemStack(item), Collections.singletonList(defaultData)), recipeType);
            }
        }
    }
}