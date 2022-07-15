package com.dslm.firewood.fireeffecthelper.flesh.major;

import com.dslm.firewood.capprovider.PlayerSpiritualDataProvider;
import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.MobEffectSubType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class MobEffectFireEffectHelper extends SubMajorFireEffectHelperBase
{
    public MobEffectFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            setType(id);
            setSubType("");
            setProcess(0);
        }}, id, TargetType.LIVING_ENTITY);
    }
    
    @Override
    public void transmuteEntity(FireEffectNBTDataInterface data, Level level, LivingEntity livingEntity, LivingEntity source)
    {
        FireEffectSubTypeBase effectData = getSubRealEffect(data);
        if(!(effectData instanceof MobEffectSubType mobEffectSubType))
        {
            return;
        }
        List<MobEffectInstance> effects = mobEffectSubType.getEffects();
        List<MobEffectInstance> effectsToEnemy = mobEffectSubType.getEffectsToEnemy();
        for(MobEffectInstance effect : effects)
        {
            CompoundTag tag = new CompoundTag();
            effect.save(tag);
            effect = MobEffectInstance.load(tag);
            
            if(effect.getEffect().isInstantenous())
            {
                effect.getEffect().applyInstantenousEffect(source, source, livingEntity, effect.getAmplifier(), 1);
            }
            else
            {
                livingEntity.addEffect(effect);
            }
        }
        for(MobEffectInstance effect : effectsToEnemy)
        {
            livingEntity.getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                    playerSpiritualData -> playerSpiritualData.getFleshToEnemyEffects().add(Pair.of(1.0, effect)));
        }
    }
}
