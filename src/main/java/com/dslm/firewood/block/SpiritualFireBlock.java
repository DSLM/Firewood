package com.dslm.firewood.block;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualFireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class SpiritualFireBlock extends BaseFireBlock implements EntityBlock
{
    public SpiritualFireBlock(Properties pProperties)
    {
        super(pProperties, 0);
    }
    
    @Override
    protected boolean canBurn(BlockState pState)
    {
        return true;
    }
    
    public static boolean canBePlacedAt(Level pLevel, BlockPos pos)
    {
        BlockState blockstate = pLevel.getBlockState(pos);
        return blockstate.isAir();
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
//    {
//
//        return super.use(state, level, pos, player, hand, hit);
//    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType)
    {
        return (lvl, pos, blockState, t) -> {
            if(t instanceof SpiritualFireBlockEntity tile)
            {
                if(pLevel.isClientSide) tile.clientTick(lvl, pos, blockState, tile);
                else tile.serverTick(lvl, pos, blockState, tile);
            }
        };
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new SpiritualFireBlockEntity(pPos, pState);
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
    {
        if(entity instanceof LivingEntity livingEntity && level.getBlockEntity(pos) instanceof SpiritualFireBlockEntity tile)
        {
            if(!livingEntity.hasEffect(Register.FIRED_FLESH.get()))
            {
                tile.triggerMajorEffects(state, level, pos, livingEntity);
            }
        }
    }
}
