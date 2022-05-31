package com.dslm.firewood.event;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.item.TinderTypeItemBase;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Firewood.MOD_ID)
public class ForgeBusClientHandler
{
    @SubscribeEvent
    public static void onTooltipEvent(RenderTooltipEvent.GatherComponents event)
    {
        if(event.getItemStack().getItem() instanceof TinderTypeItemBase)
        {
            makeTinderTooltip(event);
        }
    }

//    @SubscribeEvent
//    public static void onRenderGameOverlay(RenderGameOverlayEvent e)
//    {
//     TODO: 2022/5/9 自行实现火焰遮挡视线，参考EntityRenderDispatcher.renderFlame

//        PoseStack pose = e.getMatrixStack();
//        TextureAtlasSprite textureatlassprite = ModelBakery.FIRE_0.sprite();
//        TextureAtlasSprite textureatlassprite1 = ModelBakery.FIRE_1.sprite();
//        pose.pushPose();
//        e.getWindow();
//        draw
//        pose.popPose();
//    }
    
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
    
    
            int len = tooltipX;
    
            GuiComponent.drawString(pose, Minecraft.getInstance().font, middle,
                    len, tooltipY, middle.getStyle().getColor().getValue());
            len += Minecraft.getInstance().font.width(middle.getVisualOrderText());
    
    
            //61, 0: heath icon position
            //4, 9: heath icon size
            RenderSystem.setShaderTexture(0, ForgeIngameGui.GUI_ICONS_LOCATION);
    
            GuiComponent.blit(pose, len, tooltipY, 61, 0, 4, 9, 256, 256);
            len += 4;
    
            GuiComponent.drawString(pose, Minecraft.getInstance().font, String.format(" x%.2f", middle.getDamage()),
                    len, tooltipY, middle.getStyle().getColor().getValue());
            len += Minecraft.getInstance().font.width(String.format(" x%.2f", middle.getDamage()));
    
    
            RenderSystem.setShaderTexture(0, ForgeIngameGui.GUI_ICONS_LOCATION);
    
            GuiComponent.blit(pose, len, tooltipY, 25, 0, 4, 9, 256, 256);
            len += 4;
    
            GuiComponent.drawString(pose, Minecraft.getInstance().font, String.format(" x%.2f", middle.getMinHealth()),
                    len, tooltipY, middle.getStyle().getColor().getValue());
            len += Minecraft.getInstance().font.width(String.format(" x%.2f", middle.getMinHealth()));
    
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
            return Minecraft.getInstance().font.width(middle.getVisualOrderText())
                    + Minecraft.getInstance().font.width(String.format(" x%.2f", middle.getDamage())) + 4
                    + Minecraft.getInstance().font.width(String.format(" x%.2f", middle.getMinHealth())) + 4;
        }
    }
}

