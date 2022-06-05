package com.dslm.firewood.compat;

import com.dslm.firewood.compat.top.TOPCompat;
import com.dslm.firewood.util.StaticValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import static com.dslm.firewood.util.StaticValue.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StaticValue.MOD_ID)
public final class CompatRegistry
{
    
    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event)
    {
        event.enqueueWork(() -> InterModComms.sendTo(TOP_MOD, "getTheOneProbe", TOPCompat.GetTheOneProbe::new));
        event.enqueueWork(() -> checkMod(PATCHOULI_MOD));
    }
}
