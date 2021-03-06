package com.dslm.firewood.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.dslm.firewood.util.StaticValue.colorfulText;

public class DyingEmberItem extends Item
{
    final int color = 0x336666;
    public DyingEmberItem(Properties pProperties)
    {
        super(pProperties);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(new TranslatableComponent("tooltip.firewood.dying_ember_item.1").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        if(stack.hasTag())
        {
            tooltip.add(colorfulText(new TranslatableComponent("tooltip.firewood.dying_ember_item.2",
                            stack.getTag().get("dim").getAsString(),
                            stack.getTag().get("posX").getAsString(),
                            stack.getTag().get("posY").getAsString(),
                            stack.getTag().get("posZ").getAsString()),
                    color));
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand)
    {
        BlockPos pos = new BlockPos(player.getPosition(0));
        ItemStack itemStack = player.getItemInHand(pUsedHand);
        addPosition(level.dimension().location().toString(), pos, itemStack);
        return InteractionResultHolder.success(player.getItemInHand(pUsedHand));
    }
    
    public static void addPosition(String level, BlockPos pos, ItemStack itemStack)
    {
        CompoundTag component = itemStack.getOrCreateTag();
        component.putString("dim", level);
        component.putString("posX", String.valueOf(pos.getX()));
        component.putString("posY", String.valueOf(pos.getY()));
        component.putString("posZ", String.valueOf(pos.getZ()));
    }
}
