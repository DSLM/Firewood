package com.dslm.firewood.subtype;

import com.dslm.firewood.compat.jei.subtype.FireEffectSubTypeJEIHandler;
import com.dslm.firewood.compat.jei.subtype.PotionSubTypeJEIHandler;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.dslm.firewood.fireeffecthelper.flesh.PotionFireEffectHelper.POTION_TAG_ID;

public class PotionSubType extends FireEffectSubType
{
    protected boolean blacklist = true;
    protected ArrayList<String> list = new ArrayList<>();
    protected double colorMixed = 0;
    protected double effectMulti = 1;
    protected boolean toEnemy = false;
    
    protected ArrayList<String> realPotionList;
    
    public PotionSubType()
    {
    
    }
    
    public PotionSubType(FireEffectSubType fireEffectSubType,
                         boolean blacklist,
                         ArrayList<String> list,
                         double colorMixed,
                         double effectMulti,
                         boolean toEnemy)
    {
        super(fireEffectSubType);
        this.blacklist = blacklist;
        this.list = list;
        this.colorMixed = colorMixed;
        this.effectMulti = effectMulti;
        this.toEnemy = toEnemy;
    }
    
    public PotionSubType(FireEffectSubType copy,
                         JsonElement blacklistJsonElement,
                         JsonElement listJsonElement,
                         JsonElement colorMixedJsonElement,
                         JsonElement timeMultiJsonElement,
                         JsonElement toEnemyJsonElement)
    {
        super(copy);
        if(blacklistJsonElement != null && !blacklistJsonElement.isJsonNull())
        {
            blacklist = blacklistJsonElement.getAsBoolean();
        }
        if(listJsonElement instanceof JsonArray listJsonArray)
        {
            listJsonArray.forEach(jsonElement -> list.add(jsonElement.getAsString()));
        }
        if(colorMixedJsonElement != null && !colorMixedJsonElement.isJsonNull())
        {
            colorMixed = colorMixedJsonElement.getAsDouble();
        }
        if(timeMultiJsonElement != null && !timeMultiJsonElement.isJsonNull())
        {
            effectMulti = timeMultiJsonElement.getAsDouble();
        }
        if(toEnemyJsonElement != null && !toEnemyJsonElement.isJsonNull())
        {
            toEnemy = toEnemyJsonElement.getAsBoolean();
        }
    }
    
    public PotionSubType(FriendlyByteBuf buf)
    {
        this();
        copyFrom((PotionSubType) fromNetwork(buf));
    }
    
    public void getAllPotions()
    {
        realPotionList = new ArrayList<>();
        for(Potion potion : ForgeRegistries.POTIONS)
        {
            if(includePotion(potion.getRegistryName().toString()))
            {
                realPotionList.add(potion.getRegistryName().toString());
            }
        }
    }
    
    public void copyFrom(PotionSubType copy)
    {
        super.copyFrom(copy);
        this.blacklist = copy.blacklist;
        this.list = copy.list;
        this.colorMixed = copy.colorMixed;
        this.effectMulti = copy.effectMulti;
        this.toEnemy = copy.toEnemy;
    }
    
    public boolean includePotion(String potion)
    {
        return blacklist != list.contains(potion);
    }
    
    public NonNullList<ItemStack> getSubItems(ItemStack itemStack)
    {
        if(realPotionList == null)
        {
            getAllPotions();
        }
    
        NonNullList<ItemStack> list = NonNullList.create();
        realPotionList.stream().map(s -> FireEffectHelpers.addMajorEffect(itemStack.copy(), "potion", new FireEffectNBTData()
                {{
                    setType("potion");
                    setSubType(subType);
                    setProcess(0);
                    set(POTION_TAG_ID, s);
                }}
        )).forEach(list::add);
        
        return list;
    }
    
    public boolean isBlacklist()
    {
        return blacklist;
    }
    
    public void setBlacklist(boolean blacklist)
    {
        this.blacklist = blacklist;
    }
    
    public ArrayList<String> getList()
    {
        return list;
    }
    
    public void setList(ArrayList<String> list)
    {
        this.list = list;
    }
    
    public double getColorMixed()
    {
        return colorMixed;
    }
    
    public void setColorMixed(double colorMixed)
    {
        this.colorMixed = colorMixed;
    }
    
    public double getEffectMulti()
    {
        return effectMulti;
    }
    
    public void setEffectMulti(double effectMulti)
    {
        this.effectMulti = effectMulti;
    }
    
    public boolean isToEnemy()
    {
        return toEnemy;
    }
    
    public void setToEnemy(boolean toEnemy)
    {
        this.toEnemy = toEnemy;
    }
    
    public ArrayList<String> getRealPotionList()
    {
        if(realPotionList == null)
        {
            getAllPotions();
        }
        return realPotionList;
    }
    
    public void setRealPotionList(ArrayList<String> realPotionList)
    {
        this.realPotionList = realPotionList;
    }
    
    @Override
    public FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf)
    {
        FireEffectSubType fireEffectSubType = (FireEffectSubType) super.fromNetwork(buf);
        
        boolean blacklist1 = buf.readBoolean();
        ArrayList<String> list1 = new ArrayList<>();
        int size1 = buf.readInt();
        for(int j = 0; j < size1; j++)
        {
            list1.add(buf.readUtf());
        }
        
        return new PotionSubType(fireEffectSubType, blacklist1, list1, buf.readDouble(), buf.readDouble(), buf.readBoolean());
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe)
    {
        super.toNetwork(buf, recipe);
        
        buf.writeBoolean(blacklist);
        
        buf.writeInt(list.size());
    
        for(String string : list)
        {
            buf.writeUtf(string);
        }
    
        buf.writeDouble(colorMixed);
        buf.writeDouble(effectMulti);
        buf.writeBoolean(toEnemy);
    }
    
    @Override
    public List<ItemStack> getSubItems(List<Item> tinderItems)
    {
        ArrayList<ItemStack> results = new ArrayList<>();
        for(Potion potion : ForgeRegistries.POTIONS)
        {
            if(!includePotion(potion.getRegistryName().toString()))
            {
                continue;
            }
            
            FireEffectNBTDataInterface defaultData = FireEffectHelpers.getMajorHelperByType(getType()).getDefaultData();
            defaultData.setSubType(subType);
            defaultData.set(POTION_TAG_ID, potion.getRegistryName().toString());
            
            
            results.addAll(tinderItems.stream()
                    .map(item -> FireEffectHelpers.addMajorEffect(new ItemStack(item), POTION_TAG_ID, defaultData))
                    .toList());
        }
        return results;
    }
    
    @Override
    public FireEffectSubTypeJEIHandler getJEIHandler()
    {
        return PotionSubTypeJEIHandler.getInstance();
    }
}
