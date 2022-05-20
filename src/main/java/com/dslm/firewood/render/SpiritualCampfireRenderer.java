package com.dslm.firewood.render;

import com.dslm.firewood.block.SpiritualCampfireBlock;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpiritualCampfireRenderer implements BlockEntityRenderer<SpiritualCampfireBlockEntity>
{
    
    public SpiritualCampfireRenderer(BlockEntityRendererProvider.Context context)
    {
    }
    
    public void render(SpiritualCampfireBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay)
    {
        Direction direction = pBlockEntity.getBlockState().getValue(SpiritualCampfireBlock.FACING);
        List<ItemStack> nonnulllist = pBlockEntity.getIngredients();
        int i = (int) pBlockEntity.getBlockPos().asLong();
    
        for(int j = 0; j < SpiritualCampfireBlockEntity.NUM_SLOTS - 1; ++j)
        {
            ItemStack itemstack = nonnulllist.get(j);
            if(itemstack != ItemStack.EMPTY)
            {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5D, 0.44921875D, 0.5D);
                Direction direction1 = Direction.from2DDataValue((j / 3 + direction.get2DDataValue()) % 4);
                float f = -direction1.toYRot();
                pPoseStack.mulPose(Vector3f.YP.rotationDegrees(f));
                pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                switch((j % 6))
                {
                    case 3:
                        pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
                        pPoseStack.translate(0.75D, 0.0D, 0.0D);
                        break;
                    case 4:
                    case 5:
                        pPoseStack.translate(0.0D, 0.0D, 0.44921875D / 7 * 3);
                        break;
                }
                pPoseStack.translate(-0.375D, -0.375D, 0.0D);
                pPoseStack.translate((j % 3) * 0.25D, 0.0D, 0.0D);
                pPoseStack.scale(0.25F, 0.25F, 0.25F);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, i + j);
                pPoseStack.popPose();
            }
        }
    }
}