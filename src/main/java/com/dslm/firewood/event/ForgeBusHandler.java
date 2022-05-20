package com.dslm.firewood.event;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Firewood.MOD_ID)
public class ForgeBusHandler
{
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof Player)
        {
            if(!event.getObject().getCapability(PlayerSpiritualDamageProvider.PLAYER_SPIRITUAL_DAMAGE).isPresent())
            {
                event.addCapability(new ResourceLocation(Firewood.MOD_ID, "player_spiritual_damage"), new PlayerSpiritualDamageProvider());
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
}
