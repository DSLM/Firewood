package com.dslm.firewood.subtype;

import com.dslm.firewood.compat.jei.subtype.info.FireEffectSubTypeJEIHandler;
import com.dslm.firewood.compat.jei.subtype.info.MobEffectSubTypeJEIHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;
import java.util.List;

public class MobEffectSubType extends FireEffectSubType
{
    List<MobEffectInstance> effects;
    List<MobEffectInstance> effectsToEnemy;
    
    public MobEffectSubType()
    {
    
    }
    
    public MobEffectSubType(FireEffectSubType fireEffectSubType,
                            List<MobEffectInstance> effects,
                            List<MobEffectInstance> effectsToEnemy)
    {
        super(fireEffectSubType);
        this.effects = effects;
        this.effectsToEnemy = effectsToEnemy;
    }
    
    public MobEffectSubType(FireEffectSubType copy,
                            JsonElement effectsJsonElement,
                            JsonElement effectsToEnemyJsonElement)
    {
        super(copy);
        effects = new ArrayList<>();
        if(effectsJsonElement instanceof JsonArray effectsJsonArray)
        {
            effectsJsonArray.forEach(jsonElement -> effects.add(MobEffectInstance.load(CraftingHelper.getNBT(jsonElement))));
        }
        effectsToEnemy = new ArrayList<>();
        if(effectsToEnemyJsonElement instanceof JsonArray effectsToEnemyJsonArray)
        {
            effectsToEnemyJsonArray.forEach(jsonElement -> effectsToEnemy.add(MobEffectInstance.load(CraftingHelper.getNBT(jsonElement))));
        }
    }
    
    public MobEffectSubType(FriendlyByteBuf buf)
    {
        this();
        copyFrom((MobEffectSubType) fromNetwork(buf));
    }
    
    public void copyFrom(MobEffectSubType copy)
    {
        super.copyFrom(copy);
        this.effects = copy.effects;
        this.effectsToEnemy = copy.effectsToEnemy;
    }
    
    public List<MobEffectInstance> getEffects()
    {
        return effects;
    }
    
    public void setEffects(List<MobEffectInstance> effects)
    {
        this.effects = effects;
    }
    
    public List<MobEffectInstance> getEffectsToEnemy()
    {
        return effectsToEnemy;
    }
    
    public void setEffectsToEnemy(List<MobEffectInstance> effectsToEnemy)
    {
        this.effectsToEnemy = effectsToEnemy;
    }
    
    @Override
    public FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf)
    {
        FireEffectSubType fireEffectSubType = (FireEffectSubType) super.fromNetwork(buf);
        
        ArrayList<MobEffectInstance> effects1 = new ArrayList<>();
        int size1 = buf.readInt();
        for(int j = 0; j < size1; j++)
        {
            effects1.add(MobEffectInstance.load(buf.readNbt()));
        }
        
        ArrayList<MobEffectInstance> effectsToEnemy1 = new ArrayList<>();
        int size2 = buf.readInt();
        for(int j = 0; j < size2; j++)
        {
            effectsToEnemy1.add(MobEffectInstance.load(buf.readNbt()));
        }
        
        return new MobEffectSubType(fireEffectSubType, effects1, effectsToEnemy1);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe)
    {
        super.toNetwork(buf, recipe);
        
        buf.writeInt(effects.size());
        
        for(MobEffectInstance mobEffectInstance : effects)
        {
            CompoundTag tag = new CompoundTag();
            mobEffectInstance.save(tag);
            buf.writeNbt(tag);
        }
        
        buf.writeInt(effectsToEnemy.size());
        
        for(MobEffectInstance mobEffectInstance : effectsToEnemy)
        {
            CompoundTag tag = new CompoundTag();
            mobEffectInstance.save(tag);
            buf.writeNbt(tag);
        }
    }
    
    @Override
    public FireEffectSubTypeJEIHandler getJEIHandler()
    {
        return MobEffectSubTypeJEIHandler.getInstance();
    }
}
