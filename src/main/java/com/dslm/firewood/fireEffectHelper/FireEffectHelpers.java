package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.dslm.firewood.NBTUtils.loadMajorFireData;
import static com.dslm.firewood.NBTUtils.loadMinorFireData;

public class FireEffectHelpers
{
    public static final int majorEffectColor = 0x48c774;
    public static final int minorEffectColor = 0x48c774;
    
    private static HashMap<String, FireEffectHelperBase> fireEffectHelpers = new HashMap<String, FireEffectHelperBase>()
    {{
        put("potion", new PotionFireEffectHelper());
    }};
    
    public static void addHelper(String id, FireEffectHelperBase helper)
    {
        fireEffectHelpers.put(id, helper);
    }
    
    public static FireEffectHelperBase getHelperById(String id)
    {
        return fireEffectHelpers.get(id);
    }
    
    public static int getColorByType(HashMap<String, String> data)
    {
        return getHelperById(data.get("type")).getColor(data);
    }
    
    public static void triggerEffectByType(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        FireEffectHelperBase helper = getHelperById(data.get("type"));
        damageEntity(entity, helper.getDamage());
        helper.triggerEffect(data, state, level, pos, entity);
    }
    
    public static void triggerMajorEffects(ArrayList<HashMap<String, String>> majorEffects, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        for(HashMap<String, String> i : majorEffects)
        {
            triggerEffectByType(i, state, level, pos, entity);
        }
    }
    
    public static void damageEntity(LivingEntity entity, float amount)
    {
        if(amount > 0)
            entity.hurt(Register.SPIRITUAL_FIRE_DAMAGE, amount);
    }
    
    
    public static ArrayList<Component> fireTooltips(CompoundTag compoundTag, boolean extend)
    {
        ArrayList<Component> lines = new ArrayList<Component>();
        
        //majorEffects
        ArrayList<HashMap<String, String>> tempMajorEffect = loadMajorFireData(compoundTag);
        if(tempMajorEffect.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.major_effect"), majorEffectColor));
            for(HashMap<String, String> data : tempMajorEffect)
            {
                ArrayList<Component> list = getHelperById(data.get("type")).getToolTips(data, false);
                for(Component one : list)
                {
                    lines.add(one);
                }
            }
        }
        
        //minorEffects
        ArrayList<HashMap<String, String>> tempMinorEffect = loadMinorFireData(compoundTag);
        if(tempMinorEffect.size() > 0)
        {
            lines.add(colorfulText(new TranslatableComponent("tooltip.firewood.tinder_item.minor_effect"), minorEffectColor));
            for(HashMap<String, String> i : tempMinorEffect)
            {
                getHelperById(i.get("type")).getToolTips(i, false);
            }
        }
        
        return lines;
    }
    
    
    public static TranslatableComponent colorfulText(TranslatableComponent text, int color)
    {
        return (TranslatableComponent) text.withStyle(style -> style.withColor(TextColor.fromRgb(color)));
    }
    
    public static int getMixedColor(ArrayList<HashMap<String, String>> majorEffects, ArrayList<HashMap<String, String>> minorEffects)
    {
        int num = 0;
        int[] color = {0, 0, 0};
        Color tempColor;
        
        //mainEffect
        for(HashMap<String, String> i : majorEffects)
        {
            tempColor = new Color(getColorByType(i));
            color[0] += tempColor.getRed();
            color[1] += tempColor.getGreen();
            color[2] += tempColor.getBlue();
            
            num++;
        }
        
        Color finalColor = new Color(color[0] / num, color[1] / num, color[2] / num);
        return finalColor.getRGB();
    }
    
}
