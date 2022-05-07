package com.dslm.firewood.compat;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import com.dslm.firewood.recipe.PotionTinderRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

import static com.dslm.firewood.Firewood.LOGGER;

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
                .flatMap(this::mapRecipes)
                .toList();
        registration.addRecipes(recipes, VanillaRecipeCategoryUid.CRAFTING);
    }
    
    private Stream<CraftingRecipe> mapRecipes(final PotionTinderRecipe recipe)
    {
        String group = "jei.potiontinder";
        Item potionItem = recipe.getPotionItem();
        return ForgeRegistries.POTIONS
                .getValues()
                .stream()
                .map(potion -> {
                    Ingredient potionIngredient = Ingredient.of(PotionUtils.setPotion(new ItemStack(potionItem), potion));
                    NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);
                    inputs.set(0, potionIngredient);
                    inputs.set(1, Ingredient.of(recipe.getTinderItem()));
                    ItemStack output = new ItemStack(recipe.getTinderItem());
                    
                    CompoundTag allNBT = output.getOrCreateTag();
                    ListTag tags = new ListTag();
                    CompoundTag newEffect = new CompoundTag();
                    newEffect.putString("type", "potion");
                    String name = new TranslatableComponent(potion.getName(Util.makeDescriptionId("item", Items.POTION.getRegistryName()) + ".effect.")).getString();
                    newEffect.putString("name", name);
                    newEffect.putString("potion", potion.getRegistryName().toString());
                    tags.add(newEffect);
                    allNBT.put("majorEffects", tags);
                    output.setTag(allNBT);
                    
                    ResourceLocation id = new ResourceLocation(ModIds.MINECRAFT_ID, group + "." + output.getDescriptionId() + "." + potion.getName(""));
                    return new ShapelessRecipe(id, group, output, inputs);
                });
    }
    
    @Override
    public void registerItemSubtypes(ISubtypeRegistration r)
    {
        r.registerSubtypeInterpreter(Register.TINDER_ITEM.get(), (ingredient, context) -> String.valueOf(PotionUtils.getPotion(ingredient).getRegistryName()));
    }
}