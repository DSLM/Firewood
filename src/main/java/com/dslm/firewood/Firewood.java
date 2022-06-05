package com.dslm.firewood;

import com.dslm.firewood.config.Config;
import com.dslm.firewood.util.StaticValue;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
    
        bus.register(Register.class);
        Register.register(bus);
        Config.register();
    }
}
