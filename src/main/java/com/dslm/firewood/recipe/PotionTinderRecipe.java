package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import com.dslm.firewood.item.TinderItem;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import java.util.Objects;

import static com.dslm.firewood.Firewood.LOGGER;

public class PotionTinderRecipe extends CustomRecipe
{
    private @Nonnull
    final Item potionIng;
    private @Nonnull
    final TinderItem tinderIng;
    
    public PotionTinderRecipe(ResourceLocation id, @Nonnull Item potionIng, @Nonnull TinderItem tinderIng)
    {
        super(id);
        this.potionIng = potionIng;
        this.tinderIng = tinderIng;
    }
    
    @Override
    public boolean matches(CraftingContainer inv, @Nonnull Level world)
    {
        // TODO: 2022/5/10 支持多主要效果，并且不重复效果
        boolean potions = false;
        boolean vanilaTinder = false;
        for(int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack is = inv.getItem(i);
            if(is.getItem() == this.potionIng)
            {
                if(!potions)
                {
                    potions = true;
                }
            } else if(is.getItem() == this.tinderIng.asItem())
            {
                if(vanilaTinder) return false;
                vanilaTinder = true;
            } else if(!is.isEmpty()) return false;
        }
        return potions && vanilaTinder;
    }
    
    @Nonnull
    @Override
    public ItemStack assemble(CraftingContainer inv)
    {
        Potion potion = null;
        ItemStack tinder = null;
        String name = "";
        for(int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack is = inv.getItem(i);
            if(potion == null && is.getItem() == this.potionIng)
            {
                potion = PotionUtils.getPotion(is);
                name = is.getItem().getName(is).getString();
            }
            if(tinder == null && is.getItem() == this.tinderIng) tinder = is;
        }
        if(potion != null)
        {
            ItemStack output = tinder.copy();
            output.setCount(1);
            CompoundTag allNBT = output.getOrCreateTag();
            ListTag tags = (ListTag) allNBT.get("majorEffects");
            if(tags == null)
                tags = new ListTag();
            CompoundTag newEffect = new CompoundTag();
            newEffect.putString("type", "potion");
            newEffect.putString("name", name);
            newEffect.putString("potion", potion.getRegistryName().toString());
            tags.add(newEffect);
            allNBT.put("majorEffects", tags);
            output.setTag(allNBT);
            return output;
        }
        
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height > 2;
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
    
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PotionTinderRecipe>
    {
        @Nonnull
        @Override
        public PotionTinderRecipe fromJson(@Nonnull ResourceLocation rl, @Nonnull JsonObject json)
        {
            Item potion = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(json.get("potion").getAsString()));
            if(potion == null)
                throw new JsonParseException("Tried using an invalid item as potion item for recipe " + rl);
            Item tinder = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(json.get("tinder").getAsString()));
            if(tinder == null)
                throw new JsonParseException("Tried using an invalid item as potion tinder item for recipe " + rl);
            if(tinder instanceof TinderItem tinder1)
                return new PotionTinderRecipe(rl, potion, tinder1);
            else
                throw new JsonParseException("The defined PotionTinder is not an instance of TinderItem in recipe " + rl);
        }
        
        @Nullable
        @Override
        public PotionTinderRecipe fromNetwork(@Nonnull ResourceLocation rl, @Nonnull FriendlyByteBuf buf)
        {
            Item potion = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            Item tinder = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            return new PotionTinderRecipe(rl, potion, (TinderItem) tinder);
        }
        
        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull PotionTinderRecipe recipe)
        {
            buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(recipe.potionIng)));
            buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(recipe.tinderIng)));
        }
    }
}