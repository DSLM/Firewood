package com.dslm.firewood.recipe.serializer;

import com.dslm.firewood.recipe.BlockToBlockRecipe;
import com.dslm.firewood.recipe.TransmuteBlockRecipeBase;
import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.CraftingHelper;

public class BlockToBlockRecipeSerializer extends TransmuteBlockRecipeSerializerBase<BlockToBlockRecipe>
{
    
    public BlockToBlockRecipeSerializer(Class<BlockToBlockRecipe> recipeClass)
    {
        super(recipeClass);
    }
    
    @Override
    public BlockToBlockRecipe fromJson(ResourceLocation id, JsonObject json)
    {
        if(!json.has(StaticValue.TARGET_BLOCK))
        {
            return null;
        }
    
        TransmuteBlockRecipeBase base = super.fromJson(id, json);
    
        if(base == null)
        {
            return null;
        }
    
        BlockState blockState = NbtUtils.readBlockState(CraftingHelper.getNBT(json.get(StaticValue.TARGET_BLOCK)));
    
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
}
