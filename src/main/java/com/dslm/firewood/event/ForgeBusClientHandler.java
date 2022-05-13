package com.dslm.firewood.event;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.NBTUtils;
import com.dslm.firewood.Register;
import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.item.TinderItem;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

import static com.dslm.firewood.Firewood.LOGGER;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Firewood.MOD_ID)
public class ForgeBusClientHandler
{
    @SubscribeEvent
    public static void onTooltipEvent(RenderTooltipEvent.GatherComponents event)
    {
        if(event.getItemStack().getItem() == Register.TINDER_ITEM.get())
        {
            makeTinderTooltip(event);
        }
    }
    
    public static void register()
    {
        MinecraftForgeClient.registerTooltipComponentFactory(IconComponent.class, Function.identity());
    }
    
    public static void makeTinderTooltip(RenderTooltipEvent.GatherComponents event)
    {
        ItemStack stack = event.getItemStack();
        List<Either<FormattedText, TooltipComponent>> components = event.getTooltipElements();
        int iniSize = components.size();
        for(int i = 0; i < iniSize; i++)
        {
            if(components.get(i).left().isPresent() &&
                    components.get(i).left().get() instanceof MiddleComponent middle &&
                    middle.getDamage() > 0 &&
                    Screen.hasShiftDown())
            {
                components.set(i, Either.right(new IconComponent(middle, stack, 10, 10)));
            }
        }
        //.add(Either.right(new ));
    }
    
    public record IconComponent(MiddleComponent middle, ItemStack stack, int width,
                                int height) implements ClientTooltipComponent, TooltipComponent
    {
        
        @Override
        public void renderImage(@Nonnull Font font, int tooltipX, int tooltipY, @Nonnull PoseStack pose, @Nonnull ItemRenderer itemRenderer, int something)
        {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            pose.pushPose();
            pose.translate(0, 0, 500);

            float strLen = Minecraft.getInstance().font.width(middle.getVisualOrderText());
            GuiComponent.drawString(pose, Minecraft.getInstance().font, middle,
                    tooltipX, tooltipY, middle.getStyle().getColor().getValue());
            
            //61, 0: heath icon position
            //9, 9: heath icon size
            RenderSystem.setShaderTexture(0, ForgeIngameGui.GUI_ICONS_LOCATION);
            GuiComponent.blit(pose, tooltipX + (int) strLen, tooltipY, 61, 0, 9, 9, 256, 256);

            GuiComponent.drawString(pose, Minecraft.getInstance().font, String.format(" x%.00f", middle.getDamage()),
                    tooltipX + (int) strLen + 5, tooltipY, middle.getStyle().getColor().getValue());
            pose.popPose();
        }
        
        @Override
        public int getHeight()
        {
            return height;
        }
        
        @Override
        public int getWidth(@Nonnull Font font)
        {
            return Minecraft.getInstance().font.width(middle.getVisualOrderText()) + 30;
        }
    }
}

