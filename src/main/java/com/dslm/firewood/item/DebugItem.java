package com.dslm.firewood.item;

import com.dslm.firewood.capprovider.PlayerSpiritualDataProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class DebugItem extends Item
{
    public DebugItem(Properties pProperties)
    {
        super(pProperties);
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        String s = level.getBlockState(blockpos).toString();
        player.displayClientMessage(new TextComponent((level.isClientSide ? "client" : "server") + " BlockState:"), false);
        player.displayClientMessage(new TextComponent(s), false);
        
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        player.displayClientMessage(new TextComponent((level.isClientSide ? "client" : "server") + " Player Cap:"), false);
        player.getCapability(PlayerSpiritualDataProvider.PLAYER_SPIRITUAL_DATA).ifPresent(
                data -> player.displayClientMessage(new TextComponent(data.toString()), false));

//        Firewood.LOGGER.info("-----------------------------------------");
//        for (var item : ForgeRegistries.ITEMS.getValues()) {
//            if(item.getDefaultInstance().is(DyeColor.LIGHT_BLUE.getTag()))
//                Firewood.LOGGER.info("{}", item.getRegistryName());
//
//        }
//        Firewood.LOGGER.info("-----------------------------------------");
        return super.use(level, player, usedHand);
    }
}
