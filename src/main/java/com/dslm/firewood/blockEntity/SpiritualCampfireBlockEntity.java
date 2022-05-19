package com.dslm.firewood.blockEntity;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.SpiritualCampfireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class SpiritualCampfireBlockEntity extends BlockEntity
{
    public static final int NUM_SLOTS = 13;
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    boolean needSync;
    private int process;
    
    public SpiritualCampfireBlockEntity(BlockPos pWorldPosition, BlockState pBlockState)
    {
        super(Register.SPIRITUAL_CAMPFIRE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, SpiritualCampfireBlockEntity entity)
    {
        particleTick(level, pos, state, entity);
    }
    
    public static void serverTick(Level level, BlockPos pos, BlockState state, SpiritualCampfireBlockEntity entity)
    {
        ItemStack tinder = entity.itemHandler.getStackInSlot(0);
        if(!tinder.isEmpty() && tinder.getItem() == Register.TINDER_ITEM.get())
        {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), Block.UPDATE_ALL);
        }
        else
        {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.FALSE), Block.UPDATE_ALL);
        }
        
        entity.syncTick();
    }
    
    public static void particleTick(Level pLevel, BlockPos pPos, BlockState pState, SpiritualCampfireBlockEntity pBlockEntity)
    {
        if(pBlockEntity.itemHandler.getStackInSlot(0) == ItemStack.EMPTY)
            return;
        Random random = pLevel.random;
        if(random.nextFloat() < 0.11F)
        {
            for(int i = 0; i < random.nextInt(2) + 2; ++i)
            {
                SpiritualCampfireBlock.makeParticles(pLevel, pPos, false);
            }
        }
        
        int l = pState.getValue(CampfireBlock.FACING).get2DDataValue();
        
        for(int j = 1; j < NUM_SLOTS; ++j)
        {
            if(!pBlockEntity.itemHandler.getStackInSlot(j).isEmpty() && random.nextFloat() < 0.2F)
            {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + l, 4));
                float f = 0.3125F;
                double d0 = (double) pPos.getX() + 0.5D - (double) ((float) direction.getStepX() * 0.3125F) + (double) ((float) direction.getClockWise().getStepX() * 0.3125F);
                double d1 = (double) pPos.getY() + 0.5D;
                double d2 = (double) pPos.getZ() + 0.5D - (double) ((float) direction.getStepZ() * 0.3125F) + (double) ((float) direction.getClockWise().getStepZ() * 0.3125F);
                
                for(int k = 0; k < 4; ++k)
                {
                    pLevel.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }
    }
    
    protected void sync()
    {
        if(!level.isClientSide)
        {
            ClientboundBlockEntityDataPacket p = ClientboundBlockEntityDataPacket.create(this);
            ((ServerLevel) this.level).getChunkSource().chunkMap.getPlayers(new ChunkPos(getBlockPos()), false)
                    .forEach(k -> k.connection.send(p));
        }
    }
    
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        tag.put("Inventory", itemHandler.serializeNBT());
        
        CompoundTag infoTag = new CompoundTag();
        infoTag.putInt("Process", process);
        tag.put("Info", infoTag);
    }
    
    @Override
    public void load(CompoundTag tag)
    {
        if(tag.contains("Inventory"))
        {
            itemHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
        if(tag.contains("Info"))
        {
            process = tag.getCompound("Info").getInt("Process");
        }
        super.load(tag);
        needSync = true;
    }
    
    void syncTick()
    {
        if(needSync)
        {
            sync();
            needSync = false;
        }
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
    
    private ItemStackHandler createHandler()
    {
        return new ItemStackHandler(NUM_SLOTS)
        {
            
            @Override
            protected void onContentsChanged(int slot)
            {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }
            
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return slot > 0 || stack.getItem() == Register.TINDER_ITEM.get();
            }
            
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
            {
                if(slot > 0 || stack.getItem() == Register.TINDER_ITEM.get())
                {
                    return super.insertItem(slot, stack, simulate);
                }
                return stack;
            }
        };
    }
    
    public int getProcess()
    {
        return process;
    }
    
    public ItemStack getItemStackInSlot(int index)
    {
        return itemHandler.getStackInSlot(index);
    }
}
