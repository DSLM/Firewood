package com.dslm.firewood.fireEffectHelper.flesh.data;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.dslm.firewood.util.StaticValue.*;

public class FireEffectNBTData implements FireEffectNBTDataInterface
{
    HashMap<String, String> data = new HashMap<>();
    String type = "";
    String subType = "";
    int process = Integer.MIN_VALUE;
    boolean inCache = false;
    int[] cache = {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
    // center X, center Y, center Z, x, y, z
    
    public String getType()
    {
        return type;
    }
    
    @Override
    public String setType(String type)
    {
        this.type = type;
        return type;
    }
    
    public String getSubType()
    {
        return subType;
    }
    
    @Override
    public String setSubType(String subType)
    {
        this.subType = subType;
        return subType;
    }
    
    @Override
    public String get(String key)
    {
        return data.get(key);
    }
    
    @Override
    public String set(String key, String value)
    {
        return data.put(key, value);
    }
    
    @Override
    public int getProcess()
    {
        return process;
    }
    
    @Override
    public int setProcess(int process)
    {
        this.process = process;
        return process;
    }
    
    @Override
    public boolean isInCache()
    {
        return inCache;
    }
    
    @Override
    public boolean setInCache(boolean inCache)
    {
        this.inCache = inCache;
        return inCache;
    }
    
    @Override
    public int[] getCache()
    {
        return cache;
    }
    
    @Override
    public int[] setCache(int[] cache)
    {
        this.cache = cache;
        return cache;
    }
    
    @Override
    public FireEffectNBTDataInterface copy()
    {
        FireEffectNBTData newOne = new FireEffectNBTData();
        
        newOne.setType(type);
        newOne.setSubType(subType);
        newOne.setProcess(process);
        newOne.setInCache(inCache);
        newOne.setCache(cache.clone());
        
        for(Map.Entry<String, String> entry : data.entrySet())
        {
            newOne.set(entry.getKey(), entry.getValue());
        }
        return newOne;
    }
    
    @Override
    public Set<String> keySet()
    {
        return data.keySet();
    }
    
    @Override
    public int size()
    {
        return data.size();
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf)
    {
        buf.writeUtf(type);
        buf.writeUtf(subType);
        buf.writeInt(process);
        buf.writeBoolean(inCache);
        for(int i = 0; i < cache.length; i++)
        {
            buf.writeInt(cache[i]);
        }
        
        buf.writeInt(size());
        for(String j : keySet())
        {
            buf.writeUtf(j);
            buf.writeUtf(get(j));
        }
    }
    
    @Override
    public FireEffectNBTDataInterface fromNetwork(FriendlyByteBuf buf)
    {
        type = buf.readUtf();
        subType = buf.readUtf();
        process = buf.readInt();
        inCache = buf.readBoolean();
        for(int i = 0; i < cache.length; i++)
        {
            cache[i] = buf.readInt();
        }
        
        int mapSize = buf.readInt();
        for(int j = 0; j < mapSize; j++)
        {
            set(buf.readUtf(), buf.readUtf());
        }
        
        return this;
    }
    
    @Override
    public FireEffectNBTDataInterface fromJSON(JsonObject jsonObject)
    {
        for(var key : jsonObject.keySet())
        {
            switch(key)
            {
                case TYPE:
                    type = jsonObject.get(TYPE).getAsString();
                    break;
                case SUB_TYPE:
                    subType = jsonObject.get(SUB_TYPE).getAsString();
                    break;
                case PROCESS:
                    process = jsonObject.get(PROCESS).getAsInt();
                    break;
                case IN_CACHE:
                    inCache = jsonObject.get(IN_CACHE).getAsBoolean();
                    break;
                case CACHE:
                    var tempCache = jsonObject.getAsJsonArray(CACHE);
                    for(int i = 0; i < cache.length; i++)
                    {
                        cache[i] = tempCache.get(i).getAsInt();
                    }
                    break;
                default:
                    set(key, jsonObject.get(key).getAsString());
            }
        }
        return this;
    }
}
