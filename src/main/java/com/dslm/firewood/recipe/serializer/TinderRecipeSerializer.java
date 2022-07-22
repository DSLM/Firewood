package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.TinderRecipe;
import com.dslm.firewood.recipe.TinderRecipeAddNBT;
import com.dslm.firewood.recipe.TinderRecipeRemoveNBT;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Stream;

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
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public T fromJson(ResourceLocation id, JsonObject json)
    {
        if(Stream.of("ingredients",
                "tinder",
                StaticValue.PROCESS,
                StaticValue.CHANCE,
                StaticValue.DAMAGE,
                StaticValue.COOLDOWN,
                StaticValue.MIN_HEALTH).anyMatch(s -> !json.has(s)))
        {
            return null;
        }
    
        JsonArray ingredients = json.getAsJsonArray("ingredients");
        NonNullList<Ingredient> inputs = NonNullList.create();
        for(int i = 0; i < ingredients.size(); i++)
        {
            inputs.add(Ingredient.fromJson(ingredients.get(i)));
        }
    
        Ingredient tinder = Ingredient.fromJson(json.getAsJsonObject("tinder"));
    
        Item targetTinder = !json.has("targetTinder") ? null : ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("targetTinder").getAsString()));
    
        TinderRecipeAddNBT addEffects = !json.has("addEffects") ? new TinderRecipeAddNBT() : TinderRecipeAddNBT.fromJSON(json.getAsJsonObject("addEffects"));
        TinderRecipeRemoveNBT removeEffects = !json.has("removeEffects") ? new TinderRecipeRemoveNBT() : TinderRecipeRemoveNBT.fromJSON(json.getAsJsonObject("removeEffects"));
    
        int process = json.get(StaticValue.PROCESS).getAsInt();
    
        double chance = json.get(StaticValue.CHANCE).getAsDouble();
    
        double damage = json.get(StaticValue.DAMAGE).getAsDouble();
    
        int cooldown = json.get(StaticValue.COOLDOWN).getAsInt();
    
        double minHealth = json.get(StaticValue.MIN_HEALTH).getAsDouble();
    
        return getRealClass(new TinderRecipe(id, inputs, tinder, targetTinder, addEffects, removeEffects, process, chance, damage, cooldown, minHealth));
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
    
        String targetName = buf.readUtf();
        Item targetTinder = "".equals(targetName) ? null : ForgeRegistries.ITEMS.getValue(new ResourceLocation(targetName));
    
        TinderRecipeAddNBT addEffects = TinderRecipeAddNBT.fromNetwork(buf);
        TinderRecipeRemoveNBT removeEffects = TinderRecipeRemoveNBT.fromNetwork(buf);
    
        int process = buf.readInt();
    
        double chance = buf.readDouble();
    
        double damage = buf.readDouble();
    
        int cooldown = buf.readInt();
    
        double minHealth = buf.readDouble();
    
        return getRealClass(new TinderRecipe(id, inputs, tinder, targetTinder, addEffects, removeEffects, process, chance, damage, cooldown, minHealth));
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
    
        buf.writeUtf(recipe.getTargetTinder() == null ? "" : recipe.getTargetTinder().getRegistryName().toString());
    
        recipe.getAddEffects().toNetwork(buf);
        recipe.getRemoveEffects().toNetwork(buf);
    
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