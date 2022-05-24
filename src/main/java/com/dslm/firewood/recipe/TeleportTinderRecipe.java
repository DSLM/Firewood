package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.fireEffectHelper.block.TeleportFireEffectHelper;
import com.dslm.firewood.item.DyingEmberItem;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.block.TeleportFireEffectHelper.*;

public class TeleportTinderRecipe extends TinderRecipe
{
    protected final Ingredient ember;
    
    public TeleportTinderRecipe(TinderRecipe recipe, Ingredient ember)
    {
        super(recipe);
        this.ember = ember;
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
        int[] recipe = RecipeMatcher.findMatches(inputs, new ArrayList<>(recipeItems)
        {{
            add(ember);
        }});
        container.getIngredients().forEach((i) -> {
            if(inputs.get(recipe[recipe.length - 1]) == i && matchEmber(i))
            {
                addNBT.addMajorEffect(new HashMap<>()
                {{
                    put("type", "teleport");
                    put(DIM_TAG_ID, i.getTag().getString(DIM_TAG_ID));
                    put(X_TAG_ID, i.getTag().getString(X_TAG_ID));
                    put(Y_TAG_ID, i.getTag().getString(Y_TAG_ID));
                    put(Z_TAG_ID, i.getTag().getString(Z_TAG_ID));
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
    
    public Ingredient getEmber()
    {
        return ember;
    }
    
    public static class Type extends TinderRecipe.Type
    {
        public static final String ID = "teleport_tinder_recipe";
    }
    
    public static class Serializer extends TinderRecipe.Serializer
    {
        public static final ResourceLocation ID =
                new ResourceLocation(Firewood.MOD_ID, Type.ID);
        
        @Override
        public TeleportTinderRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            Ingredient ember = Ingredient.fromJson(json.getAsJsonObject("ember"));
            
            return new TeleportTinderRecipe(super.fromJson(id, json), ember);
        }
        
        @Override
        public TeleportTinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            Ingredient ember = Ingredient.fromNetwork(buf);
            
            return new TeleportTinderRecipe(super.fromNetwork(id, buf), ember);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buf, TinderRecipe recipe)
        {
            if(recipe instanceof TeleportTinderRecipe tele)
            {
                super.toNetwork(buf, recipe);
                tele.getEmber().toNetwork(buf);
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
        if(ember.getItems().length > 0)
        {
            List<ItemStack> emberInput = new ArrayList<>(Arrays.asList(ember.getItems()));
            emberInput.forEach(ember -> DyingEmberItem.addPosition("overworld", new BlockPos(0, 256, 0), ember.copy()));
            list.add(Either.left(emberInput));
        }
        recipeItems.forEach(item -> list.add(Either.right(item)));
        return list;
    }
    
    public List<ItemStack> getJEIResult()
    {
        NonNullList<ItemStack> list = NonNullList.create();
        for(ItemStack i : tinder.getItems())
        {
            TeleportFireEffectHelper.getInstanceList().get(0).fillItemCategory(list, i.copy());
        }
        return list;
    }
}