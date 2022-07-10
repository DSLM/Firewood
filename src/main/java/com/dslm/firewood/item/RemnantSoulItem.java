package com.dslm.firewood.item;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.RemnantSoulBoundedBlockEntity;
import com.dslm.firewood.entity.RemnantSoulEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RemnantSoulItem extends Item
{// TODO: 2022/6/24 增加掉落方式
    
    public RemnantSoulItem(Properties pProperties)
    {
        super(pProperties);
    }
    
    
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        ItemStack itemStack = context.getItemInHand();
        if(level.getBlockEntity(blockpos) instanceof RemnantSoulBoundedBlockEntity remnantSoulBoundedBlockEntity)
        {
            if(remnantSoulBoundedBlockEntity.getRemnantSoulEntity() == null)
            {
                RemnantSoulEntity entity = new RemnantSoulEntity(Register.REMNANT_SOUL_ENTITY.get(), level);
                entity.setBlockPos(blockpos);
                remnantSoulBoundedBlockEntity.setRemnantSoulEntity(entity);
            
                if(!player.getAbilities().instabuild)
                {
                    itemStack.shrink(1);
                }
            
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
            else
            {
                return InteractionResult.SUCCESS;
            }
        }
        else
        {
            return InteractionResult.PASS;
        }
    }
}
