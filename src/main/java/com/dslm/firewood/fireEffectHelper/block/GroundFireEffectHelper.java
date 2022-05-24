package com.dslm.firewood.fireEffectHelper.block;

import com.dslm.firewood.fireEffectHelper.block.baseClass.FireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.block.baseClass.MinorFireEffectHelperBase;
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
import java.util.HashMap;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class GroundFireEffectHelper extends MinorFireEffectHelperBase
{
    private static final ArrayList<FireEffectHelperInterface> INSTANCE_LIST = new ArrayList<>();
    
    private static final int COLOR = 0xa78e44;
    public static final String ID = "ground";
    public static final String BLOCK_TAG_ID = "block";
    
    public GroundFireEffectHelper(String id)
    {
        super(new HashMap<>()
        {{
            put(BLOCK_TAG_ID, "minecraft:netherrack");
        }}, id);
        INSTANCE_LIST.add(this);
    }
    
    public static int getColor()
    {
        return COLOR;
    }
    
    @Override
    public HashMap<String, String> triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos)
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
                        && ID.equals(tag.get("type").getAsString()))
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
    public int getColor(HashMap<String, String> data)
    {
        return COLOR;
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString("type", ID);
        tags.putString(BLOCK_TAG_ID, data.get(BLOCK_TAG_ID));
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", ID);
        data.put(BLOCK_TAG_ID, tags.getString(BLOCK_TAG_ID));
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = getBlock(data.get(BLOCK_TAG_ID)).getName();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.minor_effect." + data.get("type"), name), COLOR);
        lines.add(mainLine);
        return lines;
    }
    
    public static Block getBlock(String block)
    {
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(block));
    }
    
    public static Block getBlockByMinorList(ArrayList<HashMap<String, String>> minorEffects)
    {
        try
        {
            for(HashMap<String, String> i : minorEffects)
            {
                if(ID.equals(i.get("type")))
                {
                    return getBlock(i.get(BLOCK_TAG_ID));
                }
            }
        } catch(Exception e)
        {
        }
        return null;
    }
    
    public boolean canBePlacedOn(HashMap<String, String> data, Level level, BlockPos pos)
    {
        BlockState blockstate = level.getBlockState(pos.below());
        return blockstate.getBlock() == getBlock(data.get(BLOCK_TAG_ID));
    }
    
    @Override
    public String getJEIString(HashMap<String, String> data)
    {
        return data.get("type") + "-" + data.get(BLOCK_TAG_ID);
    }
    
    public static List<FireEffectHelperInterface> getInstanceList()
    {
        return INSTANCE_LIST;
    }
}
