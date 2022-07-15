package com.dslm.firewood.fireeffecthelper.flesh.minor;

import com.dslm.firewood.fireeffecthelper.flesh.base.FireEffectHelperInterface;
import com.dslm.firewood.fireeffecthelper.flesh.base.MinorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.fireeffecthelper.flesh.data.TinderSourceType;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.StaticValue;
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
import static com.dslm.firewood.util.StaticValue.colorfulText;

public class GroundFireEffectHelper extends MinorFireEffectHelperBase
{
    private static final ArrayList<FireEffectHelperInterface> INSTANCE_LIST = new ArrayList<>();
    
    public static final String ID = "ground";
    public static final String BLOCK_TAG_ID = "block";
    
    public GroundFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            set(BLOCK_TAG_ID, GROUND_DEFAULT_BLOCK.get());
        }}, id);
        INSTANCE_LIST.add(this);
    }
    
    @Override
    public FireEffectNBTDataInterface triggerEffect(FireEffectNBTDataInterface data, TinderSourceType tinderSourceType, Level level, BlockPos pos)
    {
        if(tinderSourceType == TinderSourceType.GROUND_FIRE && !canBePlacedOn(data, level, pos))
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
                        && ID.equals(tag.get(StaticValue.TYPE).getAsString()))
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
    public int getColor(FireEffectNBTDataInterface data)
    {
        return getBlock(data.get(BLOCK_TAG_ID)).defaultMaterialColor().col;
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTDataInterface data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(StaticValue.TYPE, ID);
        tags.putString(BLOCK_TAG_ID, data.get(BLOCK_TAG_ID));
        return tags;
    }
    
    @Override
    public FireEffectNBTDataInterface readFromNBT(CompoundTag tags)
    {
        FireEffectNBTDataInterface data = new FireEffectNBTData();
        data.setType(ID);
        data.set(BLOCK_TAG_ID, tags.getString(BLOCK_TAG_ID));
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTDataInterface data, boolean extended)
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
    
    public static Block getBlockByMinorList(ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        try
        {
            for(FireEffectNBTDataInterface i : minorEffects)
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
    public boolean canBePlacedOn(FireEffectNBTDataInterface data, Level level, BlockPos pos)
    {
        BlockState blockstate = level.getBlockState(pos.below());
        return blockstate.getBlock() == getBlock(data.get(BLOCK_TAG_ID));
    }
    
    @Override
    public String getJEIString(FireEffectNBTDataInterface data)
    {
        return data.getType() + "-" + data.get(BLOCK_TAG_ID);
    }
    
    public static List<FireEffectHelperInterface> getInstanceList()
    {
        return INSTANCE_LIST;
    }
}
