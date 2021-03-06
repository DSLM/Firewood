package com.dslm.firewood.block.entity;

import com.dslm.firewood.Register;
import com.dslm.firewood.compat.shimmer.ShimmerHelper;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.fireeffecthelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

import static com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers.getMixedColor;
import static com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper.*;
import static com.dslm.firewood.util.StaticValue.SHIMMER;
import static com.dslm.firewood.util.StaticValue.checkMod;

public class SpiritualFireBlockEntity extends BlockEntity
{
    private int color = -1;
    private ArrayList<FireEffectNBTDataInterface> majorEffects = new ArrayList<>();
    private ArrayList<FireEffectNBTDataInterface> minorEffects = new ArrayList<>();
    
    public SpiritualFireBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        super(Register.SPIRITUAL_FIRE_BLOCK_ENTITY.get(), blockPos, blockState);
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
        return saveFireData(new CompoundTag(), majorEffects, minorEffects);
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag)
    {
        saveFireData(pTag, majorEffects, minorEffects);
    }
    
    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        
        majorEffects = loadMajorFireData(tag);
        minorEffects = loadMinorFireData(tag);
        
        needSync = true;
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, SpiritualFireBlockEntity entity)
    {
        if(checkMod(SHIMMER))
        {
            if(!ShimmerHelper.hasLight(level, pos))
            {
                ShimmerHelper.addLight(level, pos, entity.getColor());
            }
        }
    }
    
    public static void serverTick(Level level, BlockPos pos, BlockState state, SpiritualFireBlockEntity e)
    {
        
        e.minorEffects = FireEffectHelpers.triggerMinorEffects(e.majorEffects, e.minorEffects, TinderSourceType.GROUND_FIRE, state, level, pos);
        e.majorEffects = FireEffectHelpers.cacheClear(e.majorEffects, e.minorEffects, TinderSourceType.IN_GROUND_LANTERN, state, level, pos);
        e.syncTick();
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
        majorEffects = FireEffectHelpers.triggerMajorEffects(majorEffects, minorEffects, TinderSourceType.GROUND_FIRE, state, level, pos, entity);
    }
    
    public ArrayList<FireEffectNBTDataInterface> getMajorEffects()
    {
        return majorEffects;
    }
    
    public void setMajorEffects(ArrayList<FireEffectNBTDataInterface> majorEffects)
    {
        this.majorEffects = majorEffects;
    }
    
    public ArrayList<FireEffectNBTDataInterface> getMinorEffects()
    {
        return minorEffects;
    }
    
    public void setMinorEffects(ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        this.minorEffects = minorEffects;
    }
}