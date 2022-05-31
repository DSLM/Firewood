package com.dslm.firewood.menu;

import com.dslm.firewood.Register;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.item.TinderTypeItemBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class SpiritualCampfireBlockMenu extends AbstractContainerMenu
{
    
    private BlockEntity blockEntity;
    private Player playerEntity;
    private IItemHandler playerInventory;
    public final static ArrayList<Pair<Integer, Integer>> slotsPos = new ArrayList<Pair<Integer, Integer>>()
    {{
        add(Pair.of(54, 54));
        add(Pair.of(36 * 0, 36 * 0));
        add(Pair.of(36 * 1, 36 * 0));
        add(Pair.of(36 * 2, 36 * 0));
        add(Pair.of(36 * 3, 36 * 0));
        add(Pair.of(36 * 3, 36 * 1));
        add(Pair.of(36 * 3, 36 * 2));
        add(Pair.of(36 * 3, 36 * 3));
        add(Pair.of(36 * 2, 36 * 3));
        add(Pair.of(36 * 1, 36 * 3));
        add(Pair.of(36 * 0, 36 * 3));
        add(Pair.of(36 * 0, 36 * 2));
        add(Pair.of(36 * 0, 36 * 1));
    }};
    
    public SpiritualCampfireBlockMenu(int windowId, BlockPos pos, Inventory playerInventory, Player player)
    {
        super(Register.SPIRITUAL_CAMPFIRE_BLOCK_CONTAINER.get(), windowId);
        this.blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        
        if(blockEntity != null)
        {
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                for(int i = 0; i < SpiritualCampfireBlockEntity.NUM_SLOTS; i++)
                {
                    addSlot(new SlotItemHandler(h, i,
                            26 + slotsPos.get(i).getLeft(),
                            23 + slotsPos.get(i).getRight()));
                }
            });
        }
        layoutPlayerInventorySlots(8, 167);
    }
    
    @Override
    public boolean stillValid(Player playerIn)
    {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, Register.SPIRITUAL_CAMPFIRE_BLOCK.get());
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if(slot != null && slot.hasItem())
        {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if(index <= 12)
            {
                if(!moveItemStackTo(stack, 13, 49, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if(stack.getItem() instanceof TinderTypeItemBase)
                {
                    if(!moveItemStackTo(stack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(!moveItemStackTo(stack, 1, 13, false))
                {
                    if(index < 40)
                    {
                        if(!moveItemStackTo(stack, 40, 49, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if(index < 49 && !moveItemStackTo(stack, 13, 40, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
    
            if(stack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
    
            if(stack.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }
    
            slot.onTake(playerIn, stack);
        }
    
        if(blockEntity instanceof SpiritualCampfireBlockEntity spiritualCampfireBlockEntity)
        {
            spiritualCampfireBlockEntity.askForSync();
        }
    
        return itemstack;
    }
    
    
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx)
    {
        for(int i = 0; i < amount; i++)
        {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }
    
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy)
    {
        for(int j = 0; j < verAmount; j++)
        {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
    
    private void layoutPlayerInventorySlots(int leftCol, int topRow)
    {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        
        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
