package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.base.MinorFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.tooltip.MiddleComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.GROUND_DEFAULT_BLOCK;
import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.colorfulText;

public class GroundFireEffectHelper extends MinorFireEffectHelperBase
{
    private static final ArrayList<FireEffectHelperInterface> INSTANCE_LIST = new ArrayList<>();
    
    public static final String ID = "ground";
    public static final String BLOCK_TAG_ID = "block";
    
    public GroundFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            put(BLOCK_TAG_ID, GROUND_DEFAULT_BLOCK.get());
        }}, id);
        INSTANCE_LIST.add(this);
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos)
    {
        if(!canBePlacedOn(data, level, pos))
        {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
        return data;
    }
    
    public static Block getBlockByNBTs(CompoundTag compoundTag)
    {
        try
        {
            ListTag minorList = ((ListTag) compoundTag.get("minorEffects"));
            for(Tag i : minorList)
            {
                if(i instanceof CompoundTag tag
                        && ID.equals(tag.get(TYPE).getAsString()))
                {
                    return getBlock(tag.get(BLOCK_TAG_ID).getAsString());
                }
            }
        } catch(Exception e)
        {
        }
        return null;
    }
    
    @Override
    public int getColor(FireEffectNBTData data)
    {
        return getBlock(data.get(BLOCK_TAG_ID)).defaultMaterialColor().col;
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTData data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(TYPE, ID);
        tags.putString(BLOCK_TAG_ID, data.get(BLOCK_TAG_ID));
        return tags;
    }
    
    @Override
    public FireEffectNBTData readFromNBT(CompoundTag tags)
    {
        FireEffectNBTData data = new FireEffectNBTData();
        data.put(TYPE, ID);
        data.put(BLOCK_TAG_ID, tags.getString(BLOCK_TAG_ID));
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTData data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = getBlock(data.get(BLOCK_TAG_ID)).getName();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.minor_effect." + data.getType(), name), getColor(data));
        lines.add(mainLine);
        return lines;
    }
    
    public static Block getBlock(String block)
    {
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(block));
    }
    
    public static Block getBlockByMinorList(ArrayList<FireEffectNBTData> minorEffects)
    {
        try
        {
            for(FireEffectNBTData i : minorEffects)
            {
                if(ID.equals(i.getType()))
                {
                    return getBlock(i.get(BLOCK_TAG_ID));
                }
            }
        } catch(Exception e)
        {
        }
        return null;
    }
    
    @Override
    public boolean canBePlacedOn(FireEffectNBTData data, Level level, BlockPos pos)
    {
        BlockState blockstate = level.getBlockState(pos.below());
        return blockstate.getBlock() == getBlock(data.get(BLOCK_TAG_ID));
    }
    
    @Override
    public String getJEIString(FireEffectNBTData data)
    {
        return data.getType() + "-" + data.get(BLOCK_TAG_ID);
    }
    
    public static List<FireEffectHelperInterface> getInstanceList()
    {
        return INSTANCE_LIST;
    }
}
