package com.dslm.firewood;

import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.ModList;
//import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Firewood.MOD_ID)
public class ClientHandler
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
        
        e.getBlockColors().register((state, world, pos, tintIndex) -> tintIndex == 1 ? ((SpiritualFireBlockEntity) world.getBlockEntity(pos)).getColor() : -1, Register.SPIRITUAL_FIRE_BLOCK.get());
    }
}