package com.dslm.firewood.capProvider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;


public class PlayerSpiritualDamageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    
    public static Capability<PlayerSpiritualDamage> PLAYER_SPIRITUAL_DAMAGE = CapabilityManager.get(new CapabilityToken<>()
    {
    });
    
    private PlayerSpiritualDamage playerSpiritualDamage = null;
    private final LazyOptional<PlayerSpiritualDamage> opt = LazyOptional.of(this::createPlayerSpiritualDamage);
    
    
    private PlayerSpiritualDamage createPlayerSpiritualDamage()
    {
        if(playerSpiritualDamage == null)
        {
            playerSpiritualDamage = new PlayerSpiritualDamage();
        }
        return playerSpiritualDamage;
    }
    
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap)
    {
        if(cap == PLAYER_SPIRITUAL_DAMAGE)
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
        private int fleshColor;
        private float spiritDamage;
        private int spiritColor;
    
        public PlayerSpiritualDamage()
        {
            cleanFlesh();
            cleanSpirit();
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
    
        public void cleanFlesh()
        {
            fleshDamage = 0;
            fleshColor = 0;
        }
    
        public void cleanSpirit()
        {
            spiritDamage = 0;
            spiritColor = 0;
        }
    
        public void copyFrom(PlayerSpiritualDamage source)
        {
            fleshDamage = source.fleshDamage;
            spiritDamage = source.spiritDamage;
            fleshColor = source.fleshColor;
            spiritColor = source.spiritColor;
        }
    
    
        public void saveNBTData(CompoundTag compound)
        {
            compound.putFloat("fleshDamage", fleshDamage);
            compound.putFloat("spiritDamage", spiritDamage);
            compound.putInt("fleshColor", fleshColor);
            compound.putInt("spiritColor", spiritColor);
        }
    
        public void loadNBTData(CompoundTag compound)
        {
            fleshDamage = compound.getFloat("fleshDamage");
            spiritDamage = compound.getFloat("spiritDamage");
            fleshColor = compound.getInt("fleshColor");
            spiritColor = compound.getInt("spiritColor");
        }
    
        @Override
        public String toString()
        {
            StringBuilder s = new StringBuilder();
            return s.append("fleshDamage: ").append(fleshDamage).append("\n")
                    .append("spiritDamage: ").append(spiritDamage).append("\n")
                    .append("fleshColor: ").append(fleshColor).append("\n")
                    .append("spiritColor: ").append(spiritColor).append("\n")
                    .toString();
        }
    }
}
