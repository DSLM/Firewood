package com.dslm.firewood.block.entity;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;

import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.getBlock;
import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.getMixedColor;
import static com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTHelper.*;

public class LanternBlockEntity extends BlockEntity
{
    private int color = -1;
    public ArrayList<FireEffectNBTData> majorEffects = new ArrayList<>();
    public ArrayList<FireEffectNBTData> minorEffects = new ArrayList<>();
    
    public static final ModelProperty<Integer> COLOR = new ModelProperty<>();
    public static final ModelProperty<Block> BASE_BLOCK = new ModelProperty<>();
    
    public LanternBlockEntity(BlockPos pWorldPosition, BlockState pBlockState)
    {
        super(Register.LANTERN_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        
    }
    
    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(COLOR, getColor())
                .withInitial(BASE_BLOCK, getBlock(minorEffects))
                .build();
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, LanternBlockEntity o)
    {
    
    }
    
    public static void serverTick(Level level, BlockPos pos, BlockState state, LanternBlockEntity e)
    {
        // TODO: 2022/6/7 开着且有燃料触发主要
        e.minorEffects = FireEffectHelpers.triggerMinorEffects(e.majorEffects, e.minorEffects, TinderSourceType.IN_GROUND_LANTERN, state, level, pos);
        e.syncTick();
    }
    
    public void triggerMajorEffects(BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        majorEffects = FireEffectHelpers.triggerMajorEffects(majorEffects, minorEffects, TinderSourceType.IN_GROUND_LANTERN, state, level, pos, entity);
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
    
    boolean needSync;
    
    void syncTick()
    {
        if(needSync)
        {
            sync();
            needSync = false;
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
}
