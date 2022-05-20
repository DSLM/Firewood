package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.dslm.firewood.fireEffectHelper.GroundFireEffectHelper.blockTagId;

public class GroundTinderRecipe extends TinderRecipe
{
    protected final Ingredient block;
    
    public GroundTinderRecipe(TinderRecipe recipe)
    {
        super(recipe);
        this.block = new BlockItemIngredient();
    }
    
    @Override
    public boolean matches(SpiritualCampfireBlockEntity container, Level level)
    {
        ArrayList<ItemStack> inputs = new ArrayList<>(container.getIngredients());
        inputs.removeIf((i) -> i == null || i.isEmpty());
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(block);
        }});
        return recipe != null && tinder.test(container.getTinder());
    }
    
    @Override
    public ItemStack assemble(SpiritualCampfireBlockEntity container)
    {
        ArrayList<ItemStack> inputs = new ArrayList<>(container.getIngredients());
        inputs.removeIf((i) -> i == null || i.isEmpty());
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(block);
        }});
        container.getIngredients().forEach((i) -> {
            if(inputs.get(recipe[recipe.length - 1]) == i)
            {
                addNBT.addMinorEffect(new HashMap<>()
                {{
                    put("type", "ground");
                    put(blockTagId, ((BlockItem) i.getItem()).getBlock().getRegistryName().toString());
                }});
            }
        });
        return super.assemble(container);
    }
    
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    
    public Ingredient getBlock()
    {
        return block;
    }
    
    public class BlockItemIngredient extends Ingredient
    {
        
        public BlockItemIngredient()
        {
            super(Stream.of(new ItemValue(ItemStack.EMPTY)));
        }
        
        @Override
        public boolean test(@Nullable ItemStack itemStack)
        {
            return itemStack != null && itemStack.getItem() instanceof BlockItem;
        }
    }
    
    public static class Type extends TinderRecipe.Type
    {
        public static final String ID = "ground_tinder_recipe";
    }
    
    public static class Serializer extends TinderRecipe.Serializer
    {
        public static final ResourceLocation ID =
                new ResourceLocation(Firewood.MOD_ID, Type.ID);
        
        @Override
        public GroundTinderRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            return new GroundTinderRecipe(super.fromJson(id, json));
        }
        
        @Override
        public GroundTinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            
            return new GroundTinderRecipe(super.fromNetwork(id, buf));
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buf, TinderRecipe recipe)
        {
            if(recipe instanceof GroundTinderRecipe tele)
            {
                super.toNetwork(buf, recipe);
            }
        }
        
        @Nullable
        @Override
        public ResourceLocation getRegistryName()
        {
            return ID;
        }
    }
    
    public List<Either<List<ItemStack>, Ingredient>> getJEIInputs()
    {
        ArrayList<Either<List<ItemStack>, Ingredient>> list = new ArrayList<>();
        list.add(Either.right(tinder));
        
        List<ItemStack> groundInput = new ArrayList<>();
        for(Item item : ForgeRegistries.ITEMS)
        {
            if(item instanceof BlockItem)
            {
                groundInput.add(new ItemStack(item));
            }
            
        }
        list.add(Either.left(groundInput));
        
        recipeItems.forEach(item -> list.add(Either.right(item)));
        return list;
    }
    
    public List<ItemStack> getJEIResult()
    {
        List<ItemStack> groundInput = new ArrayList<>();
        for(ItemStack i : tinder.getItems())
        {
            for(Item item : ForgeRegistries.ITEMS)
            {
                if(item instanceof BlockItem blockItem)
                {
                    ItemStack stack = FireEffectHelpers.addMinorEffect(i.copy(), "ground", new HashMap<>()
                    {{
                        put(blockTagId, blockItem.getBlock().getRegistryName().toString());
                    }});
                    groundInput.add(stack);
                }
                
            }
        }
        return groundInput;
    }
}