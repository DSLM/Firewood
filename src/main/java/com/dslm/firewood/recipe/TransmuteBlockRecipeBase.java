package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;
import java.util.List;

public class TransmuteBlockRecipeBase implements Recipe<FakeTransmuteContainer>
{
    protected final ResourceLocation id;
    protected final String recipeType;
    protected final String recipeSubType;
    protected final FakeTransmuteBlockState ingBlock;
    
    public TransmuteBlockRecipeBase(ResourceLocation id, String recipeType, String recipeSubType, FakeTransmuteBlockState ingBlock)
    {
        this.id = id;
        this.recipeType = recipeType;
        this.recipeSubType = recipeSubType;
        this.ingBlock = ingBlock;
    }
    
    public TransmuteBlockRecipeBase(TransmuteBlockRecipeBase copy)
    {
        this.id = copy.id;
        this.recipeType = copy.recipeType;
        this.recipeSubType = copy.recipeSubType;
        this.ingBlock = copy.ingBlock;
    }
    
    @Override
    public boolean matches(FakeTransmuteContainer container, Level level)
    {
        return container.isSame(ingBlock);
    }
    
    @Override
    public ItemStack assemble(FakeTransmuteContainer container)
    {
        return null;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }
    
    @Override
    public ItemStack getResultItem()
    {
        return null;
    }
    
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    
    public String getRecipeType()
    {
        return recipeType;
    }
    
    public String getRecipeSubType()
    {
        return recipeSubType;
    }
    
    public FakeTransmuteBlockState getIngBlock()
    {
        return ingBlock;
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
    
    public static class Type implements RecipeType<TransmuteBlockRecipeBase>
    {
        protected Type()
        {
        }
        
        public static final Type INSTANCE = new Type();
        public static final String ID = "transmute";
    }
    
    public static class Serializer implements RecipeSerializer<TransmuteBlockRecipeBase>
    {
        public static final String ING_BLOCK = "ing_block";
        public static final String TYPE = "type";
        public static final String SUB_TYPE = "subType";
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Firewood.MOD_ID, Type.ID);
        
        @Override
        public TransmuteBlockRecipeBase fromJson(ResourceLocation id, JsonObject json)
        {
            FakeTransmuteBlockState blockState = FakeTransmuteBlockState.readFakeBlockState(CraftingHelper.getNBT(json.get(ING_BLOCK)));
            
            String type = json.get(TYPE).getAsString();
            
            String subType = json.get(SUB_TYPE).getAsString();
            
            return new TransmuteBlockRecipeBase(id, type, subType, blockState);
        }
        
        @Override
        public TransmuteBlockRecipeBase fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            FakeTransmuteBlockState blockState = FakeTransmuteBlockState.readFakeBlockState(buf.readNbt());
            
            String type = buf.readUtf();
            
            String subType = buf.readUtf();
            
            return new TransmuteBlockRecipeBase(id, type, subType, blockState);
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
        return null;
    }
    
    public List<ItemStack> getJEIResult()
    {
        return null;
    }
}