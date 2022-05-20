package com.dslm.firewood.mobEffect;


import com.dslm.firewood.config.MobEffectConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FiredSpirit extends MobEffect
{
    public FiredSpirit(MobEffectCategory category, int color)
    {
        super(category, color);
    }
    
    public boolean isDurationEffectTick(int pDuration, int pAmplifier)
    {
        if(MobEffectConfig.FIRED_SPIRIT_INTERVAL == null) return false;
        return pDuration % MobEffectConfig.FIRED_SPIRIT_INTERVAL.get() == 0;
    }
    
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
    {
        //pLivingEntity.hurt(Register.SPIRITUAL_FIRE_DAMAGE, FIRED_SPIRIT_DAMAGE.get().floatValue() * (pAmplifier + 1));
    }
}