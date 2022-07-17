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
    private static final int LINE_HEIGHT = Minecraft.getInstance().font.lineHeight;
    
    protected int maxLineNum = 26;
    protected int leftStart = 7;
    protected int topStart = 7;
    protected final int imageWidth = 256;
    protected final int imageHeight = 256;
    protected int leftPos;
    protected int topPos;
    protected int scrollHeight = 0;
    protected ItemStack tinder;
    protected List<Component> toolTips;
    
    private static final boolean PLAN_B = true;
    
    
    protected TinderInfoScreen(Component title, ItemStack tinder)
    {
        super(title);
        this.tinder = tinder;
        this.toolTips = FireEffectHelpers.fireTooltips(tinder.getOrCreateTag(), true);
    }
    
    protected void init()
    {
        super.init();
        maxLineNum = (this.height - topStart * 2) / LINE_HEIGHT;
        topStart = (this.height - maxLineNum * LINE_HEIGHT) / 2;
        leftStart = topStart;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        
        
        if(PLAN_B)
        {
            this.leftPos = 0;
            this.topPos = 0;
        }
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
        if(PLAN_B)
        {
            return;
        }
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(poseStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
    {
    
        if(PLAN_B)
        {
            RenderSystem.setShaderTexture(0, GUI);
            blit(poseStack, 0, 0, 0, 0, 7, 7);
            blit(poseStack, this.width - 7, 0, this.imageWidth - 7, 0, 7, 7);
            blit(poseStack, 0, this.height - 7, 0, this.imageHeight - 7, 7, 7);
            blit(poseStack, this.width - 7, this.height - 7, this.imageWidth - 7, this.imageHeight - 7, 7, 7);
        
            fill(poseStack, 7, 7, this.width - 7, this.height - 7, 0xff000000);
        
            fill(poseStack, 0, 7, 1, this.height - 7, 0xff000000);
            fill(poseStack, 1, 7, 3, this.height - 7, 0xffffffff);
            fill(poseStack, 3, 7, 7, this.height - 7, 0xffc2c2c2);
        
            fill(poseStack, this.width - 1, 7, this.width - 0, this.height - 7, 0xff000000);
            fill(poseStack, this.width - 3, 7, this.width - 1, this.height - 7, 0xff555555);
            fill(poseStack, this.width - 7, 7, this.width - 3, this.height - 7, 0xffc2c2c2);
        
            fill(poseStack, 7, 0, this.width - 7, 1, 0xff000000);
            fill(poseStack, 7, 1, this.width - 7, 3, 0xffffffff);
            fill(poseStack, 7, 3, this.width - 7, 7, 0xffc2c2c2);
        
            fill(poseStack, 7, this.height - 1, this.width - 7, this.height - 0, 0xff000000);
            fill(poseStack, 7, this.height - 3, this.width - 7, this.height - 1, 0xff555555);
            fill(poseStack, 7, this.height - 7, this.width - 7, this.height - 3, 0xffc2c2c2);
        
        }
    }
    
    protected void renderTooltips(PoseStack poseStack, int mouseX, int mouseY)
    {
        int textH = topStart, textW = leftStart;
    
        for(int i = scrollHeight / LINE_HEIGHT; i < Math.min(toolTips.size(), maxLineNum + scrollHeight / LINE_HEIGHT); i++)
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
        scrollHeight = Math.min((toolTips.size() - maxLineNum) * LINE_HEIGHT, scrollHeight);
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
