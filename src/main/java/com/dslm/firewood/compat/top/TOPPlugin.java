package com.dslm.firewood.compat.top;

import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.block.entity.SpiritualFireBlockEntity;
import com.dslm.firewood.tooltip.MiddleComponent;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.gui.ForgeIngameGui;

import java.util.ArrayList;

import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.fireTooltips;


public class TOPPlugin implements TOPCompat.Provider
{
    public static void register()
    {
        TOPCompat.registerProvider(new TOPPlugin());
    }
    
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData)
    {
        BlockEntity blockEntity = level.getBlockEntity(hitData.getPos());
        if(blockEntity instanceof SpiritualFireBlockEntity spiritualFireBlockEntity)
        {
            spiritualFireBlockEntityInfo(mode, info, player, level, state, hitData, spiritualFireBlockEntity);
        }
        if(blockEntity instanceof SpiritualCampfireBlockEntity spiritualCampfireBlockEntity)
        {
            spiritualCampfireBlockEntityInfo(mode, info, player, level, state, hitData, spiritualCampfireBlockEntity);
        }
    }
    
    public static void spiritualFireBlockEntityInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData, SpiritualFireBlockEntity blockEntity)
    {
        ArrayList<Component> lines =
                fireTooltips(blockEntity.majorEffects, blockEntity.minorEffects,
                        mode != ProbeMode.NORMAL || player.isShiftKeyDown());
        for(Component line : lines)
        {
            if((mode != ProbeMode.NORMAL || player.isShiftKeyDown())
                    && line instanceof MiddleComponent middle
                    && middle.getDamage() > 0)
            {
                IconStyle tempIconStyle = new IconStyle();
                tempIconStyle.width(4).height(9);
    
                info.horizontal()
                        .mcText(line)
                        .icon(ForgeIngameGui.GUI_ICONS_LOCATION, 61, 0, 4, 9, tempIconStyle)
                        .mcText(new TextComponent(String.format(" x%.2f", middle.getDamage())).withStyle(middle.getStyle()));
            }
            else
            {
                info.mcText(line);
            }
        }
    }
    
    public static void spiritualCampfireBlockEntityInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData, SpiritualCampfireBlockEntity blockEntity)
    {
        if(blockEntity.getProcess() > 0 && blockEntity.getRecipe() != null)
        {
            info.progress(blockEntity.getProcess(), blockEntity.getRecipe().getProcess(),
                    info.defaultProgressStyle().suffix("/" + blockEntity.getRecipe().getProcess()));
        }
    }
}
