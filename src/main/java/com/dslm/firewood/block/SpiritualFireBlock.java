package com.dslm.firewood.block;

import com.dslm.firewood.Register;
import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import com.dslm.firewood.fireEffectHelper.FireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
    
    public BlockState getStateForPlacement(Level level, BlockPos pPos)
    {
        return this.defaultBlockState();
    }
    
    public static boolean canBePlacedAt(Level pLevel, BlockPos pPos, Direction pDirection)
    {
        // TODO: 2022/5/9 判断火焰基底 
        BlockState blockstate = pLevel.getBlockState(pPos);
        if(!blockstate.isAir())
        {
            return false;
        } else
        {
            return true;
        }
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState)
    {
        return RenderShape.MODEL;
    }
    
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit)
    {
        
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    
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
    
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity)
    {
        if(pEntity instanceof LivingEntity livingEntity && pLevel.getBlockEntity(pPos) instanceof SpiritualFireBlockEntity tile)
        {
            // TODO: 2022/5/10 伤害间隔
            if(pLevel.getGameTime() % (10) == 0)
            {
//               ((LivingEntity) pEntity).addEffect(new MobEffectInstance(Register.FIRED_FLESH.get(), MobEffectConfig.FIRED_FLESH_INTERVAL.get() + 1, 1));
                tile.triggerMajorEffects(pState, pLevel, pPos, livingEntity);
                
                
                // TODO: 2022/5/9 自行实现火焰遮挡视线
//                pEntity.setRemainingFireTicks(pEntity.getRemainingFireTicks() + 1);
//                if(pEntity.getRemainingFireTicks() == 0)
//                {
//                    pEntity.setSecondsOnFire(8);
//                }
            }
        }
    }
}
