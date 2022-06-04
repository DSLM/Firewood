package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.dslm.firewood.fireEffectHelper.flesh.GroundFireEffectHelper.BLOCK_TAG_ID;
import static com.dslm.firewood.util.StaticValue.TYPE;

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
                addEffects.addMinorEffect(new FireEffectNBTData()
                {{
                    put(TYPE, "ground");
                    put(BLOCK_TAG_ID, ((BlockItem) i.getItem()).getBlock().getRegistryName().toString());
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
        public boolean test(ItemStack itemStack)
        {
            return itemStack != null && itemStack.getItem() instanceof BlockItem;
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
        getJEIResultItems().forEach(i ->
        {
            for(Item item : ForgeRegistries.ITEMS)
            {
                if(item instanceof BlockItem blockItem)
                {
                    ItemStack stack = FireEffectHelpers.addMinorEffect(i.copy(), "ground", new FireEffectNBTData()
                    {{
                        put(BLOCK_TAG_ID, blockItem.getBlock().getRegistryName().toString());
                    }});
                    groundInput.add(stack);
                }
        
            }
        });
        return groundInput;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Register.GROUND_TINDER_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return Register.GROUND_TINDER_RECIPE_TYPE.get();
    }
}