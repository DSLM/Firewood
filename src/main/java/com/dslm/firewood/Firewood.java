package com.dslm.firewood;

import com.dslm.firewood.compat.top.TOPCompat;
import com.dslm.firewood.config.Config;
import com.dslm.firewood.util.StaticValue;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(StaticValue.MOD_ID)
public class Firewood
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public Firewood()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    
        bus.addListener(this::setup);
        bus.register(Register.class);
        Register.register(bus);
        Config.register();
    
        if(ModList.get().isLoaded("theoneprobe")) TOPCompat.register();
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
}
