package com.dslm.firewood.block;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.menu.SpiritualCampfireBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import java.util.Random;

public class SpiritualCampfireBlock extends BaseEntityBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
    private static final VoxelShape VIRTUAL_FENCE_POST = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final int SMOKE_DISTANCE = 5;
    
    public SpiritualCampfireBlock(Properties pProperties)
    {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.TRUE).setValue(FACING, Direction.NORTH));
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
    {
        if(!level.isClientSide
                && entity instanceof LivingEntity livingEntity
                && !livingEntity.hasEffect(Register.FIRED_FLESH.get())
                && level.getBlockEntity(pos) instanceof SpiritualCampfireBlockEntity tile)
        {
            tile.addProcess(state, level, pos, livingEntity);
        }
    }
    
    public static void makeParticles(Level pLevel, BlockPos pPos, boolean pSpawnExtraSmoke)
    {
        Random random = pLevel.getRandom();
        SimpleParticleType simpleparticletype = ParticleTypes.CAMPFIRE_COSY_SMOKE;
        pLevel.addAlwaysVisibleParticle(simpleparticletype, true, (double) pPos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pPos.getY() + random.nextDouble() + random.nextDouble(), (double) pPos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if(pSpawnExtraSmoke)
        {
            pLevel.addParticle(ParticleTypes.SMOKE, (double) pPos.getX() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pPos.getY() + 0.4D, (double) pPos.getZ() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }
        
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return (lvl, pos, blockState, t) -> {
            if(t instanceof SpiritualCampfireBlockEntity tile)
            {
                if(level.isClientSide) tile.clientTick(lvl, pos, blockState, tile);
                else tile.serverTick(lvl, pos, blockState, tile);
            }
        };
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new SpiritualCampfireBlockEntity(pPos, pState);
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
    
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand)
    {
        if(pState.getValue(LIT))
        {
            if(pRand.nextInt(10) == 0)
            {
                pLevel.playLocalSound((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + pRand.nextFloat(), pRand.nextFloat() * 0.7F + 0.6F, false);
            }
            
            if(pRand.nextInt(5) == 0)
            {
                for(int i = 0; i < pRand.nextInt(1) + 1; ++i)
                {
                    pLevel.addParticle(ParticleTypes.LAVA, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, pRand.nextFloat() / 2.0F, 5.0E-5D, pRand.nextFloat() / 2.0F);
                }
            }
            
        }
    }
    
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
    {
        return SHAPE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace)
    {
        if(!level.isClientSide)
        {
            BlockEntity be = level.getBlockEntity(pos);
            if(be instanceof SpiritualCampfireBlockEntity)
            {
                MenuProvider containerProvider = new MenuProvider()
                {
                    @Override
                    public Component getDisplayName()
                    {
                        return new TranslatableComponent("block.firewood.spiritual_campfire_block");
                    }
    
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity)
                    {
                        return new SpiritualCampfireBlockMenu(windowId, pos, playerInventory, playerEntity);
                    }
                };
                NetworkHooks.openGui((ServerPlayer) player, containerProvider, be.getBlockPos());
            }
            else
            {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
}
