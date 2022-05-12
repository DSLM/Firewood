package com.dslm.firewood.blockEntity;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.SpiritualFireBlock;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.GroundFireEffectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dslm.firewood.NBTUtils.*;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.getMixedColor;

public class SpiritualFireBlockEntity extends BlockEntity
{
    private int color = -1;
    public ArrayList<HashMap<String, String>> majorEffects = new ArrayList<>();
    public ArrayList<HashMap<String, String>> minorEffects = new ArrayList<>();
    
    public SpiritualFireBlockEntity(BlockPos pWorldPosition, BlockState pBlockState)
    {
        super(Register.SPIRITUAL_FIRE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }


//    public void use(Player player)
//    {
//        if(getLevel().isClientSide())
//        {
//        } else
//        {
//
//            sync();
//        }
//    }
    
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
        return saveFireData(new CompoundTag(), majorEffects, minorEffects);
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag)
    {
        saveFireData(pTag, majorEffects, minorEffects);
    }
    
    @Override
    public void load(CompoundTag pTag)
    {
        super.load(pTag);
        
        majorEffects = loadMajorFireData(pTag);
        minorEffects = loadMinorFireData(pTag);
        
        needSync = true;
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, SpiritualFireBlockEntity o)
    {
    
    }
    
    public static void serverTick(Level level, BlockPos pos, BlockState state, SpiritualFireBlockEntity e)
    {
        Block validGround = GroundFireEffectHelper.getBlockByMinorList(e.minorEffects);
        if(!SpiritualFireBlock.canBePlacedOn(level, pos, validGround))
        {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        } else
        {
            e.syncTick();
        }
    }
    
    boolean needSync;
    
    void syncTick()
    {
        if(needSync)
        {
            sync();
            needSync = false;
        }
    }
    
    public int getColor()
    {
        if(color == -1)
            color = CalculateColor();
        return color;
    }
    
    public int CalculateColor()
    {
        return getMixedColor(majorEffects, minorEffects);
    }
    
    public void triggerMajorEffects(BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        FireEffectHelpers.triggerMajorEffects(majorEffects, state, level, pos, entity);
    }
}