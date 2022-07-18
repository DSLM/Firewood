package com.dslm.firewood.compat.shimmer;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTStaticHelper;
import com.dslm.firewood.item.LanternItem;
import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.client.light.LightManager;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;


public class ShimmerHelper
{
    public static HashMap<Pair<String, String>, ColorPointLight> lightMap = new HashMap<>();
    
    public static void addLight(Level level, BlockPos pos, int color)
    {
        Pair<String, String> key = Pair.of(level.dimension().location().toString(), pos.toShortString());
        if(hasLight(level, pos))
        {
            removeLight(level, pos);
        }
        ColorPointLight colorPointLight = LightManager.INSTANCE.addLight(new Vector3f(pos.getX(), pos.getY(), pos.getZ()), color | 0xff000000, 15);
        if(colorPointLight != null)
        {
            lightMap.put(key, colorPointLight);
        }
    }
    
    public static void removeLight(Level level, BlockPos pos)
    {
        Pair<String, String> key = Pair.of(level.dimension().location().toString(), pos.toShortString());
        ColorPointLight colorPointLight = lightMap.remove(key);
        if(colorPointLight != null)
        {
            colorPointLight.remove();
        }
    }
    
    public static boolean hasLight(Level level, BlockPos pos)
    {
        Pair<String, String> key = Pair.of(level.dimension().location().toString(), pos.toShortString());
        if(lightMap.containsKey(key) && (lightMap.get(key) == null || lightMap.get(key).isRemoved()))
        {
            lightMap.remove(key);
            return false;
        }
        return lightMap.containsKey(key);
    }
    
    public static void updateLight(Level level, BlockPos pos, int color)
    {
        Pair<String, String> key = Pair.of(level.dimension().location().toString(), pos.toShortString());
        lightMap.get(key).setColor(color | 0xff000000);
        lightMap.get(key).update();
    }
    
    public static void registerItemLight()
    {
        LightManager.INSTANCE.registerItemLight(Register.LANTERN_ITEM.get(), (itemStack -> {
            boolean active = LanternItem.isActive(itemStack);// TODO: 2022/7/18 等构建环境更新 
//            if(!active)
//            {
//                return null;
//            }
            int color = FireEffectHelpers.getMixedColor(
                    FireEffectNBTStaticHelper.loadMajorFireData(itemStack.getOrCreateTag()),
                    FireEffectNBTStaticHelper.loadMinorFireData(itemStack.getOrCreateTag()));
            color = color | 0xff000000;
            return new ColorPointLight.Template(!active ? 0 : 15, color);
        }
        ));
    }
}
