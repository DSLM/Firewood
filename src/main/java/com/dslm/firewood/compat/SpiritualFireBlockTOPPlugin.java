package com.dslm.firewood.compat;

import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.fireTooltips;


public class SpiritualFireBlockTOPPlugin implements TOPCompat.Provider
{
    public static void register()
    {
        TOPCompat.registerProvider(new SpiritualFireBlockTOPPlugin());
    }
    
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData)
    {
        BlockEntity blockEntity = level.getBlockEntity(hitData.getPos());
        if(blockEntity instanceof SpiritualFireBlockEntity)
        {
            ArrayList<Component> lines = fireTooltips(((SpiritualFireBlockEntity) blockEntity).getUpdateTag(), mode != ProbeMode.NORMAL || player.isShiftKeyDown());
            for(Component line : lines)
            {
                info.mcText(line);
            }
        }
    }
}
