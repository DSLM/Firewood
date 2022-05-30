package com.dslm.firewood.recipe;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectSubType;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectSubTypeBuilder;
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

import static com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperBase.TYPE;

public class FireEffectSubTypeManager extends SimpleJsonResourceReloadListener
{
    private static final Gson GSON = (new GsonBuilder()).create();
    
    public static final FireEffectSubTypeBuilder defaultTypeBuilder = new FireEffectSubTypeBuilder();
    
    public static final HashMap<String, FireEffectSubTypeBuilder> typeBuilders = new HashMap<>()
    {{
        put("firewood:smelter", defaultTypeBuilder);
        put("firewood:block_to_block", defaultTypeBuilder);
    }};
    
    public static final HashMap<String, HashMap<String, FireEffectSubType>> effectsMap = new HashMap<>();
    
    public FireEffectSubTypeManager()
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
            FireEffectSubTypeBuilder builder = typeBuilders.getOrDefault(entry.getValue().get(TYPE).getAsString(), defaultTypeBuilder);
            FireEffectSubType newData = builder.getNewData(entry.getKey(), entry.getValue());
    
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
    
    public static HashMap<String, FireEffectSubTypeBuilder> getTypeBuilders()
    {
        return typeBuilders;
    }
    
    public static HashMap<String, HashMap<String, FireEffectSubType>> getEffectsMap()
    {
        return effectsMap;
    }
}
