package com.dslm.firewood.block;

import com.dslm.firewood.block.entity.LanternBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LanternBlock extends Block implements EntityBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    
    private static final VoxelShape SHAPE_NS = Shapes.box(0, 0, .2, 1, 1, .8);
    private static final VoxelShape SHAPE_WE = Shapes.box(0.2, 0, 0, .8, 1, 1);
    
    public LanternBlock(BlockBehaviour.Properties pProperties)
    {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(FACING, Direction.NORTH));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
    {
        return switch(state.getValue(BlockStateProperties.FACING))
                {
                    case DOWN, UP, EAST, WEST -> SHAPE_WE;
                    case NORTH, SOUTH -> SHAPE_NS;
                };
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(LIT, Boolean.FALSE).setValue(FACING, context.getHorizontalDirection());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
    {
        pBuilder.add(LIT, FACING);
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
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit)
    {// TODO: 2022/6/7 打开关闭灯火
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
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
}
