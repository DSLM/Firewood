package com.dslm.firewood.fireeffecthelper.flesh;

import com.dslm.firewood.fireeffecthelper.flesh.base.MinorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dslm.firewood.util.StaticValue.colorfulText;

public class BlockCheckOrderFireEffectHelper extends MinorFireEffectHelperBase
{
    public static final String ORDER = "order";
    
    public static final HashMap<Character, Integer> posNegMap = new HashMap()
    {{
        put('-', -1);
        put('+', 1);
    }};
    
    
    public static final HashMap<Character, Integer> dirMap = new HashMap()
    {{
        put('x', 0);
        put('y', 1);
        put('z', 2);
    }};
    
    public BlockCheckOrderFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            set(ORDER, "y+x-z-");
        }}, id);
    }
    
    @Override
    public int getColor(FireEffectNBTDataInterface data)
    {
        return 0xffffff;
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTDataInterface data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.minor_effect." + data.getType(), data.get(ORDER)), getColor(data));
        lines.add(mainLine);
        return lines;
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTDataInterface data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(StaticValue.TYPE, ID);
        tags.putString(ORDER, data.get(ORDER));
        return tags;
    }
    
    @Override
    public FireEffectNBTDataInterface readFromNBT(CompoundTag tags)
    {
        FireEffectNBTDataInterface data = new FireEffectNBTData();
        data.setType(ID);
        data.set(ORDER, tags.getString(ORDER));
        return data;
    }
    
    public Pair<Iterable<BlockPos>, FireEffectNBTDataInterface> getBlocksByCache(FireEffectNBTDataInterface data,
                                                                                 FireEffectSubTypeBase effectData,
                                                                                 int targetLimit,
                                                                                 String order)
    {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();
        
        int counter = 0;
        
        int[] cache = data.getCache();
        int[] effectRange = effectData.getRange();
        int[] nested = {dirMap.get(order.charAt(0)), dirMap.get(order.charAt(2)), dirMap.get(order.charAt(4))};
        int[] side = {posNegMap.get(order.charAt(1)), posNegMap.get(order.charAt(3)), posNegMap.get(order.charAt(5))};
        int[] nowPos = {cache[3], cache[4], cache[5]};
        
        for(;
            nowPos[nested[0]] * side[0] >= (cache[nested[0]] - side[0] * effectRange[nested[0]]) * side[0];
            nowPos[nested[0]] -= side[0]
        )
        {
            for(;
                nowPos[nested[1]] * side[1] >= (cache[nested[1]] - side[1] * effectRange[nested[1]]) * side[1];
                nowPos[nested[1]] -= side[1]
            )
            {
                for(;
                    nowPos[nested[2]] * side[2] >= (cache[nested[2]] - side[2] * effectRange[nested[2]]) * side[2];
                    nowPos[nested[2]] -= side[2]
                )
                {
                    if(counter >= targetLimit)
                    {
                        cache[3] = nowPos[0];
                        cache[4] = nowPos[1];
                        cache[5] = nowPos[2];
                        data.setCache(cache);
                        
                        return Pair.of(blockPosList, data);
                    }
                    counter++;
                    blockPosList.add(new BlockPos(nowPos[0], nowPos[1], nowPos[2]));
                }
                nowPos[nested[2]] = cache[nested[2]] + side[2] * effectRange[nested[2]];
            }
            nowPos[nested[1]] = cache[nested[1]] + side[1] * effectRange[nested[1]];
        }
        data.setInCache(false);
        return Pair.of(blockPosList, data);
    }
    
    public int[] initializeCash(BlockPos center, FireEffectSubTypeBase effectData, String order)
    {
        int[] cache = {center.getX(), center.getY(), center.getZ(), center.getX(), center.getY(), center.getZ()};
        
        int[] effectRange = effectData.getRange();
        int[] nested = {dirMap.get(order.charAt(0)), dirMap.get(order.charAt(2)), dirMap.get(order.charAt(4))};
        int[] side = {posNegMap.get(order.charAt(1)), posNegMap.get(order.charAt(3)), posNegMap.get(order.charAt(5))};
        
        int[] nowPos = {0, 0, 0};
        
        nowPos[nested[0]] = cache[nested[0]] + side[0] * effectRange[nested[0]];
        nowPos[nested[1]] = cache[nested[1]] + side[1] * effectRange[nested[1]];
        nowPos[nested[2]] = cache[nested[2]] + side[2] * effectRange[nested[2]];
        
        cache[3] = nowPos[0];
        cache[4] = nowPos[1];
        cache[5] = nowPos[2];
        
        return cache;
    }
}
