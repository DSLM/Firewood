package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.fireEffectHelper.block.baseClass.RangedFireEffectData;
import com.dslm.firewood.fireEffectHelper.block.baseClass.RangedFireEffectDataBuilder;
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

public class TransformEffectTypeManager extends SimpleJsonResourceReloadListener
{
    private static final Gson GSON = (new GsonBuilder()).create();
    
    public static final RangedFireEffectDataBuilder defaultDataBuilder = new RangedFireEffectDataBuilder();
    
    public static final HashMap<String, RangedFireEffectDataBuilder> dataBuilders = new HashMap<>()
    {{
        put("firewood:block_block", defaultDataBuilder);
        put("firewood:smelter", defaultDataBuilder);
    }};
    
    public static final HashMap<String, HashMap<String, RangedFireEffectData>> effectsMap = new HashMap<>();
    
    public TransformEffectTypeManager()
    {
        super(GSON, "firewood_fire_effects");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller rrofiler)
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
        
        for(Map.Entry<ResourceLocation, JsonObject> entry : effects.entrySet())
        {
            RangedFireEffectDataBuilder builder = dataBuilders.getOrDefault(entry.getValue().get("type").getAsString(), defaultDataBuilder);
            RangedFireEffectData newData = builder.getNewData(entry.getKey(), entry.getValue());
            
            if(!effectsMap.containsKey(newData.getId()))
            {
                effectsMap.put(newData.getId(), new HashMap<>());
            }
            effectsMap.get(newData.getId()).put(newData.getSubId(), newData);
        }
    }
    
    public static HashMap<String, RangedFireEffectDataBuilder> getDataBuilders()
    {
        return dataBuilders;
    }
    
    public static HashMap<String, HashMap<String, RangedFireEffectData>> getEffectsMap()
    {
        return effectsMap;
    }
}
