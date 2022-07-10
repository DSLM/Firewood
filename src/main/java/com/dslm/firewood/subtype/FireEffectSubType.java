package com.dslm.firewood.subtype;

import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FireEffectSubType implements FireEffectSubTypeBase
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
    public int[] range; // x, y, z
    public int targetLimit;
    public int cooldown;
    
    public FireEffectSubType()
    {
    
    }
    
    public FireEffectSubType(String namespace, String path, String id, String subId, int color, float damage, float minHealth, int process, float chance, int[] range, int targetLimit, int cooldown)
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
        this.targetLimit = targetLimit;
        this.cooldown = cooldown;
    }
    
    public FireEffectSubType(FireEffectSubType copy)
    {
        copyFrom(copy);
    }
    
    public void copyFrom(FireEffectSubType copy)
    {
        this.namespace = copy.namespace;
        this.path = copy.path;
        this.id = copy.id;
        this.subId = copy.subId;
        this.color = copy.color;
        this.damage = copy.damage;
        this.minHealth = copy.minHealth;
        this.process = copy.process;
        this.chance = copy.chance;
        this.range = copy.range;
        this.targetLimit = copy.targetLimit;
        this.cooldown = copy.cooldown;
    }
    
    public FireEffectSubType(ResourceLocation resourceLocation, String id, String subId, int color, float damage, float minHealth, int process, float chance, int[] range, int targetLimit, int cooldown)
    {
        this(resourceLocation.getNamespace(), resourceLocation.getPath(), id, subId, color, damage, minHealth, process, chance, range, targetLimit, cooldown);
    }
    
    public FireEffectSubType(ResourceLocation resourceLocation, JsonObject jsonObject, int[] range)
    {
        this(resourceLocation,
                jsonObject.get(StaticValue.TYPE).getAsString(),
                jsonObject.get(StaticValue.SUB_TYPE).getAsString(),
                StaticValue.colorInt(jsonObject.get(StaticValue.COLOR).getAsString()),
                jsonObject.get(StaticValue.DAMAGE).getAsFloat(),
                !jsonObject.has(StaticValue.MIN_HEALTH) ? jsonObject.get(StaticValue.DAMAGE).getAsFloat() : jsonObject.get(StaticValue.MIN_HEALTH).getAsFloat(),
                jsonObject.get(StaticValue.PROCESS).getAsInt(),
                !jsonObject.has(StaticValue.CHANCE) ? 100 : jsonObject.get(StaticValue.CHANCE).getAsFloat(),
                range,
                !jsonObject.has(StaticValue.TARGET_LIMIT) ? Integer.MAX_VALUE : jsonObject.get(StaticValue.TARGET_LIMIT).getAsInt(),
                jsonObject.get(StaticValue.COOLDOWN).getAsInt());
    }
    
    public FireEffectSubType(FriendlyByteBuf buf)
    {
        this();
        copyFrom((FireEffectSubType) fromNetwork(buf));
    }
    
    @Override
    public String getNamespace()
    {
        return namespace;
    }
    
    @Override
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }
    
    @Override
    public String getPath()
    {
        return path;
    }
    
    @Override
    public void setPath(String path)
    {
        this.path = path;
    }
    
    @Override
    public String getId()
    {
        return id;
    }
    
    @Override
    public void setId(String id)
    {
        this.id = id;
    }
    
    @Override
    public String getSubId()
    {
        return subId;
    }
    
    @Override
    public void setSubId(String subId)
    {
        this.subId = subId;
    }
    
    @Override
    public int getColor()
    {
        return color;
    }
    
    @Override
    public void setColor(int color)
    {
        this.color = color;
    }
    
    @Override
    public float getDamage()
    {
        return damage;
    }
    
    @Override
    public void setDamage(float damage)
    {
        this.damage = damage;
    }
    
    @Override
    public float getMinHealth()
    {
        return minHealth;
    }
    
    @Override
    public void setMinHealth(float minHealth)
    {
        this.minHealth = minHealth;
    }
    
    @Override
    public int getProcess()
    {
        return process;
    }
    
    @Override
    public void setProcess(int process)
    {
        this.process = process;
    }
    
    @Override
    public float getChance()
    {
        return chance;
    }
    
    @Override
    public void setChance(float chance)
    {
        this.chance = chance;
    }
    
    @Override
    public int[] getRange()
    {
        return range;
    }
    
    @Override
    public void setRange(int[] range)
    {
        this.range = range;
    }
    
    @Override
    public int getTargetLimit()
    {
        return targetLimit;
    }
    
    @Override
    public void setTargetLimit(int targetLimit)
    {
        this.targetLimit = targetLimit;
    }
    
    @Override
    public int getCooldown()
    {
        return cooldown;
    }
    
    @Override
    public void setCooldown(int cooldown)
    {
        this.cooldown = cooldown;
    }
    
    @Override
    public FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf)
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
        int[] range = {buf.readInt(), buf.readInt(), buf.readInt()};
        int targetLimit = buf.readInt();
        int cooldown = buf.readInt();
    
        return new FireEffectSubType(namespace, path, id, subId, color, damage, minHealth, process, chance, range, targetLimit, cooldown);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe)
    {
        if(recipe instanceof FireEffectSubType fireEffectSubType)
        {
            buf.writeUtf(fireEffectSubType.namespace);
            buf.writeUtf(fireEffectSubType.path);
            buf.writeUtf(fireEffectSubType.id);
            buf.writeUtf(fireEffectSubType.subId);
            buf.writeInt(fireEffectSubType.color);
            buf.writeFloat(fireEffectSubType.damage);
            buf.writeFloat(fireEffectSubType.minHealth);
            buf.writeInt(fireEffectSubType.process);
            buf.writeFloat(fireEffectSubType.chance);
            buf.writeInt(fireEffectSubType.range[0]);
            buf.writeInt(fireEffectSubType.range[1]);
            buf.writeInt(fireEffectSubType.range[2]);
            buf.writeInt(fireEffectSubType.targetLimit);
            buf.writeInt(fireEffectSubType.cooldown);
        }
    }
}
