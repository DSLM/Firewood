package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.item.TinderItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PotionTinderRecipe extends CustomRecipe
{
    @Nonnull
    private final Item potionIng;
    @Nonnull
    private final TinderItem tinderIng;
    private final ArrayList<Item> othersIng;
    
    public PotionTinderRecipe(ResourceLocation id, @Nonnull Item potionIng, @Nonnull TinderItem tinderIng, ArrayList<Item> othersIng)
    {
        super(id);
        this.potionIng = potionIng;
        this.tinderIng = tinderIng;
        this.othersIng = othersIng;
    }
    
    @Override
    public boolean matches(CraftingContainer inv, @Nonnull Level world)
    {
        boolean potion = false;
        boolean tinder = false;
        ArrayList<Boolean> others = new ArrayList<>();
        for(Item item : othersIng)
        {
            others.add(false);
        }
        ingCheck:
        for(int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack is = inv.getItem(i);
            if(is.isEmpty()) continue;
            if(is.getItem() == this.potionIng && !potion)
            {
                potion = true;
            }
            else if(is.getItem() == this.tinderIng.asItem() && !tinder)
            {
                tinder = true;
            }
            else
            {
                for(int j = 0; j < othersIng.size(); j++)
                {
                    if(is.getItem() == this.othersIng.get(j).asItem() && !others.get(j))
                    {
                        others.set(j, true);
                        continue ingCheck;
                    }
                }
                if(!is.isEmpty()) return false;
            }
        }
        for(Boolean other : others)
        {
            if(!other) return false;
        }
        return potion && tinder;
    }
    
    @Nonnull
    @Override
    public ItemStack assemble(CraftingContainer inv)
    {
        Potion potion = null;
        ItemStack tinder = null;
        for(int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack is = inv.getItem(i);
            if(potion == null && is.getItem() == this.potionIng)
            {
                potion = PotionUtils.getPotion(is);
            }
            if(tinder == null && is.getItem() == this.tinderIng) tinder = is;
        }
        if(potion != null && tinder != null)
        {
            //output
            String potionId = potion.getRegistryName().toString();
            ItemStack output = FireEffectHelpers.addMajorEffect(tinder.copy(), "potion", new HashMap<>()
            {{
                put("potion", potionId);
            }});
            output.setCount(1);
            return output;
        }
        
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height > 2 + othersIng.size();
    }
    
    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Register.POTION_TINDER_RECIPE_SERIALIZER.get();
    }
    
    public TinderItem getTinderItem()
    {
        return this.tinderIng;
    }
    
    public Item getPotionItem()
    {
        return this.potionIng;
    }
    
    public ArrayList<Item> getOthersIng()
    {
        return this.othersIng;
    }
    
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PotionTinderRecipe>
    {
        @Nonnull
        @Override
        public PotionTinderRecipe fromJson(@Nonnull ResourceLocation rl, @Nonnull JsonObject json)
        {
            if(json.get("others").getAsJsonArray() == null)
                throw new JsonParseException("Tried using an invalid items as other items for recipe " + rl);
            ArrayList<Item> others = new ArrayList<>();
            for(JsonElement i : json.get("others").getAsJsonArray())
            {
                others.add(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(i.getAsString())));
            }
            Item potion = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(json.get("potion").getAsString()));
            if(potion == null)
                throw new JsonParseException("Tried using an invalid item as potion item for recipe " + rl);
            Item tinder = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(json.get("tinder").getAsString()));
            if(tinder == null)
                throw new JsonParseException("Tried using an invalid item as potion tinder item for recipe " + rl);
            if(tinder instanceof TinderItem tinder1)
                return new PotionTinderRecipe(rl, potion, tinder1, others);
            else
                throw new JsonParseException("The defined PotionTinder is not an instance of TinderItem in recipe " + rl);
        }
        
        @Nullable
        @Override
        public PotionTinderRecipe fromNetwork(@Nonnull ResourceLocation rl, @Nonnull FriendlyByteBuf buf)
        {
            Item potion = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            Item tinder = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            ArrayList<Item> others = new ArrayList<>();
            int num = buf.readInt();
            for(int i = 0; i < num; i++)
            {
                others.add(ForgeRegistries.ITEMS.getValue(buf.readResourceLocation()));
            }
            return new PotionTinderRecipe(rl, potion, (TinderItem) tinder, others);
        }
        
        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull PotionTinderRecipe recipe)
        {
            buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(recipe.potionIng)));
            buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(recipe.tinderIng)));
            buf.writeInt(recipe.othersIng.size());
            for(Item i : recipe.othersIng)
            {
                buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(i)));
            }
        }
    }
}