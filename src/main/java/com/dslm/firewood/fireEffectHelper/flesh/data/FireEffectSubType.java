package com.dslm.firewood.fireEffectHelper.flesh.data;

import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HexFormat;

public class FireEffectSubType
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
    
    public FireEffectSubType(String namespace, String path, String id, String subId, int color, float damage, float minHealth, int process, float chance, int range)
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
    
    public FireEffectSubType(ResourceLocation resourceLocation, String id, String subId, int color, float damage, float minHealth, int process, float chance, int range)
    {
        this(resourceLocation.getNamespace(), resourceLocation.getPath(), id, subId, color, damage, minHealth, process, chance, range);
    }
    
    public FireEffectSubType(ResourceLocation resourceLocation, JsonObject jsonObject)
    {
        this(resourceLocation,
                jsonObject.get(StaticValue.TYPE).getAsString(),
                jsonObject.get(StaticValue.SUB_TYPE).getAsString(),
                colorInt(jsonObject.get(StaticValue.COLOR).getAsString()),
                jsonObject.get(StaticValue.DAMAGE).getAsFloat(),
                jsonObject.get(StaticValue.MIN_HEALTH).getAsFloat(),
                jsonObject.get(StaticValue.PROCESS).getAsInt(),
                jsonObject.get(StaticValue.CHANCE).getAsFloat(),
                jsonObject.get(StaticValue.RANGE).getAsInt());
    }
    
    public static int colorInt(String s)
    {
        var s1 = s.toLowerCase().replace("0x", "");
        return HexFormat.fromHexDigits(s1);
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
    
    
    public static FireEffectSubType fromNetwork(FriendlyByteBuf buf)
    {
        String namespace = buf.readUtf();
        String path = buf.readUtf();
        String id = buf.readUtf();
        String subId = buf.readUtf();
        int color = buf.readInt();
        float damage = buf.readFloat();
        float minHealth = buf.readFloat();
        int process = buf.readInt();
        float chance = buf.readFloat();
        int range = buf.readInt();
        
        return new FireEffectSubType(namespace, path, id, subId, color, damage, minHealth, process, chance, range);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, FireEffectSubType recipe)
    {
        buf.writeUtf(recipe.namespace);
        buf.writeUtf(recipe.path);
        buf.writeUtf(recipe.id);
        buf.writeUtf(recipe.subId);
        buf.writeInt(recipe.color);
        buf.writeFloat(recipe.damage);
        buf.writeFloat(recipe.minHealth);
        buf.writeInt(recipe.process);
        buf.writeFloat(recipe.chance);
        buf.writeInt(recipe.range);
    }
}
