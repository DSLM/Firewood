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
//public class FiredFlesh extends MobEffect
//{
//    public FiredFlesh(MobEffectCategory category, int color)
//    {
//        super(category, color);
//    }
//
//    public boolean isDurationEffectTick(int pDuration, int pAmplifier)
//    {
//        return pDuration % FIRED_FLESH_INTERVAL.get() == 0;
//    }
//
//    @Override
//    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
//    {
//        pLivingEntity.hurt(Register.FLESHY_FIRE_DAMAGE, FIRED_FLESH_DAMAGE.get().floatValue() * (pAmplifier + 1));
//    }
//}