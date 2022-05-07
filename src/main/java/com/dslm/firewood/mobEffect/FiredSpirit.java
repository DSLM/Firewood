//package com.dslm.firewood.mobEffect;
//
//
//import com.dslm.firewood.Register;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.effect.MobEffectCategory;
//import net.minecraft.world.entity.LivingEntity;
//
//import static com.dslm.firewood.config.MobEffectConfig.*;
//
//public class FiredSpirit extends MobEffect
//{
//    public FiredSpirit(MobEffectCategory category, int color)
//    {
//        super(category, color);
//    }
//
//    public boolean isDurationEffectTick(int pDuration, int pAmplifier)
//    {
//        return pDuration % FIRED_SPIRIT_INTERVAL.get() == 0;
//    }
//
//    @Override
//    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
//    {
//        pLivingEntity.hurt(Register.SPIRITUAL_FIRE_DAMAGE, FIRED_SPIRIT_DAMAGE.get().floatValue() * (pAmplifier + 1));
//    }
//}