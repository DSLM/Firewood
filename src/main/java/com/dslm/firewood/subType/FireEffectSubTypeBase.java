package com.dslm.firewood.subType;

import net.minecraft.network.FriendlyByteBuf;

public interface FireEffectSubTypeBase
{
    FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf);
    
    void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe);
    
    String getNamespace();
    
    void setNamespace(String namespace);
    
    String getPath();
    
    void setPath(String path);
    
    String getId();
    
    void setId(String id);
    
    String getSubId();
    
    void setSubId(String subId);
    
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
    
    int getRange();
    
    void setRange(int range);
    
    int getCooldown();
    
    void setCooldown(int range);
}
