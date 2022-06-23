package com.dslm.firewood.compat.top;

import com.dslm.firewood.block.entity.LanternBlockEntity;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.block.entity.SpiritualFireBlockEntity;
import com.dslm.firewood.entity.RemnantSoulEntity;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.tooltip.MiddleComponent;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;

import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.fireTooltips;
import static com.dslm.firewood.util.StaticValue.ICONS;


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
            spiritualFireBlockEntityInfo(mode,
                    info,
                    player,
                    level,
                    state,
                    hitData,
                    spiritualFireBlockEntity.getMajorEffects(),
                    spiritualFireBlockEntity.getMinorEffects());
        }
        if(blockEntity instanceof LanternBlockEntity lanternBlockEntity)
        {
            LanternBlockEntityInfo(mode,
                    info,
                    player,
                    level,
                    state,
                    hitData,
                    lanternBlockEntity.getMajorEffects(),
                    lanternBlockEntity.getMinorEffects(),
                    lanternBlockEntity.getRemnantSoulEntity());
        }
        if(blockEntity instanceof SpiritualCampfireBlockEntity spiritualCampfireBlockEntity)
        {
            spiritualCampfireBlockEntityInfo(mode,
                    info,
                    player,
                    level,
                    state,
                    hitData,
                    spiritualCampfireBlockEntity);
        }
    }
    
    public static void LanternBlockEntityInfo(ProbeMode mode,
                                              IProbeInfo info,
                                              Player player,
                                              Level level,
                                              BlockState state,
                                              IProbeHitData hitData,
                                              ArrayList<FireEffectNBTDataInterface> majorEffects,
                                              ArrayList<FireEffectNBTDataInterface> minorEffects,
                                              RemnantSoulEntity remnantSoulEntity)
    {
        boolean isActive = state.getValue(BlockStateProperties.LIT);
        TranslatableComponent newLine = isActive ?
                new TranslatableComponent("tooltip.firewood.lantern_item.active")
                :
                new TranslatableComponent("tooltip.firewood.lantern_item.inactive");
        newLine.withStyle(isActive ?
                ChatFormatting.GREEN
                :
                ChatFormatting.RED);
        info.mcText(newLine);
        
        if(remnantSoulEntity != null)
        {
            info.horizontal()
                    .mcText(new TranslatableComponent("item.firewood.remnant_soul_item"))
                    .mcText(new TextComponent(String.format(": %.2f/%.2f", remnantSoulEntity.getHealth(), remnantSoulEntity.getMaxHealth())));
        }
        
        spiritualFireBlockEntityInfo(mode, info, player, level, state, hitData, majorEffects, minorEffects);
    }
    
    public static void spiritualFireBlockEntityInfo(ProbeMode mode,
                                                    IProbeInfo info,
                                                    Player player,
                                                    Level level,
                                                    BlockState state,
                                                    IProbeHitData hitData,
                                                    ArrayList<FireEffectNBTDataInterface> majorEffects,
                                                    ArrayList<FireEffectNBTDataInterface> minorEffects)
    {
        ArrayList<Component> lines =
                fireTooltips(majorEffects, minorEffects,
                        mode != ProbeMode.NORMAL || player.isShiftKeyDown());
        
        for(Component line : lines)
        {
            if((mode != ProbeMode.NORMAL || player.isShiftKeyDown())
                    && line instanceof MiddleComponent middle
                    && middle.getDamage() > 0)
            {
                IconStyle tempIconStyle = new IconStyle();
                tempIconStyle.width(5).height(9);
    
                info.horizontal()
                        .mcText(line)
                        .icon(ICONS, 0, 0, 9, 9, tempIconStyle)
                        .mcText(new TextComponent(String.format(" x%.2f ", middle.getDamage())).withStyle(middle.getStyle()))
                        .icon(ICONS, 9, 0, 9, 9, tempIconStyle)
                        .mcText(new TextComponent(String.format(" x%.2f ", middle.getMinHealth())).withStyle(middle.getStyle()))
                        .icon(ICONS, 18, 0, 9, 9, tempIconStyle)
                        .mcText(new TranslatableComponent("tooltip.firewood.tinder_item.cooldown", middle.getCooldown() / 20.0).withStyle(middle.getStyle()));
            }
            else
            {
                info.mcText(line);
            }
        }
    }
    
    public static void spiritualCampfireBlockEntityInfo(ProbeMode mode,
                                                        IProbeInfo info,
                                                        Player player,
                                                        Level level,
                                                        BlockState state,
                                                        IProbeHitData hitData,
                                                        SpiritualCampfireBlockEntity blockEntity)
    {
        if(blockEntity.getProcess() > 0 && blockEntity.getRecipe() != null)
        {
            info.progress(blockEntity.getProcess(), blockEntity.getRecipe().getProcess(),
                    info.defaultProgressStyle().suffix("/" + blockEntity.getRecipe().getProcess()));
        }
    }
}
