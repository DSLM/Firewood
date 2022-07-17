package com.dslm.firewood.screen;

import com.dslm.firewood.menu.SpiritualCampfireBlockMenu;
import com.dslm.firewood.util.StaticValue;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import static com.dslm.firewood.datagen.LanguageUtil.BUTTON_TOOLTIP;
import static com.dslm.firewood.menu.SpiritualCampfireBlockMenu.SLOTS_POS;
import static com.dslm.firewood.util.StaticValue.ITEM_TINDER_TAG;

@OnlyIn(Dist.CLIENT)
public class SpiritualCampfireBlockScreen extends AbstractContainerScreen<SpiritualCampfireBlockMenu>
{
    private static final ResourceLocation GUI = new ResourceLocation(StaticValue.MOD_ID, "textures/gui/spiritual_campfire_block_gui.png");
    private static final TranslatableComponent TITLE = new TranslatableComponent("block.firewood.spiritual_campfire_block");
    private static final TranslatableComponent INFO = new TranslatableComponent(BUTTON_TOOLTIP + "tinder_info_gui");
    
    private InfoButton infoButton;
    
    public SpiritualCampfireBlockScreen(SpiritualCampfireBlockMenu container, Inventory inv, Component name)
    {
        super(container, inv, name);
        this.imageHeight = imageHeight - 166 + 249;
        this.inventoryLabelY = inventoryLabelY - 166 + 249;
    }
    
    @Override
    protected void init()
    {
        super.init();
        
        infoButton = addRenderableWidget(new InfoButton(
                leftPos + SLOTS_POS.get(0).getLeft() - 18 + 25,
                topPos + SLOTS_POS.get(0).getRight() + 22,
                18,
                18,
                new TextComponent("i"),
                button -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if(menu.getSlot(0).hasItem() && menu.getSlot(0).getItem().is(ITEM_TINDER_TAG))
                    {
                        TinderInfoScreen tinderInfoScreen = new TinderInfoScreen(menu.getSlot(0).getItem().getDisplayName(), menu.getSlot(0).getItem());
                        Minecraft.getInstance().setScreen(tinderInfoScreen);
                    }
                }),
                (Button button, PoseStack poseStack, int mouseX, int mouseY)
                        -> renderTooltip(poseStack, INFO, mouseX, mouseY)));
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(matrixStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}