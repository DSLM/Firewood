package com.dslm.firewood.event;

import com.dslm.firewood.Register;
import com.dslm.firewood.capprovider.PlayerSpiritualDataProvider;
import com.dslm.firewood.command.FirewoodCommand;
import com.dslm.firewood.network.FireEffectSubTypeMessage;
import com.dslm.firewood.network.NetworkHandler;
import com.dslm.firewood.subtype.FireEffectSubTypeManager;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = StaticValue.MOD_ID)
public class ForgeBusHandler
{
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof LivingEntity)
        {
            if(!event.getObject().getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).isPresent())
            {
                event.addCapability(new ResourceLocation(StaticValue.MOD_ID, "player_spiritual_damage"), new PlayerSpiritualDataProvider());
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event)
    {
        if(event.isWasDeath())
        {
            event.getOriginal().getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                    oldStore -> event.getPlayer().getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                            newStore -> newStore.copyFrom(oldStore)));
        }
    }
    
    @SubscribeEvent
    public static void onPotionRemove(PotionEvent.PotionRemoveEvent event)
    {
        if(event.getPotion() == Register.FIRED_FLESH.get())
        {
            event.getEntityLiving().getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                    PlayerSpiritualDataProvider.PlayerSpiritualData::cleanFlesh);
        }
        if(event.getPotion() == Register.FIRED_SPIRIT.get())
        {
            event.getEntityLiving().getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                    PlayerSpiritualDataProvider.PlayerSpiritualData::cleanSpirit);
        }
    }
    
    @SubscribeEvent
    public static void onPotionExpiry(PotionEvent.PotionExpiryEvent event)
    {
        if(event.getPotionEffect() == null)
        {
            return;
        }
        if(event.getPotionEffect().getEffect() == Register.FIRED_FLESH.get())
        {
            event.getEntityLiving().getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                    PlayerSpiritualDataProvider.PlayerSpiritualData::cleanFlesh);
        }
        if(event.getPotionEffect().getEffect() == Register.FIRED_SPIRIT.get())
        {
            event.getEntityLiving().getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                    PlayerSpiritualDataProvider.PlayerSpiritualData::cleanSpirit);
        }
    }
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        if(event.getSource().equals(Register.FLESHY_FIRE_DAMAGE)
                || event.getSource().equals(Register.SPIRITUAL_FIRE_DAMAGE)
                || event.getEntityLiving().equals(event.getSource().getEntity())
                || event.getEntityLiving().equals(event.getSource().getDirectEntity())
                || !(event.getSource().getEntity() instanceof LivingEntity livingEntity))
        {
            return;
        }
        
        if(livingEntity.hasEffect(Register.FIRED_FLESH.get()))
        {
            livingEntity.getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                    playerSpiritualData -> playerSpiritualData.getFleshToEnemyEffects().forEach(mobEffectInstancePair -> {
                        MobEffectInstance mobEffectInstance = mobEffectInstancePair.getSecond();
                        if(mobEffectInstance.getEffect().isInstantenous())
                        {
                            mobEffectInstance.getEffect().applyInstantenousEffect(
                                    event.getEntityLiving(),
                                    livingEntity,
                                    event.getEntityLiving(),
                                    mobEffectInstance.getAmplifier(),
                                    mobEffectInstancePair.getFirst());
                        }
                        else
                        {
                            event.getEntityLiving().addEffect(mobEffectInstance);
                        }
                    })
            );
        }
    }
    
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event)
    {
        event.addListener(new FireEffectSubTypeManager());
    }
    
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event)
    {
        if(event.getPlayer() == null)
        {
            NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new FireEffectSubTypeMessage(FireEffectSubTypeManager.getEffectsMap()));
            return;
        }
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(event::getPlayer), new FireEffectSubTypeMessage(FireEffectSubTypeManager.getEffectsMap()));
    }
    
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        FirewoodCommand.register(event.getDispatcher());
    }
}
