package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.PotionFireEffectHelper;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.flesh.PotionFireEffectHelper.POTION_TAG_ID;
import static com.dslm.firewood.util.StaticValue.TYPE;

public class PotionTinderRecipe extends TinderRecipe
{
    protected Ingredient potion = Ingredient.of(new ItemStack(Items.POTION));
    
    public PotionTinderRecipe(TinderRecipe recipe, Ingredient potion)
    {
        super(recipe);
        this.potion = potion;
    }
    
    public PotionTinderRecipe(TinderRecipe recipe)
    {
        super(recipe);
    }
    
    @Override
    public boolean matches(SpiritualCampfireBlockEntity container, Level level)
    {
        ArrayList<ItemStack> inputs = new ArrayList<>(container.getIngredients());
        inputs.removeIf((i) -> i == null || i.isEmpty());
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(potion);
        }});
        return recipe != null && tinder.test(container.getTinder());
    }
    
    @Override
    public ItemStack assemble(SpiritualCampfireBlockEntity container)
    {
        ArrayList<ItemStack> inputs = new ArrayList<>(container.getIngredients());
        inputs.removeIf((i) -> i == null || i.isEmpty());
    
        ItemStack itemStack = super.assemble(container);
    
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(potion);
        }});
    
        final ItemStack[] finalItemStack = new ItemStack[1];
        container.getIngredients().forEach((i) -> {
            if(inputs.get(recipe[recipe.length - 1]) == i)
            {
                finalItemStack[0] = FireEffectHelpers.addMajorEffect(itemStack, "potion", new FireEffectNBTData()
                {{
                    put(TYPE, "potion");
                    put(POTION_TAG_ID, PotionUtils.getPotion(i).getRegistryName().toString());
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
    
    public Ingredient getPotion()
    {
        return potion;
    }
    
    @Override
    public List<Either<List<ItemStack>, Ingredient>> getJEIInputs(int num)
    {
        ArrayList<Either<List<ItemStack>, Ingredient>> list = new ArrayList<>();
        list.add(Either.left(getTinderListByNum(num / tinder.getItems().length)));
        
        if(potion.getItems().length > 0)
        {
            List<ItemStack> potionInput = new ArrayList<>();
            for(ItemStack itemStack : potion.getItems())
            {
                for(Potion potion : ForgeRegistries.POTIONS)
                {
                    if(potion == Potions.EMPTY) continue;
                    potionInput.add(PotionUtils.setPotion(itemStack.copy(), potion));
                }
            }
            list.add(Either.left(potionInput));
        }
        recipeItems.forEach(item -> list.add(Either.right(item)));
        return list;
    }
    
    @Override
    public List<ItemStack> getJEIResult()
    {
        NonNullList<ItemStack> list = NonNullList.create();
        getJEIResultItems().forEach(i ->
                PotionFireEffectHelper.getInstanceList().get(0).fillItemCategory(list, i.copy()));
        return list;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Register.POTION_TINDER_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return Register.POTION_TINDER_RECIPE_TYPE.get();
    }
}