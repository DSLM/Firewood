package com.dslm.firewood.event;

import com.dslm.firewood.Register;
import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
import com.dslm.firewood.network.FireEffectSubTypeMessage;
import com.dslm.firewood.network.NetworkHandler;
import com.dslm.firewood.subType.FireEffectSubTypeManager;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
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
            if(!event.getObject().getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).isPresent())
            {
                event.addCapability(new ResourceLocation(StaticValue.MOD_ID, "player_spiritual_damage"), new PlayerSpiritualDamageProvider());
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event)
    {
        if(event.isWasDeath())
        {
            event.getOriginal().getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).ifPresent(
                    oldStore -> event.getPlayer().getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).ifPresent(
                            newStore -> newStore.copyFrom(oldStore)));
        }
    }
    
    @SubscribeEvent
    public static void onPotionRemove(PotionEvent.PotionRemoveEvent event)
    {
        if(event.getPotion() == Register.FIRED_FLESH.get())
        {
            event.getEntityLiving().getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).ifPresent(
                    PlayerSpiritualDamageProvider.PlayerSpiritualDamage::cleanFlesh);
        }
        if(event.getPotion() == Register.FIRED_SPIRIT.get())
        {
            event.getEntityLiving().getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).ifPresent(
                    PlayerSpiritualDamageProvider.PlayerSpiritualDamage::cleanSpirit);
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
    public static void onPlaySoundEvent(PlaySoundEvent event)
    {
        System.out.println("==================");
        System.out.println(event.getSound().getSource().getName());
    }
}
