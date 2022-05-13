package com.dslm.firewood.item;

import com.dslm.firewood.fireEffectHelper.TeleportFireEffectHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class DyingEmberItem extends Item
{
    public DyingEmberItem(Properties pProperties)
    {
        super(pProperties);
    }
    
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag)
    {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(new TranslatableComponent("tooltip.firewood.dying_ember_item.1").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        if(stack.hasTag())
        {
            tooltip.add(colorfulText(new TranslatableComponent("tooltip.firewood.dying_ember_item.2",
                            stack.getTag().get("dim"),
                            stack.getTag().get("posX"),
                            stack.getTag().get("posY"),
                            stack.getTag().get("posZ")),
                    TeleportFireEffectHelper.color));
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand)
    {
        BlockPos blockpos = new BlockPos(player.getPosition(0));
        ItemStack itemStack = player.getItemInHand(pUsedHand);
        CompoundTag component = itemStack.getOrCreateTag();
        component.putString("dim", level.dimension().location().getPath());
        component.putString("posX", String.valueOf(blockpos.getX()));
        component.putString("posY", String.valueOf(blockpos.getY()));
        component.putString("posZ", String.valueOf(blockpos.getZ()));
        return InteractionResultHolder.success(player.getItemInHand(pUsedHand));
    }
}
