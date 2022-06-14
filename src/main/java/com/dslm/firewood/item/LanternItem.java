package com.dslm.firewood.item;

import com.dslm.firewood.Register;
import com.dslm.firewood.compat.curios.Curios;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTHelper;
import com.dslm.firewood.fireEffectHelper.flesh.data.TinderSourceType;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;


public class LanternItem extends BlockItem implements TinderTypeItemBase
{
    public LanternItem(Block block, Properties properties)
    {
        super(block, properties);
    }
    
    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state)
    {
        
        Boolean result = context.getLevel().setBlock(context.getClickedPos(), state, 11);
        context.getLevel().getBlockEntity(context.getClickedPos()).load(context.getItemInHand().getOrCreateTag());
        return result;
    }
    
    public static void initNBT(ItemStack itemStack)
    {
        CompoundTag tags = itemStack.getOrCreateTag();
        if(!tags.contains(StaticValue.ACTIVE_LANTERN))
        {
            tags.putBoolean(StaticValue.ACTIVE_LANTERN, false);
        }
    }
    
    public static boolean isActive(ItemStack itemStack)
    {
        initNBT(itemStack);
        CompoundTag tags = itemStack.getOrCreateTag();
        return tags.getBoolean(StaticValue.ACTIVE_LANTERN);
    }
    
    public static boolean reverseValue(ItemStack itemStack)
    {
        CompoundTag tags = itemStack.getOrCreateTag();
        boolean now = tags.getBoolean(StaticValue.ACTIVE_LANTERN);
        tags.putBoolean(StaticValue.ACTIVE_LANTERN, !now);
        return !now;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag)
    {
        TranslatableComponent newLine = isActive(stack) ?
                new TranslatableComponent("tooltip.firewood.lantern_item.active")
                :
                new TranslatableComponent("tooltip.firewood.lantern_item.inactive");
        newLine.withStyle(isActive(stack) ?
                ChatFormatting.GREEN
                :
                ChatFormatting.RED);
        tooltip.add(newLine);
        appendTinderToolTip(stack, level, tooltip, flag);
        super.appendHoverText(stack, level, tooltip, flag);
    }
    
    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items)
    {
        if(!allowdedIn(group))
        {
            return;
        }
        
        //vanilla
        items.add(new ItemStack(this));
        
        FireEffectHelpers.fillItemCategory(items, new ItemStack(this));
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if(player.isShiftKeyDown())
        {
            reverseValue(itemStack);
        }
        
        return InteractionResultHolder.success(itemStack);
    }
    
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected)
    {
        // TODO: 2022/6/7 开着触发次要
        if(entity instanceof Player player && !(player instanceof FakePlayer) && !level.isClientSide && isActive(stack) &&
                !player.hasEffect(Register.FIRED_FLESH.get()))
        {
            BlockPos blockPos = player.blockPosition();
            BlockState blockState = player.getFeetBlockState();
        
            CompoundTag tags = stack.getOrCreateTag();
            ArrayList<FireEffectNBTData> majorEffects = FireEffectNBTHelper.loadMajorFireData(tags);
            ArrayList<FireEffectNBTData> minorEffects = FireEffectNBTHelper.loadMinorFireData(tags);
        
            tags.remove(StaticValue.MAJOR);
            tags.remove(StaticValue.MINOR);
            majorEffects = FireEffectHelpers.triggerMajorEffects(majorEffects, minorEffects, TinderSourceType.IN_BACKPACK_LANTERN, blockState, level, blockPos, player);
            
            
            tags = FireEffectNBTHelper.saveFireData(tags, majorEffects, minorEffects);
            
            stack.setTag(tags);
        }
    }
    
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt)
    {
        initNBT(stack);
        if(!StaticValue.checkMod(StaticValue.CURIOS_MOD))
        {
            return null;
        }
        return Curios.createLanternProvider(stack);
    }
    
}
