package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.block.SpiritualFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class GroundFireEffectHelper extends FireEffectHelperBase
{
    @Override
    public int getColor(HashMap<String, String> data)
    {
        return getBlockColor(data.get("block"));
    }
    
    @Override
    public void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
    
    }
    
    @Override
    public float getDamage()
    {
        return 0;
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extend)
    {
        ArrayList<Component> lines = new ArrayList<>();
        var name = getBlock(data.get("block")).getName();
        lines.add(colorfulText(
                new TranslatableComponent("tooltip.firewood.tinder_item.minor_effect." + data.get("type"),
                        name),
                getBlockColor(data.get("block"))));
        return lines;
    }
    
    @Override
    public CompoundTag getDefaultNBT()
    {
        return saveToNBT(new HashMap<>()
        {{
            put("block", "minecraft:netherrack");
        }});
    }
    
    public static Block getBlockByNBTs(CompoundTag compoundTag)
    {
        try
        {
            ListTag minorList = ((ListTag) compoundTag.get("minorEffects"));
            for(Tag i : minorList)
            {
                if(i instanceof CompoundTag tag
                        && "ground".equals(tag.get("type").getAsString()))
                {
                    return getBlock(tag.get("block").getAsString());
                }
            }
        } catch(Exception e)
        {
        }
        return null;
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString("type", "ground");
        tags.putString("block", data.get("block"));
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", "ground");
        data.put("block", tags.getString("block"));
        return data;
    }
    
    public static int getBlockColor(String block)
    {
        return 0xa78e44;
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
                if("ground".equals(i.get("type")))
                {
                    return getBlock(i.get("block"));
                }
            }
        } catch(Exception e)
        {
        }
        return null;
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return first.getString("type").equals(second.getString("type"));
    }
}
