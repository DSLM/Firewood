package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.FakeTransmuteBlockState;
import com.dslm.firewood.recipe.TransmuteBlockRecipeBase;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

public class TransmuteBlockRecipeSerializerBase<T extends TransmuteBlockRecipeBase> implements RecipeSerializer<T>
{
    ResourceLocation name;
    Class<T> recipeClass;
    
    
    public TransmuteBlockRecipeSerializerBase(Class<T> recipeClass)
    {
        this.recipeClass = recipeClass;
    }
    
    @SuppressWarnings("unchecked")
    private static <G> Class<G> castClass(Class<?> cls)
    {
        return (Class<G>) cls;
    }
    
    public T getRealClass(TransmuteBlockRecipeBase transmuteBlockRecipeBase)
    {
        try
        {
            if(recipeClass == null)
                return null;
            return recipeClass.getConstructor(TransmuteBlockRecipeBase.class).newInstance(transmuteBlockRecipeBase);
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
        FakeTransmuteBlockState blockState = FakeTransmuteBlockState.readFakeBlockState(CraftingHelper.getNBT(json.get(StaticValue.ING_BLOCK)));
        
        String type = json.get(StaticValue.TYPE).getAsString();
        
        String subType = json.get(StaticValue.SUB_TYPE).getAsString();
        
        return getRealClass(new TransmuteBlockRecipeBase(id, type, subType, blockState));
    }
    
    @Override
    public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
    {
        FakeTransmuteBlockState blockState = FakeTransmuteBlockState.readFakeBlockState(buf.readAnySizeNbt());
        
        String type = buf.readUtf();
        
        String subType = buf.readUtf();
        
        return getRealClass(new TransmuteBlockRecipeBase(id, type, subType, blockState));
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, TransmuteBlockRecipeBase recipe)
    {
        buf.writeNbt(FakeTransmuteBlockState.writeBlockState(recipe.getIngBlock()));
        
        buf.writeUtf(recipe.getRecipeType());
        
        buf.writeUtf(recipe.getRecipeSubType());
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
        return TransmuteBlockRecipeSerializerBase.castClass(RecipeSerializer.class);
    }
}
