package com.dslm.firewood.render;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper;
import com.dslm.firewood.item.LanternItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import java.util.Random;

import static com.dslm.firewood.block.entity.LanternBlockEntity.BASE_BLOCK;
import static com.dslm.firewood.block.entity.LanternBlockEntity.REMNANT_SOUL;
import static com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers.getBlock;

@OnlyIn(Dist.CLIENT)
public class LanternItemStackEntityRenderer extends BlockEntityWithoutLevelRenderer
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static LanternItemStackEntityRenderer INSTANCE = new LanternItemStackEntityRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    
    public BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    
    public LanternItemStackEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet)
    {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
    }
    
    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        BlockState state = Register.LANTERN_BLOCK.get().defaultBlockState();
        state = state.setValue(LIT, LanternItem.isActive(stack));
        
        if(!(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state) instanceof LanternBakedModel lanternBakedModel))
        {
            return;
        }
        
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer, RenderType.translucent(), true, stack.hasFoil());
        
        IModelData modelData = new ModelDataMap.Builder()
                .withInitial(BASE_BLOCK, getBlock(FireEffectNBTStaticHelper.loadMinorFireData(stack.getOrCreateTag())))
                .withInitial(REMNANT_SOUL, false)
                .build();
        
        Random random = new Random();
        long i = 42L;
        
        for(Direction direction : Direction.values())
        {
            random.setSeed(i);
            Minecraft.getInstance().getItemRenderer().renderQuadList(poseStack, vertexconsumer, lanternBakedModel.getQuads(state, direction, random, modelData), stack, packedLight, packedOverlay);
        }
        
        random.setSeed(i);
        Minecraft.getInstance().getItemRenderer().renderQuadList(poseStack, vertexconsumer, lanternBakedModel.getQuads(state, null, random, modelData), stack, packedLight, packedOverlay);
    }
}
