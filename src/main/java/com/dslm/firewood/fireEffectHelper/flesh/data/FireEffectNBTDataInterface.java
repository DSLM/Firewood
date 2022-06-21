package com.dslm.firewood.fireEffectHelper.flesh.data;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Set;

public interface FireEffectNBTDataInterface
{
    String getType();
    
    String setType(String type);
    
    String getSubType();
    
    String setSubType(String subType);
    
    String get(String s);
    
    String set(String key, String value);
    
    int getProcess();
    
    int setProcess(int process);
    
    int[] getCache();
    
    int[] setCache(int[] cache);
    
    boolean isInCache();
    
    boolean setInCache(boolean inCache);
    
    FireEffectNBTDataInterface copy();
    
    Set<String> keySet();
    
    int size();
    
    void toNetwork(FriendlyByteBuf buf);
    
    FireEffectNBTDataInterface fromNetwork(FriendlyByteBuf buf);
    
    FireEffectNBTDataInterface fromJSON(JsonObject jsonObject);
}
