package com.dslm.firewood.subType;

import com.dslm.firewood.Firewood;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

import static com.dslm.firewood.util.StaticValue.TYPE;

public class FireEffectSubTypeManager extends SimpleJsonResourceReloadListener
{
    private static final Gson GSON = (new GsonBuilder()).create();
    
    public static final FireEffectSubTypeBuilder DEFAULT_TYPE_BUILDER = new FireEffectSubTypeBuilder();
    public static final SetBlockNameSubTypeBuilder SET_BLOCK_NAME_SUB_TYPE_BUILDER = new SetBlockNameSubTypeBuilder();
    
    public static final HashMap<String, FireEffectSubTypeBuilderBase> typeBuilders = new HashMap<>()
    {{
        put("firewood:teleport", DEFAULT_TYPE_BUILDER);
        put("firewood:smelter", DEFAULT_TYPE_BUILDER);
        put("firewood:block_to_block", DEFAULT_TYPE_BUILDER);
        
        put("firewood:set_block_name", SET_BLOCK_NAME_SUB_TYPE_BUILDER);
    }};
    
    public static final HashMap<String, HashMap<String, FireEffectSubTypeBase>> effectsMap = new HashMap<>();
    
    public FireEffectSubTypeManager()
    {
        super(GSON, "firewood_fire_effects");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler)
    {
        HashMap<ResourceLocation, JsonObject> effects = new HashMap<>();
        for(Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet())
        {
            ResourceLocation resourcelocation = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            try
            {
                if(jsonElement instanceof JsonObject jsonObject)
                {
                    effects.put(resourcelocation, jsonObject);
                }
            } catch(Exception e)
            {
                Firewood.LOGGER.error("Couldn't read firewood recipe types from {}", resourcelocation, e);
            }
        }
    
        effectsMap.clear();
    
        for(String key : typeBuilders.keySet())
        {
            effectsMap.put(key, new HashMap<>());
        }
    
        for(Map.Entry<ResourceLocation, JsonObject> entry : effects.entrySet())
        {
            FireEffectSubTypeBuilderBase builder = typeBuilders.getOrDefault(entry.getValue().get(TYPE).getAsString(), DEFAULT_TYPE_BUILDER);
            FireEffectSubTypeBase newData = builder.getNewData(entry.getKey(), entry.getValue());
    
            if(newData == null)
            {
                continue;
            }
    
            if(!effectsMap.containsKey(newData.getId()))
            {
                effectsMap.put(newData.getId(), new HashMap<>());
            }
            effectsMap.get(newData.getId()).put(newData.getSubId(), newData);
        }
    }
    
    public static HashMap<String, FireEffectSubTypeBuilderBase> getTypeBuilders()
    {
        return typeBuilders;
    }
    
    public static HashMap<String, HashMap<String, FireEffectSubTypeBase>> getEffectsMap()
    {
        return effectsMap;
    }
    
    public static void setEffectsMap(HashMap<String, HashMap<String, FireEffectSubTypeBase>> newMap)
    {
        effectsMap.clear();
        for(Map.Entry<String, HashMap<String, FireEffectSubTypeBase>> one : newMap.entrySet())
        {
            effectsMap.put(one.getKey(), one.getValue());
        }
    }
}
