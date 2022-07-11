package com.dslm.firewood.item;

import com.dslm.firewood.block.SpiritualFireBlock;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

import static com.dslm.firewood.Register.SPIRITUAL_FIRE_BLOCK;

public class TinderItem extends Item implements TinderTypeItemBase
{
    
    public TinderItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag)
    {
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
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockPosDown = context.getClickedPos();
        BlockPos blockPos = blockPosDown.relative(context.getClickedFace());
        ItemStack itemStack = context.getItemInHand();
        if(!itemStack.hasTag())
        {
            return InteractionResult.PASS;
        }
    
        if(SpiritualFireBlock.canBePlacedAt(level, blockPos)
                && FireEffectHelpers.canBePlacedOn(level, blockPos, itemStack.getTag()))
        {
            level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            BlockState blockState = SPIRITUAL_FIRE_BLOCK.get().defaultBlockState();
            level.setBlock(blockPos, blockState, 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPosDown);
        
            level.getBlockEntity(blockPos).load(itemStack.getOrCreateTag());
        
            if(player instanceof ServerPlayer serverPlayer)
            {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, blockPos, itemStack);
                if(!serverPlayer.getAbilities().instabuild)
                {
                    itemStack.shrink(1);
                }
            }
        
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        else
        {
            return InteractionResult.PASS;
        }
    }
}
