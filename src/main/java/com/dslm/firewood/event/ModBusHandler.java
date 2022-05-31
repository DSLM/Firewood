package com.dslm.firewood.event;

import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
import com.dslm.firewood.util.StaticValue;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StaticValue.MOD_ID)
public class ModBusHandler
{
    
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(PlayerSpiritualDamageProvider.PlayerSpiritualDamage.class);
    }
}
