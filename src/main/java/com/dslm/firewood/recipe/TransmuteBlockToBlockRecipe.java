package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;
import java.util.List;

public class TransmuteBlockToBlockRecipe extends TransmuteBlockRecipeBase
{
    protected final BlockState targetBlock;
    
    public TransmuteBlockToBlockRecipe(TransmuteBlockRecipeBase recipe, BlockState targetBlock)
    {
        super(recipe);
        this.targetBlock = targetBlock;
    }
    
    @Override
    public ItemStack assemble(FakeTransmuteContainer container)
    {
        container.getLevel().setBlock(container.getPos(), targetBlock, Block.UPDATE_ALL);
        
        return null;
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
        public TransmuteBlockToBlockRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            TransmuteBlockRecipeBase base = super.fromJson(id, json);
            
            BlockState blockState = NbtUtils.readBlockState(CraftingHelper.getNBT(json.get(TARGET_BLOCK)));
            
            return new TransmuteBlockToBlockRecipe(base, blockState);
        }
        
        @Override
        public TransmuteBlockToBlockRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            TransmuteBlockRecipeBase base = super.fromNetwork(id, buf);
            
            BlockState blockState = NbtUtils.readBlockState(buf.readNbt());
            
            return new TransmuteBlockToBlockRecipe(base, blockState);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buf, TransmuteBlockRecipeBase recipe)
        {
            super.toNetwork(buf, recipe);
            
            if(recipe instanceof TransmuteBlockToBlockRecipe blockRecipe)
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
    
    public List<Either<List<ItemStack>, Ingredient>> getJEIInputs()
    {
        return null;
    }
    
    public List<ItemStack> getJEIResult()
    {
        return null;
    }
}