package com.dslm.firewood.capProvider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerSpiritualDamageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    
    public static Capability<PlayerSpiritualDamage> PLAYER_SPIRITUAL_DAMAGE = CapabilityManager.get(new CapabilityToken<>()
    {
    });
    
    private PlayerSpiritualDamage playerSpiritualDamage = null;
    private final LazyOptional<PlayerSpiritualDamage> opt = LazyOptional.of(this::createPlayerSpiritualDamage);
    
    @Nonnull
    private PlayerSpiritualDamage createPlayerSpiritualDamage()
    {
        if(playerSpiritualDamage == null)
        {
            playerSpiritualDamage = new PlayerSpiritualDamage();
        }
        return playerSpiritualDamage;
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap)
    {
        if(cap == PLAYER_SPIRITUAL_DAMAGE)
        {
            return opt.cast();
        }
        return LazyOptional.empty();
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return getCapability(cap);
    }
    
    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        createPlayerSpiritualDamage().saveNBTData(nbt);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        createPlayerSpiritualDamage().loadNBTData(nbt);
    }
    
    public class PlayerSpiritualDamage
    {
        private float fleshDamage;
        private float spiritDamage;
        
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
        
        public void copyFrom(PlayerSpiritualDamage source)
        {
            fleshDamage = source.fleshDamage;
            spiritDamage = source.spiritDamage;
        }
        
        
        public void saveNBTData(CompoundTag compound)
        {
            compound.putFloat("fleshDamage", fleshDamage);
            compound.putFloat("spiritDamage", spiritDamage);
        }
        
        public void loadNBTData(CompoundTag compound)
        {
            fleshDamage = compound.getFloat("fleshDamage");
            spiritDamage = compound.getFloat("spiritDamage");
        }
    }
}
