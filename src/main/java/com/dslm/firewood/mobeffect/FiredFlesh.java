package com.dslm.firewood.mobeffect;


import com.dslm.firewood.Register;
import com.dslm.firewood.capprovider.PlayerSpiritualDataProvider;
import com.dslm.firewood.config.MobEffectConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FiredFlesh extends MobEffect
{
    public FiredFlesh(MobEffectCategory category, int color)
    {
        super(category, color);
    }
    
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return duration % MobEffectConfig.FIRED_FLESH_INTERVAL.get() == 0;
    }
    
    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier)
    {
        if(amplifier == 99)
        {
            livingEntity.getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(playerSpiritualDamage -> {
                livingEntity.hurt(Register.FLESHY_FIRE_DAMAGE, playerSpiritualDamage.getFleshDamage());
            });
        }
    }
}