package com.dslm.firewood.item;

import com.dslm.firewood.block.SpiritualFireBlock;
import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nonnull;
import java.util.List;

import static com.dslm.firewood.Firewood.LOGGER;
import static com.dslm.firewood.Register.SPIRITUAL_FIRE_BLOCK;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.fireTooltips;

public class TinderItem extends Item
{
    
    public TinderItem(Properties pProperties)
    {
        super(pProperties);
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag)
    {
        super.appendHoverText(stack, world, tooltip, flag);
        // TODO: 2022/5/10 实现拓展信息
        tooltip.addAll(fireTooltips(stack.getOrCreateTag(), false));
    }
    
    @Override
    public InteractionResult useOn(UseOnContext pContext)
    {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        if(true)
        {
            BlockPos blockpos1 = blockpos.relative(pContext.getClickedFace());
            if(SpiritualFireBlock.canBePlacedAt(level, blockpos1, pContext.getHorizontalDirection()))
            {
                level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockstate1 = ((SpiritualFireBlock) SPIRITUAL_FIRE_BLOCK.get()).getStateForPlacement(level, blockpos1);
                level.setBlock(blockpos1, blockstate1, 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
            
                ItemStack itemstack = pContext.getItemInHand();
            
                level.getBlockEntity(blockpos1).load(itemstack.getOrCreateTag());
            
                if(player instanceof ServerPlayer)
                {
                    // TODO: 2022/5/10 实现物品减少 
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, itemstack);
                    if(!player.getAbilities().instabuild)
                    {
                        itemstack.shrink(1);
                    }
                }
            
                return InteractionResult.sidedSuccess(level.isClientSide());
            } else
            {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
