package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.tooltip.MiddleComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class GroundFireEffectHelper extends FireEffectHelperBase
{
    private static final ArrayList<FireEffectHelperInterface> instanceList = new ArrayList<>();
    
    private static final int color = 0xa78e44;
    public static final String id = "ground";
    public static final String blockTagId = "block";
    
    public GroundFireEffectHelper(String id)
    {
        super(new HashMap<>()
        {{
            put(blockTagId, "minecraft:netherrack");
        }}, id);
        instanceList.add(this);
    }
    
    public static int getColor()
    {
        return color;
    }
    
    @Override
    public void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        if(!canBePlacedOn(level, pos, getBlock(data.get(blockTagId))))
        {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }
    
    @Override
    public float getDamage()
    {
        return 0;
    }
    
    public static Block getBlockByNBTs(CompoundTag compoundTag)
    {
        try
        {
            ListTag minorList = ((ListTag) compoundTag.get("minorEffects"));
            for(Tag i : minorList)
            {
                if(i instanceof CompoundTag tag
                        && id.equals(tag.get("type").getAsString()))
                {
                    return getBlock(tag.get(blockTagId).getAsString());
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
        return color;
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString("type", id);
        tags.putString(blockTagId, data.get(blockTagId));
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", id);
        data.put(blockTagId, tags.getString(blockTagId));
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = getBlock(data.get(blockTagId)).getName();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.minor_effect." + data.get("type"), name), color);
        lines.add(mainLine);
        mainLine.setDamage(getDamage());
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
                if(id.equals(i.get("type")))
                {
                    return getBlock(i.get(blockTagId));
                }
            }
        } catch(Exception e)
        {
        }
        return null;
    }
    
    public static boolean canBePlacedOn(Level level, BlockPos pos, Block validGround)
    {
        BlockState blockstate = level.getBlockState(pos.below());
        return blockstate.getBlock() == validGround;
    }
    
    @Override
    public String getJEIString(HashMap<String, String> data)
    {
        return data.get("type") + "-" + data.get(blockTagId);
    }
    
    public static List<FireEffectHelperInterface> getInstanceList()
    {
        return instanceList;
    }
}
