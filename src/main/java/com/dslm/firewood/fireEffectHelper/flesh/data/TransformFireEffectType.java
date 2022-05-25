package com.dslm.firewood.fireEffectHelper.flesh.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import static com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperBase.TYPE;

public class TransformFireEffectType
{
    public String namespace;
    public String path;
    public String id;
    public String subId;
    public int color;
    public float damage;
    public float minHealth;
    public int process;
    public float chance;
    public int range;
    
    public static String ID = TYPE;
    public static String SUB_ID = "sub_name";
    public static String COLOR = "color";
    public static String DAMAGE = "damage";
    public static String MIN_HEALTH = "minHealth";
    public static String PROCESS = "process";
    public static String CHANCE = "chance";
    public static String RANGE = "range";
    
    public TransformFireEffectType(String namespace, String path, String id, String subId, int color, float damage, float minHealth, int process, float chance, int range)
    {
        this.namespace = namespace;
        this.path = path;
        this.id = id;
        this.subId = subId;
        this.color = color;
        this.damage = damage;
        this.minHealth = minHealth;
        this.process = process;
        this.chance = chance;
        this.range = range;
    }
    
    public TransformFireEffectType(ResourceLocation resourceLocation, String id, String subId, int color, float damage, float minHealth, int process, float chance, int range)
    {
        this(resourceLocation.getNamespace(), resourceLocation.getPath(), id, subId, color, damage, minHealth, process, chance, range);
    }
    
    public TransformFireEffectType(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        this(resourceLocation,
                jsonObject.get(ID).getAsString(),
                jsonObject.get(SUB_ID).getAsString(),
                jsonObject.get(COLOR).getAsInt(),
                jsonObject.get(DAMAGE).getAsFloat(),
                jsonObject.get(MIN_HEALTH).getAsFloat(),
                jsonObject.get(PROCESS).getAsInt(),
                jsonObject.get(CHANCE).getAsFloat(),
                jsonObject.get(RANGE).getAsInt());
    }
    
    public String getNamespace()
    {
        return namespace;
    }
    
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public void setPath(String path)
    {
        this.path = path;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getSubId()
    {
        return subId;
    }
    
    public void setSubId(String subId)
    {
        this.subId = subId;
    }
    
    public int getColor()
    {
        return color;
    }
    
    public void setColor(int color)
    {
        this.color = color;
    }
    
    public float getDamage()
    {
        return damage;
    }
    
    public void setDamage(float damage)
    {
        this.damage = damage;
    }
    
    public float getMinHealth()
    {
        return minHealth;
    }
    
    public void setMinHealth(float minHealth)
    {
        this.minHealth = minHealth;
    }
    
    public int getProcess()
    {
        return process;
    }
    
    public void setProcess(int process)
    {
        this.process = process;
    }
    
    public float getChance()
    {
        return chance;
    }
    
    public void setChance(float chance)
    {
        this.chance = chance;
    }
    
    public int getRange()
    {
        return range;
    }
    
    public void setRange(int range)
    {
        this.range = range;
    }
}
