package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.item.DyingEmberItem;
import com.dslm.firewood.util.StaticValue;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.flesh.TeleportFireEffectHelper.*;

public class TeleportTinderRecipe extends TinderRecipe
{
    protected final Ingredient ember;
    protected final String subType;
    
    public TeleportTinderRecipe(TinderRecipe recipe, Ingredient ember, String subType)
    {
        super(recipe);
        this.ember = ember;
        this.subType = subType;
    }
    
    public TeleportTinderRecipe(TinderRecipe recipe)
    {
        super(recipe);
        this.ember = Ingredient.of(Register.DYING_EMBER_ITEM.get());
        this.subType = "";
    }
    
    @Override
    public boolean matches(SpiritualCampfireBlockEntity container, Level level)
    {
        ArrayList<ItemStack> inputs = new ArrayList<>(container.getIngredients());
        inputs.removeIf((i) -> i == null || i.isEmpty());
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(ember);
        }});
    
        return recipe != null && matchEmber(inputs.get(recipe[recipe.length - 1]))
                && tinder.test(container.getTinder());
    }
    
    public boolean matchEmber(ItemStack itemStack)
    {
        return itemStack.hasTag()
                && itemStack.getTag().contains("dim")
                && itemStack.getTag().contains("posX")
                && itemStack.getTag().contains("posY")
                && itemStack.getTag().contains("posZ");
    }
    
    @Override
    public ItemStack assemble(SpiritualCampfireBlockEntity container)
    {
        ArrayList<ItemStack> inputs = new ArrayList<>(container.getIngredients());
        inputs.removeIf((i) -> i == null || i.isEmpty());
    
        ItemStack itemStack = super.assemble(container);
    
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(ember);
        }});
    
        final ItemStack[] finalItemStack = new ItemStack[1];
        container.getIngredients().forEach((i) -> {
            if(inputs.get(recipe[recipe.length - 1]) == i && matchEmber(i))
            {
                finalItemStack[0] = FireEffectHelpers.addMajorEffect(itemStack, "teleport", new FireEffectNBTData()
                {{
                    put(StaticValue.TYPE, "teleport");
                    put(StaticValue.SUB_TYPE, subType);
                    put(StaticValue.PROCESS, "0");
                    put(DIM_TAG_ID, i.getTag().getString(DIM_TAG_ID));
                    put(X_TAG_ID, i.getTag().getString(X_TAG_ID));
                    put(Y_TAG_ID, i.getTag().getString(Y_TAG_ID));
                    put(Z_TAG_ID, i.getTag().getString(Z_TAG_ID));
                }});
            }
        });
    
        return finalItemStack[0];
    }
    
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    
    public Ingredient getEmber()
    {
        return ember;
    }
    
    public String getSubType()
    {
        return subType;
    }
    
    @Override
    public List<Either<List<ItemStack>, Ingredient>> getJEIInputs(int num)
    {
        ArrayList<Either<List<ItemStack>, Ingredient>> list = new ArrayList<>();
        list.add(Either.left(getTinderListByNum(num / tinder.getItems().length)));
    
        if(ember.getItems().length > 0)
        {
            List<ItemStack> emberInput = new ArrayList<>(Arrays.asList(ember.getItems()));
            emberInput.forEach(ember -> DyingEmberItem.addPosition("overworld", new BlockPos(0, 256, 0), ember.copy()));
            list.add(Either.left(emberInput));
        }
        recipeItems.forEach(item -> list.add(Either.right(item)));
        return list;
    }
    
    @Override
    public List<ItemStack> getJEIResult()
    {
        NonNullList<ItemStack> list = NonNullList.create();
        getJEIResultItems().forEach(i ->
                list.add(
                        FireEffectHelpers.addMajorEffect(addEffects.implementEffects(i), "teleport", new FireEffectNBTData()
                                {{
                                    put(StaticValue.TYPE, "teleport");
                                    put(StaticValue.SUB_TYPE, subType);
                                    put(StaticValue.PROCESS, "0");
                                    put(DIM_TAG_ID, "overworld");
                                    put(X_TAG_ID, "0");
                                    put(Y_TAG_ID, "256");
                                    put(Z_TAG_ID, "0");
                                }}
                        )
                )
        );
        return list;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Register.TELEPORT_TINDER_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return Register.TELEPORT_TINDER_RECIPE_TYPE.get();
    }
}