package com.dslm.firewood.container;

import com.dslm.firewood.Register;
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
import org.jetbrains.annotations.NotNull;

public class SpiritualCampfireBlockContainer extends AbstractContainerMenu
{
    
    private BlockEntity blockEntity;
    private Player playerEntity;
    private IItemHandler playerInventory;
    
    public SpiritualCampfireBlockContainer(int windowId, BlockPos pos, Inventory playerInventory, Player player)
    {
        super(Register.SPIRITUAL_CAMPFIRE_BLOCK_CONTAINER.get(), windowId);
        blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        
        if(blockEntity != null)
        {
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 80, 77));
    
                addSlot(new SlotItemHandler(h, 1, 26 + 36 * 0, 23 + 36 * 0));
                addSlot(new SlotItemHandler(h, 2, 26 + 36 * 1, 23 + 36 * 0));
                addSlot(new SlotItemHandler(h, 3, 26 + 36 * 2, 23 + 36 * 0));
                addSlot(new SlotItemHandler(h, 4, 26 + 36 * 3, 23 + 36 * 0));
                addSlot(new SlotItemHandler(h, 5, 26 + 36 * 3, 23 + 36 * 1));
                addSlot(new SlotItemHandler(h, 6, 26 + 36 * 3, 23 + 36 * 2));
                addSlot(new SlotItemHandler(h, 7, 26 + 36 * 3, 23 + 36 * 3));
                addSlot(new SlotItemHandler(h, 8, 26 + 36 * 2, 23 + 36 * 3));
                addSlot(new SlotItemHandler(h, 9, 26 + 36 * 1, 23 + 36 * 3));
                addSlot(new SlotItemHandler(h, 10, 26 + 36 * 0, 23 + 36 * 3));
                addSlot(new SlotItemHandler(h, 11, 26 + 36 * 0, 23 + 36 * 2));
                addSlot(new SlotItemHandler(h, 12, 26 + 36 * 0, 23 + 36 * 1));
            });
        }
        layoutPlayerInventorySlots(8, 167);
    }
    
    @Override
    public boolean stillValid(Player playerIn)
    {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, Register.SPIRITUAL_CAMPFIRE_BLOCK.get());
    }
    
    @NotNull
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot != null && slot.hasItem())
        {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if(index <= 12)
            {
                //move other material to inventory
                if(!this.moveItemStackTo(stack, 13, 49, true))
                {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            }
            else
            {
                if(stack.getItem() == Register.TINDER_ITEM.get())
                {
                    if(!this.moveItemStackTo(stack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(!this.moveItemStackTo(stack, 1, 13, false))
                {
                    if(index < 40)
                    {
                        if(!this.moveItemStackTo(stack, 40, 49, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if(index < 49 && !this.moveItemStackTo(stack, 13, 40, false))
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
