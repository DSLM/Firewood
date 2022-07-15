package com.dslm.firewood.mobeffect;


import com.dslm.firewood.Register;
import com.dslm.firewood.capprovider.PlayerSpiritualDataProvider;
import com.dslm.firewood.config.MobEffectConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

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
            livingEntity.getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(playerSpiritualDamage -> {
                livingEntity.hurt(Register.SPIRITUAL_FIRE_DAMAGE, playerSpiritualDamage.getSpiritDamage());
            });
        }
    }
    
    @Override
    public List<ItemStack> getCurativeItems()
    {
        return Collections.emptyList();
    }
}