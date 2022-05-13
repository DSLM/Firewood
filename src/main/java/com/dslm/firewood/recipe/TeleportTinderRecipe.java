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

public class TeleportTinderRecipe extends CustomRecipe
{
    @Nonnull
    private final Item emberIng;
    @Nonnull
    private final TinderItem tinderIng;
    private final ArrayList<Item> othersIng;
    
    public TeleportTinderRecipe(ResourceLocation id, @Nonnull Item emberIng, @Nonnull TinderItem tinderIng, ArrayList<Item> othersIng)
    {
        super(id);
        this.emberIng = emberIng;
        this.tinderIng = tinderIng;
        this.othersIng = othersIng;
    }
    
    @Override
    public boolean matches(CraftingContainer inv, @Nonnull Level world)
    {
        boolean ember = false;
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
            if(is.getItem() == this.emberIng && !ember && is.hasTag())
            {
                ember = true;
            } else if(is.getItem() == this.tinderIng.asItem() && !tinder)
            {
                tinder = true;
            } else
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
        return ember && tinder;
    }
    
    @Nonnull
    @Override
    public ItemStack assemble(CraftingContainer inv)
    {
        ItemStack ember = null;
        ItemStack tinder = null;
        for(int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack is = inv.getItem(i);
            if(ember == null && is.getItem() == this.emberIng)
            {
                ember = is;
            }
            if(tinder == null && is.getItem() == this.tinderIng) tinder = is;
        }
        if(ember != null && tinder != null && ember.hasTag())
        {
            //output
            String dim = ember.getTag().getString("dim");
            String posX = ember.getTag().getString("posX");
            String posY = ember.getTag().getString("posY");
            String posZ = ember.getTag().getString("posZ");
            ItemStack output = FireEffectHelpers.addMajorEffect(tinder.copy(), "teleport", new HashMap<>()
            {{
                put("dim", dim);
                put("posX", posX);
                put("posY", posY);
                put("posZ", posZ);
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
        return Register.TELEPORT_TINDER_RECIPE_SERIALIZER.get();
    }
    
    public TinderItem getTinderItem()
    {
        return this.tinderIng;
    }
    
    public Item getEmberItem()
    {
        return this.emberIng;
    }
    
    public ArrayList<Item> getOthersIng()
    {
        return this.othersIng;
    }
    
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TeleportTinderRecipe>
    {
        @Nonnull
        @Override
        public TeleportTinderRecipe fromJson(@Nonnull ResourceLocation rl, @Nonnull JsonObject json)
        {
            if(json.get("others").getAsJsonArray() == null)
                throw new JsonParseException("Tried using an invalid items as other items for recipe " + rl);
            ArrayList<Item> others = new ArrayList<>();
            for(JsonElement i : json.get("others").getAsJsonArray())
            {
                others.add(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(i.getAsString())));
            }
            Item ember = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(json.get("ember").getAsString()));
            if(ember == null)
                throw new JsonParseException("Tried using an invalid item as ember item for recipe " + rl);
            Item tinder = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(json.get("tinder").getAsString()));
            if(tinder == null)
                throw new JsonParseException("Tried using an invalid item as ember tinder item for recipe " + rl);
            if(tinder instanceof TinderItem tinder1)
                return new TeleportTinderRecipe(rl, ember, tinder1, others);
            else
                throw new JsonParseException("The defined GroundTinder is not an instance of TinderItem in recipe " + rl);
        }
        
        @Nullable
        @Override
        public TeleportTinderRecipe fromNetwork(@Nonnull ResourceLocation rl, @Nonnull FriendlyByteBuf buf)
        {
            Item ember = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            Item tinder = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            ArrayList<Item> others = new ArrayList<>();
            int num = buf.readInt();
            for(int i = 0; i < num; i++)
            {
                others.add(ForgeRegistries.ITEMS.getValue(buf.readResourceLocation()));
            }
            return new TeleportTinderRecipe(rl, ember, (TinderItem) tinder, others);
        }
        
        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull TeleportTinderRecipe recipe)
        {
            buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(recipe.emberIng)));
            buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(recipe.tinderIng)));
            buf.writeInt(recipe.othersIng.size());
            for(Item i : recipe.othersIng)
            {
                buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(i)));
            }
        }
    }
}