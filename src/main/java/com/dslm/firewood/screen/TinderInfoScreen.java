package com.dslm.firewood.screen;

import com.dslm.firewood.event.ForgeBusClientHandler.IconComponent;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.StaticValue;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static com.dslm.firewood.util.StaticValue.VANILLA_FONT_COLOR;

@OnlyIn(Dist.CLIENT)
public class TinderInfoScreen extends Screen
{
    private static final ResourceLocation GUI = new ResourceLocation(StaticValue.MOD_ID, "textures/gui/tinder_info_gui.png");
    private static final int MAX_LINES = 26;
    private static final int LEFT_START = 7;
    private static final int TOP_START = 7;
    private static final int LINE_HEIGHT = Minecraft.getInstance().font.lineHeight;
    
    protected int imageWidth = 176;
    protected int imageHeight = 249;
    protected int leftPos;
    protected int topPos;
    protected int scrollHeight = 0;
    protected ItemStack tinder;
    protected List<Component> toolTips;
    
    
    protected TinderInfoScreen(Component title, ItemStack tinder)
    {
        super(title);
        this.tinder = tinder;
        this.toolTips = FireEffectHelpers.fireTooltips(tinder.getOrCreateTag(), true);
    }
    
    protected void init()
    {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }
    
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(poseStack);
        
        int i = this.leftPos;
        int j = this.topPos;
        this.renderBg(poseStack, partialTick, mouseX, mouseY);
        
        RenderSystem.disableDepthTest();
        super.render(poseStack, mouseX, mouseY, partialTick);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double) i, (double) j, 0.0D);
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        this.renderLabels(poseStack, mouseX, mouseY);
        
        this.renderTooltips(poseStack, mouseX, mouseY);
        
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableDepthTest();
    }
    
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(poseStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
    {
    }
    
    protected void renderTooltips(PoseStack poseStack, int mouseX, int mouseY)
    {
        int textH = TOP_START, textW = LEFT_START;
        
        for(int i = scrollHeight / LINE_HEIGHT; i < Math.min(toolTips.size(), MAX_LINES + scrollHeight / LINE_HEIGHT); i++)
        {
            if(toolTips.get(i) instanceof MiddleComponent middle && middle.getDamage() > 0)
            {
                IconComponent renderLine = new IconComponent(middle, 10, 10);
                renderLine.renderImage(Minecraft.getInstance().font, textW, textH, poseStack, Minecraft.getInstance().getItemRenderer(), 0);
            }
            else
            {
                Minecraft.getInstance().font.draw(poseStack, toolTips.get(i), textW, textH, VANILLA_FONT_COLOR);
            }
            textH += LINE_HEIGHT;
        }
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        scrollHeight += delta > 0 ? -LINE_HEIGHT : delta < 0 ? LINE_HEIGHT : 0;
        scrollHeight = Math.min((toolTips.size() - MAX_LINES) * LINE_HEIGHT, scrollHeight);
        scrollHeight = Math.max(0, scrollHeight);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    
    public int getScrollHeight()
    {
        return scrollHeight;
    }
    
    public void setScrollHeight(int scrollHeight)
    {
        this.scrollHeight = scrollHeight;
    }
    
    public ItemStack getTinder()
    {
        return tinder;
    }
    
    public void setTinder(ItemStack tinder)
    {
        this.tinder = tinder;
    }
}
