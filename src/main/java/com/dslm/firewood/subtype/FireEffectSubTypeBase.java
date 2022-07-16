package com.dslm.firewood.subtype;

import com.dslm.firewood.compat.jei.subtype.info.FireEffectSubTypeJEIHandler;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface FireEffectSubTypeBase
{
    FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf);
    
    void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe);
    
    String getNamespace();
    
    void setNamespace(String namespace);
    
    String getPath();
    
    void setPath(String path);
    
    String getType();
    
    void setType(String type);
    
    String getSubType();
    
    void setSubType(String subType);
    
    int getColor();
    
    void setColor(int color);
    
    float getDamage();
    
    void setDamage(float damage);
    
    float getMinHealth();
    
    void setMinHealth(float minHealth);
    
    int getProcess();
    
    void setProcess(int process);
    
    float getChance();
    
    void setChance(float chance);
    
    int[] getRange();
    
    void setRange(int[] range);
    
    int getTargetLimit();
    
    void setTargetLimit(int targetLimit);
    
    int getCooldown();
    
    void setCooldown(int range);
    
    default List<ItemStack> getSubItems(List<Item> tinderItems)
    {
        FireEffectNBTDataInterface data = FireEffectHelpers.getMajorHelperByType(getType()).getDefaultData();
        data.setSubType(getSubType());
        ArrayList<ItemStack> results = new ArrayList<>(
                tinderItems.stream()
                        .map(tinderItem -> FireEffectHelpers.addMajorEffect(new ItemStack(tinderItem), getType(), data))
                        .toList());
        return results;
    }
    
    
    default FireEffectSubTypeJEIHandler getJEIHandler()
    {
        return FireEffectSubTypeJEIHandler.getInstance();
    }
}
