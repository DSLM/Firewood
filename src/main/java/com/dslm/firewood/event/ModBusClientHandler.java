package com.dslm.firewood.event;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.LanternBlockEntity;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.block.entity.SpiritualFireBlockEntity;
import com.dslm.firewood.compat.curios.Curios;
import com.dslm.firewood.compat.shimmer.ShimmerHelper;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper;
import com.dslm.firewood.render.LanternModelLoader;
import com.dslm.firewood.render.RemnantSoulRender;
import com.dslm.firewood.render.SpiritualCampfireRenderer;
import com.dslm.firewood.screen.SpiritualCampfireBlockScreen;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = StaticValue.MOD_ID)
public class ModBusClientHandler
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        MinecraftForgeClient.registerTooltipComponentFactory(ForgeBusClientHandler.IconComponent.class, Function.identity());
        event.enqueueWork(() -> {
            MenuScreens.register(Register.SPIRITUAL_CAMPFIRE_BLOCK_CONTAINER.get(), SpiritualCampfireBlockScreen::new);
    
            ItemBlockRenderTypes.setRenderLayer(Register.SPIRITUAL_FIRE_BLOCK.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(Register.SPIRITUAL_CAMPFIRE_BLOCK.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(Register.LANTERN_BLOCK.get(), RenderType.translucent());
    
            ItemProperties.register(Register.LANTERN_ITEM.get(), new ResourceLocation("active_lantern"),
                    (stack, level, Entity, seed) -> stack.getOrCreateTag().getBoolean(StaticValue.ACTIVE_LANTERN) ? 1 : 0);
    
            if(StaticValue.checkMod(StaticValue.SHIMMER))
            {
                ShimmerHelper.registerItemLight();
            }
        });
    
        if(StaticValue.checkMod(StaticValue.CURIOS_MOD))
        {
            Curios.registerRender();
        }
    }
    
    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event)
    {
        ModelLoaderRegistry.registerLoader(LanternModelLoader.LANTERN_LOADER, new LanternModelLoader());
    }
    
    @SubscribeEvent
    public static void registerBlockColorHandler(ColorHandlerEvent.Block event)
    {
        
        event.getBlockColors().register(
                (state, world, pos, tintIndex) -> {
                    if(world == null
                            || pos == null
                            || world.getBlockEntity(pos) == null
                            || !(world.getBlockEntity(pos) instanceof SpiritualFireBlockEntity spiritualFireBlockEntity))
                        return -1;
                    return tintIndex == 1 ?
                            spiritualFireBlockEntity.getColor() : -1;
                },
                Register.SPIRITUAL_FIRE_BLOCK.get());
        
        event.getBlockColors().register(
                (state, world, pos, tintIndex) -> {
                    if(world == null
                            || pos == null
                            || world.getBlockEntity(pos) == null
                            || !(world.getBlockEntity(pos) instanceof SpiritualCampfireBlockEntity spiritualCampfireBlockEntity))
                        return -1;
                    return tintIndex == 1 ?
                            spiritualCampfireBlockEntity.getColor()
                            : -1;
                },
                Register.SPIRITUAL_CAMPFIRE_BLOCK.get());
        
        event.getBlockColors().register(
                (state, world, pos, tintIndex) -> {
                    if(world == null
                            || pos == null
                            || world.getBlockEntity(pos) == null
                            || !(world.getBlockEntity(pos) instanceof LanternBlockEntity lanternBlockEntity))
                        return -1;
                    return lanternBlockEntity.getColor();
                },
                Register.LANTERN_BLOCK.get());
    }
    
    @SubscribeEvent
    public static void registerItemColorHandler(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register(
                (stack, index) ->
                        index == 0 ?
                                -1 : FireEffectHelpers.getMixedColor(
                                FireEffectNBTStaticHelper.loadMajorFireData(stack.getOrCreateTag()),
                                FireEffectNBTStaticHelper.loadMinorFireData(stack.getOrCreateTag())),
                Register.TINDER_ITEM.get());
        event.getItemColors().register(
                (stack, index) ->
                        index != 1 ?
                                -1 : FireEffectHelpers.getMixedColor(
                                FireEffectNBTStaticHelper.loadMajorFireData(stack.getOrCreateTag()),
                                FireEffectNBTStaticHelper.loadMinorFireData(stack.getOrCreateTag())),
                Register.LANTERN_ITEM.get());
    }
    
    @SubscribeEvent
    public static void RenderRegister(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(Register.SPIRITUAL_CAMPFIRE_BLOCK_ENTITY.get(), SpiritualCampfireRenderer::new);
        event.registerEntityRenderer(Register.REMNANT_SOUL_ENTITY.get(), RemnantSoulRender::new);
    }
}