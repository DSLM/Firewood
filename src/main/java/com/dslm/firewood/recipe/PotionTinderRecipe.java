package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.fireEffectHelper.block.PotionFireEffectHelper;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.block.PotionFireEffectHelper.potionTagId;

public class PotionTinderRecipe extends TinderRecipe
{
    protected final Ingredient potion;
    
    public PotionTinderRecipe(TinderRecipe recipe, Ingredient potion)
    {
        super(recipe);
        this.potion = potion;
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
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(potion);
        }});
        container.getIngredients().forEach((i) -> {
            if(inputs.get(recipe[recipe.length - 1]) == i)
            {
                addNBT.addMajorEffect(new HashMap<>()
                {{
                    put("type", "potion");
                    put(potionTagId, PotionUtils.getPotion(i).getRegistryName().toString());
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
    
    public Ingredient getPotion()
    {
        return potion;
    }
    
    public static class Type extends TinderRecipe.Type
    {
        public static final String ID = "potion_tinder_recipe";
    }
    
    public static class Serializer extends TinderRecipe.Serializer
    {
        public static final ResourceLocation ID =
                new ResourceLocation(Firewood.MOD_ID, Type.ID);
        
        @Override
        public PotionTinderRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            Ingredient potion = Ingredient.fromJson(json.getAsJsonObject("potion"));
            
            return new PotionTinderRecipe(super.fromJson(id, json), potion);
        }
        
        @Override
        public PotionTinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            Ingredient potion = Ingredient.fromNetwork(buf);
            
            return new PotionTinderRecipe(super.fromNetwork(id, buf), potion);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buf, TinderRecipe recipe)
        {
            if(recipe instanceof PotionTinderRecipe tele)
            {
                super.toNetwork(buf, recipe);
                tele.getPotion().toNetwork(buf);
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
    
    public List<ItemStack> getJEIResult()
    {
        NonNullList<ItemStack> list = NonNullList.create();
        for(ItemStack i : tinder.getItems())
        {
            PotionFireEffectHelper.getInstanceList().get(0).fillItemCategory(list, i.copy());
        }
        return list;
    }
}