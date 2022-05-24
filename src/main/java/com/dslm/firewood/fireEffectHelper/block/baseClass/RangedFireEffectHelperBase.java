package com.dslm.firewood.fireEffectHelper.block.baseClass;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.recipe.TransformEffectTypeManager;
import com.dslm.firewood.tooltip.MiddleComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public abstract class RangedFireEffectHelperBase extends MajorFireEffectHelperBase
{
    public static String SUB_TAG_ID = "subType";
    public static String PROCESS = "process";
    
    public RangedFireEffectHelperBase(HashMap<String, String> defaultData, String id)
    {
        super(defaultData, id);
    }
    
    public RangedFireEffectHelperBase(String id)
    {
        super(id);
    }
    
    public String getSubId(HashMap<String, String> data)
    {
        return data.get(SUB_TAG_ID);
    }
    
    public String getRealProcess(HashMap<String, String> data)
    {
        return data.get(PROCESS);
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString("type", ID);
        tags.putString(SUB_TAG_ID, data.get(SUB_TAG_ID));
        tags.putString(PROCESS, data.get(PROCESS));
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", ID);
        data.put(SUB_TAG_ID, tags.getString(SUB_TAG_ID));
        data.put(PROCESS, String.valueOf(tags.getInt(PROCESS)));
        return data;
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.multi_tooltip_format",
                        new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%s".formatted(data.get("type"))),
                        new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%1$s.%2$s".formatted(data.get("type"), data.get(SUB_TAG_ID)))),
                getColor(data));
        mainLine.setDamage(getDamage(data));
        lines.add(mainLine);
        if(extended)
        {
            lines.addAll(getExtraToolTips(data));
        }
        return lines;
    }
    
    public ArrayList<Component> getExtraToolTips(HashMap<String, String> data)
    {
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(new TranslatableComponent("tooltip.firewood.recipe.now_progress", getRealProcess(data), getProcess(data)));
        return lines;
    }
    
    public static Iterable<BlockPos> getBlockPosIterable(BlockPos pos, int offset)
    {
        return BlockPos.betweenClosed(
                pos.offset(offset, offset, offset),
                pos.offset(-offset, -offset, -offset));
    }
    
    @Override
    public String getJEIString(HashMap<String, String> data)
    {
        return data.get("type") + "-" + data.get(SUB_TAG_ID);
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return super.isSameNBT(first, second) &&
                first.getString(SUB_TAG_ID).equals(second.getString(SUB_TAG_ID));
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        for(String s : getSubIdList())
        {
            
            ItemStack stack = FireEffectHelpers.addMajorEffect(item.copy(), ID, new HashMap<>()
            {{
                put(SUB_TAG_ID, s);
                put(PROCESS, "0");
            }});
            
            if(!stack.isEmpty())
                items.add(stack);
        }
        super.fillItemCategory(items, item);
    }
    
    @Override
    public float getDamage(HashMap<String, String> data)
    {
        RangedFireEffectData effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getDamage();
    }
    
    @Override
    public int getColor(HashMap<String, String> data)
    {
        RangedFireEffectData effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getColor();
    }
    
    public int getRange(HashMap<String, String> data)
    {
        RangedFireEffectData effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getRange();
    }
    
    public int getProcess(HashMap<String, String> data)
    {
        RangedFireEffectData effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getProcess();
    }
    
    public float getChance(HashMap<String, String> data)
    {
        RangedFireEffectData effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getChance();
    }
    
    @Override
    public float getMinHealth(HashMap<String, String> data)
    {
        RangedFireEffectData effectData = getSubRealEffect(data);
        return effectData == null ? 0 : effectData.getMinHealth();
    }
    
    public RangedFireEffectData getSubRealEffect(HashMap<String, String> data)
    {
        return getSubMap().getOrDefault(data.get(SUB_TAG_ID), null);
    }
    
    public HashMap<String, RangedFireEffectData> getSubMap()
    {
        return TransformEffectTypeManager.getEffectsMap().getOrDefault(getResourceName(), new HashMap<>());
    }
    
    public Set<String> getSubIdList()
    {
        return getSubMap().keySet();
    }
    
    public String getResourceName()
    {
        return "%s:%s".formatted(Firewood.MOD_ID, getId());
    }
}
