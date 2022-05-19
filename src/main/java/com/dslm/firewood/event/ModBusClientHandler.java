package com.dslm.firewood.event;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.NBTUtils;
import com.dslm.firewood.Register;
import com.dslm.firewood.blockEntity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.screen.SpiritualCampfireBlockScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Firewood.MOD_ID)
public class ModBusClientHandler
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e)
    {
        e.enqueueWork(() -> {
            MenuScreens.register(Register.SPIRITUAL_CAMPFIRE_BLOCK_CONTAINER.get(), SpiritualCampfireBlockScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Register.SPIRITUAL_FIRE_BLOCK.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(Register.SPIRITUAL_CAMPFIRE_BLOCK.get(), RenderType.cutoutMipped());
        });
    }
    
    @SubscribeEvent
    public static void registerBlockColorHandler(ColorHandlerEvent.Block e)
    {
    
        e.getBlockColors().register(
                (state, world, pos, tintIndex) ->
                        tintIndex == 1 ?
                                ((SpiritualFireBlockEntity) world.getBlockEntity(pos)).getColor() : -1,
                Register.SPIRITUAL_FIRE_BLOCK.get());
    
        e.getBlockColors().register(
                (state, world, pos, tintIndex) ->
                        tintIndex == 1 ?
                                ((SpiritualCampfireBlockEntity) world.getBlockEntity(pos)).getItemStackInSlot(0).getItem() == Register.TINDER_ITEM.get() ?
                                        FireEffectHelpers.getMixedColor(
                                                NBTUtils.loadMajorFireData(((SpiritualCampfireBlockEntity) world.getBlockEntity(pos)).getItemStackInSlot(0).getOrCreateTag()),
                                                NBTUtils.loadMinorFireData(((SpiritualCampfireBlockEntity) world.getBlockEntity(pos)).getItemStackInSlot(0).getOrCreateTag()))
                                        : -1 : -1,
                Register.SPIRITUAL_CAMPFIRE_BLOCK.get());
    }
    
    @SubscribeEvent
    public static void registerItemColorHandler(ColorHandlerEvent.Item e)
    {
        e.getItemColors().register(
                (stack, index) ->
                        index == 0 ?
                                -1 : FireEffectHelpers.getMixedColor(
                                NBTUtils.loadMajorFireData(stack.getOrCreateTag()),
                                NBTUtils.loadMinorFireData(stack.getOrCreateTag())),
                Register.TINDER_ITEM.get());
    }
}