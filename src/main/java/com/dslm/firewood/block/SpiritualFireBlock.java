package com.dslm.firewood.block;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualFireBlockEntity;
import com.dslm.firewood.compat.shimmer.ShimmerHelper;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


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
    
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof SpiritualFireBlockEntity spiritualFireBlockEntity)
        {
            return getCloneItemStack(spiritualFireBlockEntity, state);
        }
        return new ItemStack(Register.TINDER_ITEM.get());
    }
    
    public ItemStack getCloneItemStack(SpiritualFireBlockEntity spiritualFireBlockEntity, BlockState state)
    {
        ItemStack itemStack = new ItemStack(Register.TINDER_ITEM.get());
        FireEffectNBTStaticHelper.saveFireData(itemStack.getOrCreateTag(),
                spiritualFireBlockEntity.getMajorEffects(),
                spiritualFireBlockEntity.getMinorEffects());
        return itemStack;
    }
    
    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state)
    {
        if(level instanceof Level realLevel && level.isClientSide())
        {
            ShimmerHelper.removeLight(realLevel, pos);
        }
    }
}
