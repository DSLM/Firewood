package com.dslm.firewood.block;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.LanternBlockEntity;
import com.dslm.firewood.compat.shimmer.ShimmerHelper;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper;
import com.dslm.firewood.item.LanternItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.dslm.firewood.util.StaticValue.HORIZONTAL_FACING;
import static com.dslm.firewood.util.StaticValue.LIT;

public class LanternBlock extends Block implements EntityBlock
{
    
    private static final VoxelShape SHAPE_NS = Shapes.box(0, 0, .2, 1, 1, .8);
    private static final VoxelShape SHAPE_WE = Shapes.box(0.2, 0, 0, .8, 1, 1);
    
    public LanternBlock(BlockBehaviour.Properties pProperties)
    {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(HORIZONTAL_FACING, Direction.NORTH));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
    {
        return switch(state.getValue(HORIZONTAL_FACING))
                {
                    case DOWN, UP, EAST, WEST -> SHAPE_WE;
                    case NORTH, SOUTH -> SHAPE_NS;
                };
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState()
                .setValue(LIT, LanternItem.isActive(context.getItemInHand()))
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
    {
        pBuilder.add(LIT, HORIZONTAL_FACING);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new LanternBlockEntity(pPos, pState);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(player.getItemInHand(hand).isEmpty())
        {
            if(!level.isClientSide())
            {
                level.setBlock(pos, state.setValue(LIT, !state.getValue(LIT)), Block.UPDATE_ALL);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return (lvl, pos, blockState, t) -> {
            if(t instanceof LanternBlockEntity tile)
            {
                if(level.isClientSide) tile.clientTick(lvl, pos, blockState, tile);
                else tile.serverTick(lvl, pos, blockState, tile);
            }
        };
    }
    
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state)
    {
        ItemStack itemStack = new ItemStack(Register.LANTERN_ITEM.get());
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof LanternBlockEntity lanternBlockEntity)
        {
            itemStack = getCloneItemStack(lanternBlockEntity, state);
        }
        return itemStack;
    }
    
    public ItemStack getCloneItemStack(LanternBlockEntity lanternBlockEntity, BlockState state)
    {
        ItemStack itemStack = new ItemStack(Register.LANTERN_ITEM.get());
        FireEffectNBTStaticHelper.saveFireData(itemStack.getOrCreateTag(),
                lanternBlockEntity.getMajorEffects(),
                lanternBlockEntity.getMinorEffects());
        LanternItem.initNBT(itemStack);
        if(state.getValue(LIT)) LanternItem.reverseValue(itemStack);
        return itemStack;
    }
    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        List<ItemStack> itemStacks = super.getDrops(state, builder);
        if(blockEntity instanceof LanternBlockEntity lanternBlockEntity)
        {
            itemStacks.add(getCloneItemStack(lanternBlockEntity, state));
        }
        return itemStacks;
    }
    
    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state)
    {
        if(level instanceof Level realLevel && level.isClientSide() && state.getValue(LIT))
        {
            ShimmerHelper.removeLight(realLevel, pos);
        }
    }
}
