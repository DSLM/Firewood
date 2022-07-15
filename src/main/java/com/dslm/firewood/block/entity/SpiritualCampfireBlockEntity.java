package com.dslm.firewood.block.entity;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.SpiritualCampfireBlock;
import com.dslm.firewood.compat.shimmer.ShimmerHelper;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper;
import com.dslm.firewood.recipe.TinderRecipe;
import com.dslm.firewood.recipe.type.TinderRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;
import java.util.Random;

import static com.dslm.firewood.util.StaticValue.*;

public class SpiritualCampfireBlockEntity extends BlockEntity implements Container
{
    public static final int NUM_SLOTS = 13;
    protected final NonNullList<ItemStack> inventory;
    protected final IItemHandlerModifiable itemHandler;
    protected final LazyOptional<IItemHandler> handler;
    protected boolean needSync;
    protected int process;
    protected TinderRecipe recipe;
    
    public SpiritualCampfireBlockEntity(BlockPos pWorldPosition, BlockState pBlockState)
    {
        super(Register.SPIRITUAL_CAMPFIRE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        inventory = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
        itemHandler = new SpiritualCampfireBlockInvWrapper(this);
        handler = LazyOptional.of(() -> itemHandler);
    }
    
    public int getColor()
    {
        return FireEffectNBTStaticHelper.getColor(getItem(0));
    }
    
    public TinderRecipe getRecipe()
    {
        return recipe;
    }
    
    public TinderRecipe refreshRecipe()
    {
        for(var recipe1 : level.getRecipeManager().getRecipes().stream()
                .filter(recipe1 -> recipe1.getType() instanceof TinderRecipeType).toList())
        {
            if(recipe1 instanceof TinderRecipe tinderRecipe && tinderRecipe.matches(this, level))
            {
                return tinderRecipe;
            }
        }
        return null;
    }
    
    public void addProcess(BlockState state, Level level, BlockPos pos, LivingEntity livingEntity)
    {
        recipe = refreshRecipe();
        if(recipe != null)
        {
            Random random = level.random;
            if(random.nextDouble() < recipe.getChance())
            {
                double nowHealth = livingEntity.getHealth();
                FireEffectHelpers.damageEntity(livingEntity, (float) recipe.getDamage(), recipe.getCooldown());
                if(nowHealth < recipe.getMinHealth())
                {
                    return;
                }
                process++;
                if(process < recipe.getProcess())
                {
                    return;
                }
                process = 0;
                inventory.set(0, recipe.assemble(this));
                for(int i = 1; i < NUM_SLOTS; i++)
                {
                    removeItem(i, 1);
                }
    
                if(level.isClientSide() && checkMod(SHIMMER))
                {
                    if(ShimmerHelper.hasLight(level, getBlockPos()))
                    {
                        ShimmerHelper.updateLight(level, getBlockPos(), getColor());
                    }
//                    else
//                    {
//                        ShimmerHelper.addLight(level, getBlockPos(), getColor());
//                    }
                }
    
                needSync = true;
            }
        }
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, SpiritualCampfireBlockEntity entity)
    {
        particleTick(level, pos, state, entity);
        //refresh campfire color
        Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    
        if(checkMod(SHIMMER))
        {
            if(state.getValue(LIT))
            {
                if(!ShimmerHelper.hasLight(level, pos))
                {
                    ShimmerHelper.addLight(level, pos, entity.getColor());
                }
            }
            else
            {
                if(ShimmerHelper.hasLight(level, pos))
                {
                    ShimmerHelper.removeLight(level, pos);
                }
            }
        }
    }
    
    public static void serverTick(Level level, BlockPos pos, BlockState state, SpiritualCampfireBlockEntity entity)
    {
        ItemStack tinder = entity.itemHandler.getStackInSlot(0);
        if(!tinder.isEmpty() && tinder.is(ITEM_TINDER_TAG))
        {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), Block.UPDATE_ALL);
        }
        else
        {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.FALSE), Block.UPDATE_ALL);
            entity.process = 0;
        }
        
        entity.syncTick();
    }
    
    public static void particleTick(Level level, BlockPos pos, BlockState state, SpiritualCampfireBlockEntity blockEntity)
    {
        if(blockEntity.itemHandler.getStackInSlot(0) == ItemStack.EMPTY)
            return;
        Random random = level.random;
        if(random.nextFloat() < 0.11F)
        {
            for(int i = 0; i < random.nextInt(2) + 2; ++i)
            {
                SpiritualCampfireBlock.makeParticles(level, pos, false);
            }
        }
        
        int l = state.getValue(CampfireBlock.FACING).get2DDataValue();
        
        for(int j = 1; j < NUM_SLOTS; ++j)
        {
            if(!blockEntity.itemHandler.getStackInSlot(j).isEmpty() && random.nextFloat() < 0.2F)
            {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + l, 4));
                float f = 0.3125F;
                double d0 = (double) pos.getX() + 0.5D - (double) ((float) direction.getStepX() * 0.3125F) + (double) ((float) direction.getClockWise().getStepX() * 0.3125F);
                double d1 = (double) pos.getY() + 0.5D;
                double d2 = (double) pos.getZ() + 0.5D - (double) ((float) direction.getStepZ() * 0.3125F) + (double) ((float) direction.getClockWise().getStepZ() * 0.3125F);
    
                for(int k = 0; k < 4; ++k)
                {
                    level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
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
    
            setChanged();
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
    
        ContainerHelper.saveAllItems(tag, inventory);
        
        CompoundTag infoTag = new CompoundTag();
        infoTag.putInt("Process", process);
        tag.put("Info", infoTag);
    }
    
    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        clearContent();
        ContainerHelper.loadAllItems(tag, inventory);
        if(tag.contains("Info"))
        {
            process = tag.getCompound("Info").getInt("Process");
        }
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
    
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
    
    public int getProcess()
    {
        return process;
    }
    
    @Override
    public int getContainerSize()
    {
        return inventory.size();
    }
    
    @Override
    public boolean isEmpty()
    {
        for(ItemStack itemstack : inventory)
        {
            if(!itemstack.isEmpty())
            {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public ItemStack getItem(int index)
    {
        if(index < 0 || index >= inventory.size())
        {
            return ItemStack.EMPTY;
        }
        
        return inventory.get(index);
    }
    
    @Override
    public ItemStack removeItem(int index, int count)
    {
        if(count <= 0)
        {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = getItem(index);
        
        if(itemStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        
        // whole itemstack taken out
        if(itemStack.getCount() <= count)
        {
            setItem(index, ItemStack.EMPTY);
            setChangedFast();
            return itemStack;
        }
        
        // split itemstack
        itemStack = itemStack.split(count);
        // slot is empty, set to ItemStack.EMPTY
        // isn't this redundant to the above check?
        if(getItem(index).getCount() == 0)
        {
            setItem(index, ItemStack.EMPTY);
        }
        
        setChangedFast();
        // return remainder
        return itemStack;
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        ItemStack itemStack = getItem(index);
        setItem(index, ItemStack.EMPTY);
        return itemStack;
    }
    
    @Override
    public void setItem(int index, ItemStack stack)
    {
        if(index < 0 || index >= inventory.size())
        {
            return;
        }
    
        ItemStack current = inventory.get(index);
        inventory.set(index, stack);
    
        if(level != null && level.isClientSide() && index == 0 && checkMod(SHIMMER))
        {
            if(ShimmerHelper.hasLight(level, getBlockPos()))
            {
                ShimmerHelper.updateLight(level, getBlockPos(), getColor());
            }
//            else
//            {
//                ShimmerHelper.addLight(level, getBlockPos(), getColor());
//            }
        }
    
        if(!stack.isEmpty() && stack.getCount() > getMaxStackSize())
        {
            stack.setCount(getMaxStackSize());
        }
        if(!ItemStack.matches(current, stack))
        {
            setChangedFast();
        }
        else
        {
            needSync = true;
        }
    }
    
    @Override
    public boolean stillValid(Player player)
    {
        if(level == null || level.getBlockEntity(worldPosition) != this || getBlockState().getBlock() == Blocks.AIR)
        {
            return false;
        }
        
        return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D)
                <= 64D;
    }
    
    @Override
    public void clearContent()
    {
        for(int i = 0; i < inventory.size(); i++)
        {
            inventory.set(i, ItemStack.EMPTY);
        }
    
        if(level != null && level.isClientSide() && checkMod(SHIMMER))
        {
            if(ShimmerHelper.hasLight(level, getBlockPos()))
            {
                ShimmerHelper.removeLight(level, getBlockPos());
            }
        }
        needSync = true;
    }
    
    @Override
    public int getMaxStackSize()
    {
        return 64;
    }
    
    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        return !stack.isEmpty() && inventory.get(index).isEmpty() && (index != 0 || stack.is(ITEM_TINDER_TAG));
    }
    
    /**
     * Marks the chunk dirty without performing comparator updates (twice!!) or block state checks
     * Used since most of our markDirty calls only adjust TE data
     */
    @SuppressWarnings("deprecation")
    public void setChangedFast()
    {
        if(level != null)
        {
            if(level.hasChunkAt(worldPosition))
            {
                level.getChunkAt(worldPosition).setUnsaved(true);
            }
        }
    }
    
    public List<ItemStack> getIngredients()
    {
        return inventory.subList(1, inventory.size());
    }
    
    public ItemStack getTinder()
    {
        return inventory.get(0);
    }
    
    public void askForSync()
    {
        needSync = true;
    }
    
    public static class SpiritualCampfireBlockInvWrapper extends InvWrapper
    {
        public SpiritualCampfireBlockEntity entity;
    
        public SpiritualCampfireBlockInvWrapper(SpiritualCampfireBlockEntity inv)
        {
            super(inv);
            entity = inv;
        }
    
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            if(isItemValid(slot, stack))
            {
                entity.needSync = true;
                return super.insertItem(slot, stack, simulate);
            }
            return stack;
        }
    
        @Override
        public void setStackInSlot(int slot, ItemStack stack)
        {
            if(isItemValid(slot, stack))
            {
                entity.needSync = true;
                super.setStackInSlot(slot, stack);
            }
        }
    }
}
