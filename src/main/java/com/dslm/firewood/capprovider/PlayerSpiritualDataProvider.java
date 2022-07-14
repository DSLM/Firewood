package com.dslm.firewood.capprovider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;


public class PlayerSpiritualDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    
    public static Capability<PlayerSpiritualData> PLAYER_SPIRITUAL_DATA = CapabilityManager.get(new CapabilityToken<>()
    {
    });
    
    private PlayerSpiritualData playerSpiritualData = null;
    private final LazyOptional<PlayerSpiritualData> opt = LazyOptional.of(this::createPlayerSpiritualData);
    
    
    private PlayerSpiritualData createPlayerSpiritualData()
    {
        if(playerSpiritualData == null)
        {
            playerSpiritualData = new PlayerSpiritualData();
        }
        return playerSpiritualData;
    }
    
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap)
    {
        if(cap == PLAYER_SPIRITUAL_DATA)
        {
            return opt.cast();
        }
        return LazyOptional.empty();
    }
    
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        return getCapability(cap);
    }
    
    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        createPlayerSpiritualData().saveNBTData(nbt);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        createPlayerSpiritualData().loadNBTData(nbt);
    }
    
    public static class PlayerSpiritualData
    {
        private float fleshDamage;
        private int fleshColor;
        private float spiritDamage;
        private int spiritColor;
        
        private ArrayList<Pair<Double, MobEffectInstance>> fleshToEnemyEffects;
        
        public PlayerSpiritualData()
        {
            cleanAll();
        }
        
        public float getFleshDamage()
        {
            return fleshDamage;
        }
        
        public void setFleshDamage(float fleshDamage)
        {
            this.fleshDamage = fleshDamage;
        }
        
        public void addFleshDamage(float fleshDamage)
        {
            this.fleshDamage += fleshDamage;
        }
        
        public float getSpiritDamage()
        {
            return spiritDamage;
        }
    
        public void setSpiritDamage(float spiritDamage)
        {
            this.spiritDamage = spiritDamage;
        }
    
        public void addSpiritDamage(float spiritDamage)
        {
            this.spiritDamage += spiritDamage;
        }
    
        public int getFleshColor()
        {
            return fleshColor;
        }
    
        public void setFleshColor(int fleshColor)
        {
            this.fleshColor = fleshColor;
        }
        
        public int getSpiritColor()
        {
            return spiritColor;
        }
        
        public void setSpiritColor(int spiritColor)
        {
            this.spiritColor = spiritColor;
        }
        
        public ArrayList<Pair<Double, MobEffectInstance>> getFleshToEnemyEffects()
        {
            return fleshToEnemyEffects;
        }
        
        public void setFleshToEnemyEffects(ArrayList<Pair<Double, MobEffectInstance>> fleshToEnemyEffects)
        {
            this.fleshToEnemyEffects = fleshToEnemyEffects;
        }
        
        public void cleanAll()
        {
            cleanFlesh();
            cleanSpirit();
        }
        
        public void cleanFlesh()
        {
            fleshDamage = 0;
            fleshColor = 0;
            fleshToEnemyEffects = new ArrayList<>();
        }
        
        public void cleanSpirit()
        {
            spiritDamage = 0;
            spiritColor = 0;
        }
        
        public void copyFrom(PlayerSpiritualData source)
        {
            fleshDamage = source.fleshDamage;
            spiritDamage = source.spiritDamage;
            fleshColor = source.fleshColor;
            spiritColor = source.spiritColor;
            
            fleshToEnemyEffects = source.fleshToEnemyEffects;
        }
    
    
        public void saveNBTData(CompoundTag compound)
        {
            compound.putFloat("fleshDamage", fleshDamage);
            compound.putFloat("spiritDamage", spiritDamage);
            compound.putInt("fleshColor", fleshColor);
            compound.putInt("spiritColor", spiritColor);
    
            ListTag fleshToEnemyEffectsListTag = new ListTag();
            fleshToEnemyEffects.stream().forEach(mobEffectInstance -> {
                CompoundTag mobTag = new CompoundTag();
                mobEffectInstance.getSecond().save(mobTag);
                if(mobEffectInstance.getSecond().getEffect().isInstantenous())
                {
                    mobTag.putDouble("firewood_multi", mobEffectInstance.getFirst());
                }
                fleshToEnemyEffectsListTag.add(mobTag);
            });
            compound.put("fleshToEnemyEffects", fleshToEnemyEffectsListTag);
        }
    
        public void loadNBTData(CompoundTag compound)
        {
            fleshDamage = compound.getFloat("fleshDamage");
            spiritDamage = compound.getFloat("spiritDamage");
            fleshColor = compound.getInt("fleshColor");
            spiritColor = compound.getInt("spiritColor");
    
            ListTag fleshToEnemyEffectsListTag = compound.getList("fleshToEnemyEffects", Tag.TAG_COMPOUND);
            fleshToEnemyEffectsListTag.forEach(tag -> {
                if(tag instanceof CompoundTag compoundTag)
                {
                    double multi = 1;
                    MobEffectInstance mobEffectInstance = MobEffectInstance.load(compoundTag);
                    if(compoundTag.contains("firewood_multi"))
                    {
                        multi = compoundTag.getDouble("firewood_multi");
                    }
                    fleshToEnemyEffects.add(Pair.of(multi, mobEffectInstance));
                }
            });
        }
    
        @Override
        public String toString()
        {
            StringBuilder s = new StringBuilder();
            return s.append("fleshDamage: ").append(fleshDamage).append("\n")
                    .append("spiritDamage: ").append(spiritDamage).append("\n")
                    .append("fleshColor: ").append(fleshColor).append("\n")
                    .append("spiritColor: ").append(spiritColor).append("\n")
                    .append("fleshToEnemyEffects: ").append(fleshToEnemyEffects).append("\n")
                    .toString();
        }
    }
}
