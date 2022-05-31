package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.TeleportTinderRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class TeleportTinderRecipeSerializer extends TinderRecipeSerializer<TeleportTinderRecipe>
{
    public TeleportTinderRecipeSerializer(Class<TeleportTinderRecipe> recipeClass)
    {
        super(recipeClass);
    }
    
    @Override
    public TeleportTinderRecipe fromJson(ResourceLocation id, JsonObject json)
    {
        Ingredient ember = Ingredient.fromJson(json.getAsJsonObject("ember"));
        
        return new TeleportTinderRecipe(super.fromJson(id, json), ember);
    }
    
    @Override
    public TeleportTinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
    {
        Ingredient ember = Ingredient.fromNetwork(buf);
        
        return new TeleportTinderRecipe(super.fromNetwork(id, buf), ember);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, TeleportTinderRecipe recipe)
    {
        recipe.getEmber().toNetwork(buf);
        
        super.toNetwork(buf, recipe);
    }
}
