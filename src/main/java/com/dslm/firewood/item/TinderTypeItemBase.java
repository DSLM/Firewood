package com.dslm.firewood.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.fireTooltips;

public interface TinderTypeItemBase
{
    
    default void appendTinderToolTip(@Nonnull ItemStack stack, Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag)
    {
        boolean extended = false;
        if(level != null)
        {
            extended = Screen.hasShiftDown();
        }
        tooltip.addAll(fireTooltips(stack.getOrCreateTag(), extended));
    }
}
