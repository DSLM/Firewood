package com.dslm.firewood.block.entity;

import com.dslm.firewood.Register;
import com.dslm.firewood.entity.RemnantSoulEntity;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.fireeffecthelper.flesh.data.TinderSourceType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;

import static com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers.getBlock;
import static com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers.getMixedColor;
import static com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper.*;

public class LanternBlockEntity extends BlockEntity implements RemnantSoulBoundedBlockEntity
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    
    protected int color = -1;
    protected ArrayList<FireEffectNBTDataInterface> majorEffects = new ArrayList<>();
    protected ArrayList<FireEffectNBTDataInterface> minorEffects = new ArrayList<>();
    protected RemnantSoulEntity remnantSoulEntity = null;
    protected CompoundTag remnantSoulEntityTemp = null;
    
    public static final ModelProperty<Block> BASE_BLOCK = new ModelProperty<>();
    public static final ModelProperty<Boolean> REMNANT_SOUL = new ModelProperty<>();
    
    public LanternBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(Register.LANTERN_BLOCK_ENTITY.get(), worldPosition, blockState);
    
    }
    
    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(BASE_BLOCK, getBlock(minorEffects))
                .withInitial(REMNANT_SOUL, remnantSoulEntity != null)
                .build();
    }
    
    public RemnantSoulEntity getRemnantSoulEntity()
    {
        return remnantSoulEntity;
    }
    
    public void setRemnantSoulEntity(RemnantSoulEntity remnantSoulEntity)
    {
        this.remnantSoulEntity = remnantSoulEntity;
        needSync = true;
    }
    
    public static void clientTick(Level level, BlockPos pos, BlockState state, LanternBlockEntity o)
    {
    
    }
    
    public static void serverTick(Level level, BlockPos pos, BlockState state, LanternBlockEntity e)
    {
        if(e.remnantSoulEntity != null)
        {
            e.remnantSoulEntity.tick();
            if(e.remnantSoulEntity.getHealth() <= 0)
            {
                e.remnantSoulEntity = null;
            }
            else if(state.getValue(LIT) && !e.remnantSoulEntity.hasEffect(Register.FIRED_FLESH.get()))
            {
                e.majorEffects = FireEffectHelpers.triggerMajorEffects(e.majorEffects, e.minorEffects, TinderSourceType.IN_GROUND_LANTERN, state, level, pos, e.remnantSoulEntity);
            }
        }
        else if(e.remnantSoulEntityTemp != null)
        {
            e.remnantSoulEntity = new RemnantSoulEntity(Register.REMNANT_SOUL_ENTITY.get(), level);
            e.remnantSoulEntity.load(e.remnantSoulEntityTemp);
            e.remnantSoulEntityTemp = null;
        }
        e.minorEffects = FireEffectHelpers.triggerMinorEffects(e.majorEffects, e.minorEffects, TinderSourceType.IN_GROUND_LANTERN, state, level, pos);
        e.majorEffects = FireEffectHelpers.cacheClear(e.majorEffects, e.minorEffects, TinderSourceType.IN_GROUND_LANTERN, state, level, pos);
        e.syncTick();
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
        saveFireData(tag, majorEffects, minorEffects);
        if(remnantSoulEntity != null)
        {
            CompoundTag fakeEntityTag = new CompoundTag();
            remnantSoulEntity.save(fakeEntityTag);
            tag.put("fakeEntity", fakeEntityTag);
        }
    }
    
    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        
        majorEffects = loadMajorFireData(tag);
        minorEffects = loadMinorFireData(tag);
        if(tag.contains("fakeEntity"))
        {
            remnantSoulEntityTemp = tag.getCompound("fakeEntity");
        }
        
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
