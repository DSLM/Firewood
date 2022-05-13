package com.dslm.firewood.item;

import com.dslm.firewood.block.SpiritualFireBlock;
import com.dslm.firewood.fireEffectHelper.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.GroundFireEffectHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
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
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;

import static com.dslm.firewood.Register.SPIRITUAL_FIRE_BLOCK;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.fireTooltips;

public class TinderItem extends Item
{
    
    public TinderItem(Properties pProperties)
    {
        super(pProperties);
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag)
    {
        
        boolean extended = false;
        if(level != null)
        {
            extended = Screen.hasShiftDown();
        }
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.addAll(fireTooltips(stack.getOrCreateTag(), extended));
    }
    
    @Override
    public InteractionResult useOn(UseOnContext pContext)
    {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(pContext.getClickedFace());
        ItemStack itemStack = pContext.getItemInHand();
        if(!itemStack.hasTag())
        {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
    
        Block validGround = GroundFireEffectHelper.getBlockByNBTs(itemStack.getTag());
        if(SpiritualFireBlock.canBePlacedAt(level, blockpos1)
                && SpiritualFireBlock.canBePlacedOn(level, blockpos1, validGround))
        {
            level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            BlockState blockstate1 = SPIRITUAL_FIRE_BLOCK.get().defaultBlockState();
            level.setBlock(blockpos1, blockstate1, 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
        
            level.getBlockEntity(blockpos1).load(itemStack.getOrCreateTag());
            if(player instanceof ServerPlayer)
            {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, itemStack);
                if(!player.getAbilities().instabuild)
                {
                    itemStack.shrink(1);
                }
            }
        
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else
        {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
    }
    
    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items)
    {
        if(allowdedIn(group))
        {
            items.add(new ItemStack(this));
            
            for(Potion potion : ForgeRegistries.POTIONS)
            {
                if(potion == Potions.EMPTY) continue;
                
                String potionId = potion.getRegistryName().toString();
                ItemStack stack = FireEffectHelpers.addMajorEffect(new ItemStack(this), "potion", new HashMap<>()
                {{
                    put("potion", potionId);
                }});
                
                if(!stack.isEmpty())
                    items.add(stack);
            }
        }
    }
}
