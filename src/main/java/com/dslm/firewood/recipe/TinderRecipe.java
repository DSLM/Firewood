package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TinderRecipe implements Recipe<SpiritualCampfireBlockEntity>
{
    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> recipeItems;
    protected final Ingredient tinder;
    protected final TinderRecipeNBT addNBT;
    protected final int process;
    protected final double chance;
    protected final double damage;
    protected final int cooldown;
    
    public TinderRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, Ingredient tinder, TinderRecipeNBT addNBT,
                        int process, double chance, double damage, int cooldown)
    {
        this.id = id;
        this.recipeItems = recipeItems;
        this.tinder = tinder;
        this.addNBT = addNBT;
        this.process = process;
        this.chance = chance;
        this.damage = damage;
        this.cooldown = cooldown;
    }
    
    public TinderRecipe(TinderRecipe copy)
    {
        this.id = copy.id;
        this.recipeItems = copy.recipeItems;
        this.tinder = copy.tinder;
        this.addNBT = copy.addNBT;
        this.process = copy.process;
        this.chance = copy.chance;
        this.damage = copy.damage;
        this.cooldown = copy.cooldown;
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
        return addNBT.implementEffects(container.getTinder());
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return recipeItems.size() + 1 <= width * height;
    }
    
    @Override
    public ItemStack getResultItem()
    {
        return addNBT.implementEffects(new ItemStack(Register.TINDER_ITEM.get()));
    }
    
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Serializer.INSTANCE;
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return Type.INSTANCE;
    }
    
    public Ingredient getTinder()
    {
        return tinder;
    }
    
    public TinderRecipeNBT getAddNBT()
    {
        return addNBT;
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
    
    public static class Type implements RecipeType<TinderRecipe>
    {
        protected Type()
        {
        }
        
        public static final Type INSTANCE = new Type();
        public static final String ID = "tinder_recipe";
    }
    
    public static class Serializer implements RecipeSerializer<TinderRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Firewood.MOD_ID, Type.ID);
        
        @Override
        public TinderRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            JsonArray ingredients = json.getAsJsonArray("ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            for(int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
    
            Ingredient tinder = Ingredient.fromJson(json.getAsJsonObject("tinder"));
    
            TinderRecipeNBT addNBT = TinderRecipeNBT.fromJSON(json.getAsJsonObject("addNBT"));
    
            int process = json.get("process").getAsInt();
    
            double chance = json.get("chance").getAsDouble();
    
            double damage = json.get("damage").getAsDouble();
    
            int cooldown = json.get("cooldown").getAsInt();
    
            return new TinderRecipe(id, inputs, tinder, addNBT, process, chance, damage, cooldown);
        }
        
        @Override
        public TinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            for(int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }
    
            Ingredient tinder = Ingredient.fromNetwork(buf);
    
            TinderRecipeNBT addNBT = TinderRecipeNBT.fromNetwork(buf);
    
            int process = buf.readInt();
    
            double chance = buf.readDouble();
    
            double damage = buf.readDouble();
    
            int cooldown = buf.readInt();
    
            return new TinderRecipe(id, inputs, tinder, addNBT, process, chance, damage, cooldown);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buf, TinderRecipe recipe)
        {
            buf.writeInt(recipe.getIngredients().size());
            for(Ingredient ing : recipe.getIngredients())
            {
                ing.toNetwork(buf);
            }
    
            recipe.getTinder().toNetwork(buf);
    
            recipe.getAddNBT().toNetwork(buf);
    
            buf.writeInt(recipe.getProcess());
    
            buf.writeDouble(recipe.getChance());
    
            buf.writeDouble(recipe.getDamage());
    
            buf.writeInt(recipe.getCooldown());
        }
        
        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name)
        {
            return INSTANCE;
        }
        
        @Nullable
        @Override
        public ResourceLocation getRegistryName()
        {
            return ID;
        }
        
        @Override
        public Class<RecipeSerializer<?>> getRegistryType()
        {
            return Serializer.castClass(RecipeSerializer.class);
        }
    
        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls)
        {
            return (Class<G>) cls;
        }
    }
    
    public List<Either<List<ItemStack>, Ingredient>> getJEIInputs()
    {
        ArrayList<Either<List<ItemStack>, Ingredient>> list = new ArrayList<>();
        list.add(Either.right(tinder));
        recipeItems.forEach(item -> list.add(Either.right(item)));
        return list;
    }
    
    public List<ItemStack> getJEIResult()
    {
        return Arrays.stream(tinder.getItems()).map(itemStack -> addNBT.implementEffects(itemStack.copy())).toList();
    }
}