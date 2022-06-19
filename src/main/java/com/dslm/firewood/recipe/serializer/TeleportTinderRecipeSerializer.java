package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.TeleportTinderRecipe;
import com.dslm.firewood.util.StaticValue;
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
    
        String subType = json.get(StaticValue.SUB_TYPE).getAsString();
    
        return new TeleportTinderRecipe(super.fromJson(id, json), ember, subType);
    }
    
    @Override
    public TeleportTinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
    {
        Ingredient ember = Ingredient.fromNetwork(buf);
    
        String subType = buf.readUtf();
    
        return new TeleportTinderRecipe(super.fromNetwork(id, buf), ember, subType);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, TeleportTinderRecipe recipe)
    {
        recipe.getEmber().toNetwork(buf);
    
        buf.writeUtf(recipe.getSubType());
    
        super.toNetwork(buf, recipe);
    }
}
