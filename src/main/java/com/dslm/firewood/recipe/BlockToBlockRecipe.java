package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;

public class BlockToBlockRecipe extends TransmuteBlockRecipeBase
{
    protected final BlockState targetBlock;
    
    public BlockToBlockRecipe(TransmuteBlockRecipeBase recipe, BlockState targetBlock)
    {
        super(recipe);
        this.targetBlock = targetBlock;
    }
    
    public BlockState getTargetBlock()
    {
        return targetBlock;
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
    
    public static class Type extends TransmuteBlockRecipeBase.Type
    {
        public static final String ID = "block_to_block";
    }
    
    public static class Serializer extends TransmuteBlockRecipeBase.Serializer
    {
        public static final String TARGET_BLOCK = "target_block";
        public static final ResourceLocation ID =
                new ResourceLocation(Firewood.MOD_ID, Type.ID);
    
        @Override
        public BlockToBlockRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            TransmuteBlockRecipeBase base = super.fromJson(id, json);
        
            BlockState blockState = NbtUtils.readBlockState(CraftingHelper.getNBT(json.get(TARGET_BLOCK)));
        
            return new BlockToBlockRecipe(base, blockState);
        }
    
        @Override
        public BlockToBlockRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            TransmuteBlockRecipeBase base = super.fromNetwork(id, buf);
        
            BlockState blockState = NbtUtils.readBlockState(buf.readNbt());
        
            return new BlockToBlockRecipe(base, blockState);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buf, TransmuteBlockRecipeBase recipe)
        {
            super.toNetwork(buf, recipe);
    
            if(recipe instanceof BlockToBlockRecipe blockRecipe)
            {
                buf.writeNbt(NbtUtils.writeBlockState(blockRecipe.getTargetBlock()));
            }
        }
        
        @Nullable
        @Override
        public ResourceLocation getRegistryName()
        {
            return ID;
        }
        
        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls)
        {
            return (Class<G>) cls;
        }
    }
}