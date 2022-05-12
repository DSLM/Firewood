package com.dslm.firewood.compat;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.recipe.PotionTinderRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

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
    public void registerRecipes(final IRecipeRegistration registration)
    {
        Minecraft minecraft = Objects.requireNonNull(Minecraft.getInstance());
        ClientLevel level = Objects.requireNonNull(minecraft.level);
        RecipeManager recipeManager = level.getRecipeManager();
        var recipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING)
                .stream()
                .filter(PotionTinderRecipe.class::isInstance)
                .map(PotionTinderRecipe.class::cast)
                .flatMap(this::potionRecipes)
                .toList();
        registration.addRecipes(RecipeTypes.CRAFTING, recipes);
    }
    
    private Stream<CraftingRecipe> potionRecipes(final PotionTinderRecipe recipe)
    {
        String group = "jei.potiontinder";
        Item potionItem = recipe.getPotionItem();
        return ForgeRegistries.POTIONS
                .getValues()
                .stream()
                .map(potion -> {
                    Ingredient potionIngredient = Ingredient.of(PotionUtils.setPotion(new ItemStack(potionItem), potion));
                    NonNullList<Ingredient> inputs = NonNullList.withSize(recipe.getOthersIng().size() + 2, Ingredient.EMPTY);
                    inputs.set(0, potionIngredient);
                    inputs.set(1, Ingredient.of(recipe.getTinderItem()));
                    for(int i = 0; i < recipe.getOthersIng().size(); i++)
                    {
                        inputs.set(i + 2, Ingredient.of(recipe.getOthersIng().get(i)));
                    }
                    String potionId = potion.getRegistryName().toString();
                    ItemStack output = FireEffectHelpers.addMajorEffect(new ItemStack(recipe.getTinderItem()), "potion", new HashMap<>()
                    {{
                        put("potion", potionId);
                    }});
                    
                    ResourceLocation id = new ResourceLocation(ModIds.MINECRAFT_ID, group + "." + output.getDescriptionId() + "." + potion.getName(""));
                    return new ShapelessRecipe(id, group, output, inputs);
                });
    }
    
    @Override
    public void registerItemSubtypes(ISubtypeRegistration r)
    {
//        r.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, Register.TINDER_ITEM.get(), (ItemStack itemStack, UidContext context) -> {
//            CompoundTag allNBT = itemStack.getTag();
//            if(allNBT == null || allNBT.isEmpty())
//            {
//                return IIngredientSubtypeInterpreter.NONE;
//            }
//            return allNBT.toString();
//        });
        //r.registerSubtypeInterpreter(Register.TINDER_ITEM.get(), (ingredient, context) -> String.valueOf(PotionUtils.getPotion(ingredient).getRegistryName()));
    }
}