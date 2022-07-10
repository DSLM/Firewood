package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.PotionTinderRecipe;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class PotionTinderRecipeSerializer extends TinderRecipeSerializer<PotionTinderRecipe>
{
    public PotionTinderRecipeSerializer(Class<PotionTinderRecipe> recipeClass)
    {
        super(recipeClass);
    }
    
    @Override
    public PotionTinderRecipe fromJson(ResourceLocation id, JsonObject json)
    {
        Ingredient potion = Ingredient.fromJson(json.getAsJsonObject("potion"));
    
        String subType = json.get(StaticValue.SUB_TYPE).getAsString();
    
        return new PotionTinderRecipe(super.fromJson(id, json), potion, subType);
    }
    
    @Override
    public PotionTinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
    {
        Ingredient potion = Ingredient.fromNetwork(buf);
    
        String subType = buf.readUtf();
    
        return new PotionTinderRecipe(super.fromNetwork(id, buf), potion, subType);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, PotionTinderRecipe recipe)
    {
        recipe.getPotion().toNetwork(buf);
    
        buf.writeUtf(recipe.getSubType());
    
        super.toNetwork(buf, recipe);
    }
}
