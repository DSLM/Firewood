package com.dslm.firewood.compat.curios;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

public class Curios
{
    public static ICapabilityProvider createLanternProvider(ItemStack stack)
    {
        return CurioItemCapability.createProvider(new ICurio()
        {
            @Override
            public ItemStack getStack()
            {
                return stack;
            }

//            @Override
//            public void curioTick(SlotContext slotContext)
//            {
//                stack.getItem().inventoryTick(stack, slotContext.entity().level, slotContext.entity(), -1, false);
//            }
            
            @Override
            public boolean canEquipFromUse(SlotContext context)
            {
                return true;
            }

//            @Override
//            public boolean canSync(SlotContext context)
//            {
//                return true;
//            }
//
//            @Override
//            public void readSyncData(SlotContext slotContext, CompoundTag compound)
//            {
//                stack.setTag(compound);
//            }
//
//            @Override
//            public CompoundTag writeSyncData(SlotContext slotContext)
//            {
//                return stack.getOrCreateTag();
//            }
        });
    }
}