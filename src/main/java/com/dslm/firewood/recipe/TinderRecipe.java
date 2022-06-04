package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TinderRecipe implements Recipe<SpiritualCampfireBlockEntity>
{
    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> recipeItems;
    protected final Ingredient tinder;
    protected final Item targetTinder;
    protected final TinderRecipeNBT addEffects;
    protected final int process;
    protected final double chance;
    protected final double damage;
    protected final int cooldown;
    protected final double minHealth;
    
    public TinderRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, Ingredient tinder, Item targetTinder, TinderRecipeNBT addEffects,
                        int process, double chance, double damage, int cooldown, double minHealth)
    {
        this.id = id;
        this.recipeItems = recipeItems;
        this.tinder = tinder;
        this.targetTinder = targetTinder;
        this.addEffects = addEffects;
        this.process = process;
        this.chance = chance;
        this.damage = damage;
        this.cooldown = cooldown;
        this.minHealth = minHealth;
    }
    
    public TinderRecipe(TinderRecipe copy)
    {
        this.id = copy.id;
        this.recipeItems = copy.recipeItems;
        this.tinder = copy.tinder;
        this.targetTinder = copy.targetTinder;
        this.addEffects = copy.addEffects;
        this.process = copy.process;
        this.chance = copy.chance;
        this.damage = copy.damage;
        this.cooldown = copy.cooldown;
        this.minHealth = copy.minHealth;
    }
    
    @Override
    public boolean matches(SpiritualCampfireBlockEntity container, Level level)
    {
        ArrayList<ItemStack> inputs = new ArrayList<>(container.getIngredients());
        inputs.removeIf((i) -> i == null || i.isEmpty());
        return RecipeMatcher.findMatches(inputs, recipeItems) != null
                && tinder.test(container.getTinder());
    }
    
    @Override
    public ItemStack assemble(SpiritualCampfireBlockEntity container)
    {
        if(targetTinder == null)
            return addEffects.implementEffects(container.getTinder());
        ItemStack itemStack = new ItemStack(targetTinder);
        itemStack.setTag(container.getTinder().getOrCreateTag());
        return addEffects.implementEffects(itemStack);
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return recipeItems.size() + 1 <= width * height;
    }
    
    @Override
    public ItemStack getResultItem()
    {
        return addEffects.implementEffects(new ItemStack(Register.TINDER_ITEM.get()));
    }
    
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Register.TINDER_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return Register.TINDER_RECIPE_TYPE.get();
    }
    
    public Ingredient getTinder()
    {
        return tinder;
    }
    
    public Item getTargetTinder()
    {
        return targetTinder;
    }
    
    public TinderRecipeNBT getAddEffects()
    {
        return addEffects;
    }
    
    public int getProcess()
    {
        return process;
    }
    
    public double getChance()
    {
        return chance;
    }
    
    public double getDamage()
    {
        return damage;
    }
    
    public int getCooldown()
    {
        return cooldown;
    }
    
    public double getMinHealth()
    {
        return minHealth;
    }
    
    public NonNullList<Ingredient> getRecipeItems()
    {
        return recipeItems;
    }
    
    public List<Either<List<ItemStack>, Ingredient>> getJEIInputs()
    {
        ArrayList<Either<List<ItemStack>, Ingredient>> list = new ArrayList<>();
        list.add(Either.right(tinder));
        recipeItems.forEach(item -> list.add(Either.right(item)));
        return list;
    }
    
    public Stream<ItemStack> getJEIResultItems()
    {
        if(targetTinder != null)
        {
            ItemStack itemStack = new ItemStack(targetTinder);
            return Stream.of(itemStack);
        }
        return Arrays.stream(tinder.getItems()).map(ItemStack::copy);
    }
    
    public List<ItemStack> getJEIResult()
    {
        return getJEIResultItems().map(itemStack -> addEffects.implementEffects(itemStack.copy())).toList();
    }
}