package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.MobToItemRecipe;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;

public class MobToItemRecipeSerializer<T extends MobToItemRecipe> implements RecipeSerializer<T>
{
    ResourceLocation name;
    public static final String MOBS_BLACKLIST = "mobs_blacklist";
    public static final String MOBS_LIST = "mobs_list";
    public static final String ITEMS_LIST = "items_list";
    Class<T> recipeClass;
    
    public MobToItemRecipeSerializer(Class<T> recipeClass)
    {
        this.recipeClass = recipeClass;
    }
    
    
    @SuppressWarnings("unchecked")
    private static <G> Class<G> castClass(Class<?> cls)
    {
        return (Class<G>) cls;
    }
    
    public T getRealClass(MobToItemRecipe mobToItemRecipe)
    {
        try
        {
            if(recipeClass == null)
                return null;
            return recipeClass.getConstructor(MobToItemRecipe.class).newInstance(mobToItemRecipe);
        } catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public T fromJson(ResourceLocation id, JsonObject json)
    {
        if(!json.has(StaticValue.TYPE) || !json.has(StaticValue.SUB_TYPE) || !json.has(MOBS_LIST) || !json.has(ITEMS_LIST))
        {
            return null;
        }
    
        String type = json.get(StaticValue.TYPE).getAsString();
    
        String subType = json.get(StaticValue.SUB_TYPE).getAsString();
    
        boolean entitiesBlacklist = !json.has(MOBS_BLACKLIST) || json.get(MOBS_BLACKLIST).getAsBoolean();
    
        ArrayList<String> entitiesList = new ArrayList<>();
        json.getAsJsonArray(MOBS_LIST).forEach(jsonElement ->
                entitiesList.add(jsonElement.getAsString()));
        
        ArrayList<ItemStack> itemsList = new ArrayList<>();
        json.getAsJsonArray(ITEMS_LIST).forEach(jsonElement ->
                itemsList.add(CraftingHelper.getItemStack(jsonElement.getAsJsonObject(), true)));
    
        return getRealClass(new MobToItemRecipe(id, type, subType, entitiesBlacklist, entitiesList, itemsList));
    }
    
    @Override
    public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
    {
        String type = buf.readUtf();
        
        String subType = buf.readUtf();
        
        boolean entitiesBlacklist = buf.readBoolean();
        
        ArrayList<String> entitiesList = new ArrayList<>();
        int size = buf.readInt();
        for(int i = 0; i < size; i++)
        {
            entitiesList.add(buf.readUtf());
        }
        
        ArrayList<ItemStack> itemsList = new ArrayList<>();
        int size2 = buf.readInt();
        for(int i = 0; i < size2; i++)
        {
            itemsList.add(buf.readItem());
        }
    
        return getRealClass(new MobToItemRecipe(id, type, subType, entitiesBlacklist, entitiesList, itemsList));
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, MobToItemRecipe recipe)
    {
        buf.writeUtf(recipe.getRecipeType());
        
        buf.writeUtf(recipe.getRecipeSubType());
        
        buf.writeBoolean(recipe.isEntitiesBlacklist());
        
        buf.writeInt(recipe.getEntitiesList().size());
        for(var entity : recipe.getEntitiesList())
        {
            buf.writeUtf(entity);
        }
        
        buf.writeInt(recipe.getItemsList().size());
        for(var item : recipe.getItemsList())
        {
            buf.writeItem(item);
        }
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
        return MobToItemRecipeSerializer.castClass(RecipeSerializer.class);
    }
}
