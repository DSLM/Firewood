package com.dslm.firewood;

import com.dslm.firewood.compat.TOPCompat;
import com.dslm.firewood.config.Config;
import com.dslm.firewood.render.SpiritualCampfireRenderer;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Firewood.MOD_ID)
public class Firewood
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "firewood";
    
    public Firewood()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    
        bus.addListener(this::setup);
        bus.register(Register.class);
        Register.register(bus);
        Config.register();
    
        if(ModList.get().isLoaded("theoneprobe")) TOPCompat.register();
    
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::RenderRegister);
    }
    
    public void RenderRegister(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(Register.SPIRITUAL_CAMPFIRE_BLOCK_ENTITY.get(), SpiritualCampfireRenderer::new);
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
