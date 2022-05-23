package com.dslm.firewood.screen;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.menu.SpiritualCampfireBlockMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SpiritualCampfireBlockScreen extends AbstractContainerScreen<SpiritualCampfireBlockMenu>
{
    private final ResourceLocation GUI = new ResourceLocation(Firewood.MOD_ID, "textures/gui/spiritual_campfire_block_gui.png");
    private final TranslatableComponent title = new TranslatableComponent("block.firewood.spiritual_campfire_block");
    
    public SpiritualCampfireBlockScreen(SpiritualCampfireBlockMenu container, Inventory inv, Component name)
    {
        super(container, inv, name);
        this.imageHeight = 249;
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        Minecraft.getInstance().font.draw(matrixStack, title, 5, 5, 4210752);
    }
    
    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}