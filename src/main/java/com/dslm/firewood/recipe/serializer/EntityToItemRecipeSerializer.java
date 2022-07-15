package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.EntityToItemRecipe;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;

public class EntityToItemRecipeSerializer<T extends EntityToItemRecipe> implements RecipeSerializer<T>
{
    ResourceLocation name;
    public static final String ENTITIES_BLACKLIST = "entities_blacklist";
    public static final String ENTITIES_LIST = "entities_list";
    public static final String ITEMS_LIST = "items_list";
    Class<T> recipeClass;
    
    public EntityToItemRecipeSerializer(Class<T> recipeClass)
    {
        this.recipeClass = recipeClass;
    }
    
    
    @SuppressWarnings("unchecked")
    private static <G> Class<G> castClass(Class<?> cls)
    {
        return (Class<G>) cls;
    }
    
    public T getRealClass(EntityToItemRecipe entityToItemRecipe)
    {
        try
        {
            if(recipeClass == null)
                return null;
            return recipeClass.getConstructor(EntityToItemRecipe.class).newInstance(entityToItemRecipe);
        } catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public T fromJson(ResourceLocation id, JsonObject json)
    {
        String type = json.get(StaticValue.TYPE).getAsString();
        
        String subType = json.get(StaticValue.SUB_TYPE).getAsString();
        
        boolean entitiesBlacklist = !json.has(ENTITIES_BLACKLIST) || json.get(ENTITIES_BLACKLIST).getAsBoolean();
        
        if(!json.has(ENTITIES_LIST) || !json.has(ITEMS_LIST))
        {
            return null;
        }
        
        ArrayList<String> entitiesList = new ArrayList<>();
        json.getAsJsonArray(ENTITIES_LIST).forEach(jsonElement ->
                entitiesList.add(jsonElement.getAsString()));
        
        ArrayList<ItemStack> itemsList = new ArrayList<>();
        json.getAsJsonArray(ITEMS_LIST).forEach(jsonElement ->
                itemsList.add(CraftingHelper.getItemStack(jsonElement.getAsJsonObject(), true)));
        
        return getRealClass(new EntityToItemRecipe(id, type, subType, entitiesBlacklist, entitiesList, itemsList));
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
        
        return getRealClass(new EntityToItemRecipe(id, type, subType, entitiesBlacklist, entitiesList, itemsList));
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, EntityToItemRecipe recipe)
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
        return EntityToItemRecipeSerializer.castClass(RecipeSerializer.class);
    }
}
