package com.dslm.firewood.event;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.NBTUtils;
import com.dslm.firewood.Register;
import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
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
            ItemBlockRenderTypes.setRenderLayer(Register.SPIRITUAL_FIRE_BLOCK.get(), RenderType.cutoutMipped());
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