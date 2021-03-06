package com.dslm.firewood.event;

import com.dslm.firewood.item.TinderTypeItemBase;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.StaticValue;
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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.dslm.firewood.config.ColorConfig.HIGH_CONTRAST_MODE;


@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = StaticValue.MOD_ID)
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
//        TextureAtlasSprite textureAtlasSprite = ModelBakery.FIRE_0.sprite();
//        TextureAtlasSprite textureAtlasSprite1 = ModelBakery.FIRE_1.sprite();
//        pose.pushPose();
//        e.getWindow();
//        draw
//        pose.popPose();
//    }
    
    public static void makeTinderTooltip(RenderTooltipEvent.GatherComponents event)
    {
        ItemStack stack = event.getItemStack();
        List<Either<FormattedText, TooltipComponent>> components = event.getTooltipElements();
        
        getToolTipsAfterSpecialRender(stack, components);
    }
    
    private static void getToolTipsAfterSpecialRender(ItemStack stack, List<Either<FormattedText, TooltipComponent>> components)
    {
        int iniSize = components.size();
        for(int i = 0; i < iniSize; i++)
        {
            if(components.get(i).left().isPresent() &&
                    components.get(i).left().get() instanceof MiddleComponent middle &&
                    middle.getDamage() > 0 &&
                    Screen.hasShiftDown())
            {
                components.set(i, Either.right(new IconComponent(middle, 10, 10)));
            }
        }
    }
    
    public record IconComponent(MiddleComponent middle, int width,
                                int height) implements ClientTooltipComponent, TooltipComponent
    {
        
        @Override
        public void renderImage(Font font, int tooltipX, int tooltipY, PoseStack pose, ItemRenderer itemRenderer, int something)
        {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            pose.pushPose();
            pose.translate(0, 0, 500);
    
            int color = HIGH_CONTRAST_MODE.get() ? 0xffffff : middle.getStyle().getColor().getValue();
    
            int len = tooltipX;
    
            GuiComponent.drawString(pose, Minecraft.getInstance().font, middle,
                    len, tooltipY, color);
            len += Minecraft.getInstance().font.width(middle.getVisualOrderText());
    
    
            RenderSystem.setShaderTexture(0, StaticValue.ICONS);
    
            GuiComponent.blit(pose, len, tooltipY, 0, 0, 9, 9, 256, 256);
            len += 9;
    
            GuiComponent.drawString(pose, Minecraft.getInstance().font, String.format(" x%.2f ", middle.getDamage()),
                    len, tooltipY, color);
            len += Minecraft.getInstance().font.width(String.format(" x%.2f ", middle.getDamage()));
    
    
            RenderSystem.setShaderTexture(0, StaticValue.ICONS);
    
            GuiComponent.blit(pose, len, tooltipY, 9, 0, 9, 9, 256, 256);
            len += 9;
    
            GuiComponent.drawString(pose, Minecraft.getInstance().font, String.format(" x%.2f ", middle.getMinHealth()),
                    len, tooltipY, color);
            len += Minecraft.getInstance().font.width(String.format(" x%.2f ", middle.getMinHealth()));
    
    
            RenderSystem.setShaderTexture(0, StaticValue.ICONS);
    
            GuiComponent.blit(pose, len, tooltipY, 18, 0, 9, 9, 256, 256);
            len += 9;
    
            TranslatableComponent cooldownCom = new TranslatableComponent("tooltip.firewood.tinder_item.cooldown", middle.getCooldown() / 20.0);
            GuiComponent.drawString(pose, Minecraft.getInstance().font, cooldownCom,
                    len, tooltipY, color);
            len += Minecraft.getInstance().font.width(cooldownCom);
    
            pose.popPose();
        }
        
        @Override
        public int getHeight()
        {
            return height;
        }
    
        @Override
        public int getWidth(Font font)
        {
            return Minecraft.getInstance().font.width(middle.getVisualOrderText())
                    + Minecraft.getInstance().font.width(String.format(" x%.2f", middle.getDamage())) + 9
                    + Minecraft.getInstance().font.width(String.format(" x%.2f", middle.getMinHealth())) + 9
                    + Minecraft.getInstance().font.width(new TranslatableComponent("tooltip.firewood.tinder_item.cooldown", middle.getCooldown() / 20.0)) + 9;
        }
    }
}

