package com.dslm.firewood.subtype;

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
    public static final TeleportSubTypeBuilder TELEPORT_SUB_TYPE_BUILDER = new TeleportSubTypeBuilder();
    public static final PotionSubTypeBuilder POTION_SUB_TYPE_BUILDER = new PotionSubTypeBuilder();
    
    public static final HashMap<String, FireEffectSubTypeBuilderBase> TYPE_BUILDERS = new HashMap<>()
    {{
        put("potion", POTION_SUB_TYPE_BUILDER);
    
        put("teleport", TELEPORT_SUB_TYPE_BUILDER);
    
        put("smelter", DEFAULT_TYPE_BUILDER);
    
        put("block_to_block", DEFAULT_TYPE_BUILDER);
    
        put("set_block_name", SET_BLOCK_NAME_SUB_TYPE_BUILDER);
    }};
    
    public static final HashMap<String, HashMap<String, FireEffectSubTypeBase>> EFFECTS_MAP = new HashMap<>();
    
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
    
        EFFECTS_MAP.clear();
    
        for(String key : TYPE_BUILDERS.keySet())
        {
            EFFECTS_MAP.put(key, new HashMap<>());
        }
    
        for(Map.Entry<ResourceLocation, JsonObject> entry : effects.entrySet())
        {
            FireEffectSubTypeBuilderBase builder = TYPE_BUILDERS.getOrDefault(entry.getValue().get(TYPE).getAsString(), DEFAULT_TYPE_BUILDER);
            FireEffectSubTypeBase newData = builder.getNewData(entry.getKey(), entry.getValue());
    
            if(newData == null)
            {
                continue;
            }
    
            if(!EFFECTS_MAP.containsKey(newData.getId()))
            {
                EFFECTS_MAP.put(newData.getId(), new HashMap<>());
            }
            EFFECTS_MAP.get(newData.getId()).put(newData.getSubType(), newData);
        }
    }
    
    public static HashMap<String, FireEffectSubTypeBuilderBase> getTypeBuilders()
    {
        return TYPE_BUILDERS;
    }
    
    public static HashMap<String, HashMap<String, FireEffectSubTypeBase>> getEffectsMap()
    {
        return EFFECTS_MAP;
    }
    
    public static void setEffectsMap(HashMap<String, HashMap<String, FireEffectSubTypeBase>> newMap)
    {
        EFFECTS_MAP.clear();
        for(Map.Entry<String, HashMap<String, FireEffectSubTypeBase>> one : newMap.entrySet())
        {
            EFFECTS_MAP.put(one.getKey(), one.getValue());
        }
    }
}
