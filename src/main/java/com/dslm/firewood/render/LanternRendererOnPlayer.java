package com.dslm.firewood.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class LanternRendererOnPlayer implements ICurioRenderer
{
    
    
    public LanternRendererOnPlayer()
    {
    }
    
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                          SlotContext slotContext,
                                                                          PoseStack matrixStack,
                                                                          RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource renderTypeBuffer,
                                                                          int light, float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks, float netHeadYaw,
                                                                          float headPitch)
    {
        if(!slotContext.visible())
        {
            return;
        }
    
        matrixStack.scale(0.3f, -0.3f, 0.3f);
        matrixStack.translate(-1, -3, 0);
    
        Minecraft.getInstance().getItemRenderer()
                .renderStatic(
                        stack,
                        ItemTransforms.TransformType.FIXED,
                        light,
                        OverlayTexture.NO_OVERLAY,
                        matrixStack,
                        renderTypeBuffer,
                        0);
    }
}
