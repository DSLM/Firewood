package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.TinderRecipe;
import com.dslm.firewood.recipe.TinderRecipeNBT;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TinderRecipeSerializer<T extends TinderRecipe> implements RecipeSerializer<T>
{
    ResourceLocation name;
    Class<T> recipeClass;
    
    public TinderRecipeSerializer(Class<T> recipeClass)
    {
        this.recipeClass = recipeClass;
    }
    
    @SuppressWarnings("unchecked")
    private static <G> Class<G> castClass(Class<?> cls)
    {
        return (Class<G>) cls;
    }
    
    public T getRealClass(TinderRecipe tinderRecipe)
    {
        try
        {
            if(recipeClass == null)
                return null;
            return recipeClass.getConstructor(TinderRecipe.class).newInstance(tinderRecipe);
        } catch(Exception e)
        {
            System.out.println("----------------------");
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public T fromJson(ResourceLocation id, JsonObject json)
    {
        JsonArray ingredients = json.getAsJsonArray("ingredients");
        NonNullList<Ingredient> inputs = NonNullList.create();
        for(int i = 0; i < ingredients.size(); i++)
        {
            inputs.add(Ingredient.fromJson(ingredients.get(i)));
        }
    
        Ingredient tinder = Ingredient.fromJson(json.getAsJsonObject("tinder"));
    
        TinderRecipeNBT addEffects = TinderRecipeNBT.fromJSON(json.getAsJsonObject("addEffects"));
    
        int process = json.get("process").getAsInt();
        
        double chance = json.get("chance").getAsDouble();
        
        double damage = json.get("damage").getAsDouble();
        
        int cooldown = json.get("cooldown").getAsInt();
        
        double minHealth = json.get("minHealth").getAsDouble();
        
        return getRealClass(new TinderRecipe(id, inputs, tinder, addEffects, process, chance, damage, cooldown, minHealth));
    }
    
    @Override
    public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
    {
        NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
        for(int i = 0; i < inputs.size(); i++)
        {
            inputs.set(i, Ingredient.fromNetwork(buf));
        }
        
        Ingredient tinder = Ingredient.fromNetwork(buf);
        
        TinderRecipeNBT addEffects = TinderRecipeNBT.fromNetwork(buf);
        
        int process = buf.readInt();
        
        double chance = buf.readDouble();
        
        double damage = buf.readDouble();
        
        int cooldown = buf.readInt();
        
        double minHealth = buf.readDouble();
        
        return getRealClass(new TinderRecipe(id, inputs, tinder, addEffects, process, chance, damage, cooldown, minHealth));
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, T recipe)
    {
        buf.writeInt(recipe.getRecipeItems().size());
        for(Ingredient ing : recipe.getRecipeItems())
        {
            ing.toNetwork(buf);
        }
    
        recipe.getTinder().toNetwork(buf);
    
        recipe.getAddEffects().toNetwork(buf);
    
        buf.writeInt(recipe.getProcess());
        
        buf.writeDouble(recipe.getChance());
        
        buf.writeDouble(recipe.getDamage());
        
        buf.writeInt(recipe.getCooldown());
        
        buf.writeDouble(recipe.getMinHealth());
    }
    
    @Override
    public RecipeSerializer<?> setRegistryName(ResourceLocation name)
    {
        this.name = name;
        return this;
    }
    
    @Override
    public ResourceLocation getRegistryName()
    {
        return name;
    }
    
    @Override
    public Class<RecipeSerializer<?>> getRegistryType()
    {
        return TinderRecipeSerializer.castClass(RecipeSerializer.class);
    }
}