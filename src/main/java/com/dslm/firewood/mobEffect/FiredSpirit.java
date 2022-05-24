package com.dslm.firewood.mobEffect;


import com.dslm.firewood.Register;
import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
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
        return pDuration % MobEffectConfig.FIRED_SPIRIT_INTERVAL.get() == 0;
    }
    
    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier)
    {
        if(amplifier == 99)
        {
            livingEntity.getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).ifPresent(playerSpiritualDamage -> {
                livingEntity.hurt(Register.SPIRITUAL_FIRE_DAMAGE, playerSpiritualDamage.getSpiritDamage());
            });
        }
    }
}